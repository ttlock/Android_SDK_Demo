package ttlock.demo.iccard;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.DeleteICCardCallback;
import com.ttlock.bl.sdk.callback.ModifyICCardPeriodCallback;
import com.ttlock.bl.sdk.entity.LockError;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;

import ttlock.demo.databinding.ActivityIccardModifyBinding;
import ttlock.demo.iccard.model.ICCardObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class ICCardModifyActivity extends BaseActivity {

    ActivityIccardModifyBinding binding;
    ICCardObj mSelectCardObj;
    private final static String SELECT_PARAM = "select_param";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_iccard_modify);
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        mSelectCardObj = (ICCardObj)getIntent().getSerializableExtra(SELECT_PARAM);
        initListener();
    }

    private void initListener(){
        binding.btnDeleteCard.setOnClickListener(v -> deleteCard());
        binding.btnModifyPeriod.setOnClickListener(v -> modifyPeriod());
    }

    public static void launch(Context context, ICCardObj cardObj){
        Intent intent = new Intent(context,ICCardModifyActivity.class);
        intent.putExtra(SELECT_PARAM,cardObj);
        context.startActivity(intent);
    }

    private void modifyPeriod(){
        showConnectLockToast();
        long newStartDate = System.currentTimeMillis();
        long newEndDate = newStartDate + 5 * 60 * 1000;
        TTLockClient.getDefault().modifyICCardValidityPeriod(newStartDate, newEndDate, mSelectCardObj.getCardNumber(), mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ModifyICCardPeriodCallback() {
            @Override
            public void onModifyICCardPeriodSuccess() {
//                makeToast("-lock-modify success--");
                //this must be done to upload to server
                notifyModifyDate2Server(newStartDate,newEndDate);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }


    private void deleteCard(){
        showConnectLockToast();
        TTLockClient.getDefault().deleteICCard(mSelectCardObj.getCardNumber(), mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new DeleteICCardCallback() {
            @Override
            public void onDeleteICCardSuccess() {
                makeToast("--IC card is deleted-");
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
        param.put("cardId",String.valueOf(mSelectCardObj.getCardId()));
        param.put("date",String.valueOf(System.currentTimeMillis()));
        Call<ResponseBody> call = apiService.deleteICCard(param);
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>(){}, result -> {
            if(!result.success){
                makeToast("--delete my card  fail-" + result.getMsg());
                return;
            }

            makeToast("-lock and server-delete success-");
            finish();


        }, requestError -> {
            makeToast("--delete my card  fail-" + requestError.getMessage());
        });
    }

    private void notifyModifyDate2Server(long startDate,long endDate){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> param = new HashMap<>(6);
        param.put("clientId",ApiService.CLIENT_ID);
        param.put("accessToken",MyApplication.getmInstance().getAccountInfo().getAccess_token());
        param.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        param.put("cardId",String.valueOf(mSelectCardObj.getCardId()));
        param.put("startDate",String.valueOf(startDate));
        param.put("endDate",String.valueOf(endDate));
        param.put("date",String.valueOf(System.currentTimeMillis()));
        Call<ResponseBody> call = apiService.modifyICCardPeriod(param);
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>(){}, result -> {
            if(!result.success){
                makeToast("--modify my card  fail-" + result.getMsg());
                return;
            }

            makeToast("-lock and server-modified success-");


        }, requestError -> {
            makeToast("--modify my card  fail-" + requestError.getMessage());
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
