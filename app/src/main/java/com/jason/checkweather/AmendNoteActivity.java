package com.jason.checkweather;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.checkweather.db.NoteRecond;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.TimeZone;

public class AmendNoteActivity extends BaseActivity {
    private Button btnSave;
    private Button btnBack;
    private TextView amendTime;
    private TextView amendTitle;
    private EditText amendBody;
    private NoteRecond record;
    private AlertDialog.Builder dialog;
    @Override
    public void initView() {
        setContentView(R.layout.activity_amend_note);

        btnBack = (Button) findViewById(R.id.button_back);
        btnSave = (Button) findViewById(R.id.button_save);
        amendTitle = (TextView) findViewById(R.id.amend_title);
        amendBody = (EditText) findViewById(R.id.amend_body);
        amendTime = (TextView) findViewById(R.id.amend_title_time);
    }

    @Override
    public void initData() {
        LitePal.initialize(this);
        Intent intent = this.getIntent();
        if (intent!=null){
            record = new NoteRecond();
            record.setId(Integer.valueOf(intent.getStringExtra("id")));
            record.setTitle_name(intent.getStringExtra("title"));
            record.setText_body(intent.getStringExtra("body"));
            record.setCreate_time(intent.getStringExtra("time"));

            amendTitle.setText(record.getTitle_name());
            amendTime.setText(record.getCreate_time());
            amendBody.setText(record.getText_body());
        }
    }

    @Override
    public void initListener() {
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String body;
        body = amendBody.getText().toString();
        switch (v.getId()){
            case R.id.button_save:
                if (updateFunction(body)){
                    intentStart();
                }
                break;
            case R.id.button_back:
                showDialog(body);
                clearDialog();
                break;
            default:
                break;
        }
    }
    /*
     * 返回备忘录主界面
     */
    void intentStart(){
        Intent intent = new Intent(AmendNoteActivity.this,NoteActivity.class);
        startActivity(intent);
        this.finish();
    }
    /*
     * 弹窗函数
     * @param title
     * @param body
     * @param createDate
     */
    void showDialog(final String body){
        dialog = new AlertDialog.Builder(AmendNoteActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("是否保存当前编辑内容");
        dialog.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateFunction(body);
                        intentStart();
                    }
                });

        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intentStart();
                    }
                });
        dialog.show();
    }

    void clearDialog(){
        dialog = null;
    }

    boolean isShowIng(){
        if (dialog!=null){
            return true;
        }else{
            return false;
        }
    }


    public String getTimes(){
        String hour;
        String minute;
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String day = String.valueOf(cal.get(Calendar.DATE));
        if (cal.get(Calendar.AM_PM) == 0)
            hour = String.valueOf(cal.get(Calendar.HOUR));
        else
            hour = String.valueOf(cal.get(Calendar.HOUR)+12);
        if(cal.get(Calendar.MINUTE)<10)
            minute =0+String.valueOf(cal.get(Calendar.MINUTE));
        else
            minute = String.valueOf(cal.get(Calendar.MINUTE));
        String time=year + "年" + month + "月" + day + "日"+hour + ":" + minute ;
        return time;
    }

    /*
     * 保存函数
     */
    boolean updateFunction(String body){



        boolean flag = true;
        if (body.length()>200){
            Toast.makeText(this,"内容过长",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(flag){
            // update

            NoteRecond noteRecond=new NoteRecond();
            noteRecond.setCreate_time(getTimes());
            noteRecond.setText_body(body);
            noteRecond.update(record.getId());
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();

        }
        return flag;
    }
}

