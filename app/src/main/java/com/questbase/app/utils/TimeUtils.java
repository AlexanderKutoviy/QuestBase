package com.questbase.app.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    private static final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

    public static String formatTime(long time) {
        return dateFormat.format(new Date(time));
    }
}