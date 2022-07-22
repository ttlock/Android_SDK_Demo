package ttlock.demo.gateway;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.ttlock.bl.sdk.gateway.api.GatewayDfuClient;
import com.ttlock.bl.sdk.gateway.callback.DfuCallback;
import com.ttlock.bl.sdk.util.GsonUtil;

import retrofit2.Call;
import retrofit2.Callback;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;
import ttlock.demo.R;
import ttlock.demo.databinding.ActivityGatewayDfuBinding;
import ttlock.demo.gateway.model.GatewayUpgradeObj;
import ttlock.demo.model.GatewayObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class GatewayDfuActivity extends BaseActivity {

    private GatewayUpgradeObj gatewayUpgradeObj;
    private GatewayObj gatewayObj;
    private ActivityGatewayDfuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gateway_dfu);
        parseIntent(getIntent());
        check();
        initListener();
    }

    private void updateUI() {
        if (gatewayUpgradeObj != null) {
            binding.getRoot().setVisibility(View.VISIBLE);
            binding.version.setText(gatewayUpgradeObj.getVersion());
            switch (gatewayUpgradeObj.getNeedUpgrade()) {
                case 0:
                    binding.status.setText(R.string.no_updates);
                    binding.btnUpgrade.setVisibility(View.GONE);
                    break;
                case 1:
                    binding.status.setText(R.string.new_version_found);
                    binding.btnUpgrade.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    binding.status.setText(R.string.unknown_lock_version);
                    binding.btnUpgrade.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void initListener(){
        binding.btnUpgrade.setOnClickListener(v -> {
            makeToast("start dfu");
            startDfu();
        });

        binding.btnRetryByBle.setOnClickListener(v->{
            //use retryEnterDfuModeByBle,need Re Connect the Power
            GatewayDfuClient.getDefault().retryEnterDfuModeByBle();
        });

        binding.btnRetryByNet.setOnClickListener(v -> {
            GatewayDfuClient.getDefault().retryEnterDfuModeByNet();
        });

    }

    private void parseIntent(Intent intent) {
        gatewayObj = (GatewayObj) intent.getSerializableExtra(GatewayObj.class.getName());
    }

    public static void launch(Activity activity, GatewayObj gatewayObj) {
        Intent intent = new Intent(activity, GatewayDfuActivity.class);
        intent.putExtra(GatewayObj.class.getName(), gatewayObj);
        activity.startActivity(intent);
    }

    private void startDfu() {
        GatewayDfuClient.getDefault().startDfu(GatewayDfuActivity.this, ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), gatewayObj.getGatewayId(), gatewayObj.getGatewayMac(), new DfuCallback() {
            @Override
            public void onDfuSuccess(String deviceAddress) {
                makeToast("dfu completed");
                startTargetActivity(UserGatewayActivity.class);
            }

            @Override
            public void onDfuAborted(String deviceAddress) {
                makeToast("dfu aborted");
            }

            @Override
            public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {

            }

            @Override
            public void onError() {
                binding.llRetry.setVisibility(View.VISIBLE);
                makeToast("dfu error");
            }
        });
    }

    private void check() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.gatewayUpgradeCheck(ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), gatewayObj.getGatewayId(), System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                gatewayUpgradeObj = GsonUtil.toObject(json, GatewayUpgradeObj.class);
                if (gatewayUpgradeObj != null) {
                    if (gatewayUpgradeObj.errcode == 0)
                        updateUI();
                    else makeToast(gatewayUpgradeObj.errmsg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }

}
