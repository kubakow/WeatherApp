package com.example.rent.weather;

import android.util.Log;

/**
 * Created by RENT on 2017-03-16.
 */

public class DisplayNameFirstCommand implements Command {
    @Override
    public void execute() {
        Log.d("result", "Johnny Bravo");
    }
}
