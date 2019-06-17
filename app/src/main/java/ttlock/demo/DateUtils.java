package ttlock.demo;



import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class DateUtils {
    public static String getMillsTimeFormat(long date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault());
        return dateFormat.format(date);
    }
}
