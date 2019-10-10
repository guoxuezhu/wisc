package com.zhqz.wisc.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.CabinetInfo;
import com.zhqz.wisc.data.model.CabinetPerson;
import com.zhqz.wisc.ui.adapter.CabinetInfoAdapter;

import java.util.List;

/**
 * Created by jingjingtan on 11/23/17.
 */

public class CabintDialog extends Dialog {
    private Context mContext;

    public CabintDialog(Context context, DialogCallback cb) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
        mCallback = cb;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabint_dialog);
        getView();
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
    private RecyclerView listview;
    private TextView personal_cabinet_status, personal_cabinet_position, personal_cabinet_name, personal_cabinet_number, closed_time;
    private Button personal_cabint, class_cabint;
    private DialogCallback mCallback;

    private ImageView close_image;

    private LinearLayout cabint_leave_class_cabint, cabint_leave_personal_cabint, btn_cabint_LL;

    private void getView() {

        listview = (RecyclerView) findViewById(R.id.class_cabint_listview);


        btn_cabint_LL = (LinearLayout) findViewById(R.id.btn_cabint_LL);
        cabint_leave_class_cabint = (LinearLayout) findViewById(R.id.cabint_leave_class_cabint);
        cabint_leave_personal_cabint = (LinearLayout) findViewById(R.id.cabint_leave_personal_cabint);

        personal_cabinet_status = (TextView) findViewById(R.id.personal_cabinet_status);
        personal_cabinet_position = (TextView) findViewById(R.id.personal_cabinet_position);
        personal_cabinet_number = (TextView) findViewById(R.id.personal_cabinet_number);
        personal_cabinet_name = (TextView) findViewById(R.id.personal_cabinet_name);

        personal_cabint = (Button) findViewById(R.id.personal_cabint);
        class_cabint = (Button) findViewById(R.id.class_cabint);

        if (WiscApplication.prefs.getisClassTeacher() == true) {
            btn_cabint_LL.setVisibility(View.VISIBLE);
        } else {
            btn_cabint_LL.setVisibility(View.GONE);
        }
        cabint_leave_class_cabint.setVisibility(View.GONE);
        cabint_leave_personal_cabint.setVisibility(View.VISIBLE);

        personal_cabint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_cabint.setBackgroundResource(R.mipmap.mela_all_left);
                class_cabint.setBackgroundResource(R.mipmap.meal_right);
                mCallback.PersonalCabintOnclick();
            }
        });
        class_cabint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_cabint.setBackgroundResource(R.mipmap.meal_left);
                class_cabint.setBackgroundResource(R.mipmap.meal_all_right);
                mCallback.ClassCabintOnclick();
            }
        });

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


    private void initDataPersonal(List<CabinetPerson.CabinetClassItem> cabinetPersonInfos) {
        if (cabinetPersonInfos == null || cabinetPersonInfos.size() <= 0) {
            personal_cabinet_name.setText("姓名：本人");
            personal_cabinet_number.setText("柜子编号：未知");
            personal_cabinet_position.setText("位置：未知");
            personal_cabinet_status.setText("状态：未分配");
        } else {
            ELog.d("====个人柜子========" + cabinetPersonInfos.get(0).toString());
            personal_cabinet_name.setText("姓名：" + cabinetPersonInfos.get(0).name.toString());
            personal_cabinet_number.setText("柜子编号：" + cabinetPersonInfos.get(0).lockerNoNumber + "");
            personal_cabinet_position.setText("位置：" + cabinetPersonInfos.get(0).position + "");
            if (cabinetPersonInfos.get(0).distribution == 0) {
                personal_cabinet_status.setText("状态：未分配");
            } else {
                personal_cabinet_status.setText("状态：分配");
            }
        }
    }

    private void initDataClass(List<CabinetInfo> cabinetInfos) {
        ELog.d("====点击了班级柜子========" + cabinetInfos.toString());
        listview.setLayoutManager(new LinearLayoutManager(mContext));
        CabinetInfoAdapter cabinetInfoAdapter = new CabinetInfoAdapter(cabinetInfos, mContext, mCallback);
        listview.setAdapter(cabinetInfoAdapter);

    }

    public void setPersonalData(List<CabinetPerson.CabinetClassItem> cabinetPersonInfos) {
        initDataPersonal(cabinetPersonInfos);
        cabint_leave_class_cabint.setVisibility(View.GONE);
        cabint_leave_personal_cabint.setVisibility(View.VISIBLE);
        TimeThread.setOpenedTime(mmhandler);
    }

    public void setClassData(List<CabinetInfo> cabinetInfo) {
        initDataClass(cabinetInfo);
        cabint_leave_class_cabint.setVisibility(View.VISIBLE);
        cabint_leave_personal_cabint.setVisibility(View.GONE);
        TimeThread.setOpenedTime(mmhandler);
    }


    public interface DialogCallback extends CabinetInfoAdapter.CallBack {
        void PersonalCabintOnclick();

        void ClassCabintOnclick();

        void OnClickItemCabinet_urgent(CabinetInfo item);

        @Override
        void OnClickItemCabinet_locking(CabinetInfo item);

        @Override
        void OnClickItemCabinet_open(CabinetInfo item);
    }
}