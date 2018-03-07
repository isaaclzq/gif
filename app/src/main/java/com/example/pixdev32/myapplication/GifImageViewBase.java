package com.example.pixdev32.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pixdev32 on 3/5/18.
 */

public class GifImageViewBase extends ImageView {

    private final String TAG = GifImageViewBase.class.getSimpleName();
    private GifDecoder gifDecoder;

    int repetitionCounter1 = 0;
    int frameCount = 0;
    int loopCount = 0;
    AnimationDrawable animationDrawable;


    public GifImageViewBase(Context context, InputStream stream) {
        super(context);
    }

    public GifImageViewBase(Context context) {
        super(context);
    }

    public GifImageViewBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GifImageViewBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startGif(String resId) {

        if (gifDecoder == null) {
            gifDecoder = new GifDecoder();
        }

        try {

            animationDrawable = GifCache.getInstance().get(resId);

            if (animationDrawable != null) {
                GifImageViewBase.this.post(new Runnable() {
                    @Override
                    public void run() {
                        GifImageViewBase.this.setImageDrawable(animationDrawable);
                    }
                });
                return;
            }

            InputStream stream = getContext().getResources().openRawResource(Integer.parseInt(resId));

            gifDecoder.read(stream, stream.available());
            frameCount = gifDecoder.getFrameCount();
            loopCount = gifDecoder.getLoopCount();
            if (repetitionCounter1 <= loopCount && gifDecoder.getFrameCount() > 0) {
                animationDrawable = new AnimationDrawable();
                for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                    gifDecoder.advance();
                    Bitmap bm = gifDecoder.getNextFrame();
                    float ratio = bm.getWidth() / bm.getHeight();
                    int resultHeight = bm.getHeight() / 2;
                    int resultWidth = (int) ratio * resultHeight;
//                    if (resultWidth > 300) {
//                        resultWidth = 300;
//                        resultHeight = (int) (300 / ratio);
//                    }
                    Bitmap nbm = Bitmap.createScaledBitmap(bm, resultWidth, resultHeight, false);
                    int t = gifDecoder.getNextDelay();
                    animationDrawable.addFrame(new BitmapDrawable(getResources(), nbm), t);
                }
                gifDecoder.clearFrames();
                GifCache.getInstance().put(resId, animationDrawable);
                GifImageViewBase.this.post(new Runnable() {
                    @Override
                    public void run() {
                        GifImageViewBase.this.setImageDrawable(animationDrawable);
                        animationDrawable.start();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
