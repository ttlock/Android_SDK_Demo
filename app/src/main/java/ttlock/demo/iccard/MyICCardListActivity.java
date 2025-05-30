package ttlock.demo.iccard;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;

import ttlock.demo.iccard.model.ICCardListObj;
import ttlock.demo.iccard.model.ICCardObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class MyICCardListActivity extends BaseActivity implements ICCardListAdapter.onListItemClick{

//    ActivityMyIccardListBinding binding;
    ICCardListAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_iccard_list);
        initView();
    }

    @Override
    public void onResume(){
        super.onResume();
        getICCardList();
    }

    private void initView(){
        mListAdapter = new ICCardListAdapter(this);
        RecyclerView rvCardList = findViewById(R.id.rv_card_list);
        rvCardList.setAdapter(mListAdapter);
        rvCardList.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter.setOnListItemClick(this);
    }

    private void getICCardList(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> param = new HashMap<>(6);
        param.put("clientId",ApiService.CLIENT_ID);
        param.put("accessToken",MyApplication.getmInstance().getAccountInfo().getAccess_token());
        param.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        param.put("pageNo","1");
        param.put("pageSize","1000");
        param.put("date",String.valueOf(System.currentTimeMillis()));

        Call<ResponseBody> call = apiService.getUserICCardList(param);
        RetrofitAPIManager.enqueue(call, new TypeToken<ICCardListObj>(){}, result -> {
            if(!result.success){
                makeToast("--get my fingerprint list fail-" + result.getMsg());
                return;
            }
            Log.d("OMG","===result===" + result.getResult() + "===" + result);
            ICCardListObj icCardListObj = result.getResult();
            ArrayList<ICCardObj> myCardList = icCardListObj.getList();
            if(myCardList.isEmpty()){
                makeToast("- please add IC Card first --");
            }
            mListAdapter.updateData(myCardList);

        }, requestError -> {
            makeToast("--get my fingerprint list fail-" + requestError.getMessage());
        });
    }

    @Override
    public void onItemClick(ICCardObj cardObj) {
        if(cardObj != null){
            ICCardModifyActivity.launch(MyICCardListActivity.this,cardObj);
        }
    }
}
