package ttlock.demo.lock.model;

import java.util.ArrayList;

/**
 * Created on  2019/4/28 0028 14:26
 *
 * @author theodre
 */
public class KeyListObj {
    int total;
    int pages;
    int pageNo;
    int pageSize;

    ArrayList<KeyObj> list;

    public ArrayList<KeyObj> getList() {
        return list;
    }

}
