package ttlock.demo.lock;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.GetLockMuteModeStateCallback;
import com.ttlock.bl.sdk.callback.GetRemoteUnlockStateCallback;
import com.ttlock.bl.sdk.callback.SetLockMuteModeCallback;
import com.ttlock.bl.sdk.callback.SetRemoteUnlockSwitchCallback;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.DigitUtil;

import ttlock.demo.BaseActivity;

import ttlock.demo.R;

import ttlock.demo.databinding.ActivityEnableDisableSomeLockFuncionBinding;

public class EnableDisableSomeLockFuncionActivity extends BaseActivity {
    ActivityEnableDisableSomeLockFuncionBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_enable_disable_some_lock_funcion);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }


    private void initListener(){
        binding.btnGetMuteState.setOnClickListener(v -> {
            if(!DigitUtil.isSupportAudioManagement(mCurrentLock.getSpecialValue())){
                makeToast("this lock does not support this feature");
            }else {
                makeToast("get mute mode state..");
                getLockMuteModeState();
            }
        });
        binding.swAudio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!DigitUtil.isSupportAudioManagement(mCurrentLock.getSpecialValue())){
                makeToast("this lock does not support this feature");
            }else {
                makeToast("switch mute mode...");
                setMuteMode(isChecked);
            }
        });

        binding.btnRemoteUnlockState.setOnClickListener(v -> {
            if(!DigitUtil.isSupportRemoteUnlockSwitch(mCurrentLock.getSpecialValue())){
                makeToast("this lock does not support this feature");
            }else {
                makeToast("get remote unlock state..");
                getRemoteUnlockState();
            }
        });

        binding.swRemoteUnlock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!DigitUtil.isSupportRemoteUnlockSwitch(mCurrentLock.getSpecialValue())){
                makeToast("this lock does not support this feature");
            }else {
                makeToast("switch remote unlock function ...");
                setRemoteUnlockFunction(isChecked);
            }
        });
    }

    /**
     * before query or set Lock Property such as setAudioSwitchState / setRemoteUnlockSwitchState,you should use specialValue to judge if the lock is support or not.
     */
    private void getLockMuteModeState(){
        if(!DigitUtil.isSupportAudioManagement(mCurrentLock.getSpecialValue())){
            makeToast("this lock does not support mute mode");
            return;
        }

        if(!TTLockClient.getDefault().isBLEEnabled(this)){
            return;
        }

        TTLockClient.getDefault().getMuteModeState( mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetLockMuteModeStateCallback() {
            @Override
            public void onGetMuteModeStateSuccess(boolean enabled) {

            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * send data to Bluetooth should one by one.It only support one connect.So the TTLockSDK Api should be called one by one.
     */
    private void getRemoteUnlockState(){
        if(!DigitUtil.isSupportAudioManagement(mCurrentLock.getSpecialValue())){
            makeToast("this lock does not support remote unlock");

            return;
        }

        TTLockClient.getDefault().getRemoteUnlockSwitchState( mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new GetRemoteUnlockStateCallback() {
            @Override
            public void onGetRemoteUnlockSwitchStateSuccess(boolean enabled) {

            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * set lock mute mode
     * @param enable true means the lock will be mute,false means lock volume > 0.
     */
    private void setMuteMode(boolean enable){
        TTLockClient.getDefault().setMuteMode(enable,  mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new SetLockMuteModeCallback() {
            @Override
            public void onSetMuteModeSuccess(boolean enabled) {
                makeToast("--set lock mute state Success-is mute-" + enable);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**  NOTICE !!!!!!!!
     *  remote unlock feature switch on/off will change the specialValue.So you should update the latest specialValue to your Server in time.
     * @param enable the remote unlock feature on/off  true means on,false means off.
     */
    private void setRemoteUnlockFunction(final boolean enable){
        TTLockClient.getDefault().setRemoteUnlockSwitchState(enable, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new SetRemoteUnlockSwitchCallback() {
            @Override
            public void onSetRemoteUnlockSwitchSuccess(int specialValue) {
                makeToast("--remote unlock switch has been changed success--specialValue is ===" + specialValue);

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
