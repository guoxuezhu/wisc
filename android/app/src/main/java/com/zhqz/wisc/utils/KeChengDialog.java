package com.zhqz.wisc.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.Courses;
import com.zhqz.wisc.ui.adapter.KeChengAdapter;
import com.zhqz.wisc.ui.adapter.TomorrwKeChengAdapter;
import com.zhqz.wisc.ui.adapter.ViewPagerAdapter;
import com.zhqz.wisc.ui.adapter.YesterdayKeChengAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guoxuezhu on 16-12-20.
 */
public class KeChengDialog extends Dialog {

    private Context mContext;
    private ListView kecheng_list_view, kecheng_list_view2, kecheng_list_view3;
    private TextView closed_time, name_tv, status_course;
    private ImageView close_image;

    public KeChengDialog(Context context) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
    }

    Handler mmhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WiscClient.CLOSED_DIALOG_HANDLER:
                    int miao = (int) msg.obj;
                    closed_time.setText(miao + "");
                    if (miao == 0) {
                        dismiss();
                    }
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kecheng_chakan);
        getView();
    }

    ViewPager viewPager;
    private LayoutInflater mInflater;
    private List<View> mViews;
    private ViewPagerAdapter mAdapter;

    private void getView() {

        closed_time = (TextView) findViewById(R.id.closed_time);
        name_tv = (TextView) findViewById(R.id.name_tv);
        status_course = (TextView) findViewById(R.id.status_course);

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        close_image = (ImageView) findViewById(R.id.close_image);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                TimeThread.setClosed();
            }
        });
    }

    LinearLayout no_course, no_course1, no_course2;

    public void aaa(Courses coursesList) {
        mInflater = getLayoutInflater();
        View view1 = mInflater.inflate(R.layout.kecheng_chakan1, null);
        View view2 = mInflater.inflate(R.layout.kecheng_chakan1, null);
        View view3 = mInflater.inflate(R.layout.kecheng_chakan1, null);
        kecheng_list_view = (ListView) view1.findViewById(R.id.kecheng_list_view);
        kecheng_list_view2 = (ListView) view2.findViewById(R.id.kecheng_list_view);
        kecheng_list_view3 = (ListView) view3.findViewById(R.id.kecheng_list_view);
        no_course = (LinearLayout) view1.findViewById(R.id.no_course);
        no_course1 = (LinearLayout) view2.findViewById(R.id.no_course);
        no_course2 = (LinearLayout) view3.findViewById(R.id.no_course);


        mViews = new ArrayList<>();
        mViews.add(view3);
        mViews.add(view1);
        mViews.add(view2);


        mAdapter = new ViewPagerAdapter(mViews);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //当页面滑动时
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //当页面选中时  即当前页面
            @Override
            public void onPageSelected(int position) {

                ELog.i("========" + position + "===" + position % mViews.size());
                no_course.setVisibility(View.GONE);
                no_course1.setVisibility(View.GONE);
                no_course2.setVisibility(View.GONE);
                if (position % mViews.size() == 0) {
                    status_course.setText("昨天的课程");
                    if (coursesList.yesterday != null && coursesList.yesterday.size() != 0) {
                        YesterdayKeChengAdapter keChengAdapter = new YesterdayKeChengAdapter(mContext, coursesList.yesterday);
                        kecheng_list_view3.setAdapter(keChengAdapter);
                    } else {
                        kecheng_list_view3.setVisibility(View.GONE);
                        no_course2.setVisibility(View.VISIBLE);
                    }

                } else if (position % mViews.size() == 1) {
                    status_course.setText("今天的课程");
                    if (coursesList.today != null && coursesList.today.size() != 0) {
                        KeChengAdapter keChengAdapter = new KeChengAdapter(mContext, coursesList.today);
                        kecheng_list_view.setAdapter(keChengAdapter);
                    } else {
                        kecheng_list_view.setVisibility(View.GONE);
                        no_course.setVisibility(View.VISIBLE);
                    }

                } else if (position % mViews.size() == 2) {
                    status_course.setText("明天的课程");
                    if (coursesList.tomorrow != null && coursesList.tomorrow.size() != 0) {
                        TomorrwKeChengAdapter keChengAdapter = new TomorrwKeChengAdapter(mContext, coursesList.tomorrow);
                        kecheng_list_view2.setAdapter(keChengAdapter);
                    } else {
                        kecheng_list_view2.setVisibility(View.GONE);
                        no_course1.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置一开始View位于中间值位置，左右都可以滑动，并且为了使它一开始在第一张图片，需要View的总数对最大值的一半取模
        viewPager.setCurrentItem(1);//Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mViews.size()

    }

    public void setData(Courses coursesList) {
        if (coursesList != null) {
            name_tv.setText(coursesList.name);
        }

        aaa(coursesList);

        TimeThread.setOpenedTime(mmhandler);
    }

}
