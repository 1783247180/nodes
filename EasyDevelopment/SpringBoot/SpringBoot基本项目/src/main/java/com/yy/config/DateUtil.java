package com.yy.config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date getDate(String sDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",java.util.Locale.US);
        try {
            Date date = simpleDateFormat.parse(sDate);
            return date;
        }catch(Exception e){
            System.out.println("时间转化异常");
            return null;
        }

    }
}
