package com.jason.checkweather;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jason.checkweather.db.CityRecond;
import com.jason.checkweather.gson.BasicSearch;
import com.jason.checkweather.gson.CitySearch;
import com.jason.checkweather.util.HttpUtil;
import com.jason.checkweather.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaActivity extends BaseActivity {
    private EditText searchText;
    private Button searchButton;
    private Button delectcity;
    private ListView listView;
    private ListView listViewRecond;
    private List<String> cityList = new ArrayList<>();
    private List<String> recondList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> recondAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_choose_area);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("选择地区");
        }

        searchText = (EditText)findViewById(R.id.search_text);
        searchButton = (Button)findViewById(R.id.search_button);
        listView = (ListView)findViewById(R.id.list_view);
        delectcity= (Button) findViewById(R.id.delectcity);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getNetworkInfo() != null && getNetworkInfo().isAvailable()){
                    String cityName = cityList.get(position);
                    solveSearchRecond(cityName);
                    MainActivity.actionStart(ChooseAreaActivity.this, cityName);
                    finish();
                }else{
                    showShort("当前没有网络");
                }
            }
        });

        listViewRecond = (ListView)findViewById(R.id.list_view_recond);
        recondAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recondList);
        listViewRecond.setAdapter(recondAdapter);
        listViewRecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getNetworkInfo() != null && getNetworkInfo().isAvailable()){
                    String cityName = recondList.get(position);
                    MainActivity.actionStart(ChooseAreaActivity.this, cityName);
                    finish();
                }else{
                    showShort("当前没有网络");
                }
            }
        });
        showRecond();

    }

    @Override
    public void initData() {
        LitePal.getDatabase();

    }

    @Override
    public void initListener() {

        searchButton.setOnClickListener(this);

        delectcity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:
                hideSoftInputMethod(ChooseAreaActivity.this);
                showSearchResult();
                break;
            case R.id.delectcity:
                deleteSearchRecond();
                showRecond();
                break;
            default:
        }
    }

    public void showSearchResult(){
        String cityName = searchText.getText().toString();
        if (!TextUtils.isEmpty(cityName)){
            // 如果不为空，则进行查询
            if (getNetworkInfo() != null && getNetworkInfo().isAvailable()){
                String address = "https://search.heweather.net/find?location=" + cityName + "&key=0c6010f67e4648e39af80b623c4b0cd1&group=cn";
                requestData(address);
            }else{
                showShort("当前网络无连接");
            }
        }else{
            showShort("请输入城市名称");
        }

    }

    public void requestData(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShort("请求数据失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final CitySearch mCitySearch = Utility.parseCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCity(mCitySearch);
                    }
                });
            }
        });
    }

    //关闭键盘函数
    public static void hideSoftInputMethod(Activity act) {
        View view = act.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) act
                    .getSystemService(act.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void showCity(CitySearch mCitySearch){
        cityList.clear();
        for (BasicSearch basicSearch : mCitySearch.basicSearchList) {
            BasicSearch a = new BasicSearch(basicSearch.cityName,basicSearch.parentName ,basicSearch.provinceName);
           cityList.add(a.cityName);
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }


    /**
     * 保存城市名称 -> 数据库
     */
    public void solveSearchRecond(String cityName){
        int i=0;//标志位 0代表没有 1代表有
        for (String citynames:recondList){
            if (citynames.equals(cityName)){
                i=1;
                break;
            }
        }
        if (i==0){
        CityRecond cityRecond = new CityRecond();
        cityRecond.setCityName(cityName);
        cityRecond.save();}
    }
    public void deleteSearchRecond()
    {
        CityRecond cityRecond = new CityRecond();
        cityRecond.deleteAll(CityRecond.class);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 显示数据库中保存的信息
     */
    public void showRecond(){
        recondList.clear();
        List<CityRecond> list = DataSupport.select("cityName").find(CityRecond.class);
        for (CityRecond recond:list){
            recondList.add(recond.getCityName());
        }
        recondAdapter.notifyDataSetChanged();
        listViewRecond.setSelection(0);
    }
}
