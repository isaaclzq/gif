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
    private boolean mIsPlayingGif = false;
    private GifUtils mGifUtils;
    private GifDecoder gifDecoder;
    private Bitmap mTmpBitmap;

    int j;
    int repetitionCounter1 = 0;
    int frameCount = 0;
    int loopCount = 0;
    AnimationDrawable animationDrawable;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if(mGifUtils != null){
//            if(repetitionCounter1 <= loopCount && frameCount > 0){
//                mTmpBitmap = mGifUtils.getFrame(j % frameCount);
//                j++;
//                int t = mGifUtils.getDelay(j);
//                canvas.drawBitmap(mTmpBitmap, 0, 0, new Paint());
//                if (loopCount != 0) {
//                    repetitionCounter1++;
//                }
//                postInvalidateDelayed(t);
//            }
//        }

//        if(gifDecoder != null){
//            if(repetitionCounter1 <= loopCount && gifDecoder.getFrameCount() > 0){
//                gifDecoder.advance();
//                mTmpBitmap = gifDecoder.getNextFrame();
//                int t = gifDecoder.getNextDelay();
//                if(mTmpBitmap != null && !mTmpBitmap.isRecycled())
//                    canvas.drawBitmap(mTmpBitmap, 0, 0, new Paint());
//                if (loopCount != 0) {
//                    repetitionCounter1++;
//                }
//                postInvalidateDelayed(t);
//            }
//        }
    }

    private Runnable gifRunnable = new Runnable() {
        //        @Override
        public void run() {
            final int n = mGifUtils.getFrameCount();
            final int ntimes = mGifUtils.getLoopCount();
            int repetitionCounter = 0;
//            if(getTag() != this){
//                Log.d(TAG, "Not the same runnable");
//                return;
//            }
            do {
                Log.d(TAG, "Running");
                for (int i = 0; i < n; i++) {
                    mTmpBitmap = mGifUtils.getFrame(i);
                    int t = mGifUtils.getDelay(i);
                    mHandler.post(mUpdateResults);
//                        try {
//                            Thread.sleep(t);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                }
                if (ntimes != 0) {
                    repetitionCounter++;
                }
            } while (mIsPlayingGif && (repetitionCounter <= ntimes));
        }

    };
    private Thread mGifThread;

    final Handler mHandler = new Handler();

    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
                GifImageViewBase.this.setImageBitmap(mTmpBitmap);
            }
        }
    };

    public GifImageViewBase(Context context, InputStream stream) {
        super(context);
//        startGif(stream);
    }

    public GifImageViewBase(Context context) {
        super(context);
    }

    public GifImageViewBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GifImageViewBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        if (attrs.getAttributeName(1).equals("background")) {
//            int id = Integer.parseInt(attrs.getAttributeValue(1).substring(1));
//            Log.i("===================", attrs.getAttributeValue(1));
//            InputStream is = context.getResources().openRawResource(+R.drawable.clap);
//            startGif(is);
//        }
    }

    public void startGif(InputStream stream) {

        if (mGifThread != null) {
//            stopRendering();
//            stopGifThread();
//            setTag(gifRunnable);
        } else {
            mGifUtils = new GifUtils();
        }

        gifDecoder = new GifDecoder();
        try {
            gifDecoder.read(stream, stream.available());
            frameCount = gifDecoder.getFrameCount();
            loopCount = gifDecoder.getLoopCount();
            if(gifDecoder != null){
                if(repetitionCounter1 <= loopCount && gifDecoder.getFrameCount() > 0){
                    animationDrawable = new AnimationDrawable();
                    for(int i = 0; i < gifDecoder.getFrameCount(); i++){
                        gifDecoder.advance();
                        Bitmap bm = Bitmap.createBitmap(gifDecoder.getNextFrame());

                        int t = gifDecoder.getNextDelay();
                        animationDrawable.addFrame(new BitmapDrawable(getResources(), bm), t);
                    }
                    GifImageViewBase.this.post(new Runnable() {
                        @Override
                        public void run() {
                            GifImageViewBase.this.setImageDrawable(animationDrawable);
                            animationDrawable.start();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        mGifUtils.read(stream);
//        animationDrawable = new AnimationDrawable();
//        for (int i = 0; i < mGifUtils.getFrameCount(); i++) {
//            mTmpBitmap = mGifUtils.getFrame(i);
//            int t = mGifUtils.getDelay(i);
//            animationDrawable.addFrame(new BitmapDrawable(getResources(), mTmpBitmap), t);
//        }

//        GifImageViewBase.this.post(new Runnable() {
//            @Override
//            public void run() {
//                GifImageViewBase.this.setImageDrawable(animationDrawable);
//                animationDrawable.start();
//                if(animationDrawable.isRunning()){
//                    Log.d(TAG, "running");
//                }
//            }
//        });

//        frameCount = mGifUtils.getFrameCount();
//        loopCount = mGifUtils.getLoopCount();
//        Log.d(TAG, "Reading inputstream");
//        Log.d(TAG, "Status - " + status);
//        mIsPlayingGif = true;
//        mGifThread = new Thread(gifRunnable);
//        mGifThread = new Thread(new Runnable() {
//            public void run() {
//                final int n = mGifUtils.getFrameCount();
//                final int ntimes = mGifUtils.getLoopCount();
//                int repetitionCounter = 0;
//                do {
//                    for (int i = 0; i < n; i++) {
//                        mTmpBitmap = mGifUtils.getFrame(i);
//                        int t = mGifUtils.getDelay(i);
//                        mHandlerBasePoi.post(mUpdateResults);
//                        try {
//                            Thread.sleep(t);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (ntimes != 0) {
//                        repetitionCounter++;
//                    }
//                } while (mIsPlayingGif && (repetitionCounter <= ntimes));
//            }
//        });
//        mGifThread.start();
    }

    public void startRendering() {
        mGifThread.start();
    }

    public void stopRendering() {
        mIsPlayingGif = false;
    }

    public void stopGifThread () {
        mGifThread.interrupt();
    }


}
