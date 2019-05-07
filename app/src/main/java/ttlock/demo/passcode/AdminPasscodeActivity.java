package ttlock.demo.passcode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.GetAdminPasscodeCallback;
import com.ttlock.bl.sdk.callback.ModifyAdminPasscodeCallback;
import com.ttlock.bl.sdk.constant.Feature;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.SpecialValueUtil;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;;

import ttlock.demo.databinding.ActivityAdminPasscodeBinding;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class AdminPasscodeActivity extends BaseActivity {
    ActivityAdminPasscodeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_passcode);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }

    private void initListener(){
        binding.btnGet.setOnClickListener(v -> getAdminPasscode());
        binding.btnModify.setOnClickListener(v -> modifyAdminPasscode());
    }

    /**
     * this operation may be not support this operation.
     */
    private void getAdminPasscode(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        if(!SpecialValueUtil.isSupportFeature(mCurrentLock.getSpecialValue(),Feature.GET_ADMIN_CODE)){
            makeToast("--this lock does not support this operation--");
            return;
        }
        TTLockClient.getDefault().getAdminPasscode(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetAdminPasscodeCallback() {
            @Override
            public void onGetAdminPasscodeSuccess(String passcode) {
                makeToast("admin passcode get success=");
                binding.tvAdminPasscode.setText(passcode);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void modifyAdminPasscode(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        String newAdminPasscode = binding.edtNewPasscode.getText().toString();
        if(TextUtils.isEmpty(newAdminPasscode)){
            makeToast("-please input new admin passcode-");
            return;
        }
        TTLockClient.getDefault().modifyAdminPasscode(newAdminPasscode, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ModifyAdminPasscodeCallback() {
            @Override
            public void onModifyAdminPasscodeSuccess(String passcode) {
                //this must be done after callback success.
                uploadNewAdminPasscode2Server(passcode);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void uploadNewAdminPasscode2Server(String adminPasscode){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> params = new HashMap<>(6);
        params.put("clientId",ApiService.CLIENT_ID);
        params.put("accessToken",MyApplication.getmInstance().getAccountInfo().getAccess_token());
        params.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        params.put("password",adminPasscode);
        params.put("date",String.valueOf(System.currentTimeMillis()));


        Call<ResponseBody> call = apiService.changeAdminPasscode(params);
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
        }, result -> {
            if (!result.success) {
                makeToast("-modify admin passcode fail -" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("--modify admin passcode and notify server success--");
            binding.tvAdminPasscode.setText(adminPasscode);


        }, requestError -> {
            makeToast(requestError.getMessage());
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
