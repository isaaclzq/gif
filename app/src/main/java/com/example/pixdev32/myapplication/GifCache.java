package com.example.pixdev32.myapplication;

import android.graphics.drawable.AnimationDrawable;
import android.util.LruCache;

/**
 * Created by pixdev32 on 3/7/18.
 */

public class GifCache {

    public static GifCache mCache;

    private LruCache mLru;

    private GifCache(){}

    public static GifCache getInstance() {

        if (mCache == null) {

        }

        return mCache;
    }
}
