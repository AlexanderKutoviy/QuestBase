package com.questbase.app.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BottomCropImageView extends ImageView {

    public BottomCropImageView(Context context) {
        super(context);
        setup();
    }

    public BottomCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public BottomCropImageView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Matrix matrix = new Matrix();

        Drawable drawable = getDrawable();
        if (drawable != null) {
            float scale;
            int viewWidth = w - getPaddingLeft() - getPaddingRight();
            int viewHeight = h - getPaddingTop() - getPaddingBottom();
            int drawableWidth = getDrawable().getIntrinsicWidth();
            int drawableHeight = getDrawable().getIntrinsicHeight();

            //Get the scale
            if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
                scale = (float) viewHeight / (float) drawableHeight;
            } else {
                scale = (float) viewWidth / (float) drawableWidth;
            }

            //Define the rect to take image portion from
            RectF drawableRect = new RectF(0, drawableHeight - (viewHeight / scale), drawableWidth, drawableHeight);
            RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
            matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);
            setImageMatrix(matrix);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }
}