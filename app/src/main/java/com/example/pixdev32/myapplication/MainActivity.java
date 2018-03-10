package com.example.pixdev32.myapplication;

import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.pixdev32.myapplication.V2.GifAdapterV2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements GifEditText.GifUriListener, View.OnClickListener{

//    @BindView(R.id.img_gif1)
//    ImageView mGif1;
//
//    @BindView(R.id.img_gif2)
//    ImageView mGif2;
//
//    @BindView(R.id.img_gif3)
//    ImageView mGif3;
//
//    @BindView(R.id.img_gif4)
//    ImageView mGif4;
//
//    @BindView(R.id.img_gif5)
//    ImageView mGif5;
//
//    @BindView(R.id.img_gif6)
//    ImageView mGif6;

//    @BindView(R.id.img_gif7)
//    GifImageViewBase mGif7;

    @BindView(R.id.gif_recycler)
    RecyclerView mRecycler;

    @BindView(R.id.gif_comment)
    GifEditText editText;

    @BindView(R.id.gif_post_btn)
    ImageButton post;

    @BindView(R.id.gif_text)
    GifImageView gifImageView;

    private Unbinder mUnbinder;
    private List gifList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

//        adapter = getAdapterV2();
        adapter = getCommentAdapter();
        mRecycler.setAdapter(adapter);
        editText.setListener(this);

//        mRecycler.setAdapter(getAdapterForNetworkGif());
//        mRecycler.setAdapter(getAdapterV2());
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        post.setOnClickListener(this);
    }

    private GifAdapter getAdapterForLocalGif() {
        gifList = new ArrayList();
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.tiger);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.tiger);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.tiger);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.tiger);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.tiger);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.tiger);
        return new GifAdapter(this, gifList);
    }

    private GifAdapterWithNetwork getAdapterForNetworkGif() {
        gifList = new ArrayList<>();
        gifList.add("https://media.giphy.com/media/CyRC2jhUo6nDy/giphy.gif");
        gifList.add("https://media.giphy.com/media/3o6ZtiqckeZGMHITQY/giphy.gif");
        gifList.add("https://media.giphy.com/media/OsOP6zRwxrnji/giphy.gif");
        gifList.add("https://media.giphy.com/media/CyRC2jhUo6nDy/giphy.gif");
        gifList.add("https://media.giphy.com/media/3o6ZtiqckeZGMHITQY/giphy.gif");
        gifList.add("https://media.giphy.com/media/OsOP6zRwxrnji/giphy.gif");
        gifList.add("https://media.giphy.com/media/CyRC2jhUo6nDy/giphy.gif");
        gifList.add("https://media.giphy.com/media/3o6ZtiqckeZGMHITQY/giphy.gif");
        gifList.add("https://media.giphy.com/media/OsOP6zRwxrnji/giphy.gif");
        gifList.add("https://media.giphy.com/media/CyRC2jhUo6nDy/giphy.gif");
        gifList.add("https://media.giphy.com/media/3o6ZtiqckeZGMHITQY/giphy.gif");
        gifList.add("https://media.giphy.com/media/OsOP6zRwxrnji/giphy.gif");


        gifList.add("https://media.giphy.com/media/k2RYhEjB9THm8/giphy.gif");
        gifList.add("https://media.giphy.com/media/S0SRH7in45I4w/giphy.gif");
        gifList.add("https://media.giphy.com/media/DQ7UyAWFlHNVm/giphy.gif");
        gifList.add("https://media.giphy.com/media/mlG1xkRbsubK/giphy.gif");
        gifList.add("https://media.giphy.com/media/mlG1xkRbsubK/giphy.gif");
        gifList.add("https://media.giphy.com/media/Pzc4fSCWxuTQI/giphy.gif");
        gifList.add("https://media.giphy.com/media/oLWXdNKNlMdS8/giphy.gif");

        return new GifAdapterWithNetwork(this, gifList);
    }

    private GifAdapterV2 getAdapterV2() {
        Resources res = getResources();
        String[] urls = res.getStringArray(R.array.urls);
        gifList = new ArrayList();

        return new GifAdapterV2(this, gifList);
    }

    private GifCommentAdapter getCommentAdapter() {
        return new GifCommentAdapter(new ArrayList<Uri>(), this);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onGifUriLoaded(Uri uri) {
//        gifImageView.setImageURI(uri);
        if(adapter instanceof GifCommentAdapter){
            ((GifCommentAdapter) adapter).addUri(uri);
        }
    }

    @Override
    public void onClick(View v) {
        String content = editText.getText().toString();
        if (content == null || content.equals("")) {
            return;
        }

        gifList.add(content);
        adapter.notifyItemInserted(gifList.size()-1);

        editText.setText("");
    }
}
