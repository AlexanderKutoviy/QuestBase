package com.questbase.app.cache;

import android.graphics.Bitmap;

import com.annimon.stream.Optional;

import rx.Observable;

public interface BitmapCache {
    Observable<Optional<Bitmap>>  requestBitmap(String url);
}
