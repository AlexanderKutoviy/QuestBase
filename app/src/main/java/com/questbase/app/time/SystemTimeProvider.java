package com.questbase.app.time;

public class SystemTimeProvider implements TimeProvider {

    @Override
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
