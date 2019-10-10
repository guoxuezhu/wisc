package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.Courses;

import java.util.List;

/**
 * Created by guoxuezhu on 16-12-20.
 */
public class TomorrwKeChengAdapter extends BaseAdapter {

    Context context;
    List<Courses.Tomorrow> tomorrowList;

    public TomorrwKeChengAdapter(Context context, List<Courses.Tomorrow> tomorrows) {
        this.context = context;
        this.tomorrowList = tomorrows;
    }

    @Override
    public int getCount() {
        return tomorrowList.size();
    }

    @Override
    public Object getItem(int position) {
        return tomorrowList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        convertView = View.inflate(context, R.layout.kecheng_xiangqing_item, null);
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.jieci_tv= (TextView) convertView.findViewById(R.id.jieci_tv);
            viewHolder.jiaoshi_tv= (TextView) convertView.findViewById(R.id.jiaoshi_tv);
            viewHolder.renkelaoshi_tv= (TextView) convertView.findViewById(R.id.renkelaoshi_tv);
            viewHolder.kechengname_tv= (TextView) convertView.findViewById(R.id.kechengname_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Courses.Tomorrow mcourses= tomorrowList.get(position);
        viewHolder.jieci_tv.setText(mcourses.classSection);
//        viewHolder.shijian_tv.setText(mcourses.classTime);
        viewHolder.jiaoshi_tv.setText(mcourses.classRoom);
        viewHolder.renkelaoshi_tv.setText(mcourses.teacherName);
        viewHolder.kechengname_tv.setText(mcourses.courseName);
        return convertView;
    }


    class ViewHolder {
        TextView jieci_tv;
        TextView jiaoshi_tv;
        TextView renkelaoshi_tv;
        TextView kechengname_tv;
    }
}
