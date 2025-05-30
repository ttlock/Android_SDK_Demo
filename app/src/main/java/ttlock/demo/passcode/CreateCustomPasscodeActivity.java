package ttlock.demo.passcode;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.CreateCustomPasscodeCallback;
import com.ttlock.bl.sdk.entity.LockError;

import ttlock.demo.BaseActivity;

import ttlock.demo.R;

import ttlock.demo.databinding.ActivityCreateCustomPasscodeBinding;

public class CreateCustomPasscodeActivity extends BaseActivity {
    ActivityCreateCustomPasscodeBinding binding;
    long passcodeActivePeriodOneHour = 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_custom_passcode);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initView();
    }

    private void initView(){
        long startDate = System.currentTimeMillis();
        long endDate = startDate + passcodeActivePeriodOneHour;
        binding.tvStartDate.setText(getDateFormat(startDate));
        binding.tvEndDate.setText(getDateFormat(endDate));
        binding.btnCustom.setOnClickListener(v -> createCustomPasscode(startDate,endDate));
    }

    /** NOTICE !!
     *         Passcode range : 4 - 9 Digits in length.
     * @param startDate the active date for passcode
     * @param endDate the expiring date for passcod
     */
    private void createCustomPasscode(long startDate,long endDate){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        String passcode = binding.edtCustom.getText().toString();
        if(TextUtils.isEmpty(passcode)){
            makeToast("passcode is required");
            return;
        }

        TTLockClient.getDefault().createCustomPasscode(passcode, startDate, endDate, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new CreateCustomPasscodeCallback() {
            @Override
            public void onCreateCustomPasscodeSuccess(String passcode) {
                makeToast(" passcode is created : " + passcode + " you can try it on lock now");
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
