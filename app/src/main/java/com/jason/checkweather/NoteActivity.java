package com.jason.checkweather;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.jason.checkweather.db.NoteRecond;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import java.util.List;


public class NoteActivity extends BaseActivity
        implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    private final static String TAG = "NoteActivity";
    private ListView myListView;
    private Button createButton;
    private NoteRecondAdapter myBaseAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("备忘录");
        }
        createButton = (Button) findViewById(R.id.createButton);
        myListView = (ListView) findViewById(R.id.list_view);

    }

    @Override
    public void initData() {
        LitePal.getDatabase();
        List<NoteRecond> noteReconds1=DataSupport.findAll(NoteRecond.class);
        if(noteReconds1.size()==0) {
            initializeLitePal();
            noteReconds1 = DataSupport.findAll(NoteRecond.class);
        }
        myBaseAdapter=new NoteRecondAdapter(NoteActivity.this, R.layout.list_item,noteReconds1);
        myListView.setAdapter(myBaseAdapter);
    }

    @Override
    public void initListener() {
        createButton.setOnClickListener(this);
        myListView.setOnItemClickListener(this);
        myListView.setOnItemLongClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.createButton:
                Intent intent = new Intent(NoteActivity.this, EditNoteActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }


    //向数据库中添加数据
    private void initializeLitePal() {

        addRecordToLitePal("测试1","测试1111","2019-05-13");
        addRecordToLitePal("测试2","测试2222","2019-05-13");
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(NoteActivity.this,AmendNoteActivity.class);
        NoteRecond record = (NoteRecond) myListView.getItemAtPosition(position);
        intent.putExtra("title",record.getTitle_name().trim());
        intent.putExtra("body",record.getText_body().trim());
        intent.putExtra("time",record.getCreate_time().trim());
        intent.putExtra("id",String.valueOf(record.getId()).trim());
        this.startActivity(intent);
        NoteActivity.this.finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        NoteRecond record = (NoteRecond) myListView.getItemAtPosition(position);
        showDialog(record,position);
        return true;
    }

    void showDialog(final NoteRecond record,final int position){

        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(NoteActivity.this);
        dialog.setTitle("是否删除？");
        String textBody = record.getText_body();
        dialog.setMessage(
                textBody.length()>150?textBody.substring(0,150)+"...":textBody);
        dialog.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        NoteRecond noteRecond=new NoteRecond();
                        noteRecond.delete(NoteRecond.class,record.getId());
                        myBaseAdapter.removeItem(position);
                        myListView.post(new Runnable() {
                            @Override
                            public void run() {
                                myBaseAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
