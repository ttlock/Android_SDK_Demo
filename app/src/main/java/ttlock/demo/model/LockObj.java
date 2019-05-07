package ttlock.demo.model;

import java.io.Serializable;

/**
 * Created by TTLock on 2019/4/23.
 */

public class LockObj implements Serializable{
    /**
     * date : 1534470870000
     * specialValue : 21729
     * lockAlias : AS1003_84e13c
     * modelNum : SN139-M101-T1_PV53
     * lockMac : D3:58:7F:58:DE:B9
     * lockId : 24409
     * electricQuantity : 75
     * lockData : Djksy80snK7hKn9QdPutr6QI9YsRHDy+id/vo3o330AeQjMd4vgjWh/9ydA6b5H4e1+eVOyMgFlUjI2f8sfw8jkm/3RrPYeZ8wuSGN9yDiwYibmPx7QJGF/Pw7vQTyT7zSKgisAjOxWIk+AnDbNajZ3bhBH+AzP+APWl4FywDykIFLigO/8rCO7//o4Ziz932ynSOWiX15mirIjOz3tghwZMi6nl6m/cLxQWv1flKVt8MJdLr4G3ezdizHk8wEvOuNSK+F75ToJ+esUTK6tL50++8qNb6FBsB4Y3cmuLI1sTTZ6swtnmUALc7V2oijznmX89qG7I4y7SFDUEhVlkhy1zkHrq53ZMUgqutRCDdad7GAkTqtSL9eNYl3lfdSe5mAzz2cZ4l6CiE+yJo5GGcFkv9n31/sf//eNecJ7G0ulmH9+H5vaJ7SvMYuLUkw/FigQ9++0p5SiibEx1ZMTPC32mr8UoN0ZiAvhWQ3qeGj4MyoxEm1lw0I5o7VU/xuUA1p6+EDaGCyaYbU3GyemvH/sZsJj331zvImykLVCX9GB5Js7pzHSkAD/QcBgE49GsYlelBnC+4ZZj68eArtaIm0eT91F2FqeD/r+odA8VHiXTWH9Y3rk=
     * keyboardPwdVersion : 4
     * hardwareRevision : 1.1
     * lockVersion : {"showAdminKbpwdFlag":true,"groupId":1,"protocolVersion":3,"protocolType":5,"orgId":1,"logoUrl":"","scene":2}
     * userType : 110301
     * lockName : M101T_b9de58
     * firmwareRevision : 4.1.18.0131
     */

    private long date;
    private int specialValue;
    private String lockAlias;
    private String modelNum;
    private String lockMac;
    private int lockId;
    private int electricQuantity;
    private String lockData;
    private int keyboardPwdVersion;
    private String hardwareRevision;
    private LockVersionObj lockVersion;
    private String userType;
    private String lockName;
    private String firmwareRevision;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(int specialValue) {
        this.specialValue = specialValue;
    }

    public String getLockAlias() {
        return lockAlias;
    }

    public void setLockAlias(String lockAlias) {
        this.lockAlias = lockAlias;
    }

    public String getModelNum() {
        return modelNum;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public String getLockMac() {
        return lockMac;
    }

    public void setLockMac(String lockMac) {
        this.lockMac = lockMac;
    }

    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public int getElectricQuantity() {
        return electricQuantity;
    }

    public void setElectricQuantity(int electricQuantity) {
        this.electricQuantity = electricQuantity;
    }

    public String getLockData() {
        return lockData;
    }

    public void setLockData(String lockData) {
        this.lockData = lockData;
    }

    public int getKeyboardPwdVersion() {
        return keyboardPwdVersion;
    }

    public void setKeyboardPwdVersion(int keyboardPwdVersion) {
        this.keyboardPwdVersion = keyboardPwdVersion;
    }

    public String getHardwareRevision() {
        return hardwareRevision;
    }

    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public LockVersionObj getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(LockVersionObj lockVersion) {
        this.lockVersion = lockVersion;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getFirmwareRevision() {
        return firmwareRevision;
    }

    public void setFirmwareRevision(String firmwareRevision) {
        this.firmwareRevision = firmwareRevision;
    }
}
