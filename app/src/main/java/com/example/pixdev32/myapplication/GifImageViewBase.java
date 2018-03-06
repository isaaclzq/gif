package com.example.pixdev32.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by pixdev32 on 3/5/18.
 */

public class GifImageViewBase extends ImageView {

    private boolean mIsPlayingGif = false;
    private GifUtils mGifUtils;
    private Bitmap mTmpBitmap;
    private Runnable gifRunnable = new Runnable() {
        //        @Override
        public void run() {
            Log.d("GifImageViewBase", "run: ");

            final int n = mGifUtils.getFrameCount();
            final int ntimes = mGifUtils.getLoopCount();
            int repetitionCounter = 0;
            do {
                for (int i = 0; i < n; i++) {
                    mTmpBitmap = mGifUtils.getFrame(i);
                    int t = mGifUtils.getDelay(i);
                    mHandler.post(mUpdateResults);
                    try {
                        if (t >= 0) {
                            Thread.sleep(t);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        startGif(stream);
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
            InputStream is = context.getResources().openRawResource(+R.drawable.clap);
            startGif(is);
//        }
    }

    public void startGif(InputStream stream) {

        if (mGifThread != null) {
            stopRendering();
            stopGifThread();
        } else {
            mGifUtils = new GifUtils();
        }


        mGifUtils.read(stream);
        mIsPlayingGif = true;
        mGifThread = new Thread(gifRunnable);
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
        mGifThread.start();
    }

    //    public void startRendering() {
//        mIsPlayingGif = true;
////    }
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
