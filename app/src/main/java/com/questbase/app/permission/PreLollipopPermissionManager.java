package com.questbase.app.permission;

public class PreLollipopPermissionManager implements PermissionManager {
    @Override
    public boolean hasUsageStatsPermission() {
        return true;
    }
}
