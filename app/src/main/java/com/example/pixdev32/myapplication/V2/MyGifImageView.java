package com.example.pixdev32.myapplication.V2;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

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
                            setBackground(drawable);
                            invalidate();
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

}
