package com.jason.checkweather.util;

import com.jason.checkweather.gson.CitySearch;
import com.jason.checkweather.gson.Weather;
import com.google.gson.Gson;
import com.jason.checkweather.gson.Weathers;

import org.json.JSONArray;
import org.json.JSONObject;



public class Utility {
    /**
     *
     * 处理得到的 weather 数据，转化为 weather 对象
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理得到的位置信息，转化为 city对象
     */
  /*  public static City handleCityResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String cityContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(cityContent, City.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/


    /**
     * 将返回的 JSON 数据解析成 city 实体类
     */
    public static CitySearch parseCityResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String cityContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(cityContent, CitySearch.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *
     * 处理得到的 weathers 数据，转化为 weathers 对象
     */
    public static Weathers handleWeathersResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weathers.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
