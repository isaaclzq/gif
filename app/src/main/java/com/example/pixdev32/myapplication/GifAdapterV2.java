package com.example.pixdev32.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pixdev32 on 3/8/18.
 */

public class GifAdapterV2 extends RecyclerView.Adapter<GifAdapterV2.GifViewHolderV2> {

    private Context mContext;
    private List mList;

    public GifAdapterV2(Context context, List list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public GifViewHolderV2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GifViewHolderV2 holder = new GifViewHolderV2(View.inflate(mContext, R.layout.list_item_v2, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GifViewHolderV2 holder, int position) {
        String url = (String) mList.get(position);
        holder.bind(url);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class GifViewHolderV2 extends RecyclerView.ViewHolder {

        @BindView(R.id.img_gif8)
        MyGifImageView mGif8;

        public GifViewHolderV2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String url) {
            mGif8.setResource(url);
        }
    }

}
