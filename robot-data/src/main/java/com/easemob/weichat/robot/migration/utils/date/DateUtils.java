package com.easemob.weichat.robot.migration.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dongwentao on 16/9/26.
 */
public class DateUtils {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public static String format(Date date){
        if (null==date) return null;
        return DateUtils.format.format(date);
    }

    public static String format(Date date,String pattern){
        if(null==date) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
