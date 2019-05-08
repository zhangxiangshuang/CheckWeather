package com.jason.checkweather;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AboutApplicationActivity extends BaseActivity {

    private Button github;
    private Button csdn;
    @Override
    public void initView() {
        setContentView(R.layout.activity_about_application);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("关于天气");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        github = (Button)findViewById(R.id.github);
        csdn = (Button)findViewById(R.id.csdn);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        github.setOnClickListener(this);
        csdn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (v.getId()){
            case R.id.github:
                intent.setData(Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=630520570&version=1&src_type=web&web_src=oicqzone.com"));
                break;
            case R.id.csdn:
                intent.setData(Uri.parse("https://weibo.cn/qr/userinfo?uid=5029421487"));
                break;
        }
        startActivity(intent);
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
}
