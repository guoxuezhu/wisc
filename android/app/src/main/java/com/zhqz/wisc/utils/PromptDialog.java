package com.zhqz.wisc.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;


/**
 * 普通的提示对话框
 * <p>
 * 如:更新提示
 *
 * @author jqqiu
 */

public class PromptDialog extends Dialog {


    public final static int DAO = 0x1;
    public final static int CHIDAO = 0x2;
    public final static int CHONGFU = 0x3;
    public final static int CUOWU = 0x4;
    public final static int KAOQIN = 0x5;
    private Handler mHandler;

    private Context mContext;
    private int mType;
    private TextView tv1, tv2, tv3;
    private ImageView biaoqing_img;

    public PromptDialog(Context context, int type) {
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
        tv1 = (TextView) findViewById(R.id.dialog_tv1);
        tv2 = (TextView) findViewById(R.id.dialog_tv2);
        tv3 = (TextView) findViewById(R.id.dialog_tv3);
        biaoqing_img = (ImageView) findViewById(R.id.biaoqing_img);
        switch (mType) {
            case DAO:
                tv1.setText("到");
                tv2.setText("打卡成功");
                tv3.setText("养成学习好习惯" + "\n" + "树立时间观念,不迟到不早退");
                biaoqing_img.setImageResource(R.mipmap.dao);
                break;
            case CHIDAO:
                tv1.setText("迟到");
                tv2.setText("打卡成功");
                tv3.setText("养成学习好习惯" + "\n" + "树立时间观念,不迟到不早退");
                biaoqing_img.setImageResource(R.mipmap.chidao);
                break;
            case CHONGFU:
                tv1.setText("重复");
                tv2.setText("你已打卡,请直接进入教室");
                tv3.setText("养成学习好习惯" + "\n" + "树立时间观念,不迟到不早退");
                biaoqing_img.setImageResource(R.mipmap.chongfu);
                break;
            case CUOWU:
                tv1.setText("错误");
                tv2.setText("请确认是否在本教室上课,或等待3秒再打卡");
                tv3.setText("养成学习好习惯" + "\n" + "树立时间观念,不迟到不早退");
                biaoqing_img.setImageResource(R.mipmap.cuowu);
                break;
            case KAOQIN:
                tv1.setText("考勤");
                tv2.setText("打卡成功");
                tv3.setText("养成学习好习惯" + "\n" + "树立时间观念,不迟到不早退");
                biaoqing_img.setImageResource(R.mipmap.dao);
                break;
        }

        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            dismiss();
            mHandler = null;
        }, 3000);
    }


    public void setImage(String userImg) {
        Glide.with(mContext).load(userImg)
                .placeholder(R.mipmap.me)
                .error(R.mipmap.me)
                .fitCenter()
                .dontAnimate()
                .into(biaoqing_img);
    }

    @SuppressLint("ResourceAsColor")
    public void setkaoqingdata(String msg) {
        tv2.setText(msg.substring(msg.indexOf("&")+1));//截取#之前的字符串  userId.substring(userId.indexOf("?u="));截取之后的字符
        tv1.setText(msg.substring(0, msg.indexOf("&")));
        tv1.setTextSize(20);
        if (tv1.getText().equals("到")){
            tv1.setTextColor(R.color.tianqibg);
        } else {
            tv1.setTextColor(R.color.redbg);
        }

    }
}
