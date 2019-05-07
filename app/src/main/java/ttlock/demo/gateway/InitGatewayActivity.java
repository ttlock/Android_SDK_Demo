package ttlock.demo.gateway;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.gateway.api.GatewayClient;
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanWiFiByGatewayCallback;
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo;
import com.ttlock.bl.sdk.gateway.model.DeviceInfo;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.WiFi;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.util.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import ttlock.demo.BaseActivity;
import ttlock.demo.MyApplication;
import ttlock.demo.R;
import ttlock.demo.databinding.ActivityInitGatewayBinding;
import ttlock.demo.gateway.dialog.ChooseNetDialog;
import ttlock.demo.model.GatewayObj;
import ttlock.demo.model.ServerError;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;


public class InitGatewayActivity extends BaseActivity {

    private ActivityInitGatewayBinding binding;
    private ConfigureGatewayInfo configureGatewayInfo;
    private ExtendedBluetoothDevice device;
    private ChooseNetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_init_gateway);
        device = getIntent().getParcelableExtra(ExtendedBluetoothDevice.class.getName());
        configureGatewayInfo = new ConfigureGatewayInfo();
        initView();
        initListener();
    }

    private void initView() {
        if (NetworkUtil.isWifiConnected(this)) {
            binding.wifiName.setText(NetworkUtil.getWifiSSid(this));
        }
    }

    private void uploadGatewayDetail(DeviceInfo deviceInfo, int gatewayId) {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.uploadGatewayDetail(ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), gatewayId, deviceInfo.getModelNum(), deviceInfo.hardwareRevision, deviceInfo.getFirmwareRevision(), binding.wifiName.getText().toString(), System.currentTimeMillis());
        LogUtil.d("call server isSuccess api");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                if (!TextUtils.isEmpty(json)) {
                    ServerError error = GsonUtil.toObject(json, ServerError.class);
                    if (error.errcode == 0)
                        startTargetActivity(UserGatewayActivity.class);
                    else makeToast(error.errmsg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                LogUtil.d("t.getMessage():" + t.getMessage());
            }
        });
    }

    private void isInitSuccess(DeviceInfo deviceInfo) {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.gatewayIsInitSuccess(ApiService.CLIENT_ID, MyApplication.getmInstance().getAccountInfo().getAccess_token(), device.getAddress(), System.currentTimeMillis());
        LogUtil.d("call server isSuccess api");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                if (!TextUtils.isEmpty(json)) {
                    GatewayObj gatewayObj = GsonUtil.toObject(json, GatewayObj.class);
                    if (gatewayObj.errcode == 0)
                        uploadGatewayDetail(deviceInfo, gatewayObj.getGatewayId());
                    else makeToast(gatewayObj.errmsg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                LogUtil.d("t.getMessage():" + t.getMessage());
            }
        });
    }

    private void initListener() {
        binding.btnInitGateway.setOnClickListener(v -> {
            configureGatewayInfo.uid = MyApplication.getmInstance().getAccountInfo().getUid();
            configureGatewayInfo.userPwd = MyApplication.getmInstance().getAccountInfo().getMd5Pwd();

            configureGatewayInfo.ssid = binding.wifiName.getText().toString().trim();
//            configureGatewayInfo.plugName = binding.gatewayName.getText().toString().trim();
            configureGatewayInfo.wifiPwd = binding.wifiPwd.getText().toString().trim();

            //TODO:
            configureGatewayInfo.plugName = device.getAddress();

            GatewayClient.getDefault().initGateway(configureGatewayInfo, new InitGatewayCallback() {
                @Override
                public void onInitGatewaySuccess(DeviceInfo deviceInfo) {
                    LogUtil.d("gateway init success");
                    isInitSuccess(deviceInfo);
                }

                @Override
                public void onFail(GatewayError error) {
                    makeToast(error.getDescription());
                    finish();
                }
            });
        });

        binding.rlWifiName.setOnClickListener(v -> {
            chooseWifiDialog();
        });
    }

    private void chooseWifiDialog() {
        if (dialog == null) {
            dialog = new ChooseNetDialog(this);
            dialog.setOnSelectListener(new ChooseNetDialog.OnSelectListener() {
                @Override
                public void onSelect(WiFi wiFi) {
                    binding.wifiName.setText(wiFi.ssid);
                }
            });
        }
        dialog.show();
        GatewayClient.getDefault().scanWiFiByGateway(device.getAddress(), new ScanWiFiByGatewayCallback() {
            @Override
            public void onScanWiFiByGateway(List<WiFi> wiFis) {
                dialog.updateWiFi(wiFis);
            }

            @Override
            public void onScanWiFiByGatewaySuccess() {
                makeToast("scan completed");
            }

            @Override
            public void onFail(GatewayError error) {
                makeToast(error.getDescription());
            }
        });
    }

    public static void launch(Activity activity, ExtendedBluetoothDevice device) {
        Intent intent = new Intent(activity, InitGatewayActivity.class);
        intent.putExtra(ExtendedBluetoothDevice.class.getName(), device);
        activity.startActivity(intent);
    }
}
