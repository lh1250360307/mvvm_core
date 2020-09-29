package com.jiantaokeji.answerliu.mvvm_core.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;


/**
 *  Created by LH on 2016/6/22.
 *  Glide加载圆角图片工具类
 *  将图片转化为圆角
 *  构造中第二个参数定义半径
 */
public class GlideRoundTransform extends BitmapTransformation {

    private Paint mBorderPaint;
    private float mBorderWidth;

    public GlideRoundTransform() {

    }

    public GlideRoundTransform(int borderWidth, int borderColor) {

        mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        if (mBorderPaint != null) {
            float borderRadius = r - mBorderWidth / 2;
            canvas.drawCircle(r, r, borderRadius, mBorderPaint);
        }
        return result;
    }
}
