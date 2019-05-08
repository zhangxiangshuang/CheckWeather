package com.jason.checkweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weathers {
    public Now now;
    public String status;
    @SerializedName("daily_forecast")
    public List<Forecasts> forecastList;

    @SerializedName("hourly")
    public List<Hourly> hourlyList;
}
