package ttlock.demo.lock.model;

import ttlock.demo.model.LockVersionObj;

/**
 * Created on  2019/4/28 0028 14:01
 *
 * @author theodre
 */
public class KeyObj {
    int keyId;
    int lockId;
    String userType;
    String keyStatus;
    String lockName;
    String lockAlias;
    String lockData;
    String lockKey;
    String lockMac;
    int lockFlagPos;
    String adminPwd;
    String noKeyPwd;
    String deletePwd;
    int electricQuantity;
    String aesKeyStr;
    LockVersionObj lockVersion;
    long startDate;
    long endDate;
    long timezoneRawOffset;
    String remarks;
    int keyRight;
    int keyboardPwdVersion;
    int specialValue;
    int remoteEnable;

    public int getKeyId() {
        return keyId;
    }

    public int getLockId() {
        return lockId;
    }

    public String getUserType() {
        return userType;
    }

    public String getKeyStatus() {
        return keyStatus;
    }

    public String getLockName() {
        return lockName;
    }

    public String getLockAlias() {
        return lockAlias;
    }

    public String getLockData() {
        return lockData;
    }

    public String getLockKey() {
        return lockKey;
    }

    public String getLockMac() {
        return lockMac;
    }

    public int getLockFlagPos() {
        return lockFlagPos;
    }

    public String getAdminPwd() {
        return adminPwd;
    }

    public String getNoKeyPwd() {
        return noKeyPwd;
    }

    public String getDeletePwd() {
        return deletePwd;
    }

    public int getElectricQuantity() {
        return electricQuantity;
    }

    public String getAesKeyStr() {
        return aesKeyStr;
    }

    public LockVersionObj getLockVersion() {
        return lockVersion;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getTimezoneRawOffset() {
        return timezoneRawOffset;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getKeyRight() {
        return keyRight;
    }

    public int getKeyboardPwdVersion() {
        return keyboardPwdVersion;
    }

    public int getSpecialValue() {
        return specialValue;
    }

    public int getRemoteEnable() {
        return remoteEnable;
    }
}
