package com.jason.checkweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitySearch {

    public String status;

    @SerializedName("basic")
    public List<BasicSearch> basicSearchList;
}
