package ttlock.demo.gateway;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import ttlock.demo.R;
import ttlock.demo.databinding.ActivityUserGatewayBinding;
import ttlock.demo.gateway.adapter.UserGatewayListAdapter;
import ttlock.demo.model.GatewayObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class UserGatewayActivity extends BaseActivity {

    private int pageNo = 1;
    private int pageSize = 100;
    private UserGatewayListAdapter mListApapter;
    private ActivityUserGatewayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_gateway);
        binding.btnScan.setOnClickListener(v -> {startTargetActivity(GatewayActivity.class);});
        initList();
        gatewayList();
    }

    private void initList() {
        mListApapter = new UserGatewayListAdapter(this);
        binding.rvGatewayList.setAdapter(mListApapter);
        binding.rvGatewayList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void gatewayList() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.getGatewayList(ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), pageNo, pageSize, System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                if (json.contains("list")) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray array = jsonObject.getJSONArray("list");
                        ArrayList<GatewayObj> gatewayObjs = GsonUtil.toObject(array.toString(), new TypeToken<ArrayList<GatewayObj>>(){});
                        mListApapter.updateData(gatewayObjs);
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
