package com.zhqz.wisc.ui.bind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhqz.wisc.R;

import java.util.List;

/**
 * Created by jingjingtan on 8/11/17.
 */

public class SeclectSchoolAdapter extends BaseAdapter {
    private List<Person> mList;
    private Context mContext;

    public SeclectSchoolAdapter(Context pContext, List<Person> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater= LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.item_select, null);
        if(convertView!=null) {
            TextView _TextView1=(TextView)convertView.findViewById(R.id.textView1);
            _TextView1.setText(mList.get(position).value);
        }
        return convertView;
    }
}