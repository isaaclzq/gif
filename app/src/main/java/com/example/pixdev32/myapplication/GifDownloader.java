package com.example.pixdev32.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.MultiCallback;

/**
 * Created by pixdev32 on 3/8/18.
 */

public class GifDownloader {

    private static GifDownloader downloader;
    private static final Object lock = new Object();
    private LinkedHashSet<String> mCache;
    private Context mContext;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private final BlockingQueue<Runnable> mWorkQueue;

    private ThreadPoolExecutor threadPoolExecutor;

    private GifDownloader(Context context) {
        mCache = new LinkedHashSet<>();
        mContext = context.getApplicationContext();
        mWorkQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mWorkQueue
        );
    }

    public interface OnDownloadCompleteListener {
        void onDownloadComplete(File file);
    }

    public static GifDownloader getInstance(Context context) {

        if (downloader != null) {
            return downloader;
        }

        synchronized (lock) {
            if (downloader == null) {
                downloader = new GifDownloader(context);
            }
        }

        return downloader;
    }

    public void download(final String url, final OnDownloadCompleteListener listener) {

        boolean exist = mCache.contains(url);
        String filename = "temporary";

        if (exist) {
            return;
        }



        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // gif has been downloaded
                if (checkLocal(url, listener)) {
                    return;
                }

                mCache.add(url);
                try {
                    URL address = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) address.openConnection();
                    conn.setConnectTimeout(5000);
                    String filename = URLEncoder.encode(url, "UTF-8").replace(".", "");
                    File file = new File(mContext.getCacheDir(), filename);
                    InputStream inputStream = conn.getInputStream();
                    OutputStream outputStream = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, read);
                        }

                        listener.onDownloadComplete(file);

                        outputStream.flush();
                    } finally {
                        outputStream.close();
                        inputStream.close();
                        conn.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                synchronized (this) {
                    mCache.remove(url);
                }
            }
        });

    }

    private boolean checkLocal(String url, OnDownloadCompleteListener listener) {

        File file = null;
        String filename = null;

        try {
            filename = URLEncoder.encode(url, "UTF-8").replace(".", "");
        } catch (UnsupportedEncodingException e) {
            filename = "temporary";
            e.printStackTrace();
        }

        file = new File(mContext.getCacheDir(), filename);

        if (file.exists()) {
            listener.onDownloadComplete(file);
            return true;
        }

        return false;
    }
}
