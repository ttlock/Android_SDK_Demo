package ttlock.demo.model;

/**
 * Created by TTLock on 2019/4/23.
 */

public class AccountInfo extends ServerError {
    /**
     * access_token : e335b88621994a689e42ffcb57ecdf6e
     * refresh_token : 5119b4d9015b73cabd6bf69dedb6a1c9
     * uid : 2458
     * openid : 1146717084
     * scope : user,key,room
     * token_type : Bearer
     * expires_in : 2849185
     */

    private String access_token;
    private String refresh_token;
    private int uid;
    private int openid;
    private String scope;
    private String token_type;
    private int expires_in;

    private String md5Pwd;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getMd5Pwd() {
        return md5Pwd;
    }

    public void setMd5Pwd(String md5Pwd) {
        this.md5Pwd = md5Pwd;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", uid=" + uid +
                ", openid=" + openid +
                ", scope='" + scope + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
