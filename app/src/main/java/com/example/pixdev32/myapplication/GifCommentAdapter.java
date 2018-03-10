package com.example.pixdev32.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by pixuredlinux3 on 3/9/18.
 */

public class GifCommentAdapter extends RecyclerView.Adapter<GifCommentAdapter.GifCommentViewHolder> {
    List<Uri> uriList;
    Context context;

    public GifCommentAdapter(List<Uri> uriList, Context context){
        this.uriList = uriList;
        this.context = context;
    }

    public void addUri(Uri uri){
        uriList.add(uri);
        notifyItemInserted(uriList.size());
    }
    @NonNull
    @Override
    public GifCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GifCommentViewHolder((View.inflate(context, R.layout.list_gif_comments, null)));
    }

    @Override
    public void onBindViewHolder(@NonNull GifCommentViewHolder holder, int position) {
        holder.bind(uriList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class GifCommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_gif_comment) GifImageView gifImageView;

        public GifCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Uri uri){
            gifImageView.setImageURI(uri);
        }
    }
}
