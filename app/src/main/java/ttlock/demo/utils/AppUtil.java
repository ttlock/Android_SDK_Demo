package ttlock.demo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class AppUtil {
    public static final int REQUEST_PERMISSION_REQ_CODE = 1;

    public static boolean isAndroid12OrOver() {
        if (getAndroidSDKVersion() >= 31) {
            return true;
        }
        return false;
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static boolean checkPermissions(Activity activity, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(permissions, REQUEST_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{permission}, REQUEST_PERMISSION_REQ_CODE);
            return false;
        }
        return true;
    }


}
