package com.example.pixdev32.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        mCache = initCache();
    }

    private LruCache initCache() {
        int size = 10 * 1024 * 1024;
        return new LruCache<>(size);
    }

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
        GifImageview mGif7;

        public GifViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String str) {
            Log.v("adapter", "onbind");
            mGif7.setCache(mCache);
            mGif7.setGifImageUri(str);
        }
    }
}

