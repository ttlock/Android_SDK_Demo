package ttlock.demo.lock;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.GetBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetLockStatusCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;
import com.ttlock.bl.sdk.callback.GetOperationLogCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.SetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.constant.FeatureValue;
import com.ttlock.bl.sdk.constant.LogType;
import com.ttlock.bl.sdk.entity.DeviceInfo;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.FeatureValueUtil;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;
import ttlock.demo.R;
import ttlock.demo.databinding.ActivityLockApiBinding;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

;

public class LockApiActivity extends BaseActivity {

    ActivityLockApiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lock_api);
        if(mCurrentLock == null){
            makeToast("please choose at least one initialized lock first");
        }
        /**
         * this should be called first,to make sure Bluetooth configuration is ready.
         */
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }


    private void initListener(){
        binding.btnResetKey.setOnClickListener(v -> resetEKey());
        binding.btnResetLock.setOnClickListener(v -> resetLock());
        binding.btnUnlock.setOnClickListener(v -> startTargetActivity(UnlockActivity.class));
        binding.btnLockProperty.setOnClickListener(v -> startTargetActivity(EnableDisableSomeLockFuncionActivity.class));
        binding.btnPassageMode.setOnClickListener(v -> startTargetActivity(PassageModeActivity.class));
        binding.btnTime.setOnClickListener(v -> startTargetActivity(LockTimeActivity.class));
        binding.btnLog.setOnClickListener(v -> getOperationLog());
        binding.btnBattery.setOnClickListener(v -> getLockBatteryLevel());
        binding.btnLockInfo.setOnClickListener(v -> getLockSystemInfo());
        binding.btnLockStatus.setOnClickListener(v -> getLockStatus());
        binding.btnSetAutoLockTime.setOnClickListener(v -> setAutoLockingPeriod());
    }

    /**
     * ensure Bluetooth is enabled
     *
     * NOTICE !!!!!!!!
     * after resetEKey success,the lockFlagPos should upload to Server to update lock data.
     */
    private void resetEKey(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().resetEkey(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new ResetKeyCallback() {
            @Override
            public void onResetKeySuccess(String lockData) {
                updateLockData(lockData);
//               uploadResetEkeyResult2Server();
            }

            @Override
            public void onFail(LockError error) {
               makeErrorToast(error);
            }
        });
    }

    /**
     * The lockData of the lock have been changed, so you need notify the server to update the lockData.
     * @param lockData
     */
    private void updateLockData(String lockData){
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
                makeToast("-update the lock data to server fail -" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }

            //you need get the new lockData from the server, the lockData has been changed.
            mCurrentLock.setLockData(lockData);
            makeToast("--update the lock data to server success--");

        }, requestError -> {
            makeToast(requestError.getMessage());
        });
    }

    /**
     *  call https://api.sciener.cn/v3/lock/resetKey this api to notify server resetKey has been done.
     *
     */
//    private void uploadResetEkeyResult2Server(){
//        ApiService apiService = RetrofitAPIManager.provideClientApi();
//        Call<ResponseBody> call = apiService.restEkey(ApiService.CLIENT_ID,  MyApplication.getmInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(),System.currentTimeMillis());
//        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
//        }, result -> {
//            if (!result.success) {
//                makeToast("-init fail-upload to server-" + result.getMsg());
//                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
//                return;
//            }
//            makeToast("--reset key and notify server success--");
//
//        }, requestError -> {
//            makeToast(requestError.getMessage());
//        });
//    }

    /**
     * resetLock
     * means the lock will be reset to factory mode and if you want to use it,you should do initLock.
     */
    private void resetLock(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().resetLock(mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new ResetLockCallback() {
            @Override
            public void onResetLockSuccess() {
                makeToast("-lock is reset and now upload to  server -");
                uploadResetLock2Server();
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * this must be done after resetLock is called success.
     */
    private void uploadResetLock2Server(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<ResponseBody> call = apiService.deleteLock(ApiService.CLIENT_ID,  MyApplication.getmInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(),System.currentTimeMillis());
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
        }, result -> {
            if (!result.success) {
                makeToast("-reset lock -" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("--reset lock and notify server success--");

        }, requestError -> {
            makeToast(requestError.getMessage());
        });
    }

    /**
     *  logType  ALL - all the operation record from lock is initialized.
     *          NEW - only the new added operation record from last time you get log.
     */
    private void getOperationLog(){
        showConnectLockToast();
        TTLockClient.getDefault().getOperationLog(LogType.NEW, mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new GetOperationLogCallback() {
            @Override
            public void onGetLogSuccess(String log) {
                makeToast("Get log success!");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void getLockBatteryLevel(){
        showConnectLockToast();
        TTLockClient.getDefault().getBatteryLevel(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new GetBatteryLevelCallback() {
            @Override
            public void onGetBatteryLevelSuccess(int electricQuantity) {
                makeToast("lock battery is " + electricQuantity + "%");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void getLockSystemInfo(){
        showConnectLockToast();
        TTLockClient.getDefault().getLockSystemInfo(mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new GetLockSystemInfoCallback() {
            @Override
            public void onGetLockSystemInfoSuccess(DeviceInfo info) {
                makeToast(info.toString());
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * query lock status,such as unlock,lock.
     *
     * status :0-lock 1-unlock  2-unknown status 3-unlocked,has car top(this is only for car parking lock)
     */
    private void getLockStatus(){
        showConnectLockToast();
        TTLockClient.getDefault().getLockStatus(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new GetLockStatusCallback() {
            @Override
            public void onGetLockStatusSuccess(int status) {
                makeToast("lock status is now " + status);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * set Automatic locking period.
     *
     */
    private void setAutoLockingPeriod(){
        if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.AUTO_LOCK)){
            makeToast("this lock dose not support automatic locking");
        }
        showConnectLockToast();
        TTLockClient.getDefault().setAutomaticLockingPeriod(5, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new SetAutoLockingPeriodCallback() {
            @Override
            public void onSetAutoLockingPeriodSuccess() {
                makeToast("set automatic locking period success");
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
