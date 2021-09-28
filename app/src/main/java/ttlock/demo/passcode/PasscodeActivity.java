package ttlock.demo.passcode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.DeletePasscodeCallback;
import com.ttlock.bl.sdk.callback.GetAllValidPasscodeCallback;
import com.ttlock.bl.sdk.callback.GetPasscodeVerificationInfoCallback;
import com.ttlock.bl.sdk.callback.ResetPasscodeCallback;
import com.ttlock.bl.sdk.entity.LockError;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;;

import ttlock.demo.databinding.ActivityPasscodeBinding;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class PasscodeActivity extends BaseActivity {

    ActivityPasscodeBinding binding;
    private final String passcode = "4321";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_passcode);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }

    private void initListener(){
        binding.btnAdmin.setOnClickListener(v -> startTargetActivity(AdminPasscodeActivity.class));
        binding.btnCustom.setOnClickListener(v -> startTargetActivity(CreateCustomPasscodeActivity.class));
        binding.btnModify.setOnClickListener(v -> startTargetActivity(ModifyPasscodeActivity.class));
        binding.btnDeletePasscode.setOnClickListener(v -> deletePasscode());
        binding.btnGetAllPasscodes.setOnClickListener(v -> getAllValidPasscodes());
        binding.btnResetPasscode.setOnClickListener(v -> resetPasscodes());
        binding.btnGetPwdInfo.setOnClickListener(v -> getPwdInfo());
    }

    private void deletePasscode(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().deletePasscode(passcode, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new DeletePasscodeCallback() {
            @Override
            public void onDeletePasscodeSuccess() {
                makeToast(" passcode is deleted");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * get all valid passcodes in lock .
     */
    private void getAllValidPasscodes(){
        showConnectLockToast();
        TTLockClient.getDefault().getAllValidPasscodes(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetAllValidPasscodeCallback() {
            @Override
            public void onGetAllValidPasscodeSuccess(String passcodeStr) {
                makeToast("- Success  passcodes are " + passcodeStr);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * NOTICE !!!!!!!!
     * all passcodes will be invalid
     * you should upload pwdInfo and timestamp to you server to update lock data.
     */
    private void resetPasscodes(){
        showConnectLockToast();
        TTLockClient.getDefault().resetPasscode(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ResetPasscodeCallback() {
            @Override
            public void onResetPasscodeSuccess(String lockData) {
                //this must be done after callback success,or the lock passcode would be work well.
                uploadResetPasscodeResult2Server(lockData);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void uploadResetPasscodeResult2Server(String lockData){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> params = new HashMap<>(8);
        params.put("clientId",ApiService.CLIENT_ID);
        params.put("accessToken", MyApplication.getmInstance().getAccountInfo().getAccess_token());
        params.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        params.put("lockData", lockData);
        params.put("date",String.valueOf(System.currentTimeMillis()));

        Call<ResponseBody> call = apiService.updateLockData(params);
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
        }, result -> {
            if (!result.success) {
                makeToast("-reset passcode fail -" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("--reset passcode and notify server success--");
            //you need get the new lockData from the server, the lockData has been changed.
            mCurrentLock.setLockData(lockData);
        }, requestError -> {
            makeToast(requestError.getMessage());
        });
    }

    private void getPwdInfo(){
        showConnectLockToast();
        TTLockClient.getDefault().getPasscodeVerificationParams(mCurrentLock.getLockData(), new GetPasscodeVerificationInfoCallback() {
            @Override
            public void onGetInfoSuccess(String lockData) {
                makeToast("--success--lockData-" + lockData);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
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
