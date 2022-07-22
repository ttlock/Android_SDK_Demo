package ttlock.demo.iccard;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.AddICCardCallback;
import com.ttlock.bl.sdk.callback.ClearAllICCardCallback;
import com.ttlock.bl.sdk.callback.GetAllValidICCardCallback;
import com.ttlock.bl.sdk.entity.LockError;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;;

import ttlock.demo.databinding.ActivityIccardBinding;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class ICCardActivity extends BaseActivity {
    ActivityIccardBinding binding;
    private final static int ADD_PERMANENT = 1;
    private final static int ADD_TIMED = 2;
    long addStartDate = 0;
    long addEndDate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_iccard);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }

    private void initListener(){
        binding.btnMyList.setOnClickListener(v -> startTargetActivity(MyICCardListActivity.class));
        binding.btnAddPermanent.setOnClickListener(v -> addICCard(ADD_PERMANENT));
        binding.btnAddTimed.setOnClickListener(v -> addICCard(ADD_TIMED));
        binding.btnGetAllCards.setOnClickListener(v -> getAllCards());
        binding.btnClearCard.setOnClickListener(v -> clearAllCards());

    }

    /**
     * startDate the card active date
     * endDate the card expired date
     *startDate and endDate set 0 means the card will be valid for ever.
     * onEnterAddMode:the lock is ready to add a card.
     *
     * @param type
     */
    private void addICCard(int type){
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


        TTLockClient.getDefault().addICCard(addStartDate, addEndDate, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new AddICCardCallback() {
            @Override
            public void onEnterAddMode() {
                makeToast("-you can put ic card on lock now-");
            }

            @Override
            public void onAddICCardSuccess(long cardNum) {
                makeToast("card is added to lock -" + cardNum);
                uploadICCard2Server(addStartDate,addEndDate,cardNum);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void uploadICCard2Server(long startDate,long endDate,long cardNumber){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> params = new HashMap<>(8);
        params.put("clientId",ApiService.CLIENT_ID);
        params.put("accessToken",MyApplication.getmInstance().getAccountInfo().getAccess_token());
        params.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        params.put("cardNumber",String.valueOf(cardNumber));
        params.put("cardName","MyTestICCard " + System.currentTimeMillis());
        if(startDate > 0 && endDate > 0){
            params.put("startDate",String.valueOf(startDate));
            params.put("endDate",String.valueOf(endDate));
        }
        params.put("date",String.valueOf(System.currentTimeMillis()));


        Call<ResponseBody> call = apiService.addICCard(params);
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
        }, result -> {
            if (!result.success) {
                makeToast("-add fail -" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("-card is added success to server" );


        }, requestError -> {
            makeToast(requestError.getMessage());
        });
    }



    private void getAllCards(){
        showConnectLockToast();
        TTLockClient.getDefault().getAllValidICCards(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetAllValidICCardCallback() {
            @Override
            public void onGetAllValidICCardSuccess(String cardDataStr) {
                makeToast("-all ic cards info " + cardDataStr);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void clearAllCards(){
        showConnectLockToast();
        TTLockClient.getDefault().clearAllICCard(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ClearAllICCardCallback() {
            @Override
            public void onClearAllICCardSuccess() {
//                makeToast("--all ic cards have been cleared success-");
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

        Call<ResponseBody> call = apiService.clearICCards(ApiService.CLIENT_ID,  MyApplication.getmInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(),System.currentTimeMillis());
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>(){}, result -> {
            if(!result.success){
                makeToast("--clear my cards  fail-" + result.getMsg());
                return;
            }

            makeToast("-upload to server success-");


        }, requestError -> {
            makeToast("--clear cards  fail-" + requestError.getMessage());
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
