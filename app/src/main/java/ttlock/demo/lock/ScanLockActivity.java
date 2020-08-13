package ttlock.demo.lock;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.ParamInvalidException;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.SetNBServerCallback;
import com.ttlock.bl.sdk.constant.Feature;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;

import com.ttlock.bl.sdk.util.SpecialValueUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ttlock.demo.BaseActivity;
import ttlock.demo.DateUtils;
import ttlock.demo.MyApplication;
import ttlock.demo.R;

import ttlock.demo.databinding.ActivityScanLockBinding;
import ttlock.demo.lock.adapter.LockListAdapter;
import ttlock.demo.lock.model.LockInitResultObj;
import ttlock.demo.retrofit.ApiService;
import ttlock.demo.retrofit.RetrofitAPIManager;

public class ScanLockActivity extends BaseActivity implements LockListAdapter.onLockItemClick{
    ActivityScanLockBinding binding;
    protected static final int REQUEST_PERMISSION_REQ_CODE = 11;
    private LockListAdapter mListApapter;
    private String mInitLockData;
    //mHotelInfoStr should get from server by call Url
    private String mHotelInfoStr = "LTExMywtMTE2LC0xMTYsLTExNiwtMTEwLC0xMTUsLTEyMSwtMzgsLTExNiwtMTE0LC0xMTcsLTEyMSwtNDAsLTM3LC0zNywtMzUsLTExNSwtMTEwLC0xMTYsLTExMywtMTEzLC0xMTQsLTM1LC0xMjIsLTM1LC0zNywtMTE0LC0xMTYsLTEyMSwtMzYsLTExOCwtMzYsLTExNywtMzcsLTExMywtMzMsLTM1LC0xMTgsLTExNiwtMTE1LC0xMTcsLTM4LC0xMTMsLTExNCwtNDAsLTExMywtMzMsLTExNSwtMzcsLTExNiwtMTEwLC0xMTYsLTExNywtMTIxLC0xMTksLTExNiwtMTE0LC0xMjAsLTEyMCwzMg==";
    //the number of your hotel building
    private int mBuildingNumber = 1;
    //the number of your hotel floor
    private int mFloorNumber = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_scan_lock);
        initList();
        initBtService();
        initListener();

    }

    private void initList(){
        mListApapter = new LockListAdapter(this);
        binding.rvLockList.setAdapter(mListApapter);
        binding.rvLockList.setLayoutManager(new LinearLayoutManager(this));
        mListApapter.setOnLockItemClick(this);
    }

    /**
     * prepareBTService should be called first,or all TTLock SDK function will not be run correctly
     */
    private void initBtService(){
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
    }

    private void initListener(){
        binding.btnEnableBle.setOnClickListener(v -> {
            boolean isBtEnable =  TTLockClient.getDefault().isBLEEnabled(ScanLockActivity.this);
            if(!isBtEnable){
                TTLockClient.getDefault().requestBleEnable(ScanLockActivity.this);
            }
        });

        binding.btnStartScan.setOnClickListener(v -> startScan());
        binding.btnStopScan.setOnClickListener(v -> TTLockClient.getDefault().stopScanLock());
    }


    /**
     * before call startScanLock,the location permission should be granted.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void startScan(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }

        getScanLockCallback();
    }

    /**
     * start scan BT lock
     */
    private void getScanLockCallback(){
        TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
            @Override
            public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                if(mListApapter != null){
                    mListApapter.updateData(device);
                }
            }

            @Override
            public void onFail(LockError error) {

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
                    getScanLockCallback();
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
    public void onDestroy(){
        super.onDestroy();
        /**
         * BT service should be released before Activity finished.
         */
        TTLockClient.getDefault().stopBTService();
    }

    @Override
    public void onClick(ExtendedBluetoothDevice device) {
        makeToast("--start init lock--");


        // if you need to add a hotel lock you should set hotel data for lock init.

//        HotelData hotelData = new HotelData();
//        hotelData.setBuildingNumber(mBuildingNumber);
//        hotelData.setFloorNumber(mFloorNumber);
//        hotelData.setHotelInfo(mHotelInfoStr);
//        try {
//            device.setHotelData(hotelData);
//        } catch (ParamInvalidException e){
//
//        }


        /**
         * lockData: the server api lockData param need
         * isNBLock: is a NB-IoT lock.
         */
        TTLockClient.getDefault().initLock(device, new InitLockCallback() {
            @Override
            public void onInitLockSuccess(String lockData,int specialValue) {
                //this must be done after lock is initialized,call server api to post to your server
                if(SpecialValueUtil.isSupportFeature(specialValue,Feature.NB_LOCK)){
                    setNBServerForNBLock(lockData,device.getAddress());
                }else {
                    makeToast("--lock is initialized success--");
                    upload2Server(lockData);
                }


            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * if a NB-IoT lock you'd better do set NB-IoT server before upload lockData to server to active NB-IoT lock service.
     * And no matter callback is success or fail,upload lockData to server.
     * @param lockData
     * @param lockMac
     */
    private void setNBServerForNBLock(String lockData,String lockMac){
        //NB server port
        short mNBServerPort = 8011;
        String mNBServerAddress = "192.127.123.11";
        TTLockClient.getDefault().setNBServerInfo(mNBServerPort, mNBServerAddress, lockData, lockMac, new SetNBServerCallback() {
            @Override
            public void onSetNBServerSuccess(int battery) {
                makeToast("--set NB server success--");
                upload2Server(lockData);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
                //no matter callback is success or fail,upload lockData to server.
                upload2Server(lockData);
            }
        });
    }

    private void upload2Server(String lockData){
        String lockAlias = "MyTestLock" + DateUtils.getMillsTimeFormat(System.currentTimeMillis());
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<ResponseBody> call = apiService.lockInit(ApiService.CLIENT_ID,  MyApplication.getmInstance().getAccountInfo().getAccess_token(), lockData,lockAlias,System.currentTimeMillis());
        RetrofitAPIManager.enqueue(call, new TypeToken<LockInitResultObj>() {
        }, result -> {
            if (!result.success) {
                makeToast("-init fail-to server-" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("--init lock success--");
            Intent intent = new Intent(this,UserLockActivity.class);
            startActivity(intent);
            finish();

        }, requestError -> {

        });
    }
}
