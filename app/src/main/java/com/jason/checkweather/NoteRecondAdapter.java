package com.jason.checkweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jason.checkweather.db.NoteRecond;


import java.util.List;

public class NoteRecondAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<NoteRecond> noteRecondList;



    public NoteRecondAdapter(Context context, int list_item, List<NoteRecond> noteList){
        this.noteRecondList=noteList;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return noteRecondList==null?0:noteRecondList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteRecondList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position){
        this.noteRecondList.remove(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =inflater.inflate(R.layout.list_item,null);
        NoteRecond note = (NoteRecond) getItem(position);
        TextView title_name = (TextView) view.findViewById(R.id.list_item_title);
        TextView text_body = (TextView) view.findViewById(R.id.list_item_body);
        TextView create_time = (TextView) view.findViewById(R.id.list_item_time);
        title_name.setText(note.getTitle_name());
        text_body.setText(note.getText_body());
        create_time.setText(note.getCreate_time());
        return view;
    }
}
