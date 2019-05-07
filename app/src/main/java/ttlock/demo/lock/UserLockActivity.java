package ttlock.demo.lock;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;

import ttlock.demo.R;;

import ttlock.demo.databinding.ActivityUserLockBinding;
import ttlock.demo.lock.adapter.UserLockListAdapter;
import ttlock.demo.model.LockObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class UserLockActivity extends BaseActivity {

    private int pageNo = 1;
    private int pageSize = 100;
    private UserLockListAdapter mListApapter;
    ActivityUserLockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_lock);
        initList();
        lockList();
        binding.btnScan.setOnClickListener(v -> startTargetActivity(ScanLockActivity.class));
    }

    private void initList(){
        mListApapter = new UserLockListAdapter(this);
        binding.rvLockList.setAdapter(mListApapter);
        binding.rvLockList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void lockList() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.getLockList(ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), pageNo, pageSize, System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                if (json.contains("list")) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray array = jsonObject.getJSONArray("list");
                        ArrayList<LockObj> lockObjs = GsonUtil.toObject(array.toString(), new TypeToken<ArrayList<LockObj>>(){});
                        mListApapter.updateData(lockObjs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    makeToast(json);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }
}
