package com.questbase.app.db;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Database;
import com.questbase.app.QuestBaseApplication;

@Database(name = RespoDatabase.NAME, version = RespoDatabase.VERSION)
public class RespoDatabase {

    public static final String TAG = RespoDatabase.class.getSimpleName();
    public static final String NAME = "respo";
    public static final int VERSION = 1;

    public static void migrate() {
        QuestBaseApplication.migrateDataBase();
        Log.d(TAG, "migration to VERSION " + VERSION + " executed");
    }
}