# NewSDKAndroidDemo

#### Description
New SDK Android Demo

#### Software Architecture
Software architecture description
1.Lock Operate Api:TTLockClient
2.Lock firmware update Api:LockDfuClient
3.Gateway Api:GatewayClient

#### IDE
Android Studio

#### Minimum SDK Version
18

#### Installation
1. \ implementation 'com.tongtonglock:ttlock:3.0.0'
2. add permission in manifest
   \<uses-permission android:name="android.permission.BLUETOOTH" /><br />
   \<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" /><br />
   \<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   \<uses-permission android:name="android.permission.INTERNET" /> <br />
   \<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

3. before use sdk,make sure bluetooth is enabled.

#### Instructions

1. init sdk : \GatewayClient.getDefault().prepareBTService(getApplicationContext());
2. use api:\TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
                                                  @Override
                                                  public void onScanLockSuccess(ExtendedBluetoothDevice device) {

                                                  }
                                              });
3. when Activity finished,you should close SDK service:\TTLockClient.getDefault().stopBTService();

