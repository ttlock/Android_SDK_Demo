package ttlock.demo.lock;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.entity.ControlLockResult;
import com.ttlock.bl.sdk.entity.LockError;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;
import ttlock.demo.R;
import ttlock.demo.databinding.ActivityUnlockBinding;
import ttlock.demo.lock.model.KeyListObj;
import ttlock.demo.lock.model.KeyObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class UnlockActivity extends BaseActivity {
    ActivityUnlockBinding binding;
    //demo only add one lock,so we just get key for one lock,and the key is admin key.
    KeyObj mMyTestLockEKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserKeyList();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_unlock);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }

    private void initListener(){
        binding.btnUnlock.setOnClickListener(v -> doUnlock());
        binding.btnLock.setOnClickListener(v -> doLockLock());
    }

    //user should get a key list and show them with a list.In demo,we just have one admin key.
    private void getUserKeyList(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> param = new HashMap<>(6);
        param.put("clientId",ApiService.CLIENT_ID);
        param.put("accessToken",MyApplication.getmInstance().getAccountInfo().getAccess_token());
        param.put("pageNo","1");
        param.put("pageSize","1000");
        param.put("date",String.valueOf(System.currentTimeMillis()));

        Call<ResponseBody> call = apiService.getUserKeyList(param);
        RetrofitAPIManager.enqueue(call, new TypeToken<KeyListObj>(){}, result -> {
            if(!result.success){
                makeToast("--get my key list fail-" + result.getMsg());
                return;
            }
            Log.d("OMG","===result===" + result.getResult() + "===" + result);
            KeyListObj keyListObj = result.getResult();
            ArrayList<KeyObj> myKeyList = keyListObj.getList();
            if(!myKeyList.isEmpty()){
                for(KeyObj keyObj : myKeyList){
                    if(keyObj.getLockId() == mCurrentLock.getLockId()){
                        mMyTestLockEKey = keyObj;
                    }
                }
            }
        }, requestError -> {
            makeToast("--get key list fail-" + requestError.getMessage());
        });
    }

    /**
     * use eKey for controlLock interface
     */
    private void doUnlock(){
//        if(mMyTestLockEKey == null){
//            makeToast(" you should get your key list first ");
//            return;
//        }
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().controlLock(ControlAction.UNLOCK, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new ControlLockCallback() {
            @Override
            public void onControlLockSuccess(ControlLockResult controlLockResult) {
                Toast.makeText(UnlockActivity.this,"lock is unlock  success!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(LockError error) {
                Toast.makeText(UnlockActivity.this,"unLock fail!--" + error.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * use eKey for controlLock interface
     */
    private void doLockLock(){
//        if(mMyTestLockEKey == null){
//            makeToast(" you should get your key list first ");
//            return;
//        }
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().controlLock(ControlAction.LOCK, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new ControlLockCallback() {
            @Override
            public void onControlLockSuccess(ControlLockResult controlLockResult) {
                Toast.makeText(UnlockActivity.this,"lock is locked!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(LockError error) {
                Toast.makeText(UnlockActivity.this,"lock lock fail!--" + error.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * stopBTService should be called when Activity is finishing to release Bluetooth resource.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        TTLockClient.getDefault().stopBTService();
    }


}
