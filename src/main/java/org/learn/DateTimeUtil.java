package org.learn;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
