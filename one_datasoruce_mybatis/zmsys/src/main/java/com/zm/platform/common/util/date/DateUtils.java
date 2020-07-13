package com.zm.platform.common.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat CHINESE_FORMAT = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
    private static final SimpleDateFormat LINE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDateToChinese (Date date) {
        return CHINESE_FORMAT.format(date);
    }
    public static String formatDateToLine (Date date) {
        return LINE_FORMAT.format(date);
    }
    public static String formatDateToOther (Date date, String format) {
        return new SimpleDateFormat("yyyy"+format+"MM"+format+"dd HH:mm:ss").format(date);
    }

}
