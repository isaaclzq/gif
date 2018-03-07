package com.example.pixdev32.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private ArrayList<Integer> gifList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        gifList = new ArrayList<>();
//        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
//        gifList.add(R.drawable.tiger);
//        gifList.add(R.drawable.clap);
//        gifList.add(R.drawable.shop);
//        gifList.add(R.drawable.tiger);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.shop);
//        gifList.add(R.drawable.clap);
//        gifList.add(R.drawable.shop);
//        gifList.add(R.drawable.clap);
//        gifList.add(R.drawable.shop);
//        gifList.add(R.drawable.clap);
//        gifList.add(R.drawable.shop);
//        gifList.add(R.drawable.tiger);

        GifAdapter adapter = new GifAdapter(this, gifList);
        mRecycler.setAdapter(adapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        initViewWithGifDecoder();

//        initViewWithCustomView();
//        initViewWithGlide();
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
