package ttlock.demo.model;

import java.io.Serializable;

/**
 * Created by TTLock on 2019/4/23.
 */

public class LockVersionObj implements Serializable {
    /**
     * showAdminKbpwdFlag : true
     * groupId : 1
     * protocolVersion : 3
     * protocolType : 5
     * orgId : 1
     * logoUrl :
     * scene : 2
     */

    private boolean showAdminKbpwdFlag;
    private int groupId;
    private int protocolVersion;
    private int protocolType;
    private int orgId;
    private String logoUrl;
    private int scene;

    public boolean isShowAdminKbpwdFlag() {
        return showAdminKbpwdFlag;
    }

    public void setShowAdminKbpwdFlag(boolean showAdminKbpwdFlag) {
        this.showAdminKbpwdFlag = showAdminKbpwdFlag;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getScene() {
        return scene;
    }

    public void setScene(int scene) {
        this.scene = scene;
    }
}
