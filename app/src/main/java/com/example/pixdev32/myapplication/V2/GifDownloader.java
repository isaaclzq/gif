package com.example.pixdev32.myapplication.V2;

import android.content.Context;
import android.util.Log;

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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by pixdev32 on 3/8/18.
 */

public class GifDownloader {

    // singleton downloader
    private static GifDownloader downloader;

    // lock
    private static final Object lock = new Object();

    // cache for preventing downloading the same file
    private LinkedHashSet<String> mCache;
    private Context mContext;

    // thread pool information
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private final BlockingQueue<Runnable> mWorkQueue;
    private ThreadPoolExecutor threadPoolExecutor;

    private LinkedHashMap<String, Future> table;

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
        table = new LinkedHashMap<>();
    }

    public interface OnDownloadCompleteListener {
        void onDownloadComplete(File file);
    }

    /**
     * retrieve the global file downloader
     * @param context
     * @return downloader (singleton)
     */
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

    public void cancel(String viewID) {
        Future future = table.get(viewID);
        if (future == null) {
            return;
        }
        if (future.isDone() || future.isCancelled()) {
            future.cancel(true);
        }
    }

    /**
     * download (or retrieve) file
     * @param url file url
     * @param listener post execution (given the downloaded file)
     */
    public void download(String viewID, final String url, final OnDownloadCompleteListener listener) {

        boolean exist = mCache.contains(url);
        String filename = "temporary";

        if (exist) {
            return;
        }

        Runnable runnable = new Runnable() {
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
        };

        cancel(viewID);

        table.put(viewID, threadPoolExecutor.submit(runnable));

        threadPoolExecutor.execute(runnable);

    }

    /**
     * check if the file has been downloaded
     * @param url url (can transform to filename)
     * @param listener
     * @return
     */
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
