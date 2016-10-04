package com.questbase.app.usage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Service is responsible for:
 * 1. Screen on / off broadcast receiver registration
 * 2. Alarm timer registration/unregistration
 */
public class UsageRegistrationService extends Service {

    private static final String TAG = UsageRegistrationService.class.getSimpleName();
    private static final String SCREEN_STATE = "screen_state";
    private static final long ONE_MINUTE = 60000;
    private static boolean receiverRegistered;
    private BroadcastReceiver receiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerScreenBroadcastReceiver();
        handleScreenStateChange(intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        synchronized (UsageRegistrationService.class) {
            if (receiver != null) {
                unregisterReceiver(receiver);
                receiverRegistered = false;
            }
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleScreenStateChange(Intent screenStateIntent) {
        if (screenStateIntent != null && screenStateIntent.hasExtra(SCREEN_STATE)) {
            boolean screenOn = screenStateIntent.getBooleanExtra(SCREEN_STATE, false);
            if (screenOn) {
                startService(createScanningIntent(true));
                startRepeatingAlarm();
            } else {
                startService(createScanningIntent(false));
                stopRepeatingAlarm();
            }
        }
    }

    private void registerScreenBroadcastReceiver() {
        synchronized (UsageRegistrationService.class) {
            if (!receiverRegistered) {
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_ANSWER);
                receiver = new ScreenBroadcastReceiver();
                receiverRegistered = true;
                registerReceiver(receiver, filter);
            }
        }
    }

    private void startRepeatingAlarm() {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(), ONE_MINUTE, createStartScanningPendingIntent());
    }

    private void stopRepeatingAlarm() {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(createStartScanningPendingIntent());
    }

    private Intent createScanningIntent(boolean start) {
        Intent intent = new Intent(getApplicationContext(), UsageService.class);
        if (start) {
            intent.putExtra(UsageService.START_SCANNING, true);
        } else {
            intent.putExtra(UsageService.STOP_SCANNING, true);
        }
        return intent;
    }

    private PendingIntent createStartScanningPendingIntent() {
        Intent repeatingIntent = createScanningIntent(true);
        return PendingIntent.getService(getApplicationContext(), 0, repeatingIntent, 0);
    }

    public static void startUsageRegistrationService(Context context) {
        context.startService(new Intent(context, UsageRegistrationService.class));
    }

    public class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean screenOn;
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                screenOn = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                screenOn = true;
            } else {
                return;
            }

            Intent screenStateIntent = new Intent(context, UsageRegistrationService.class);
            screenStateIntent.putExtra(SCREEN_STATE, screenOn);
            context.startService(screenStateIntent);
        }
    }
}