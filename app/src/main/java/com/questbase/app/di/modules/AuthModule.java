package com.questbase.app.di.modules;

import android.content.Context;

import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.auth.PrefsAuthDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthModule {

    @Singleton
    @Provides
    AuthDao provideAuthDao(Context ctx) {
        return new PrefsAuthDao(ctx);
    }
}
