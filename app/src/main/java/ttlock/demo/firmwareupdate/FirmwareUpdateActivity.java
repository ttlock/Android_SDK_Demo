package ttlock.demo.firmwareupdate;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.ttlock.bl.sdk.api.LockDfuClient;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.DfuCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;
import com.ttlock.bl.sdk.entity.DeviceInfo;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;
import ttlock.demo.R;
import ttlock.demo.databinding.ActivityFirmwareUpdateBinding;
import ttlock.demo.lock.UserLockActivity;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;


public class FirmwareUpdateActivity extends BaseActivity {

    private ActivityFirmwareUpdateBinding binding;
    private LockUpgradeObj lockUpgradeObj;
    private boolean isFailure;
    protected static final int REQUEST_PERMISSION_REQ_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firmware_update);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());

        check();
        initListener();
    }

    private void check() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.lockUpgradeCheck(ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(), System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                lockUpgradeObj = GsonUtil.toObject(json, LockUpgradeObj.class);
                if (lockUpgradeObj != null) {
                    if (lockUpgradeObj.errcode == 0){
                        updateUI();
                    }else {
                        makeToast(lockUpgradeObj.errmsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }

    private void checkAgain(DeviceInfo deviceInfo) {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.lockUpgradeCheckAgain(ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), deviceInfo.getDeviceInfo(), mCurrentLock.getLockId(), System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                lockUpgradeObj = GsonUtil.toObject(json, LockUpgradeObj.class);
                if (lockUpgradeObj != null) {
                    if (lockUpgradeObj.errcode == 0){
                        updateUI();
                    }else{
                        makeToast(lockUpgradeObj.errmsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }

    private void updateUI() {
        if (lockUpgradeObj != null) {
            binding.getRoot().setVisibility(View.VISIBLE);
            binding.version.setText(lockUpgradeObj.getVersion());
            switch (lockUpgradeObj.getNeedUpgrade()) {
                case 0:
                    binding.status.setText(R.string.no_updates);
                    binding.btnUpgrade.setVisibility(View.GONE);
                    break;
                case 1:
                    binding.status.setText(R.string.new_version_found);
                    binding.btnUpgrade.setVisibility(View.VISIBLE);
                    binding.btnUpgrade.setText(R.string.upgrade);
                    break;
                case 2:
                    binding.status.setText(R.string.unknown_lock_version);
                    binding.btnUpgrade.setVisibility(View.VISIBLE);
                    binding.btnUpgrade.setText(R.string.recheck_version);
                    break;
                    default:
                        break;
            }
        }
    }

    private void getLockSysInfo() {
        if (mCurrentLock != null) {
            LockDfuClient.getDefault().getLockSystemInfo(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetLockSystemInfoCallback() {
                @Override
                public void onGetLockSystemInfoSuccess(DeviceInfo info) {
                    LogUtil.d("info:" + info);
                    checkAgain(info);
                }

                @Override
                public void onFail(LockError error) {
                    makeErrorToast(error);
                }
            });
        }
    }

    private void startDfu() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
        if (mCurrentLock != null) {
            LockDfuClient.getDefault().startDfu(getApplicationContext(), ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(), mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new DfuCallback() {
                @Override
                public void onDfuSuccess(String deviceAddress) {
                    makeToast("dfu success");
                    startTargetActivity(UserLockActivity.class);
                }

                @Override
                public void onStatusChanged(int status) {

                }

                @Override
                public void onDfuAborted(String deviceAddress) {

                }

                @Override
                public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
                    LogUtil.d("percent:" + percent);
                }

                @Override
                public void onError(int errorCode, String errorContent) {
                    makeToast(errorContent);
                    isFailure = true;
                    binding.btnUpgrade.setText(R.string.retry);
                }
            });
        }
    }

    private void initListener(){
        binding.btnUpgrade.setOnClickListener(v -> {
            if (isFailure) {
                makeToast("retry");
                LockDfuClient.getDefault().retry();
            } else {
                switch (lockUpgradeObj.getNeedUpgrade()) {
                    case 1:
                        makeToast("start dfu");
                        startDfu();
                        break;
                    case 2:
                        makeToast("check version again");
                        getLockSysInfo();
                        break;
                        default:
                            break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length == 0 ){
            return;
        }

        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    startDfu();
                }
                break;
            }
            default:
                break;
        }
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
