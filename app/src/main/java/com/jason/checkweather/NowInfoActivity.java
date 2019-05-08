package com.jason.checkweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.checkweather.gson.Now;
import com.jason.checkweather.gson.Weathers;
import com.jason.checkweather.util.Utility;

public class NowInfoActivity extends BaseActivity {

    private ImageView nowinfocode;
    private TextView nowinfotxt;
    private TextView nowinfotmp;
    private TextView nowinfofl;
    private TextView nowinfowinddeg;
    private TextView nowinfowinddir;
    private TextView nowinfowindsc;
    private TextView nowinfowindspd;
    private TextView nowinfohum;
    private TextView nowinfopcpn;
    private TextView nowinfopres;
    private TextView nowinfovis;
    private TextView nowinfocloud;
    @Override
    public void initView() {

        setContentView(R.layout.activity_now_info);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("实况天气");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nowinfocode= (ImageView) findViewById(R.id.now_info_code);
        nowinfocloud= (TextView) findViewById(R.id.now_info_cloud);
        nowinfofl= (TextView) findViewById(R.id.now_info_fl);
        nowinfohum= (TextView) findViewById(R.id.now_info_hum);
        nowinfopcpn= (TextView) findViewById(R.id.now_info_pcpn);
        nowinfopres= (TextView) findViewById(R.id.now_info_pres);
        nowinfotmp= (TextView) findViewById(R.id.now_info_tmp);
        nowinfotxt= (TextView) findViewById(R.id.now_info_txt);
        nowinfovis= (TextView) findViewById(R.id.now_info_vis);
        nowinfowinddeg= (TextView) findViewById(R.id.now_info_wind_deg);
        nowinfowinddir= (TextView) findViewById(R.id.now_info_wind_dir);
        nowinfowindsc= (TextView) findViewById(R.id.now_info_wind_sc);
        nowinfowindspd= (TextView) findViewById(R.id.now_info_wind_spd);
    }

    @Override
    public void initData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weathersString = prefs.getString("weatherResponses", null);
        Weathers weathers= Utility.handleWeathersResponse(weathersString);
       showWeathersInfo(weathers);

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View view) {

    }

    private void showWeathersInfo(Weathers weathers)
    {
        String weatherInfoCode = "weather_"+weathers.now.cond_code;
        int resId1 = getResources().getIdentifier(weatherInfoCode, "drawable", this.getPackageName());
        if (resId1 != 0){
            nowinfocode.setImageResource(resId1);
        }
        nowinfocloud.setText("云量："+ weathers.now.cloud+"%");
        nowinfowindspd.setText("风速："+weathers.now.wind_spd+"公里/小时");
        nowinfowindsc.setText("风力："+weathers.now.wind_sc+"级");
        nowinfowinddir.setText("风向："+weathers.now.wind_dir);
        nowinfowinddeg.setText("风向360角度："+weathers.now.wind_deg+"°");
        nowinfovis.setText("能见度："+weathers.now.vis+"公里");
        nowinfotxt.setText("天气状况："+weathers.now.cond_txt);
        nowinfotmp.setText("温度："+weathers.now.tmp+"℃");
        nowinfopres.setText("大气压强："+weathers.now.pres+"hpa");
        nowinfopcpn.setText("降水量："+weathers.now.pcpn+"毫米");
        nowinfohum.setText("相对湿度："+weathers.now.hum+"%");
        nowinfofl.setText("体感温度："+weathers.now.fl+"℃");
    }
}
