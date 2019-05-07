package ttlock.demo.gateway.model;

import com.ttlock.bl.sdk.gateway.model.DeviceInfo;

import ttlock.demo.model.ServerError;

/**
 * Created by TTLock on 2019/4/30.
 */

public class GatewayUpgradeObj extends ServerError {
    /**
     * needUpgrade : 1
     * firmwareInfo : {"modelNum":"SN227","hardwareRevision":"1.1","firmwareRevision":"1.0.19.0404"}
     * version : 1.3
     */

    private int needUpgrade;
    private DeviceInfo firmwareInfo;
    private String version;

    public int getNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(int needUpgrade) {
        this.needUpgrade = needUpgrade;
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

    public static class FirmwareInfoBean {
        /**
         * modelNum : SN227
         * hardwareRevision : 1.1
         * firmwareRevision : 1.0.19.0404
         */

        private String modelNum;
        private String hardwareRevision;
        private String firmwareRevision;

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

        public String getFirmwareRevision() {
            return firmwareRevision;
        }

        public void setFirmwareRevision(String firmwareRevision) {
            this.firmwareRevision = firmwareRevision;
        }
    }
}
