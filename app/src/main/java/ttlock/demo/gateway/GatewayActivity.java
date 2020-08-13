package ttlock.demo.gateway;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.ttlock.bl.sdk.gateway.api.GatewayClient;
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;

import ttlock.demo.BaseActivity;

import ttlock.demo.R;;
import ttlock.demo.databinding.ActivityGatewayBinding;
import ttlock.demo.gateway.adapter.GatewayListAdapter;


public class GatewayActivity extends BaseActivity {

    private ActivityGatewayBinding binding;
    private GatewayListAdapter mListApapter;
    protected static final int REQUEST_PERMISSION_REQ_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gateway);
        GatewayClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
        initList();
    }

    private void initList(){
        mListApapter = new GatewayListAdapter(this);
        binding.rvGatewayList.setAdapter(mListApapter);
        binding.rvGatewayList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListener() {
        binding.btnScan.setOnClickListener(v -> {
            boolean isBtEnable =  GatewayClient.getDefault().isBLEEnabled(GatewayActivity.this);
            if(isBtEnable){
                startScan();
            } else {
                GatewayClient.getDefault().requestBleEnable(GatewayActivity.this);
            }
        });
    }

    /**
     * before call startScanGateway,the location permission should be granted.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void startScan(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
        getScanGatewayCallback();
    }

    /**
     * start scan BT lock
     */
    private void getScanGatewayCallback(){
        GatewayClient.getDefault().startScanGateway(new ScanGatewayCallback() {
            @Override
            public void onScanGatewaySuccess(ExtendedBluetoothDevice device) {
//                LogUtil.d("device:" + device);
                if (mListApapter != null)
                    mListApapter.updateData(device);
            }

            @Override
            public void onScanFailed(int errorCode) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length == 0 ){
            return;
        }

        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getScanGatewayCallback();
                } else {
                    if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)){

                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GatewayClient.REQUEST_ENABLE_BT && requestCode == Activity.RESULT_OK) {
            startScan();
        }
    }
}
