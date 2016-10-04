package com.questbase.app.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.questbase.app.CommonUtils;

import java.io.File;

public class FileUtils {

    private static final String ROBOLECTRIC = "robolectric";

    public static File getResultImage(Context ctx, int formId, String image) {
        return new File(new File(getFormDirectory(ctx, formId), "result"), image + ".jpg");
    }

    private static File getFormDirectory(Context ctx, int id) {
        return new File(getFormsParentDirectory(ctx), String.valueOf(id));
    }

    public static File getFormsParentDirectory(Context ctx) {
        File parentDir;
        if (CommonUtils.EMULATOR_TESTING) {
            parentDir = new File(ctx.getFilesDir(), ctx.getPackageName());
        } else {
            parentDir = getDataDirectory(ctx);
        }
        if (ROBOLECTRIC.equals(Build.FINGERPRINT)) {
            return new File("src/main/assets/content/forms"); // Directory for tests
        } else {
            return new File(parentDir, "forms");
        }
    }

    public static File getCacheDirectory(Context ctx) {
        return new File(getDataDirectory(ctx), "cache");
    }

    private static File getDataDirectory(Context ctx) {
        return new File(new File(new File(
                Environment.getExternalStorageDirectory(), "Android"), "data"), ctx.getPackageName());
    }
}
