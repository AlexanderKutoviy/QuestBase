package com.questbase.app.usage.packageprovider;

import com.annimon.stream.Optional;

public interface AppPackageProvider {

    Optional<String> getVisibleAppPackage();
}
