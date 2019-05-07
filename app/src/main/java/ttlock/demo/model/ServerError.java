package ttlock.demo.model;

/**
 * Created by TTLock on 2019/4/23.
 */

public class ServerError {
    /**
     * errcode : 10000
     * errmsg : invalid client_id
     * description : client_id不存在
     */

    public int errcode;
    public String errmsg;
    public String description;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
