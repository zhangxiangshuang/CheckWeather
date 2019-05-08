package com.jason.checkweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class MenuSetting extends BaseActivity {

    private static final String TAG = "MenuSetting";
    private View view;
    private View include_hourly;
    private View include_forecast;
    private View include_aqi;
    private View include_suggestion;
    private Switch switch_hourly;
    private Switch switch_forecast;
    private Switch switch_aqi;
    private Switch switch_suggestion;
    private LinearLayout mainview;
    @Override
    public void initView() {
        setContentView(R.layout.activity_menu_setting);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("功能选择");
        }
        view = LayoutInflater.from(MenuSetting.this).inflate(R.layout.acticity_main, null);
        include_hourly = view.findViewById(R.id.include_hourly);
        include_forecast=view.findViewById(R.id.include_forecast);
        include_aqi=view.findViewById(R.id.include_aqi);
        include_suggestion=view.findViewById(R.id.include_suggestion);
        switch_hourly= (Switch) findViewById(R.id.switch_hourly);
        switch_forecast= (Switch) findViewById(R.id.switch_forecast);
        switch_aqi= (Switch) findViewById(R.id.switch_aqi);
        switch_suggestion= (Switch) findViewById(R.id.switch_suggestion);
        mainview= (LinearLayout) view.findViewById(R.id.main_layout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int hourlysign=prefs.getInt("includehourlysign",0);
        int forecastsign=prefs.getInt("includeforecastsign",0);
        int aqisign=prefs.getInt("includeaqisign",0);
        int suggestionsign=prefs.getInt("includesuggestionsign",0);
        setOnChecked(switch_hourly,hourlysign);
        setOnChecked(switch_forecast,forecastsign);
        setOnChecked(switch_aqi,aqisign);
        setOnChecked(switch_suggestion,suggestionsign);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        switch_hourly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MenuSetting.this).edit();
                if (isChecked){
                    include_hourly.setVisibility(View.VISIBLE);
                    editor.putInt("includehourlysign", include_hourly.getVisibility());
                    editor.apply();
                }else {
                    include_hourly.setVisibility(View.GONE);
                    editor.putInt("includehourlysign", include_hourly.getVisibility());
                    editor.apply();
                }
            }
        });

        switch_forecast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MenuSetting.this).edit();
                if (isChecked){
                    include_forecast.setVisibility(View.VISIBLE);
                    editor.putInt("includeforecastsign", include_forecast.getVisibility());
                    editor.apply();
                }else {
                    include_forecast.setVisibility(View.GONE);
                    editor.putInt("includeforecastsign", include_forecast.getVisibility());
                    editor.apply();
                }
            }
        });

        switch_aqi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MenuSetting.this).edit();
                if (isChecked){
                    include_aqi.setVisibility(View.VISIBLE);
                    editor.putInt("includeaqisign", include_aqi.getVisibility());
                    editor.apply();
                }else {
                    include_aqi.setVisibility(View.GONE);
                    editor.putInt("includeaqisign", include_aqi.getVisibility());
                    editor.apply();
                }
            }
        });
        switch_suggestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MenuSetting.this).edit();
                if (isChecked){
                    include_suggestion.setVisibility(View.VISIBLE);
                    editor.putInt("includesuggestionsign", include_suggestion.getVisibility());
                    editor.apply();
                }else {
                    include_suggestion.setVisibility(View.GONE);
                    editor.putInt("includesuggestionsign", include_suggestion.getVisibility());
                    editor.apply();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mainview.invalidate();
                finish();
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public void onClick(View view) {

    }
    public  void setOnChecked(Switch switch1,int sign){
        if (sign==0){
           switch1.setChecked(true);
        }else if (sign==8){
            switch1.setChecked(false);
        }
    }
}
