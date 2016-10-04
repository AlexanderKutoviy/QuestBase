package com.questbase.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.annimon.stream.Optional;
import com.questbase.app.sync.stubs.AuthenticatorService;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class SyncUtils {
    public final static String SYNC_TAG = "SYNC";
    private static final long SYNC_FREQUENCY = 60 * 15;  // 15 minutes
    private static final String CONTENT_AUTHORITY = "com.therespo.sync.stubs";
    private static Account account;

    private static volatile Long syncStartTime;
    private static volatile Long syncFinishTime;
    private static volatile Long syncDurationTime;
    private static volatile ReplaySubject<Optional<Long>> replaySubject = ReplaySubject.create(1);

    public static void createSyncAccount(Context context) {
        boolean newAccount = false;
        account = AuthenticatorService.getAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
            ContentResolver.addPeriodicSync(account, CONTENT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);
            newAccount = true;
        }
        if (newAccount) {
            triggerRefresh();
        }
    }

    public static boolean isSyncActive() {
        return ContentResolver.isSyncActive(account, CONTENT_AUTHORITY);
    }

    public static void triggerRefresh() {
        if (!isSyncActive()) {
            Bundle b = new Bundle();
            b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(AuthenticatorService.getAccount(), CONTENT_AUTHORITY, b);
        }
    }

    public static void setSyncFinishTime(Long syncFinishTime) {
        SyncUtils.syncFinishTime = syncFinishTime;
    }

    public static void setSyncStartTime(Long syncStartTime) {
        SyncUtils.syncStartTime = syncStartTime;
    }

    public static void setSyncDurationTime(Long syncDurationTime) {
        SyncUtils.syncDurationTime = TimeUnit.MILLISECONDS.toSeconds(syncDurationTime);
        replaySubject.onNext(Optional.ofNullable(TimeUnit.MILLISECONDS.toSeconds(syncDurationTime)));
    }

    public static Optional<Long> getSyncStartTime() {
        return Optional.ofNullable(syncStartTime);
    }

    public static Optional<Long> getSyncFinishTime() {
        return Optional.ofNullable(syncFinishTime);
    }

    public static Optional<Long> getSyncDurationTime() {
        return Optional.ofNullable(syncDurationTime);
    }

    public static Observable<Optional<Long>> observeSyncDurationTime() {
        return replaySubject;
    }
}
