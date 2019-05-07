package ttlock.demo.fingerprint;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.AddFingerprintCallback;
import com.ttlock.bl.sdk.callback.ClearAllFingerprintCallback;
import com.ttlock.bl.sdk.callback.GetAllValidFingerprintCallback;
import com.ttlock.bl.sdk.entity.LockError;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;;
import ttlock.demo.databinding.ActivityFingerprintBinding;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class FingerprintActivity extends BaseActivity {
    ActivityFingerprintBinding binding;
    private final static int ADD_PERMANENT = 1;
    private final static int ADD_TIMED = 2;
    private long theAddedFingerprintNum = 39912140898304L;
    long addStartDate = 0;
    long addEndDate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_fingerprint);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }

    private void initListener(){
        binding.btnAddPermanent.setOnClickListener(v -> addFingerprint(ADD_PERMANENT));
        binding.btnAddTimed.setOnClickListener(v -> addFingerprint(ADD_TIMED));
        binding.btnGetAll.setOnClickListener(v -> getAllFingerprints());
        binding.btnClear.setOnClickListener(v -> clearAllFingerprints());
        binding.btnMyList.setOnClickListener(v -> startTargetActivity(MyFingerprintListActivity.class));
    }

    /**
     * startDate the card active date
     * endDate the card expired date
     *startDate and endDate set 0 means the fingerprint will be valid for ever.
     * onEnterAddMode:the lock is ready to add a card.
     * totalCount: lock collect your fingerprint needed.
     * currentCount : current count the lock is collecting.
     *
     * @param type
     */
    private void addFingerprint(int type){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        switch (type){
            case ADD_PERMANENT:
                break;
            case ADD_TIMED:
                addStartDate = System.currentTimeMillis();
                //means 2 minutes later this card will expired.
                addEndDate = addStartDate + 2 * 60 * 1000;
                break;
            default:
                break;
        }


        TTLockClient.getDefault().addFingerprint(addStartDate, addEndDate, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new AddFingerprintCallback() {

            @Override
            public void onEnterAddMode(int totalCount) {
                makeToast("==put your fingerprint on lock=" + totalCount);
            }

            @Override
            public void onCollectFingerprint(int currentCount) {
                makeToast("==currentCount is " + currentCount);
            }

            @Override
            public void onAddFingerpintFinished(long fingerprintNum) {
                uploadFingerprint2Server(addStartDate,addEndDate,fingerprintNum);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void uploadFingerprint2Server(long startDate,long endDate,long fingerprintNumber){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> params = new HashMap<>(8);
        params.put("clientId",ApiService.CLIENT_ID);
        params.put("accessToken",MyApplication.getmInstance().getAccountInfo().getAccess_token());
        params.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        params.put("fingerprintNumber",String.valueOf(fingerprintNumber));
        params.put("fingerprintName","myTestFingerprint" + System.currentTimeMillis());
        if(startDate > 0 && endDate > 0){
            params.put("startDate",String.valueOf(addStartDate));
            params.put("endDate",String.valueOf(addEndDate));
        }
        params.put("date",String.valueOf(System.currentTimeMillis()));


        Call<ResponseBody> call = apiService.addFingerprint(params);
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
        }, result -> {
            if (!result.success) {
                makeToast("-add fail -" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("-fingerprint is added success " );


        }, requestError -> {
            makeToast(requestError.getMessage());
        });
    }



    private void getAllFingerprints(){
        showConnectLockToast();
        TTLockClient.getDefault().getAllValidFingerprints( mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetAllValidFingerprintCallback() {
            @Override
            public void onGetAllFingerprintsSuccess(String fingerprintStr) {
                makeToast("-get-all fingerprints-success-" + fingerprintStr);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void clearAllFingerprints(){
        showConnectLockToast();
        TTLockClient.getDefault().clearAllFingerprints( mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ClearAllFingerprintCallback() {

            @Override
            public void onClearAllFingerprintSuccess() {
                makeToast("--clear all fingerprints-");
                //this must be done
                uploadClear2Server();
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void uploadClear2Server(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();

        Call<ResponseBody> call = apiService.clearFingerprints(ApiService.CLIENT_ID,  MyApplication.getmInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(),System.currentTimeMillis());
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>(){}, result -> {
            if(!result.success){
                makeToast("--clear my fingerprint  fail-" + result.getMsg());
                return;
            }

            makeToast("-upload to server success-");


        }, requestError -> {
            makeToast("--clear my fingerprint  fail-" + requestError.getMessage());
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
