package com.questbase.app.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.controller.analytics.AnalyticsController;
import com.questbase.app.db.RespoDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AssetsUtils {

    private static final String TAG = AssetsUtils.class.getSimpleName();
    private static final String DB_JOURNAL = RespoDatabase.NAME + ".db-journal";
    private static final String DB_NAME = RespoDatabase.NAME + ".db";
    private static final String DB_DIR = "databases";
    private static final String FORMS = "content/forms";
    private static final String CONTENT = "content";
    private static final int BYTES_AMOUNT = 1024;

    public static void copyDatabase(Context context, PrefsHelper prefsHelper) {
        if (copyDbFile(DB_NAME, context) && copyDbFile(DB_JOURNAL, context)) {
            prefsHelper.setIsDbImported(true);
        }
    }

    public static void copyForms(Context context, PrefsHelper prefsHelper,
                                 AnalyticsController firebaseAnalyticsController) {
        File targetDir = FileUtils.getFormsParentDirectory(context);
        long startTime = System.currentTimeMillis();
        copyDirectory(FORMS, context.getAssets(), targetDir);
        firebaseAnalyticsController.logFilesCopyingDurationEvent(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
        prefsHelper.setIsContentImported(true);
    }

    private static boolean copyDbFile(String fileName, Context context) {
        InputStream dbInputStream = null;
        OutputStream dbOutputStream = null;
        try {
            File path = new File(context.getFilesDir().getParentFile(), DB_DIR);
            path.mkdirs();
            List<String> list = Stream.of(context.getAssets().list("")).collect(Collectors.toList());
            if (!list.contains(DB_JOURNAL) || !list.contains(DB_NAME)) {
                return false;
            }
            dbInputStream = context.getAssets().open(fileName);
            File outFileName = new File(path, fileName);
            dbOutputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[BYTES_AMOUNT];
            int length;
            while ((length = dbInputStream.read(buffer)) > 0) {
                dbOutputStream.write(buffer, 0, length);
            }
            dbOutputStream.flush();

            Log.d(TAG, "Database copying - SUCCESS");
            return true;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (dbInputStream != null) {
                    dbInputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Cannot close input stream", e);
            } finally {
                try {
                    if (dbOutputStream != null) {
                        dbOutputStream.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Cannot close output stream", e);
                }
            }
        }
    }

    private static void copyDirectory(String sourceName, AssetManager assetManager, File target) {
        String[] files;
        String[] content;
        target.mkdirs();
        try {
            content = assetManager.list(CONTENT);
            if (content.length == 0) {
                Log.e(TAG, "No forms directory");
                return;
            }
            files = assetManager.list(sourceName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get asset file list.", e);
        }
        Stream.of(files).forEach(oneFormDir -> {
            File formFolder = new File(target, oneFormDir);
            formFolder.mkdirs();
            try {
                Stream.of(assetManager.list(sourceName + "/" + oneFormDir))
                        .forEach(formFile ->
                                findAndCopyFile(formFile, formFolder, assetManager, sourceName, oneFormDir));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void findAndCopyFile(String formFile, File formFolder,
                                        AssetManager assetManager,
                                        String sourceName, String oneFormDir) {
        InputStream in = null;
        OutputStream out = null;
        File destination = new File(formFolder, formFile);
        try {
            in = assetManager.open(sourceName + "/" + oneFormDir + "/" + formFile);
            out = new FileOutputStream(destination);
            copyFile(in, out);
            out.flush();
            Log.d(TAG, "Form copying - SUCCESS");
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy asset file: " + formFile, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Cannot close input stream", e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Cannot close output stream", e);
                }
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BYTES_AMOUNT];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}