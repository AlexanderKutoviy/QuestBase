package com.questbase.app.di.modules;

import com.questbase.app.controller.files.FilesController;
import com.questbase.app.obsolete.ScriptManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ScriptManagerModule {
    @Singleton
    @Provides
    ScriptManager provideScriptManager(FilesController filesController) {
        return new ScriptManager(filesController);
    }
}
