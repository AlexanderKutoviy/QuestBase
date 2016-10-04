package com.questbase.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SyncService extends Service {

    //storage for the instance
    private static SyncAdapter syncAdapterInstance = null;
    //object to use a thread-safe lock
    private static final Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SyncUtils.SYNC_TAG, "Service created");
        synchronized (syncAdapterLock) {
            if (syncAdapterInstance == null) {
                syncAdapterInstance = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(SyncUtils.SYNC_TAG, "Service destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapterInstance.getSyncAdapterBinder();
    }
}
