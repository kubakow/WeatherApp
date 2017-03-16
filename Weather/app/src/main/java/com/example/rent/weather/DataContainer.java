package com.example.rent.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by RENT on 2017-03-15.
 */

public class DataContainer {

    @SerializedName("data")
    private WeatherModel weatherModel;

    public WeatherModel getData(){
        return weatherModel;
    }


}
