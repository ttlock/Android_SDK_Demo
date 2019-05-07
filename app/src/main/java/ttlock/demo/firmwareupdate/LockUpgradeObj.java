package ttlock.demo.firmwareupdate;

import com.ttlock.bl.sdk.entity.DeviceInfo;

import ttlock.demo.model.ServerError;

/**
 * Created by TTLock on 2019/5/6.
 */

public class LockUpgradeObj extends ServerError {
    //TODO:
    /**
     * needUpgrade : 1
     * modelNum : SN118_PV53
     * hardwareRevision : 1.3
     * firmwareInfo : {"modelNum":"SN118_PV53","hardwareRevision":"1.3","firmwareRevision":"4.0.17.0721"}
     * version : 4.1.29.0722
     * firmwareRevision : 4.0.17.0721
     */

    private int needUpgrade;
    private String modelNum;
    private String hardwareRevision;
    private DeviceInfo firmwareInfo;
    private String version;
    private String firmwareRevision;

    public int getNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(int needUpgrade) {
        this.needUpgrade = needUpgrade;
    }

    public String getModelNum() {
        return modelNum;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public String getHardwareRevision() {
        return hardwareRevision;
    }

    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public DeviceInfo getFirmwareInfo() {
        return firmwareInfo;
    }

    public void setFirmwareInfo(DeviceInfo firmwareInfo) {
        this.firmwareInfo = firmwareInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirmwareRevision() {
        return firmwareRevision;
    }

    public void setFirmwareRevision(String firmwareRevision) {
        this.firmwareRevision = firmwareRevision;
    }
}
