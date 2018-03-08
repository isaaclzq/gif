package com.example.pixdev32.myapplication;

import android.graphics.drawable.AnimationDrawable;
import android.util.LruCache;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by pixdev32 on 3/7/18.
 */

public class GifCache {

    public static GifCache mCache;
    private static final Object lock = new Object();

    private LruCache<String, GifDrawable> mLru;
    public static final int CACHE_SIZE = 1024 * 1024;

    private GifCache(){
        mLru = new LruCache<>(CACHE_SIZE);
    }

    public static GifCache getInstance() {

        if (mCache == null) {

            synchronized (lock) {

                if (mCache == null) {
                    mCache = new GifCache();
                }

            }

        }

        return mCache;
    }

    public GifDrawable get(String key) {

        return mLru.get(key);

    }

    public GifDrawable put(String key, GifDrawable animationDrawable) {

        return mLru.put(key, animationDrawable);

    }


}
