# NewSDKAndroidDemo

#### Description
New SDK Android Demo

#### Software Architecture:
Software architecture description <br />
1.Lock Operate Api:TTLockClient <br />
2.Lock firmware update Api:LockDfuClient <br />
3.Gateway Api:GatewayClient <br />

#### IDE
Android Studio

#### Minimum SDK Version
18

#### Installation
1. import lib in build.gradle:
```
implementation 'com.tongtonglock:ttlock:3.0.6'
```
2. add permission in manifest:
```
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
#### Instructions
1. before call sdk api,make sure bluetooth is enabled.
2. init sdk :
```
 TTLockClient.getDefault().prepareBTService(getApplicationContext());
```
3. use api:
```
TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
    @Override
    public void onScanLockSuccess(ExtendedBluetoothDevice device) {

    }
});
```
4. when Activity finished,you should close SDK service:
```
TTLockClient.getDefault().stopBTService();
```

