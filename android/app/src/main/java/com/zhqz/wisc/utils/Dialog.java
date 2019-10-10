package com.zhqz.wisc.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhqz.wisc.R;


/**
 * 普通的提示对话框
 * <p>
 * 如:更新提示
 *
 * @author jqqiu
 */

public class Dialog extends android.app.Dialog implements View.OnClickListener {


    /**
     * 点名提交确认
     */
    public final static int BIND_QUEREN = 0x1;

    public int mIndex1 = 1;
    private Context mContext;
    private Button mBtnSure, mBtnCancel;
    private int mType;
    private TextView mTextTitle;
    private TextView dialogContent;

    public Dialog(Context context, int type) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
        mType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view);
        getView();
    }

    private void getView() {
        mTextTitle = (TextView) findViewById(R.id.dialog_title);
        dialogContent = (TextView) findViewById(R.id.dialog_content);
        mBtnSure = (Button) findViewById(R.id.dialog_confirm);
        mBtnCancel = (Button) findViewById(R.id.dialog_cancel);
        mBtnSure.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        switch (mType) {
            case BIND_QUEREN:
                mTextTitle.setText("绑定提示");
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_confirm:
                mIndex1 = 1;
                dismiss();
                break;
            case R.id.dialog_cancel:
                mIndex1 = 0;
                dismiss();
                break;
            default:
                break;
        }

    }

    public void setText(String string){
        dialogContent.setText(string);
    }
}
