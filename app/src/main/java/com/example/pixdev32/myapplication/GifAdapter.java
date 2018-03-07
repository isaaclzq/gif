package com.example.pixdev32.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pixdev32 on 3/5/18.
 */

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.GifViewHolder> {

    private List<Integer> mList;
    private Context mContext;


    public GifAdapter(Context context, List list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GifViewHolder holder = new GifViewHolder(View.inflate(mContext, R.layout.list_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GifViewHolder holder, int position) {
        int str = mList.get(position);
        holder.bind(str);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull GifViewHolder holder) {
        Log.v("adapter", "onDetach");
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class GifViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_gif7)
        GifImageViewBase mGif7;

        AsyncTask asyncTask;
        public GifViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Integer str) {
            Log.v("adapter", "onbind");
            if (asyncTask != null) {
                asyncTask.cancel(true);
            }
            asyncTask = new GifAsync().execute(str);
//            gifDecoderHandler.queueGif(this, str);

//            InputStream is = mContext.getResources().openRawResource(+str);
//            mGif7.startGif(is);
        }

        public void stop() {
            mGif7.stopRendering();
            mGif7.stopGifThread();
        }

        public class GifAsync extends AsyncTask<Integer, Void, Void> {

            @Override
            protected Void doInBackground(Integer... integers) {
                if (!isCancelled()) {
                    int drawable = integers[0];
                    InputStream is = mContext.getResources().openRawResource(+drawable);
                    mGif7.startGif(is);
                }
                return null;
            }
        }
    }


}
