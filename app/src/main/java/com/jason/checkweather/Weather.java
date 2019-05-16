package com.jason.checkweather;

/**
 * Created by tb on 2017/5/22.
 */

public class Weather {
    /**
     * 周几
     */
    private String week;
    /**
     * 日期
     */
    private String date;
    /**
     * 白天天气
     */
    private String weatherDay;
    /**
     * 晚上天气
     */
    private String weatherNight;
    /**
     * 风向
     */
    private String wind;
    /**
     * 风力
     */
    private String windLevel;
    /**
     * 最高温度
     */
    private int highTemperature;
    /**
     * 最低温度
     */
    private int lowTemperature;
    /**
     * 白天天气状况图标
     */
    private String condDay;
    /**
     * 晚上天气状况图标
     */
    private String condNight;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public void setWeatherDay(String weatherDay) {
        this.weatherDay = weatherDay;
    }

    public String getWeatherNight() {
        return weatherNight;
    }

    public void setWeatherNight(String weatherNight) {
        this.weatherNight = weatherNight;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(String windLevel) {
        this.windLevel = windLevel;
    }

    public int getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(int highTemperature) {
        this.highTemperature = highTemperature;
    }

    public int getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(int lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public String getCondDay() {
        return condDay;
    }

    public void setCondDay(String condDay) {
        this.condDay = condDay;
    }

    public String getCondNight() {
        return condNight;
    }

    public void setCondNight(String condNight) {
        this.condNight = condNight;
    }
}
