package com.questbase.app.di.modules;

import android.content.Context;
import android.content.res.AssetManager;

import com.questbase.app.controller.files.DefaultFilesController;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.font.FontController;
import com.questbase.app.net.RestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FileModule {
    @Singleton
    @Provides
    FilesController provideFileController(Context context, RestApi restApi) {
        return new DefaultFilesController(context, restApi);
    }

    @Singleton
    @Provides
    FontController provideFontController(AssetManager assetManager) {
        return new FontController(assetManager);
    }
}