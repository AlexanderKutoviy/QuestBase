package com.questbase.app.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.annimon.stream.Optional;
import com.squareup.picasso.Picasso;
import com.questbase.app.CommonUtils;
import com.questbase.app.utils.Lu;
import com.questbase.app.utils.RespoSchedulers;

import rx.Observable;

public class PicassoBitmapCache implements BitmapCache {

    private final Context ctx;

    public PicassoBitmapCache(Context ctx) {
        this.ctx = ctx.getApplicationContext();
    }

    @Override
    public Observable<Optional<Bitmap>> requestBitmap(String url) {
        return Observable.<Optional<Bitmap>>create(subscriber -> {
            try {
                subscriber.onNext(Optional.ofNullable(Picasso.with(ctx).load(
                        CommonUtils.HOST_PREFIX + url).get()));
            } catch (Exception e) {
                Lu.handleTolerableException(e);
                subscriber.onNext(Optional.<Bitmap>empty());
            } finally {
                subscriber.onCompleted();
            }
        }).subscribeOn(RespoSchedulers.io()).observeOn(RespoSchedulers.main());
    }
}
