package com.questbase.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;


public class CommonUtils {

    public static final boolean EMULATOR_TESTING = false;

    /**
     * Package name, used as prefix for bundle constants.
     */
    public static final String PACKAGE = "com.therespo";

    /**
     * Negative value is used as marker when id is undefined.
     */
    public static final int UNDEFINED = -1;

    /**
     *
     */
    public static final boolean IGNORE_CERTIFICATES = false;

    /**
     * Production value is: https://therespo.com
     * <p/>
     * Possible dev/debug values may be like that (e.g. for VPN-accessed dev server):
     * https://192.168.50.22:7075
     */
    public static final String HOST_PREFIX = "https://therespo.com";
    public static final String DEV_HOST_PREFIX = "https://dev.therespo.com";
//    public static final String HOST_PREFIX = "https://192.168.1.111:7075";
    public static final String THERESPO_HOST_PREFIX = "https://therespo.com";
//    public static final String HOST_PREFIX = "https://192.168.50.22:7075";


    public static void setOrientation(Activity activity) {
        if (isScreenLarge(activity.getResources())) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private static boolean isScreenLarge(Resources res) {
        final int screenSize = res.getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
                || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static float getWidthDp(Context ctx) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    public static float getHeightDp(Context ctx) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels / displayMetrics.density;
    }

    private static boolean isConnectionAvailable(Context ctx, int type) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo info = connectivityManager.getNetworkInfo(type);
            return info != null && info.isConnected();
        }
    }

    public static boolean isWifiConnected(Context ctx) {
        return isConnectionAvailable(ctx, ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isMobileNetworkConnected(Context ctx) {
        return isConnectionAvailable(ctx, ConnectivityManager.TYPE_MOBILE);
    }
}
