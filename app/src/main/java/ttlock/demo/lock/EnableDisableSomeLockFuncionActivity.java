package ttlock.demo.lock;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.GetLockMuteModeStateCallback;
import com.ttlock.bl.sdk.callback.GetRemoteUnlockStateCallback;
import com.ttlock.bl.sdk.callback.SetLockMuteModeCallback;
import com.ttlock.bl.sdk.callback.SetRemoteUnlockSwitchCallback;
import com.ttlock.bl.sdk.constant.FeatureValue;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.FeatureValueUtil;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.DateUtils;
import ttlock.demo.MyApplication;
import ttlock.demo.R;
import ttlock.demo.databinding.ActivityEnableDisableSomeLockFuncionBinding;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

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
            if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.AUDIO_MANAGEMENT)){
                makeToast("this lock does not support this feature");
            }else {
                makeToast("get mute mode state..");
                getLockMuteModeState();
            }
        });
        binding.swAudio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.AUDIO_MANAGEMENT)){
                makeToast("this lock does not support this feature");
            }else {
                makeToast("switch mute mode...");
                setMuteMode(isChecked);
            }
        });

        binding.btnRemoteUnlockState.setOnClickListener(v -> {
            if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.CONFIG_GATEWAY_UNLOCK)){
                makeToast("this lock does not support this feature");
            }else {
                makeToast("get remote unlock state..");
                getRemoteUnlockState();
            }
        });

        binding.swRemoteUnlock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.CONFIG_GATEWAY_UNLOCK)){
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
        if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.AUDIO_MANAGEMENT)){
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
        if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.GATEWAY_UNLOCK)){
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
     *  remote unlock feature switch on/off will change the lockData.So you should update the latest lockData to your Server in time.
     * @param enable the remote unlock feature on/off  true means on,false means off.
     */
    private void setRemoteUnlockFunction(final boolean enable){
        TTLockClient.getDefault().setRemoteUnlockSwitchState(enable, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new SetRemoteUnlockSwitchCallback() {
            @Override
            public void onSetRemoteUnlockSwitchSuccess(String lockData) {
                makeToast("--remote unlock switch has been changed success--");
                updateLockData(lockData);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

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
     * stopBTService should be called when Activity is finishing to release Bluetooth resource.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        TTLockClient.getDefault().stopBTService();
    }

}
