package com.zhqz.wisc.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhqz.wisc.R;


/**
 * 普通的提示对话框
 * <p>
 * 如:更新提示
 *
 * @author jqqiu
 */

public class NoDataDialog extends Dialog {

    public final static int No_Data = 0x5;
    private Handler mHandler;

    private Context mContext;
    private int mType;
    private TextView tv1,tv2,tv3;
    private ImageView biaoqing_img;

    public NoDataDialog(Context context, int type) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
        mType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nicedialog_view);
        getView();
    }

    private void getView() {
        tv1 = (TextView) findViewById(R.id. dialog_tv1);
        tv2 = (TextView) findViewById(R.id. dialog_tv2);
        tv3 = (TextView) findViewById(R.id. dialog_tv3);
        biaoqing_img = (ImageView) findViewById(R.id.biaoqing_img);
        switch (mType) {
            case No_Data:
                tv1.setText("");
                tv2.setVisibility(View.GONE);
                tv3.setText("无班主任权限或者此阶段无选修课学生数据");
                biaoqing_img.setImageResource(R.mipmap.chidao);
                break;
        }

        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            dismiss();
            mHandler = null;
        }, 5000);
    }


}
