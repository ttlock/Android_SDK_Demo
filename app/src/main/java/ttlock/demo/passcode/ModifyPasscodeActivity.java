package ttlock.demo.passcode;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ModifyPasscodeCallback;
import com.ttlock.bl.sdk.entity.LockError;

import ttlock.demo.BaseActivity;

import ttlock.demo.R;

import ttlock.demo.databinding.ActivityModifyPasscodeBinding;

public class ModifyPasscodeActivity extends BaseActivity {

    ActivityModifyPasscodeBinding binding;
    private final static int MODIFY_DATE_ONLY = 1;
    private final static int MODIFY_PASSCODE_ONLY = 1 << 1;
    private final static int MODIFY_BOTH = 1 << 2;

    String originalPasscode = "2356";
    String passcodeNew;
    long startDate = System.currentTimeMillis();
    long endDate = startDate + 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_modify_passcode);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initView();
    }

    private void initView(){
        binding.tvStartDate.setText(getDateFormat(startDate));
        binding.tvEndDate.setText(getDateFormat(endDate));
        binding.btnDateChange.setOnClickListener(v -> modifyPasscode(MODIFY_DATE_ONLY));
        binding.btnPasscodeChange.setOnClickListener(v -> modifyPasscode(MODIFY_PASSCODE_ONLY));
        binding.btnModifyBoth.setOnClickListener(v -> modifyPasscode(MODIFY_BOTH));
    }


    /**
     *
     *      * @param originalPasscode      old passcode
     *      * @param newPasscode         new passcode ,Passcode range : 4 - 9 Digits in length.If you do not need to modify the password,   set the value to null
     *      * @param startDate           The time when it becomes valid.       If you do not need to modify the time, set the value to 0
     *      * @param endDate             The time when it becomes expired.     If you do not need to modify the time, set the value to 0
     *
     *
     */
    private void modifyPasscode(int modifyType){
        ensureBluetoothIsEnabled();
        /**
         * if newPasscode is null or "" means you do not want to change original passcode.
         */
        String newPasscode = null;
        /**
         * startDate
         * endDate
         *  values are both 0 means you do not want to change expired date,or both values should not be 0.
         */
        long newStartDate = 0;
        long newEndDate = 0;

        switch (modifyType){
            case MODIFY_DATE_ONLY:
                newStartDate = startDate - 60 * 1000;
                newEndDate = newStartDate + 2 * 60 * 60 * 1000;
                break;
            case MODIFY_PASSCODE_ONLY:
                newPasscode = binding.edtNewPasscode.getText().toString();
                if(TextUtils.isEmpty(newPasscode)){
                    makeToast("new passcode is required or it won't be changed");
                    return;
                }
                break;
            case MODIFY_BOTH:
                newStartDate = startDate - 60 * 1000;
                newEndDate = newStartDate + 2 * 60 * 60 * 1000;
                newPasscode = binding.edtNewPasscode.getText().toString();
                if(TextUtils.isEmpty(newPasscode)){
                    makeToast("new passcode is required or it won't be changed");
                    return;
                }
                break;
                default:
                    break;
        }
        showConnectLockToast();
        TTLockClient.getDefault().modifyPasscode(originalPasscode, newPasscode, newStartDate, newEndDate, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ModifyPasscodeCallback() {
            @Override
            public void onModifyPasscodeSuccess() {
                makeToast("passcode is modified success");
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
