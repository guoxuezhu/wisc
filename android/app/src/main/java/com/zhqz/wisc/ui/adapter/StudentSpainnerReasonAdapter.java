package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.StudentLeaveReason;

import java.util.List;

/**
 * Created by jingjingtan on 11/21/17.
 */

public class StudentSpainnerReasonAdapter extends BaseAdapter {
    private List<StudentLeaveReason> mList;
    private Context mContext;

    public StudentSpainnerReasonAdapter(Context pContext, List<StudentLeaveReason> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
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
        convertView=_LayoutInflater.inflate(R.layout.item_select1, null);
        if(convertView!=null) {
            TextView _TextView1=(TextView)convertView.findViewById(R.id.textView1);
            _TextView1.setText(mList.get(position).name);
        }
        return convertView;
    }
    public void setdata(List<StudentLeaveReason> pList){
        this.mList = pList;
        notifyDataSetChanged();
    }
}