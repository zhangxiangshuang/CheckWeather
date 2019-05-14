package com.jason.checkweather;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jason.checkweather.db.NoteRecond;

import org.litepal.LitePal;

import java.util.Calendar;

import java.util.TimeZone;


public class EditNoteActivity extends BaseActivity {

    private static final String TAG = "EditNoteActivity";
    private Button btnSave;
    private Button btnBack;
    private TextView editTime;
    private EditText editTitle;
    private EditText editBody;
    private AlertDialog.Builder dialog;

    @Override
    public void initView() {
        setContentView(R.layout.activity_edit_note);
        btnBack = (Button) findViewById(R.id.button_back);
        btnSave = (Button) findViewById(R.id.button_save);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editBody = (EditText) findViewById(R.id.edit_body);
        editTime = (TextView) findViewById(R.id.edit_title_time);
    }

    @Override
    public void initData() {
        LitePal.initialize(this);
        Log.d(TAG, getTimes());
        editTime.setText(getTimes());
    }

    @Override
    public void initListener() {
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String title;
        String body;
        title = editTitle.getText().toString();
        body = editBody.getText().toString();
        switch (v.getId()){
            case R.id.button_save:
                if (saveFunction(title,body,getTimes())){
                    intentStart();
                }
                break;
            case R.id.button_back:
                if (!"".equals(title)||!"".equals(body)){
                    showDialog(title,body,getTimes());
                    clearDialog();
                } else {
                    intentStart();
                }
                break;
            default:
                break;
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
     * 弹窗函数
     * @param title
     * @param body
     * @param createDate
     */
    void showDialog(final String title, final String body, final String createDate){
        dialog = new AlertDialog.Builder(EditNoteActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("是否保存当前编辑内容");
        dialog.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveFunction(title, body, createDate);
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

    /*
     *  清空弹窗
     */
    void clearDialog(){
        dialog = null;
    }

    /*
     *  判断是否弹窗是否显示
     */
    boolean isShowIng(){
        if (dialog!=null){
            return true;
        }else{
            return false;
        }
    }


    /*
     * 备忘录保存函数
     */
    boolean saveFunction(String title,String body,String createDate){

        boolean flag = true;
        if ("".equals(title)){
            Toast.makeText(this,"标题不能为空",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (title.length()>10){
            Toast.makeText(this,"标题过长",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (body.length()>200){
            Toast.makeText(this,"内容过长",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if ("".equals(createDate)){
            Toast.makeText(this,"时间格式错误",Toast.LENGTH_SHORT).show();
            flag = false;
        }

        if(flag){
            addRecordToLitePal(title,body,createDate);
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();

        }
        return flag;
    }

    //数据库插入数据
    private void addRecordToLitePal( String title_name, String text_body, String create_time) {
        NoteRecond record=new NoteRecond();
        record.setTitle_name(title_name);
        record.setText_body(text_body);
        record.setCreate_time(create_time);
        record.save();
        Log.d(TAG,"添加成功");
    }

    /*
     * 返回备忘录主界面
     */
    void intentStart(){
        Intent intent = new Intent(EditNoteActivity.this,NoteActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String title;
            String body;
            String createDate;
            title = editTitle.getText().toString();
            body = editBody.getText().toString();
            createDate = editTime.getText().toString();
            //当返回按键被按下
            if (!isShowIng()){
                if (!"".equals(title)||!"".equals(body)){
                    showDialog(title,body,createDate);
                    clearDialog();
                } else {
                    intentStart();
                }
            }
        }
        return false;
    }
}
