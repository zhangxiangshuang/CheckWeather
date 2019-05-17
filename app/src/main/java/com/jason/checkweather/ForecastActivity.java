package com.jason.checkweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.baidu.location.LocationClientOption;
import com.jason.checkweather.gson.Forecasts;
import com.jason.checkweather.gson.Weathers;
import com.jason.checkweather.util.Time;
import com.jason.checkweather.util.Utility;
import com.jason.checkweather.view.FutureDaysChart;
import com.jason.checkweather.view.ScrollFutureDaysWeatherView;

import java.util.ArrayList;
import java.util.List;


public class ForecastActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    private ScrollFutureDaysWeatherView scrollFutureDaysWeatherView;
    private FutureDaysChart futureDaysChart;
    private List<Weather> datas=new ArrayList<>();
    private ImageView closeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        scrollFutureDaysWeatherView= (ScrollFutureDaysWeatherView) findViewById(R.id.sfdwv);
        futureDaysChart=scrollFutureDaysWeatherView.getSevenDaysChart();
        closeActivity= (ImageView) findViewById(R.id.close_image);
//        futureDaysChart.setCubic(true);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weathersString = prefs.getString("weatherResponses", null);
        Weathers weathers= Utility.handleWeathersResponse(weathersString);
        showWeathersInfo(weathers);


        futureDaysChart.setDatas(datas);
        List<View> viewList=scrollFutureDaysWeatherView.getAllViews();
        Log.i(TAG,String.valueOf(viewList.size()));
        for (int i = 0; i < viewList.size(); i++) {
            View view=viewList.get(i);
            TextView tvWeek= (TextView) view.findViewById(R.id.tv_week);
            TextView tvDate= (TextView) view.findViewById(R.id.tv_date);
            TextView tvWeatherDay= (TextView) view.findViewById(R.id.tv_weather_day);
            TextView tvWeatherNight= (TextView) view.findViewById(R.id.tv_weather_night);
            TextView tvWind= (TextView) view.findViewById(R.id.tv_wind);
            TextView tvWindLevel= (TextView) view.findViewById(R.id.tv_wind_level);
            ImageView tvWeatherCodeDay = (ImageView) view.findViewById(R.id.iv_weather_img_day);
            ImageView tvWeatherCodeNight = (ImageView) view.findViewById(R.id.iv_weather_img_night);
            tvWeek.setText(datas.get(i).getWeek());
            tvDate.setText(datas.get(i).getDate());
            tvWeatherDay.setText(datas.get(i).getWeatherDay());
            tvWeatherNight.setText(datas.get(i).getWeatherNight());
            tvWind.setText(datas.get(i).getWind());
            tvWindLevel.setText(datas.get(i).getWindLevel());
            String weatherDayCode = "weather_"+datas.get(i).getCondDay();
            String weatherNightCode = "weather_"+datas.get(i).getCondNight();
            int resIdDay = getResources().getIdentifier(weatherDayCode, "drawable", this.getPackageName());
            int resIdNight = getResources().getIdentifier(weatherNightCode, "drawable", this.getPackageName());
            if (resIdDay != 0){
                tvWeatherCodeDay.setImageResource(resIdDay);
            }
            if (resIdNight != 0){
                tvWeatherCodeNight.setImageResource(resIdNight);
            }

            if(i==0){
                tvWeek.setTextColor(Color.BLUE);
                view.setBackgroundColor(0x22808080);
            }else{
                tvWeek.setTextColor(Color.BLACK);
            }
        }

        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showWeathersInfo(Weathers weathers)
    {
        for (Forecasts forecasts : weathers.forecastList){
            Weather w=new Weather();
            w.setDate(Time.parseTimeDate(forecasts.date));
            w.setHighTemperature(Integer.valueOf(forecasts.tmp_max));
            w.setLowTemperature(Integer.valueOf(forecasts.tmp_min));
            w.setCondDay(forecasts.cond_code_d);
            w.setCondNight(forecasts.cond_code_n);
            w.setWeatherDay(forecasts.cond_txt_d);
            w.setWeatherNight(forecasts.cond_txt_n);
            w.setWeek(Time.parseTimeWeek(forecasts.date));
            w.setWind(forecasts.wind_dir);
            w.setWindLevel(forecasts.wind_sc+"级");
            datas.add(w);
        }

    }



}
