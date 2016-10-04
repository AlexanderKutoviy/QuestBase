package com.questbase.app.controller;

import rx.Observable;

public interface Syncable {

    Observable<SyncResult> sync();

    enum SyncResult {
        FAIL, SUCCESS
    }
}
