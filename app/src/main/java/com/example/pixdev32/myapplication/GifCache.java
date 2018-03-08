package com.example.pixdev32.myapplication;

import android.graphics.drawable.AnimationDrawable;
import android.util.LruCache;
import android.util.Pair;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.MultiCallback;

/**
 * Created by pixdev32 on 3/7/18.
 */

public class GifCache {

    public static GifCache mCache;
    private static final Object lock = new Object();

    private LruCache<String, Pair<GifDrawable, MultiCallback>> mLru;
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

    public Pair<GifDrawable, MultiCallback> get(String key) {

        return mLru.get(key);

    }

    public Pair<GifDrawable, MultiCallback> put(String key, Pair<GifDrawable, MultiCallback> value) {

        return mLru.put(key, value);

    }


}
