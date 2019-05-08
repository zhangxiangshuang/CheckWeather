package com.jason.checkweather.gson;

import com.google.gson.annotations.SerializedName;

public class BasicSearch {
    @SerializedName("location")
    public String cityName;

    @SerializedName("parent_city")
    public String parentName;

    @SerializedName("admin_area")
    public String provinceName;

    public BasicSearch(String cityName,String parentName, String provinceName) {
        this.cityName = cityName;
        this.parentName=parentName;
        this.provinceName = provinceName;
    }
}
