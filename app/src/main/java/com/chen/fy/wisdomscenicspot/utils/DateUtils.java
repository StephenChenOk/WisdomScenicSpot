package com.chen.fy.wisdomscenicspot.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 时间格式转化1( 毫秒数 转为 年.月.日 )
     *
     * @param time 当前评价的时间
     * @return 格式化好的日期格式
     */
    public static String dateToString(long time) {
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return simpleDateFormat.format(date);
    }
}