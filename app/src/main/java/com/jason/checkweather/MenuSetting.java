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
    private View include_calculator;
    private View include_flashlight;
    private Switch switch_hourly;
    private Switch switch_forecast;
    private Switch switch_aqi;
    private Switch switch_suggestion;
    private Switch switch_flashlight;
    private Switch switch_isauto;
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
        include_flashlight=view.findViewById(R.id.include_flashlight);

        switch_hourly= (Switch) findViewById(R.id.switch_hourly);
        switch_forecast= (Switch) findViewById(R.id.switch_forecast);
        switch_aqi= (Switch) findViewById(R.id.switch_aqi);
        switch_suggestion= (Switch) findViewById(R.id.switch_suggestion);
        switch_flashlight= (Switch) findViewById(R.id.switch_flashlight);
        switch_isauto= (Switch) findViewById(R.id.switch_isAuto);
        mainview= (LinearLayout) view.findViewById(R.id.main_layout);


    }

    @Override
    public void initData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int hourlysign=prefs.getInt("includehourlysign",0);
        int forecastsign=prefs.getInt("includeforecastsign",0);
        int aqisign=prefs.getInt("includeaqisign",0);
        int suggestionsign=prefs.getInt("includesuggestionsign",0);
        int flashlightsign=prefs.getInt("includeflashlightsign",0);
        boolean isAutosign=prefs.getBoolean("isAutosign",true);
        if (isAutosign==true){
            switch_isauto.setChecked(true);
        }else {
            switch_isauto.setChecked(false);
        }
        setOnChecked(switch_hourly,hourlysign);
        setOnChecked(switch_forecast,forecastsign);
        setOnChecked(switch_aqi,aqisign);
        setOnChecked(switch_suggestion,suggestionsign);
        setOnChecked(switch_flashlight,flashlightsign);
    }

    @Override
    public void initListener() {
        switch_isauto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MenuSetting.this).edit();
                if (isChecked){
                    editor.putBoolean("isAutosign",true);
                    editor.apply();
                }else {
                    editor.putBoolean("isAutosign",false);
                    editor.apply();
                }
            }
        });
        setSwitchOnChecked(switch_hourly,include_hourly,"includehourlysign");
        setSwitchOnChecked(switch_forecast,include_forecast,"includeforecastsign");
        setSwitchOnChecked(switch_aqi,include_aqi,"includeaqisign");
        setSwitchOnChecked(switch_suggestion,include_suggestion,"includesuggestionsign");
        setSwitchOnChecked(switch_flashlight,include_flashlight,"includeflashlightsign");

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


    /*
    设置switch默认开关
     */
    public  void setOnChecked(Switch switch1,int sign){
        if (sign==0){
           switch1.setChecked(true);
        }else if (sign==8){
            switch1.setChecked(false);
        }
    }
    /*
        设置switch开关的点击事件
         */
    public void setSwitchOnChecked(Switch s, final View view1, final String string){
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MenuSetting.this).edit();
                if (isChecked){
                    view1.setVisibility(View.VISIBLE);
                    editor.putInt(string, view1.getVisibility());
                    editor.apply();
                }else {
                    view1.setVisibility(View.GONE);
                    editor.putInt(string, view1.getVisibility());
                    editor.apply();
                }
                String aaa=String.valueOf(view1.getVisibility());
                Log.i(TAG,aaa);
            }
        });
    }
}
