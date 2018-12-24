package onlyf.omgtu.ru.appwthr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static long getCurrentTimeStamp() {
        return (new Date()).getTime();
    }

    public static Date getDateFromDbValue(long value) {
        return new Date(value);
    }

    public static String formatDate(Date date) {
        return (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(date));
    }

}
