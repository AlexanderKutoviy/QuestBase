package com.questbase.app.utils;

import android.util.Log;


public class Lu {

    private static final String LOG_TAG = "TheRespoApp";

    public static void e(String msg) {
        Log.e(LOG_TAG, msg);
    }

    public static void es(String msg) {
        String msgs[] = msg.split("\r");
        for (String msgLine : msgs) {
            String msgs2[] = msgLine.split("\n");
            for (String msgSubLine : msgs2) {
                Log.e(LOG_TAG, msgSubLine);
            }
        }
    }

    public static void logMark(String s) {
        Lu.e(s);
    }

    public static void handleException(Exception e) {
        handleTolerableException(e);
    }

    public static void handleTolerableException(Throwable e) {
        Log.wtf(LOG_TAG, e);
    }

    public static void handleSilentException(Exception e) {
        Log.wtf(LOG_TAG, e);
    }

    public static void v(String msg) {
        Log.v(LOG_TAG, msg);
    }
}
