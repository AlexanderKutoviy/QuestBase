package com.questbase.app.di.modules;

import android.content.Context;

import com.questbase.app.cache.BitmapCache;
import com.questbase.app.cache.PicassoBitmapCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CacheModule {

    @Singleton
    @Provides
    BitmapCache provideBitmapCache(Context ctx) {
        return new PicassoBitmapCache(ctx);
    }
}
