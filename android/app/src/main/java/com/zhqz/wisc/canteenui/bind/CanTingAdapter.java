package com.zhqz.wisc.canteenui.bind;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.CanTing;

import java.util.List;

/**
 * Created by guoxuezhu on 17-3-12.
 */

public class CanTingAdapter extends BaseAdapter {

    Context context;
    private List<CanTing> cantings;
    int mSelect = 0;   //选中项
    public CanTingAdapter(Context context, List<CanTing> canting) {
        this.context = context;
        this.cantings = canting;
    }

    @Override
    public int getCount() {
        return cantings == null ? 0 : cantings.size();
    }

    @Override
    public Object getItem(int position) {
        return cantings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        convertView = View.inflate(context, R.layout.canting_item, null);
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.canting_tv = (TextView) convertView.findViewById(R.id.canting_tv);
            viewHolder.selector_image = (ImageView) convertView.findViewById(R.id.selector_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CanTing canting = cantings.get(position);
        viewHolder.canting_tv.setText(canting.canteenName);

        if (mSelect == position) {
            viewHolder.selector_image.setVisibility(View.VISIBLE);  //选中项背景
        } else {
            viewHolder.selector_image.setVisibility(View.GONE);  //其他项背景
        }

        return convertView;
    }

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
    }


    class ViewHolder {
        TextView canting_tv;
        ImageView selector_image;
    }
}