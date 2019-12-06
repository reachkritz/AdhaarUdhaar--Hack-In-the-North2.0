package com.hint.paranoid.aadharudhaar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jatin_jt_narula on 24/3/17.
 */

public class ListViewAdapter extends BaseAdapter
{
    Activity context1;
    private ArrayList<RowData> rowText;
    String description[];
    int type;
    public ListViewAdapter(Activity context,
                             ArrayList<RowData> rowText,int type) {
        context1 = context;
        this.rowText = rowText;
        this.type = type;
    }

    @Override
    public int getCount() {
        return rowText.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            if(type == 1)convertView = LayoutInflater.from(context1).inflate(
                    R.layout.list_item_row, null);
            else
                convertView = LayoutInflater.from(context1).inflate(
                        R.layout.list_item_row_borrow, null);
            viewHolder = new ViewHolder();
            viewHolder.txt1 = (TextView) convertView
                    .findViewById(R.id.name_lend);
            viewHolder.txt2 = (TextView) convertView
                    .findViewById(R.id.money_lend);
            viewHolder.txt3 = (TextView) convertView
                    .findViewById(R.id.date_lend);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt1.setText(rowText.get(position).nameLend);
        viewHolder.txt2.setText("\u20B9 "+rowText.get(position).moneyLend);
        viewHolder.txt3.setText(rowText.get(position).dateLend);
        return convertView;
    }

    public class ViewHolder {
        public TextView txt1;
        public TextView txt2;
        public TextView txt3;

    }
}