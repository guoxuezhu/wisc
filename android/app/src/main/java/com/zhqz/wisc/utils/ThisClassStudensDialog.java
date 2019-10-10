package com.zhqz.wisc.utils;

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
import com.zhqz.wisc.data.model.ThisClassStudents;
import com.zhqz.wisc.ui.adapter.ThisClassStudentsAdapter;

import java.util.List;

/**
 * Created by jingjingtan on 4/6/17.
 */

public class ThisClassStudensDialog extends Dialog {

    private Context mContext;
    private RecyclerView class_student_listview;
    private TextView closed_time;
    private ImageView close_image;

    public TextView this_rooms_name;//总次数


    public ThisClassStudensDialog(Context context) {
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
        setContentView(R.layout.this_class_students);
        getView();
    }

    private void getView() {
        this_rooms_name = (TextView) findViewById(R.id.this_rooms_name);
        class_student_listview = (RecyclerView) findViewById(R.id.class_student_listview);
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


    public void setData(List<ThisClassStudents> thisClassStudents) {
        this_rooms_name.setText(thisClassStudents.get(0).className);
        class_student_listview.setLayoutManager(new LinearLayoutManager(mContext));
        ThisClassStudentsAdapter thisClassStudentsAdapter = new ThisClassStudentsAdapter(mContext, thisClassStudents);
        class_student_listview.setAdapter(thisClassStudentsAdapter);
        TimeThread.setOpenedTime(mmhandler);
    }
}