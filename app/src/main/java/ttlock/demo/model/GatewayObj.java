package ttlock.demo.model;

import java.io.Serializable;

/**
 * Created by TTLock on 2019/5/5.
 */

public class GatewayObj implements Serializable {
    /**
     * gatewayMac : 0B:91:C7:BD:61:FF
     * lockNum : 0
     * gatewayName : ttt
     * isOnline : 0
     * gatewayVersion : 2
     * gatewayId : 299
     */

    private String gatewayMac;
    private int lockNum;
    private String gatewayName;
    private int isOnline;
    private int gatewayVersion;
    private int gatewayId;

    public String getGatewayMac() {
        return gatewayMac;
    }

    public void setGatewayMac(String gatewayMac) {
        this.gatewayMac = gatewayMac;
    }

    public int getLockNum() {
        return lockNum;
    }

    public void setLockNum(int lockNum) {
        this.lockNum = lockNum;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getGatewayVersion() {
        return gatewayVersion;
    }

    public void setGatewayVersion(int gatewayVersion) {
        this.gatewayVersion = gatewayVersion;
    }

    public int getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(int gatewayId) {
        this.gatewayId = gatewayId;
    }
}
