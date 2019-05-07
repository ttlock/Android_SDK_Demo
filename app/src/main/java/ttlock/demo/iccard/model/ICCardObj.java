package ttlock.demo.iccard.model;

import java.io.Serializable;

/**
 * Created on  2019/4/28 0028 17:07
 *
 * @author theodre
 */
public class ICCardObj implements Serializable {
    int cardId;
    int lockId;
    String cardNumber;
    String cardName;
    long startDate;
    long endDate;
    long createDate;

    public int getCardId() {
        return cardId;
    }

    public int getLockId() {
        return lockId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardName() {
        return cardName;
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
