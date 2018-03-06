package com.example.pixdev32.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.util.LruCache;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by pixdev32 on 3/5/18.
 */

public class GifImageview extends View {

    private InputStream mInputStream;
    private Movie mMovie;
    private int mWidth, mHeight;
    private long mStart;
    private Context mContext;
    private HandlerThread mGifThread;
    private Handler mGifHandler;
    private Looper mGifLooper;

    private LruCache<Integer, Movie> mCache;

    public GifImageview(Context context) {
        super(context);
        this.mContext = context;
    }

    public GifImageview(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifImageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        if (attrs.getAttributeName(1).equals("background")) {
            int id = Integer.parseInt(attrs.getAttributeValue(1).substring(1));
            setGifImageResource(id);
        }
        mGifThread = new HandlerThread("Gif" + UUID.randomUUID());
        mGifThread.start();
        mGifLooper = mGifThread.getLooper();
        mGifHandler = new Handler(mGifLooper);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(Math.max(getSuggestedMinimumWidth(), 900),
                             Math.max(getSuggestedMinimumHeight(), 900));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = SystemClock.uptimeMillis();

        if (mStart == 0) {
            mStart = now;
        }

        if (mMovie != null) {
            canvas.scale((float)this.getWidth() / (float)mMovie.width(),(float)this.getHeight() /      (float)mMovie.height());
            int duration = mMovie.duration();
            if (duration == 0) {
                duration = 1000;
            }

            int relTime = (int) ((now - mStart) % duration);

            mMovie.setTime(relTime);

            mMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }

    public void setCache(LruCache cache) {
        mCache = cache;
    }

    public void setGifImageResource(final int resId) {
        mGifHandler.post(new Runnable() {
            @Override
            public void run() {
                mMovie = mCache.get(resId);

                if (mMovie == null) {
                    mInputStream = mContext.getResources().openRawResource(resId);
                    mMovie = Movie.decodeStream(mInputStream);
                    mCache.put(resId, mMovie);
                    try {
                        mInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mWidth = mMovie.width();
                mHeight = mMovie.height();
                postInvalidate();
            }
        });
    }

    public void setGifImageUri(final String uri) {
        mGifHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    URL gifUrl = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) gifUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    mInputStream = conn.getInputStream();
                    mMovie = Movie.decodeStream(mInputStream);
                    mWidth = mMovie.width();
                    mHeight = mMovie.height();
                    postInvalidate();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


//        try {
//            mInputStream = mContext.getContentResolver().openInputStream(uri);
//            init(id);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
