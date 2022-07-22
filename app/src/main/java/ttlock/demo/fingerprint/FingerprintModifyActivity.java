package ttlock.demo.fingerprint;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.DeleteFingerprintCallback;
import com.ttlock.bl.sdk.callback.ModifyFingerprintPeriodCallback;
import com.ttlock.bl.sdk.entity.LockError;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;;

import ttlock.demo.databinding.ActivityFingerprintModifyBinding;
import ttlock.demo.fingerprint.model.FingerprintObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class FingerprintModifyActivity extends BaseActivity {
    ActivityFingerprintModifyBinding binding;
    FingerprintObj mSelectFingerprintObj;
    private final static String SELECT_PARAM = "select_param";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_fingerprint_modify);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        mSelectFingerprintObj = (FingerprintObj)getIntent().getSerializableExtra(SELECT_PARAM);
        initListener();
    }

    private void initListener(){
        binding.btnDelete.setOnClickListener(v -> deleteFingerprint());
        binding.btnModifyPeriod.setOnClickListener(v -> modifyPeriod());
    }

    public static void launch(Context context,FingerprintObj fingerprintObj){
        Intent intent = new Intent(context,FingerprintModifyActivity.class);
        intent.putExtra(SELECT_PARAM,fingerprintObj);
        context.startActivity(intent);
    }

    private void modifyPeriod(){
        showConnectLockToast();
        long newStartDate = System.currentTimeMillis();
        long newEndDate = newStartDate + 5 * 60 * 1000;
        TTLockClient.getDefault().modifyFingerprintValidityPeriod(newStartDate, newEndDate, mSelectFingerprintObj.getFingerprintNumber(), mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ModifyFingerprintPeriodCallback() {

            @Override
            public void onModifyPeriodSuccess() {
                makeToast("-modify success-");
                //call server api to update modify data
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }


    private void deleteFingerprint(){
        showConnectLockToast();
        TTLockClient.getDefault().deleteFingerprint(mSelectFingerprintObj.getFingerprintNumber(),  mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new DeleteFingerprintCallback() {
            @Override
            public void onDeleteFingerprintSuccess() {
                makeToast("--fingerprint is deleted by lock-");
                //this must be done
                notifyDelete2Server();
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void notifyDelete2Server(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> param = new HashMap<>(6);
        param.put("clientId",ApiService.CLIENT_ID);
        param.put("accessToken",MyApplication.getmInstance().getAccountInfo().getAccess_token());
        param.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        param.put("fingerprintId",String.valueOf(mSelectFingerprintObj.getFingerprintId()));
        param.put("date",String.valueOf(System.currentTimeMillis()));
        Call<ResponseBody> call = apiService.deleteFingerprint(param);
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>(){}, result -> {
            if(!result.success){
                makeToast("--delete my fingerprint  fail-" + result.getMsg());
                return;
            }

            makeToast("-lock and server-delete success-");
            finish();


        }, requestError -> {
            makeToast("--delete my fingerprint  fail-" + requestError.getMessage());
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
