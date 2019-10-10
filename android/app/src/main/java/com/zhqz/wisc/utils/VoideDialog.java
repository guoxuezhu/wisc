package com.zhqz.wisc.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhqz.wisc.R;

/**
 * Created by jingjingtan on 3/21/18.
 */

public class VoideDialog extends android.app.Dialog {

    private Context mContext;
    private TextView tv1, tv2, tv3;
    private ImageView biaoqing_img;
    private Handler mHandler;

    public VoideDialog(Context context) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nicedialog_view);
        getView();
    }

    private void getView() {
        tv1 = (TextView) findViewById(R.id.dialog_tv1);
        tv2 = (TextView) findViewById(R.id.dialog_tv2);
        tv3 = (TextView) findViewById(R.id.dialog_tv3);
        biaoqing_img = (ImageView) findViewById(R.id.biaoqing_img);

        tv1.setText("正在录音 请勿关闭");
        tv1.setTextColor(Color.parseColor("#ff0000"));
        tv1.setTextSize(18);
        tv2.setText("例如：输入通知、打开通知");

        biaoqing_img.setImageResource(R.mipmap.lvying);


        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            dismiss();
            mHandler = null;
        }, 1000 * 60 * 2);
    }

    public void setData(String text) {
        tv3.setText(text);
        tv3.setTextSize(18);
    }

}
