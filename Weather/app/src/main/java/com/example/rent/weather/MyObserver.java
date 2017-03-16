package com.example.rent.weather;

import android.util.Log;

/**
 * Created by RENT on 2017-03-16.
 */

public class MyObserver implements Observer {

    @Override
    public void notifyMe() {
        Log.d("result", "oby nie był słaby jak poprzedni");
    }
}
