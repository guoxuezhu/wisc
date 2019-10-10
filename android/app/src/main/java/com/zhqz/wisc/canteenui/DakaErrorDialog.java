package com.zhqz.wisc.canteenui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.zhqz.wisc.R;


/**
 * 普通的提示对话框
 * 如:更新提示
 */
public class DakaErrorDialog extends Dialog {

    private Context mContext;
    private TextView daka_error_dialog_content;
    private Handler mHandler;

    public DakaErrorDialog(Context context) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daka_error_dialog);
        getView();
    }

    private void getView() {
        daka_error_dialog_content = (TextView) findViewById(R.id.daka_error_dialog_content);
    }

    public void setContents(String error) {
        daka_error_dialog_content.setText(error);
        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            dismiss();
            mHandler = null;
        }, 1000);
    }
}
