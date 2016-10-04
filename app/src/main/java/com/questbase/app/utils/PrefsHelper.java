package com.questbase.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {

    private final SharedPreferences prefs;
    private static final String PREFS_NAME = "RespoPreferences";
    private static final String DB_IMPORTED = "isDbImported";
    private static final String CONTENT_IMPORTED = "isContentImported";
    private static final String DEBUG_ROLE = "debugRole";
    private static final String DB_VERSION = "dbVersion";
    private static final String LAST_SYNC_TIME = "lastSyncTime";
    private static final int DEFAULT_INT_VALUE = 0;
    private static final long DEFAULT_LONG_VALUE = 0;

    public PrefsHelper(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setIsDbImported(boolean value) {
        synchronized (prefs) {
            prefs.edit().putBoolean(DB_IMPORTED, value).apply();
        }
    }

    public boolean isDbImported() {
        synchronized (prefs) {
            return prefs.contains(DB_IMPORTED) && prefs.getBoolean(DB_IMPORTED, true);
        }
    }

    public void setDbVersion(int value) {
        synchronized (prefs) {
            prefs.edit().putInt(DB_VERSION, value).apply();
        }
    }

    public int getDbVersion() {
        synchronized (prefs) {
            return prefs.getInt(DB_VERSION, DEFAULT_INT_VALUE);
        }
    }

    public void setIsContentImported(boolean value) {
        synchronized (prefs) {
            prefs.edit().putBoolean(CONTENT_IMPORTED, value).apply();
        }
    }

    public boolean isContentImported() {
        synchronized (prefs) {
            return prefs.contains(CONTENT_IMPORTED) && prefs.getBoolean(CONTENT_IMPORTED, true);
        }
    }

    public void setDebugRole(boolean value) {
        synchronized (prefs) {
            prefs.edit().putBoolean(DEBUG_ROLE, value).apply();
        }
    }

    public boolean hasDebugRole() {
        synchronized (prefs) {
            return prefs.contains(DEBUG_ROLE) && prefs.getBoolean(DEBUG_ROLE, true);
        }
    }

    public void removeDebugRole() {
        synchronized (prefs) {
            prefs.edit().remove(DEBUG_ROLE).apply();
        }
    }

    public void setLastSyncTime(long value) {
        synchronized (prefs) {
            prefs.edit().putLong(LAST_SYNC_TIME, value).apply();
        }
    }

    public long getLastSyncTime() {
        synchronized (prefs) {
            return prefs.getLong(LAST_SYNC_TIME, DEFAULT_LONG_VALUE);
        }
    }
}