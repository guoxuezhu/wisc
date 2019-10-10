package com.zhqz.wisc.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.CourseTwo;
import com.zhqz.wisc.ui.adapter.AllCourseAdapter;

import java.util.List;


/**
 * Created by guoxuezhu on 16-12-20.
 */
public class AllCourseDialog extends Dialog {

    private Context mContext;
    private RecyclerView all_course_list;
    private TextView closed_time;
    private ImageView close_image;

    public AllCourseDialog(Context context) {
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
        setContentView(R.layout.all_kecheng);
        getView();
    }

    private void getView() {
        all_course_list = (RecyclerView) findViewById(R.id.all_course_list);
        closed_time = (TextView) findViewById(R.id.closed_time);
        close_image = (ImageView) findViewById(R.id.close_image);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                TimeThread.setClosed();
            }
        });
    }


    public void setData(List<CourseTwo> coursesList) {
        //创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        all_course_list.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        all_course_list.setHasFixedSize(true);
        //创建并设置Adapter
        AllCourseAdapter allCourseAdapter = new AllCourseAdapter(mContext, coursesList);
        all_course_list.setAdapter(allCourseAdapter);
        TimeThread.setOpenedTime(mmhandler);
    }

}
