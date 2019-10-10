package com.zhqz.wisc.canteenui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhqz.wisc.R;

/**
 * Created by jingjingtan on 3/22/18.
 */

public class CanteenPromptDialog extends Dialog implements View.OnClickListener {

    public final static int ZAOCAN = 0x1;
    public final static int WUCAN = 0x2;
    public final static int WANCAN = 0x3;
    public final static int BIND = 0x4;
    private int mType;
    private Context mContext;
    private Button mBtnSure, mBtnCancel;
    public int mIndex = 0;
    private TextView dialogContent;
    private Button dialog_queren;

    public CanteenPromptDialog(Context context,int type) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
        mType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_nicedialog_view);
        getView();
    }

    private void getView() {
        dialogContent = (TextView) findViewById(R.id.dialog_content);
        mBtnSure = (Button) findViewById(R.id.dialog_confirm);
        mBtnCancel = (Button) findViewById(R.id.dialog_cancel);
        dialog_queren = (Button) findViewById(R.id.dialog_queren);
        mBtnSure.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        dialog_queren.setOnClickListener(this);
        switch (mType) {
            case ZAOCAN:
                dialogContent.setText("现在是早餐时间");
                mBtnSure.setVisibility(View.GONE);
                mBtnCancel.setVisibility(View.GONE);
                dialog_queren.setVisibility(View.VISIBLE);
                break;
            case WUCAN:
                dialogContent.setText("现在是午餐时间");
                mBtnSure.setVisibility(View.GONE);
                mBtnCancel.setVisibility(View.GONE);
                dialog_queren.setVisibility(View.VISIBLE);
                break;
            case WANCAN:
                dialogContent.setText("现在是晚餐时间");
                mBtnSure.setVisibility(View.GONE);
                mBtnCancel.setVisibility(View.GONE);
                dialog_queren.setVisibility(View.VISIBLE);
                break;
            case BIND:
                mBtnSure.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.VISIBLE);
                dialog_queren.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_confirm:
                mIndex = 1;
                dismiss();
                break;
            case R.id.dialog_cancel:
                mIndex = 2;
                dismiss();
                break;
            case R.id.dialog_queren:
                mIndex = 1;
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setContentTV(String canteenName) {
        dialogContent.setText("你将此设备绑定为"+canteenName);
    }
}
