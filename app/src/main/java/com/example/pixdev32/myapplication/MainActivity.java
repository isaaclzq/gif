package com.example.pixdev32.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pixdev32.myapplication.V2.GifAdapterV2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

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

    private Unbinder mUnbinder;
    private ArrayList gifList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);



//        mRecycler.setAdapter(getAdapterForNetworkGif());
        mRecycler.setAdapter(getAdapterV2());
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        initViewWithGifDecoder();

//        initViewWithCustomView();
//        initViewWithGlide();
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

        return new GifAdapterV2(this, gifList);
    }

//    private void initViewWithCustomView() {
//        setContentView(R.layout.activity_main);
//    }

//    private void initViewWithGlide() {
//        setContentView(R.layout.activity_gif);
//
//        mUnbinder = ButterKnife.bind(this);
//
//        Glide.with(this).load(R.drawable.clap).into(mGif1);
//        Glide.with(this).load(R.drawable.shop).into(mGif2);
//        Glide.with(this).load(R.drawable.tiger).into(mGif3);
//        Glide.with(this).load(R.drawable.clap).into(mGif4);
//        Glide.with(this).load(R.drawable.shop).into(mGif5);
//        Glide.with(this).load(R.drawable.tiger).into(mGif6);
//    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
