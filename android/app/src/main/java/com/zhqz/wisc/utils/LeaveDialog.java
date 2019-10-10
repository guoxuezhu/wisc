package com.zhqz.wisc.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.Leave;
import com.zhqz.wisc.data.model.StudentLeaveReason;
import com.zhqz.wisc.data.model.StudentLeaveSpainner;
import com.zhqz.wisc.ui.adapter.LeaveAdapter;
import com.zhqz.wisc.ui.adapter.StudentLeaveSpainnerAdapter;
import com.zhqz.wisc.ui.adapter.StudentLeaveSpainnerEndDateAdapter;
import com.zhqz.wisc.ui.adapter.StudentSpainnerReasonAdapter;

import java.util.List;

/**
 * Created by jingjingtan on 11/23/17.
 */

public class LeaveDialog extends Dialog {
    private Context mContext;
    Leave leavelist;
    private ImageView leave_close_image;
    private TextView closed_time;

    public LeaveDialog(Context context, DialogCallback cb, Leave leave) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
        mCallback = cb;
        leavelist = leave;
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
        setContentView(R.layout.leave_dialog);
        getView();
    }

    private RecyclerView listview;
    LinearLayout leave_date_start_dialog, leave_date_end_dialog, leave_nodata_de, leave_LL;
    TextView Textleave_date_end, Textleave_date_satrt;
    Spinner start_spinner, end_spinner, Spinner_Reason;
    Button summit;
    private DialogCallback mCallback;


    private void getView() {

        closed_time = (TextView) findViewById(R.id.closed_time);
        leave_close_image = (ImageView) findViewById(R.id.close_image);
        leave_close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                TimeThread.setClosed();
            }
        });

        listview = (RecyclerView) findViewById(R.id.leave_listview);

        leave_date_start_dialog = (LinearLayout) findViewById(R.id.leave_date_start_dialog);
        leave_date_end_dialog = (LinearLayout) findViewById(R.id.leave_date_end_dialog);
        leave_nodata_de = (LinearLayout) findViewById(R.id.leave_nodata_de);
        leave_LL = (LinearLayout) findViewById(R.id.leave_LL);

        Textleave_date_end = (TextView) findViewById(R.id.Textleave_date_end);
        Textleave_date_satrt = (TextView) findViewById(R.id.Textleave_date_start);
        start_spinner = (Spinner) findViewById(R.id.start_spinner);
        end_spinner = (Spinner) findViewById(R.id.end_spinner);

        summit = (Button) findViewById(R.id.sure);
        summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.Leave_summit(leavelist.userId, "2", Textleave_date_satrt.getText().toString(), Textleave_date_end.getText().toString(), beginNum, endNum, beginLessonNum, endLessonNum, typeId, leaveName);
            }
        });

        Spinner_Reason = (Spinner) findViewById(R.id.Spinner_Reason);


        mCallback.leave_date_start_dialog("0", leavelist.userId);
        mCallback.leave_date_end_dialog("0", leavelist.userId);

        Textleave_date_satrt.setText(DateUtil.getNowYMd() + "");
        Textleave_date_end.setText(DateUtil.getNowYMd() + "");
        Textleave_date_satrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.leave_date_start_dialog("1", leavelist.userId);
                dismiss();
            }
        });
        Textleave_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.leave_date_end_dialog("2", leavelist.userId);
                dismiss();
            }
        });
        initData();
        spinnerOnItemClick();
        TimeThread.setOpenedTime(mmhandler);
    }

    List<StudentLeaveSpainner> StartmList;
    List<StudentLeaveSpainner> EndList;
    List<StudentLeaveReason> ReasonList;
    private String beginLessonNum = null; // 开始时间
    private String endLessonNum = null; // 结束时间
    private int beginNum = -1;//开始的节数 （数字）
    private int endNum = -1;  //结束的节数 （数字）
    private int typeId = -1; // 请假类型 4.事假  5.病假
    private String leaveName = null; //请假名称

    private void spinnerOnItemClick() {
        start_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beginLessonNum = StartmList.get(position).name;
                beginNum = StartmList.get(position).lessonNum;
                TimeThread.setOpenedTime(mmhandler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        end_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endLessonNum = EndList.get(position).name;
                endNum = EndList.get(position).lessonNum;
                TimeThread.setOpenedTime(mmhandler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner_Reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeId = ReasonList.get(position).id;
                leaveName = ReasonList.get(position).name;
                TimeThread.setOpenedTime(mmhandler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    LeaveAdapter leaveAdapter;

    private void initData() {
        if (leavelist != null && leavelist.data != null && leavelist.data.size() > 0) {
            leave_LL.setVisibility(View.VISIBLE);
            leave_nodata_de.setVisibility(View.GONE);
            listview.setLayoutManager(new LinearLayoutManager(mContext));
            leaveAdapter = new LeaveAdapter(mContext, leavelist.data);
            listview.setAdapter(leaveAdapter);
        } else {
            leave_nodata_de.setVisibility(View.VISIBLE);
            leave_LL.setVisibility(View.GONE);
        }
    }

    public void setStartDate(String time) {
        Textleave_date_satrt.setText(time);
        TimeThread.setOpenedTime(mmhandler);
    }

    public void setEndDate(String time) {
        Textleave_date_end.setText(time);
        TimeThread.setOpenedTime(mmhandler);
    }

    public void setData(Leave leaves) {

    }

    StudentLeaveSpainnerAdapter SatrtDateAdapter;
    StudentLeaveSpainnerEndDateAdapter EndDateAdapter;
    StudentSpainnerReasonAdapter ReasonAdapter;

    public void SatrtDateAdapter(List<StudentLeaveSpainner> studentLeaveSpainners) {
        if (studentLeaveSpainners == null || studentLeaveSpainners.size() <= 0) {
            beginNum = 0;
            beginLessonNum = null;
        }
        StartmList = studentLeaveSpainners;
        if (SatrtDateAdapter == null) {
            SatrtDateAdapter = new StudentLeaveSpainnerAdapter(mContext, studentLeaveSpainners);
            start_spinner.setAdapter(SatrtDateAdapter);
        } else {
            SatrtDateAdapter.setdata(studentLeaveSpainners);
        }

    }

    public void EndDateAdapter(List<StudentLeaveSpainner> studentLeaveSpainners) {
        if (studentLeaveSpainners == null || studentLeaveSpainners.size() <= 0) {
            endNum = 0;
            endLessonNum = null;
        }
        EndList = studentLeaveSpainners;
        if (EndDateAdapter == null) {
            EndDateAdapter = new StudentLeaveSpainnerEndDateAdapter(mContext, studentLeaveSpainners);
            end_spinner.setAdapter(EndDateAdapter);
        } else {
            EndDateAdapter.setdata(studentLeaveSpainners);
        }
    }

    public void ReasonAdapter(List<StudentLeaveReason> mList) {
        ReasonList = mList;
        if (ReasonAdapter == null) {
            ReasonAdapter = new StudentSpainnerReasonAdapter(mContext, mList);
            Spinner_Reason.setAdapter(ReasonAdapter);
        } else {
            ReasonAdapter.setdata(mList);
        }
    }

    public interface DialogCallback {
        void leave_date_start_dialog(String time, int UserId);

        void leave_date_end_dialog(String time, int UserId);

        void Leave_summit(int UserId, String psType, String startDate, String endDate, int beginNum, int endNum, String beginLessonNum, String endLessonNum, int typeId, String leaveName);
    }
}
