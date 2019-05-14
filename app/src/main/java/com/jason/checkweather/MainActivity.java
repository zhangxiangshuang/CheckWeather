package com.jason.checkweather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jason.checkweather.gson.Forecast;
import com.jason.checkweather.gson.Forecasts;
import com.jason.checkweather.gson.Hourly;
import com.jason.checkweather.gson.Weather;
import com.jason.checkweather.gson.Weathers;
import com.jason.checkweather.service.AutoUpdateService;
import com.jason.checkweather.util.HttpUtil;
import com.jason.checkweather.util.TaskKiller;
import com.jason.checkweather.util.Time;
import com.jason.checkweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";
    private static int SIGN_NO_INTERNET = 0;
    private static int SIGN_ALARMS = 1;

    private ScrollView weatherLayout;

    private LinearLayout mainLayout;

    private TextView titleCity;

    private ImageView iv_loc;

    private ImageView iv_add_city;
    private ImageView weather_info_code;

    //引入布局开关

    private View include_hourly;
    private View space_hourly;
    private View include_forecast;
    private View space_forecast;
    private View include_aqi;
    private View space_aqi;
    private View include_suggestion;
    private View space_suggestion;
    private View include_flashlight;
    private View space_flashlight;
    private View include_addfunction;

    List<String> permissionList  = new ArrayList<>();

    // 以下是 weather_noew 的内容

    private TextView degreeText;

    private TextView weatherInfoText;

    private RelativeLayout weaherNowLayout;


    private RelativeLayout flashlight;

    private TextView updateTimeText;

    // 以下是 weather_hour 的内容

    private TextView hourTime;

    private  TextView hourText;

    private TextView hourDegree;

    private List<Hour> hourList = new ArrayList<>();

    private RecyclerView recyclerView;

    private  HourAdapter hourAdapter;


    // 以下是 weather_aqi 内容

    private TextView aqiText;

    private TextView pm25Text;

    private TextView coText;

    private TextView o3Text;

    private TextView pm10Text;

    private TextView so2Text;

    private TextView qltyText;

    // 以下是 weather_forecast 内容
    private LinearLayout forecastLayout;

    // 以下是 weather_suggestion 内容
    private TextView carWashText;

    private TextView sportText;

    private TextView comfortText;

    private TextView uvText;

    private TextView clothesText;

    private TextView coldText;

    private Button carWashBtn;

    private Button sportBtn;

    private Button comfortBtn;

    private Button uvBtn;

    private Button clothesBtn;

    private Button coldBtn;

    private String carWashInfo;
    private String carWashSign;

    private String sportInfo;
    private String sportSign;

    private String comfortInfo;
    private String comfortSign;

    private String uvInfo;
    private String uvSign;

    private String clothesInfo;
    private String clothesSign;

    private String coldInfo;
    private String coldSign;
    // 以下是 additional_function 内容
    private Button noteBtn;

    private Button flashlightBtn;

    private Button calculatorBtn;

    public SwipeRefreshLayout swipeRefresh;


    private long triggerAtTimefirst = 0;

    // LBS
    public LocationClient mlocationClient;
    public static String currentPosition = "";


    @Override
    public void initView() {
        setContentView(R.layout.acticity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // 初始化各种控件


        include_hourly=findViewById(R.id.include_hourly);
        include_forecast=findViewById(R.id.include_forecast);
        include_aqi=findViewById(R.id.include_aqi);
        include_suggestion=findViewById(R.id.include_suggestion);
        include_flashlight=findViewById(R.id.include_flashlight);
        include_addfunction=findViewById(R.id.include_addfunction);

        space_hourly=findViewById(R.id.space_hourly);
        space_forecast=findViewById(R.id.space_forecast);
        space_aqi=findViewById(R.id.space_aqi);
        space_suggestion=findViewById(R.id.space_suggestion);
        space_flashlight=findViewById(R.id.space_flashlight);


        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        iv_loc = (ImageView) findViewById(R.id.iv_loc);
        iv_add_city = (ImageView) findViewById(R.id.iv_add_city);
        weather_info_code= (ImageView) findViewById(R.id.weather_info_code);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        // weather_now
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        weaherNowLayout = (RelativeLayout)findViewById(R.id.weather_now_layout);
        updateTimeText = (TextView)findViewById(R.id.update_time_text);


        // weather_hour
        hourDegree = (TextView)findViewById(R.id.hour_degree);
        hourText = (TextView)findViewById(R.id.hour_text);
        hourTime = (TextView)findViewById(R.id.hout_time);

        recyclerView = (RecyclerView)findViewById(R.id.weather_hourly);
        hourAdapter = new HourAdapter(hourList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(hourAdapter);


        // weather_aqi
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        coText = (TextView)findViewById(R.id.co_text);
        o3Text = (TextView)findViewById(R.id.o3_text);
        pm10Text = (TextView)findViewById(R.id.pm10_text);
        so2Text = (TextView)findViewById(R.id.so2_text);
        qltyText= (TextView) findViewById(R.id.qlty_text);

        // weather_suggestion
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        uvText = (TextView)findViewById(R.id.uv_text);
        clothesText = (TextView)findViewById(R.id.clothes_text);
        coldText = (TextView)findViewById(R.id.cold_text);
        comfortBtn = (Button)findViewById(R.id.comfort_button);
        carWashBtn = (Button)findViewById(R.id.car_wash_button);
        sportBtn = (Button)findViewById(R.id.sport_button);
        uvBtn = (Button)findViewById(R.id.uv_button);
        clothesBtn = (Button)findViewById(R.id.clothes_button);
        coldBtn = (Button)findViewById(R.id.cold_button);

        //附加功能
        calculatorBtn= (Button) findViewById(R.id.calculator_button);
        flashlightBtn= (Button) findViewById(R.id.flashlight_button);
        noteBtn= (Button) findViewById(R.id.note_button);


        //手电筒
        flashlight= (RelativeLayout) findViewById(R.id.flashlight_layout);

        // LBS
        mlocationClient = new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MyLocationListener());
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));


    }

    /**
     * 权限申请处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            // 如果存在某个权限没有处理
                            finish();
                        }
                    }
                }else{
                    // 发生未知错误
                    showShort("权限申请出现位置错误");
                }
                break;
            default:
        }
    }



    @Override
    public void initListener() {
        comfortBtn.setOnClickListener(this);
        carWashBtn.setOnClickListener(this);
        sportBtn.setOnClickListener(this);
        uvBtn.setOnClickListener(this);
        clothesBtn.setOnClickListener(this);
        coldBtn.setOnClickListener(this);
        iv_add_city.setOnClickListener(this);
        iv_loc.setOnClickListener(this);
        weaherNowLayout.setOnClickListener(this);
//        calculator.setOnClickListener(this);
//        flashlight.setOnClickListener(this);
        calculatorBtn.setOnClickListener(this);
        flashlightBtn.setOnClickListener(this);
        noteBtn.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getNetworkInfo() == null){
                    Snackbar.make(swipeRefresh, "当前无网络，无法刷新 %>_<% ",Snackbar.LENGTH_LONG).setAction("去设置网络", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                    swipeRefresh.setRefreshing(false);
                }else{
                    showAnimationAlpha(weaherNowLayout);

                }
            }
        });
    }

    @Override
    public void initData() {
        String cityName = getIntent().getStringExtra("cityName");
        if (!TextUtils.isEmpty(cityName)){
            requestWeather(cityName);
        }else{
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String weatherString = prefs.getString("weatherResponse", null);        // weather 保存API 返回的字符串
            String weathersString = prefs.getString("weatherResponses", null);
            if (weatherString != null && weathersString !=null){
                // 有缓存时直接解析天气数据
                Weather weather = Utility.handleWeatherResponse(weatherString);
                Weathers weathers=Utility.handleWeathersResponse(weathersString);
                showWeatherInfo(weather);
                showWeathersInfo(weathers);
                mainLayout.setVisibility(View.VISIBLE);
            }else {
                // 无缓存时向服务器查询数据
                if (getNetworkInfo() != null && getNetworkInfo().isAvailable()){
                    // 查询完之后显示 coordinatorLayout.setVisibility(View.VISIBLE);
                    LocationClientOption option = new LocationClientOption();
                    option.setIsNeedAddress(true);
                    mlocationClient.setLocOption(option);
                    mlocationClient.start();
                }else{
                    showDialog(null, "当前无网络，请打开网络",SIGN_NO_INTERNET);
                }

            }
            Intent intent = new Intent(MainActivity.this, AutoUpdateService.class);
            startService(intent);
        }

    }

    /**
     * 显示对话框
     */
    public void showDialog(String title, String info, final int SIGN){
        final AlertDialog.Builder alertDialog  = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage(info);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (SIGN == SIGN_NO_INTERNET){
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    TaskKiller.dropAllAcitivty();
                }

                if (SIGN == SIGN_ALARMS){
                    alertDialog.setCancelable(true);
                }

            }
        });
        alertDialog.show();
    }


    /**
     * 用来自动定位,显示第一次的天气信息
     */
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
           /* currentPosition = bdLocation.getCity();*/
            currentPosition = bdLocation.getDistrict();
            if (currentPosition != null){
                requestWeather(currentPosition);
                showShort(currentPosition + " 定位成功");
            }else{
//                showShort("没有获取到定位权限，请打开定位权限后再打开此应用");
        }
        }


        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


    /**
     * 根据城市地点请求城市天气信息
     */
    public void requestWeather(final String cityName){

        String address = "https://free-api.heweather.com/v5/weather?city=" + cityName + "&key=a0187789a4424bc89254728acd4a08ed";
        String addresss ="https://free-api.heweather.net/s6/weather?location=" + cityName + "&key=0c6010f67e4648e39af80b623c4b0cd1";
        Log.i(TAG,address);
        Log.i(TAG,addresss);
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShort("获取天气信息1失败");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null & "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("weatherResponse", responseText);
                            editor.putString("cityName",cityName);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            showShort("获取天气信息2失败");
                        }
                    }
                });

            }
        });
        HttpUtil.sendOkHttpRequest(addresss, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShort("获取天气信息1失败");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responsesText = response.body().string();
                final Weathers weathers = Utility.handleWeathersResponse(responsesText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weathers != null & "ok".equals(weathers.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("weatherResponses", responsesText);
                            editor.putString("cityName",cityName);
                            editor.apply();
                            showWeathersInfo(weathers);
                        }else{
                            showShort("获取天气信息2失败");
                        }
                    }
                });

            }
        });
        swipeRefresh.setRefreshing(false);


    }

    /**
     * 显示天气信息
     */
    private void showWeathersInfo(Weathers weathers)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String degree = weathers.now.tmp ;
        String weatherInfo = weathers.now.cond_txt;
        String weatherInfoCode = "weather_"+weathers.now.cond_code;
        int resId1 = getResources().getIdentifier(weatherInfoCode, "drawable", this.getPackageName());
        if (resId1 != 0){
            weather_info_code.setImageResource(resId1);
        }
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecasts forecasts : weathers.forecastList){
            // 将未来几天的天气添加到视图中
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView dateText = (TextView)view.findViewById(R.id.data_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxMinText = (TextView)view.findViewById(R.id.max_min_text);
            ImageView weatherPic = (ImageView)view.findViewById(R.id.weather_pic);

            // 动态获取 资源id
            String weatherCode = "weather_"+forecasts.cond_code_d;
            int resId = getResources().getIdentifier(weatherCode, "drawable", this.getPackageName());
            if (resId != 0){
                weatherPic.setImageResource(resId);
            }
            String infotext;
            if(forecasts.cond_txt_d.equals(forecasts.cond_txt_n)){
                infotext=forecasts.cond_txt_d;
            }else
            {
                infotext=forecasts.cond_txt_d+"转"+forecasts.cond_txt_n;
            }
            dateText.setText(Time.parseTime(forecasts.date));
//            dateText.setText(forecasts.date);

            infoText.setText(infotext);
            maxMinText.setText(forecasts.tmp_max + " ～ " + forecasts.tmp_min);
            forecastLayout.addView(view);

            int includeforecast=prefs.getInt("includeforecastsign",0);
            setincludeview(include_forecast,includeforecast);
            setincludeview(space_forecast,includeforecast);
            //显示小时预报
                hourList.clear();
        for (Hourly hourly:weathers.hourlyList){
            Hour hour = new Hour();
            hour.setDegree(hourly.tmp + "°" );
            hour.setText(hourly.cond_txt);
            hour.setTime(hourly.time.split(" ")[1]);
            hourList.add(hour);
        }

             hourAdapter.notifyDataSetChanged();


            int includehourly = prefs.getInt("includehourlysign", 0);
            setincludeview(include_hourly,includehourly);
            setincludeview(space_hourly,includehourly);
        }
    }
    private void showWeatherInfo(Weather weather){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.loc;
        titleCity.setText(cityName);
        updateTimeText.setText("数据更新时间: " + updateTime.split(" ")[1]);

        // weather_aqi 空气质量

        String infoText = "无";
        if (weather.aqi == null){
            aqiText.setText(infoText);
            pm25Text.setText(infoText);
            coText.setText(infoText);
            o3Text.setText(infoText);
            pm10Text.setText(infoText);
            so2Text.setText(infoText);
            qltyText.setText(infoText);
        }else{
            if (weather.aqi.city.aqi != null){
                aqiText.setText(weather.aqi.city.aqi);
            }else{
                aqiText.setText(infoText);
            }

            if (weather.aqi.city.pm25 != null){
                pm25Text.setText(weather.aqi.city.pm25);
            }else{
                pm25Text.setText(infoText);
            }

            if (weather.aqi.city.co != null){
                coText.setText(weather.aqi.city.co);
            }else{
                coText.setText(infoText);
            }

            if (weather.aqi.city.o3 != null){
                o3Text.setText(weather.aqi.city.o3);
            }else{
                o3Text.setText(infoText);
            }

            if (weather.aqi.city.pm10 != null){
                pm10Text.setText(weather.aqi.city.pm10);
            }else{
                pm10Text.setText(infoText);
            }

            if (weather.aqi.city.so2 != null){
                so2Text.setText(weather.aqi.city.so2);
            }else{
                so2Text.setText(infoText);
            }
            if (weather.aqi.city.qlty != null){
                qltyText.setText("空气质量："+weather.aqi.city.qlty);
            }else{
                qltyText.setText(infoText);
            }
        }
        aqiText.getPaint().setFakeBoldText(true);
        pm25Text.getPaint().setFakeBoldText(true);
        coText.getPaint().setFakeBoldText(true);
        o3Text.getPaint().setFakeBoldText(true);
        pm10Text.getPaint().setFakeBoldText(true);
        so2Text.getPaint().setFakeBoldText(true);

        int includeaqi = prefs.getInt("includeaqisign",0);
        setincludeview(include_aqi,includeaqi);
        setincludeview(space_aqi,includeaqi);
        // 舒适指数

        comfortSign = weather.suggestion.comfort.sign;
        carWashSign = weather.suggestion.carWash.sign;
        sportSign = weather.suggestion.sport.sign;
        uvSign = weather.suggestion.uv.sign;
        clothesSign = weather.suggestion.clothes.sign;
        coldSign = weather.suggestion.cold.sign;


        comfortText.setText(comfortSign);
        comfortText.getPaint().setFakeBoldText(true);
        carWashText.setText(carWashSign);
        carWashText.getPaint().setFakeBoldText(true);
        sportText.setText(sportSign);
        sportText.getPaint().setFakeBoldText(true);
        uvText.setText(uvSign);
        uvText.getPaint().setFakeBoldText(true);
        clothesText.setText(clothesSign);
        clothesText.getPaint().setFakeBoldText(true);
        coldText.setText(coldSign);
        coldText.getPaint().setFakeBoldText(true);

        comfortInfo = weather.suggestion.comfort.info;
        carWashInfo = weather.suggestion.carWash.info;
        sportInfo = weather.suggestion.sport.info;
        uvInfo = weather.suggestion.uv.info;
        clothesInfo = weather.suggestion.clothes.info;
        coldInfo = weather.suggestion.cold.info;

        int includesuggestion=prefs.getInt("includesuggestionsign",0);
        setincludeview(include_suggestion,includesuggestion);
        setincludeview(space_suggestion,includesuggestion);



        int includeflashlight = prefs.getInt("includeflashlightsign",0);
        setincludeview(include_flashlight,includeflashlight);
        setincludeview(space_flashlight,includeflashlight);
        setincludeview(include_addfunction,includeflashlight);

        weatherLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.VISIBLE);

        // 天气预警
        if (weather.alarms != null){
            String level = weather.alarms.level;
            String title = weather.alarms.title;
            String text = weather.alarms.txt;
            showDialog(title, text, SIGN_ALARMS);
        }else{
//            showDialog("无", "当前没有预警信息，请放心出行", SIGN_ALARMS);
        }

    }


    /**
     * 停止定位
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.stop();
    }

    /**
     * 添加 actionbar 菜单项
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * 菜单点击事件响应
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                // 跳转到设置界面
                Intent intent1 = new Intent(this, SettingActivity.class);
                intent1.putExtra("weather_title","设置");
                startActivity(intent1);
                break;
            case R.id.night_model:
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = pref.edit();
                boolean isNight = pref.getBoolean("isNight", false);
                if (isNight){
                    // 如果已经是夜间模式
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                    editor.putBoolean("isNight", false);
                    editor.apply();
                }else{
                    // 如果是日间模式
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                    editor.putBoolean("isNight", true);
                    editor.apply();
                }
                break;
        }
        return true;
    }

    /**
     * 对 back 键监听，如果连续点击两次 back 键的时间差 < 2s ,则退出所有程序
     */
    @Override
    public void onBackPressed() {
        long triggerAtTimeSecond = triggerAtTimefirst;
        triggerAtTimefirst = SystemClock.elapsedRealtime();
        if (triggerAtTimefirst - triggerAtTimeSecond <= 2000){
            TaskKiller.dropAllAcitivty();
        }else{
            showShort("请再点击 Back 键, 确认退出");
        }

    }
    /**
     * 主页面点击事件响应
     */
    @Override
    public void onClick(View v) {
        // 通过 SuggestionInfoActivity 中的静态方法直接传值
        switch (v.getId()){
            case R.id.comfort_button:
                SuggestionInfoActivity.actionStart(this, comfortInfo,comfortSign,"舒适度指数");
                break;
            case R.id.car_wash_button:
                SuggestionInfoActivity.actionStart(this, carWashInfo,carWashSign,"洗车指数");
                break;
            case R.id.sport_button:
                SuggestionInfoActivity.actionStart(this, sportInfo,sportSign,"运动指数");
                break;
            case R.id.cold_button:
                SuggestionInfoActivity.actionStart(this, coldInfo,coldSign,"感冒指数");
                break;
            case R.id.clothes_button:
                SuggestionInfoActivity.actionStart(this, clothesInfo,clothesSign,"穿衣指数");
                break;
            case R.id.uv_button:
                SuggestionInfoActivity.actionStart(this, uvInfo,uvSign,"紫外线指数");
                break;
            case R.id.iv_add_city:
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_loc:
                showShort("开始定位");
                LocationClientOption option = new LocationClientOption();
                option.setIsNeedAddress(true);
                mlocationClient.setLocOption(option);
                mlocationClient.start();
                break;
            case R.id.weather_now_layout:
                Intent intent1 = new Intent(this,NowInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.calculator_button:
                Intent intent4 = new Intent(this,CalculatorActivity.class);
                startActivity(intent4);
                break;
            case R.id.flashlight_button:
                Intent intent5 = new Intent(this,FlashlightActivity.class);
                startActivity(intent5);
                break;
            case R.id.note_button:
                Intent intent6 = new Intent(this,NoteActivity.class);
                startActivity(intent6);
                break;
            default:
                break;
        }
    }

    /**
     * 更新动画，在一个动画结束时进行更新，再进行另一个动画
     */
    private void showAnimationAlpha(final View view){
        Animation alpha = AnimationUtils.loadAnimation(MainActivity.this,R.anim.alpha_before);
        view.startAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String cityName = prefs.getString("cityName", null);
                requestWeather(cityName);
                Animation alpha = AnimationUtils.loadAnimation(MainActivity.this,R.anim.alpha_after);
                view.startAnimation(alpha);
                showShort("刷新成功");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 设置自身的静态跳转函数
     */
    public static void actionStart(Context context ,String cityName){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("cityName", cityName);
        context.startActivity(intent);
    }

    /**
     * 设置是否显示函数
     */
    public void setincludeview(View view,int sign){
        if (sign==8){
            view.setVisibility(View.GONE);
        }else if (sign==0){
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"HAHHA");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String cityName = prefs.getString("cityName", null);
        String weatherString = prefs.getString("weatherResponse", null);        // weather 保存API 返回的字符串
        String weathersString = prefs.getString("weatherResponses", null);
        if (weatherString != null && weathersString !=null){
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            Weathers weathers=Utility.handleWeathersResponse(weathersString);
            showWeatherInfo(weather);
            showWeathersInfo(weathers);
        }else {
            // 无缓存时向服务器查询数据
            if (getNetworkInfo() != null && getNetworkInfo().isAvailable()){
                // 查询完之后显示 coordinatorLayout.setVisibility(View.VISIBLE);
                requestWeather(cityName);
            }else{
                showDialog(null, "当前无网络，请打开网络",SIGN_NO_INTERNET);
            }

        }





    }
}
