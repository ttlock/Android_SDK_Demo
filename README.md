# NewSDKAndroidDemo

## instructions in English
https://github.com/ttlock/Android_SDK_Demo/blob/master/README.en.md

## Developers Email list
ttlock-developers-email-list@googlegroups.com

#### 介绍
新版SDK Android端Demo<br />
1.锁操作相关： TTLockClient<br />
2.锁固件升级相关：LockDfuClient<br />
3.网关操作相关：GatewayClient<br />

#### IDE
Android Studio

#### Minimum SDK Version
18

## 使用说明
### 引入SDK
```
implementation 'com.tongtonglock:ttlock:3.0.6'
```

### manifest配置
#### 添加Permission
```
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
### SDK初始化

#### 1)确保蓝牙开启
#### 2)SDK初始化
```
TTLockClient.getDefault().prepareBTService(getApplicationContext());
```

#### 3)开始调用接口 比如扫描周围蓝牙智能锁:
```
TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
    @Override
    public void onScanLockSuccess(ExtendedBluetoothDevice device) {

    }
});
```
### 重置SDK服务
#### 退出页面之后记得关闭SDK服务
```
TTLockClient.getDefault().stopBTService();
```

