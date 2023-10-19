package ttlock.demo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.keypad.model.KeypadError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ttlock.demo.model.LockObj;

/**
 * Created on  2019/4/12 0012 10:52
 *
 * @author theodre
 */
public class BaseActivity extends AppCompatActivity {

    public final static String LOCK_OBJ = "lock_obj_data";
    public LockObj mCurrentLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mCurrentLock = MyApplication.getmInstance().getChoosedLock();
    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void startTargetActivity(Class targetClass) {
        Intent mIntent = new Intent(getApplicationContext(), targetClass);
        startActivity(mIntent);
    }

    public void makeToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_LONG).show();
    }

    public void makeErrorToast(LockError error){
        Toast.makeText(this,error.getDescription(),Toast.LENGTH_LONG).show();
    }

    public void makeErrorToast(KeypadError error){
        Toast.makeText(this,error.getDescription(),Toast.LENGTH_LONG).show();
    }


    /**
     * make sure Bluetooth is enabled.
     */
    public void ensureBluetoothIsEnabled(){
        if(!TTLockClient.getDefault().isBLEEnabled(this)){
            TTLockClient.getDefault().requestBleEnable(this);
        }
    }

    public String getDateFormat(long timestamp) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        return sDateFormat.format(new Date(timestamp));
    }

    public void showConnectLockToast(){
        makeToast("start connect lock...");
    }
}
