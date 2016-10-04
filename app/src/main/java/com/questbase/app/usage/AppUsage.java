package com.questbase.app.usage;

/**
 * Datum structure of application usage measurement
 */
public class AppUsage {

    public String packageName;
    public long utcTime;

    public AppUsage() {
    }

    public AppUsage(String packageName, long utcTime) {
        this.packageName = packageName;
        this.utcTime = utcTime;
    }
}
