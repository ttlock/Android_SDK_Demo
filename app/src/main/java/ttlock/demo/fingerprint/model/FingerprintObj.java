package ttlock.demo.fingerprint.model;

import java.io.Serializable;

/**
 * Created on  2019/4/28 0028 16:06
 *
 * @author theodre
 */
public class FingerprintObj implements Serializable {
    int fingerprintId;
    int lockId;
    String fingerprintNumber;
    String fingerprintName;
    long startDate;
    long endDate;
    long createDate;

    public int getFingerprintId() {
        return fingerprintId;
    }

    public int getLockId() {
        return lockId;
    }

    public String getFingerprintNumber() {
        return fingerprintNumber;
    }

    public String getFingerprintName() {
        return fingerprintName;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getCreateDate() {
        return createDate;
    }
}
