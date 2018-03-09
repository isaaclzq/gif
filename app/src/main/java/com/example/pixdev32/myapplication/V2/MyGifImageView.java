package com.example.pixdev32.myapplication.V2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.icu.util.Measure;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by pixdev32 on 3/8/18.
 */

public class MyGifImageView extends GifImageView {

    private Context mContext;
    private Handler UIHandler;
    private GifDownloader.OnDownloadCompleteListener listener;

    private final int MAX_HEIGHT = 300;
    private final int MAX_WIDTH = 0;

    public MyGifImageView(Context context) {
        super(context);
        mContext = context;
        initCallback();
    }

    public MyGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initCallback();
    }

    public MyGifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initCallback();
    }

    public MyGifImageView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        mContext = context;
        initCallback();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Drawable gifDrawable = getDrawable();
        if(!(getDrawable() instanceof GifDrawable)){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
//        switch(widthMode){
//            case MeasureSpec.AT_MOST:
//                break;
//            case MeasureSpec.EXACTLY:
//                break;
//            case MeasureSpec.UNSPECIFIED:
//                break;
//        }

        switch (heightMode){
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                if(gifDrawable != null){
                    int ratio = (((GifDrawable)gifDrawable).getCurrentFrame().getWidth() / (((GifDrawable)gifDrawable).getCurrentFrame().getHeight()));
                    if(ratio > 0)
                        height = width * 1 / ((((GifDrawable)gifDrawable).getCurrentFrame().getWidth() / (((GifDrawable)gifDrawable).getCurrentFrame().getHeight())));
                }
                if(height == 0) height = MAX_HEIGHT;
                break;
        }

        Log.d("OnMeasure", "W: " + width + ", H: " + height);
        setMeasuredDimension(width, height);

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initCallback() {
        listener = new GifDownloader.OnDownloadCompleteListener() {
            @Override
            public void onDownloadComplete(File file) {
                if (file == null) {
                    return;
                }
                try {
                    final GifDrawable drawable = new GifDrawable(file);
                    UIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setImageDrawable(drawable);
                            invalidate();
                            requestLayout();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        UIHandler = new Handler(mContext.getMainLooper());
    }

    public void setResource(String viewID, String url) {

        GifDownloader.getInstance(mContext).download(viewID, url, listener);

    }

    public void setImage(String viewID, String url) {

        GifDownloader.getInstance(mContext).download(viewID, url, new GifDownloader.OnDownloadCompleteListener() {
            @Override
            public void onDownloadComplete(File file) {
                if (file == null) {
                    return;
                }
                try {
                    final GifDrawable drawable = new GifDrawable(file);
                    UIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setImageBitmap(drawable.getCurrentFrame());
                            invalidate();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
