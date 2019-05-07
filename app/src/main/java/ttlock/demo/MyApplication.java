package ttlock.demo;

import android.app.Application;

import ttlock.demo.model.AccountInfo;
import ttlock.demo.model.LockObj;

/**
 * Created by TTLock on 2019/4/23.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private AccountInfo accountInfo;
    private LockObj mTestLockObj;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApplication getmInstance() {
        return mInstance;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public void saveChoosedLock(LockObj lockObj){
        this.mTestLockObj = lockObj;
    }

    public LockObj getChoosedLock(){
        return this.mTestLockObj;
    }
}
