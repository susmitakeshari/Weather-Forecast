package task.susmita.example.com.wheatherproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    public static final String DateFormat="yyyy-MM-dd HH:mm:ss";
    public static Date getDate(String str){
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        /*Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(str));
            calendar.setTimeInMillis(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        Date date = null;
        try {
             date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
