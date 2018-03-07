package com.example.pixdev32.myapplication;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.LruCache;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
    private AsyncTask mAsync;

    private LruCache<String, Movie> mCache;
    private String gifUrl;

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
            canvas.scale((float) this.getWidth() / (float) mMovie.width(), (float) this.getHeight() / (float) mMovie.height());
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
//        mGifHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mMovie = mCache.get(resId);
//
//                if (mMovie == null) {
//                    mInputStream = mContext.getResources().openRawResource(resId);
//                    mMovie = Movie.decodeStream(mInputStream);
//                    mCache.put(resId, mMovie);
//                    try {
//                        mInputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                mWidth = mMovie.width();
//                mHeight = mMovie.height();
//                postInvalidate();
//            }
//        });
    }

    public void setGifImageUri(final String uri) {
        if (mAsync != null) {
            mAsync.cancel(true);
        }
        mAsync = new GifAsync().execute(uri);
    }

    class GifAsync extends AsyncTask<String, Void, Void> {

        private OutputStream mOutput = null;
        private HttpURLConnection mConnection = null;
        private InputStream mInputStream = null;

        @Override
        protected void onPostExecute(Void aVoid) {
            invalidate();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            if (mOutput != null) {
                try {
                    mOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mOutput = null;
                }
            }

            if (mInputStream != null) {
                try {
                    mInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mInputStream = null;
                }
            }

            if (mConnection != null) {
                mConnection.disconnect();
                mConnection = null;
            }

            super.onCancelled();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String str = strings[0];
            String filename = "1";
            try {
                filename = URLEncoder.encode(str, "UTF-8").replace(".", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            File file = null;
            FileInputStream fis = null;

            mMovie = mCache.get(filename);

            if (mMovie != null) {
                return null;
            }

            try {
                fis = mContext.openFileInput(filename);
                mMovie = Movie.decodeStream(fis);
                mCache.put(filename, mMovie);
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                URL gifUrl = new URL(str);
                mConnection = (HttpURLConnection) gifUrl.openConnection();
                mConnection.setConnectTimeout(5000);
                mInputStream = mConnection.getInputStream();
                file = new File(mContext.getCacheDir(), filename);
                mOutput = new FileOutputStream(file);

                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = mInputStream.read(buffer)) != -1 && !isCancelled()) {
                        mOutput.write(buffer, 0, read);
                    }

                    if (file != null) {
                        mMovie = Movie.decodeFile(file.getAbsolutePath());
                        mCache.put(filename, mMovie);
                    }

                    mOutput.flush();
                } finally {
                    mOutput.close();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
