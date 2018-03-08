package com.example.pixdev32.myapplication;

import android.content.Context;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by pixdev32 on 3/6/18.
 */

public class GifAdapterWithNetwork extends RecyclerView.Adapter<GifAdapterWithNetwork.GifViewHolder> {

    private List<String> mList;
    private Context mContext;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Looper mLooper;

    private LruCache<String, InputStream> mCache;

    public GifAdapterWithNetwork(Context context, List list) {
        mContext = context;
        mList = list;

//        mCache = initCache();
    }

//    private LruCache initCache() {
//        int size = 10 * 1024 * 1024;
//        return new LruCache<>(size);
//    }

    @NonNull
    @Override
    public GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GifViewHolder holder = new GifViewHolder(View.inflate(mContext, R.layout.list_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GifViewHolder holder, int position) {
        final String str = mList.get(position);
        holder.bind(str);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class GifViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_gif7)
        GifImageView mGif7;

        public GifViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String str) {
            Log.v("adapter", "onbind");
//            mGif7.setCache(mCache);
            GifAsync gifAsync =  new GifAsync(mContext, mGif7);
            mGif7.setTag(gifAsync);
//            gifAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, str);
            gifAsync.execute(str);
        }

        public class GifAsync extends AsyncTask<String, Void, GifDrawable> {

            private GifImageView view;
            private Context mContext;

            public GifAsync(Context context, GifImageView view) {
                super();
                this.view = view;
                this.mContext = context;
            }

            @Override
            protected void onPostExecute(GifDrawable file) {
                if (file == null) {
                    return;
                }
                if(view.getTag() != this){
                    Log.d("postExecute", "Wrong async");
                    return;
                }
                view.setBackground(file);
                file.start();
                super.onPostExecute(file);
            }

            @Override
            protected GifDrawable doInBackground(String... strings) {
                if(view.getTag() != this){
                    Log.d("postExecute", "Wrong async");
                    return null;
                }
                String str = strings[0];
                String filename = "1";
                try {
                    filename = URLEncoder.encode(str, "UTF-8").replace(".", "");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                File file = null;
                FileInputStream fis = null;
                GifDrawable drawable;

                drawable = GifCache.getInstance().get(filename);

                if (drawable != null) {
                    return drawable;
                }

                file = new File(mContext.getCacheDir(), filename);

                if (file.exists()) {

                    try {
                        drawable = new GifDrawable(file);
                        GifCache.getInstance().put(filename, drawable);
                        return drawable;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                try {
                    URL gifUrl = new URL(str);
                    HttpURLConnection mConnection = (HttpURLConnection) gifUrl.openConnection();
                    mConnection.setConnectTimeout(5000);
                    InputStream mInputStream = mConnection.getInputStream();
                    file = new File(mContext.getCacheDir(), filename);
                    OutputStream mOutput = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = mInputStream.read(buffer)) != -1 && !isCancelled()) {
                            mOutput.write(buffer, 0, read);
                        }

                        drawable = new GifDrawable(file);
                        GifCache.getInstance().put(filename, drawable);

                        mOutput.flush();
                    } finally {
                        mOutput.close();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return drawable;
            }

        }
    }
}

