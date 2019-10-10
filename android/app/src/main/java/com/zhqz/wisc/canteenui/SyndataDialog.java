package com.zhqz.wisc.canteenui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.zhqz.wisc.R;


/**
 * 普通的提示对话框
 * 如:更新提示
 */
public class SyndataDialog extends Dialog {


    private Context mContext;
    private ProgressBar syndata_progressBar;


    public SyndataDialog(Context context) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syndata_dialog);
        getView();
    }

    private void getView() {
        syndata_progressBar = (ProgressBar) findViewById(R.id.syndata_progressBar);
    }

}
