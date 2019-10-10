package com.zhqz.wisc.ui.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.DbDao.FingerUsersDao;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.FingerUsers;
import com.zhqz.wisc.ui.FingerprintEntry.FingerprintEntryActivity;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.fingerprint.fingerprintUtils.FingerUtil;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.PromptDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class FingerprintActivity extends BaseActivity implements FingerprintMvpView {

    @Inject
    FingerprintPresenter fingerprintPresenter;

    @BindView(R.id.m_txtStatus)
    TextView m_txtStatus;
    @BindView(R.id.m_FpImageViewer)
    ImageView m_FpImageViewer;


    @BindView(R.id.fingerprint_title)
    TextView fingerprint_title;

    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.fingerprint_timeBack)
    TextView fingerprint_timeBack;


    private int stId;
    private int psType;
    private Scheduler.Worker fingerprintworker = Schedulers.io().createWorker();
    private Scheduler.Worker fingerEnrollworker = Schedulers.io().createWorker();
    private int timeBack;
    private FingerUtil fingerUtil;
    private PromptDialog kaoqinFingerDialog;
    private String imgUrl = null;

    Handler fingerprintHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WiscClient.FINGER_NAME_HANDLER:
                    if (msg.obj != null) {
                        user_name.setText(msg.obj.toString());
                    }
                    if (msg.arg1 == PromptDialog.DAO) {
                        kaoqinFingerDialog = new PromptDialog(FingerprintActivity.this, PromptDialog.DAO);
                    } else if (msg.arg1 == PromptDialog.CHIDAO) {
                        kaoqinFingerDialog = new PromptDialog(FingerprintActivity.this, PromptDialog.CHIDAO);
                    } else if (msg.arg1 == PromptDialog.CHONGFU) {
                        kaoqinFingerDialog = new PromptDialog(FingerprintActivity.this, PromptDialog.CHONGFU);
                    } else if (msg.arg1 == PromptDialog.CUOWU) {
                        kaoqinFingerDialog = new PromptDialog(FingerprintActivity.this, PromptDialog.CUOWU);
                    }
                    kaoqinFingerDialog.show();
                    if (imgUrl != null) {
                        kaoqinFingerDialog.setImage(imgUrl);
                    }
                    if (!fingerprintworker.isDisposed()) {
                        fingerprintworker.dispose();
                    }
//                    setFingerprintBackTimer();
                    break;
                case WiscClient.FINGER_HANDLER:
                    fingerToast(msg.obj.toString());

                    break;
                case WiscClient.FINGER_EMPTY_NAME_HANDLER:
                    user_name.setText("");
                    m_txtStatus.setText(msg.obj.toString());
                    if (kaoqinFingerDialog != null && kaoqinFingerDialog.isShowing()) {
                        kaoqinFingerDialog.dismiss();
                    }
                    if (!fingerprintworker.isDisposed()) {
                        fingerprintworker.dispose();
                    }
//                    setFingerprintBackTimer();
                    break;
                case WiscClient.FINGER_TIMEBACK_HANDLER:
                    fingerprint_timeBack.setVisibility(View.VISIBLE);
                    fingerprint_timeBack.setText(timeBack + " 秒后无操作自动退出111");
                    break;
                case WiscClient.FINGER_DEVICE_HANDLER:
                    if (msg.arg1 == 0) {
                        Toast.makeText(FingerprintActivity.this, "打开指纹设备失败，请同意USB权限", Toast.LENGTH_SHORT).show();
//                        openFingerDevice = false;
                    } else if (msg.arg1 == 1) {
                        Toast.makeText(FingerprintActivity.this, "打开指纹设备成功12", Toast.LENGTH_SHORT).show();
//                        openFingerDevice = true;
                    } else if (msg.arg1 == 2) {
                        Toast.makeText(FingerprintActivity.this, "指纹权限被拒绝，无法使用指纹识别", Toast.LENGTH_SHORT).show();
//                        openFingerDevice = false;
                    } else if (msg.arg1 == 3) {
                        Toast.makeText(FingerprintActivity.this, "找不到指纹USB设备", Toast.LENGTH_SHORT).show();
//                        openFingerDevice = false;
                    } else if (msg.arg1 == 66){
                        Toast.makeText(FingerprintActivity.this, "Fail to receive response!Please check the connection to target", Toast.LENGTH_SHORT).show();
//                        openFingerDevice = false;
                    } else {
                        Toast.makeText(FingerprintActivity.this, "无法连接到指纹设备", Toast.LENGTH_SHORT).show();
//                        openFingerDevice = false;
                    }
                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        fingerprintPresenter.attachView(this);
        WiscClient.isFinger= true;
        fingerUtil = new FingerUtil(this, fingerprintPresenter,fingerprintHander);
//        fingerUtil.OpenDevice();模拟出现黑屏
        ELog.e("tjj===FingerprintActivity开始了");

            fingerprint_title.setText("指纹录入");
            stId = getIntent().getIntExtra("stId", 0);
            psType = getIntent().getIntExtra("psType", 0);

            fingerprint_timeBack.setVisibility(View.GONE);
            startLuruFinger();
    }

    private void startLuruFinger() {
        fingerUtil.Run_CmdDeleteAll();
        ELog.e("tjj删除所有的指纹1111111");
        if (fingerEnrollworker.isDisposed()) {
            fingerEnrollworker = Schedulers.io().createWorker();
        }
        fingerEnrollworker.schedule(new Runnable() {
            @Override
            public void run() {
                fingerEnrollworker.dispose();
                ELog.e("tjj删除所有的指纹22222" + stId);
                fingerUtil.Run_CmdEnroll(stId);
            }
        }, 1000 * 3, TimeUnit.MILLISECONDS);

    }



    @OnClick(R.id.fingerprint_back_LL)
    void fingerprint_back_LL() {
        if (!fingerprintworker.isDisposed()) {
            fingerprintworker.dispose();
        }
//        fingerUtil.Run_CmdDeleteAll();
        fingerUtil.Run_CmdCancel();
        if (WiscClient.isEnter == true) {
            startActivity(new Intent(FingerprintActivity.this, FingerprintEntryActivity.class));
        } else {
            WiscClient.isFinger = false;
            MainActivity.openFingerDevice = true;
            startActivity(new Intent(FingerprintActivity.this, MainActivity.class));
        }
        finish();
    }




    @Override
    public void sendZhiwenDatas(String str) {
        fingerprintPresenter.sendTemplateData(psType, stId, str);
    }

    @Override
    public void sendTemplateDatas(boolean isSuccess,String s) {
        if (!isSuccess) {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
        if (!fingerprintworker.isDisposed()) {
            fingerprintworker.dispose();
        }
        fingerUtil.Run_CmdCancel();
        ELog.e("==========录入成功，数据传入成功==11111===");
        startActivity(new Intent(FingerprintActivity.this, FingerprintEntryActivity.class));
        finish();
    }

    int fingerIds = -1;
    @Override
    public void showMessage(int type, int fingerId) {
        Message msg = new Message();
        switch (type) {
            case 1:
                ELog.e("==========录入成功=====");
                msg.obj = "1";
                msg.what = WiscClient.FINGER_HANDLER;
//                fingerUtil.Run_CmdReadTemplate(stId);
                break;
            case -1:
                msg.obj = "-1";
                msg.what = WiscClient.FINGER_HANDLER;
                ELog.e("==========失败,请重新录入23=====");
//                fingerUtil.Run_CmdEnroll(stId);
                break;
            case 2:
                if (fingerId != 0) {
                    ELog.e("==========识别指纹成功=====");
                    fingerIds= fingerId;
                    msg.obj = "2";
                    msg.what = WiscClient.FINGER_HANDLER;
//                    fingerprintPresenter.daka(fingerId);
                } else {
                    ELog.e("==========失败,请重新识别或今天不需要在此教室上课=====");
                    msg.obj = "21";
                    msg.what = WiscClient.FINGER_EMPTY_NAME_HANDLER;
                }
                break;
            case 3:
                ELog.e("==========请松开你的手指=====");
                msg.obj = "3";
                msg.what = WiscClient.FINGER_HANDLER;
                break;
            case 4:
                ELog.e("==========请按下你的手指=====");
                msg.obj = "4";
                msg.what = WiscClient.FINGER_HANDLER;
                break;
            case 5:
                ELog.e("==========请再次按下你的手指=====");
                msg.obj = "5";
                msg.what = WiscClient.FINGER_HANDLER;
                break;
        }
        fingerprintHander.sendMessage(msg);

    }
    private void fingerToast(String s) {
        if (s.equals("1")){
            m_txtStatus.setText("录入成功");
            fingerUtil.Run_CmdReadTemplate(stId);
        } else if(s.equals("2")){
            m_txtStatus.setText("识别指纹成功");//学生考勤方法
//            fingerprintPresenter.daka(fingerIds);
        }else if(s.equals("21")){
            m_txtStatus.setText("识别指纹成功");
        }else if(s.equals("-1")){
            m_txtStatus.setText("失败,请重新录入");
//            startLuruFinger();
            setAgaginFinger();
        }else if(s.equals("3")){
            m_txtStatus.setText("请松开你的手指");
        }else if(s.equals("4")){
            m_txtStatus.setText("请按下你的手指");
        }else if(s.equals("5")){
            m_txtStatus.setText("请再次按下你的手指");
        }

    }

    private void setAgaginFinger() {
        if (!fingerprintworker.isDisposed()) {
            fingerprintworker.dispose();
        }
        fingerUtil.Run_CmdCancel();
        ELog.e("==========失败,请重新录入==11111===");
        startActivity(new Intent(FingerprintActivity.this, FingerprintEntryActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        ELog.e("==========失败,请重新录入=onDestroy===");
        fingerprintPresenter.detachView();
        super.onDestroy();
    }


    @Override
    public void fingerDaKaXinXi(String userImg, int integer, String name, boolean isQingjia) {
        if (kaoqinFingerDialog != null && kaoqinFingerDialog.isShowing()) {
            kaoqinFingerDialog.dismiss();
        }
        imgUrl = userImg;
        Message msg = new Message();
        msg.obj = name;
        msg.arg1 = integer;
        msg.what = WiscClient.FINGER_NAME_HANDLER;
        fingerprintHander.sendMessage(msg);
    }

    @Override
    public void fingerDakaOK() {

    }

    @Override
    public void fingerErrorMessage(Throwable e) {
        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
        boolean isConnectException = e.getClass().equals(ConnectException.class);
        boolean isHttpException = e.getClass().equals(HttpException.class);
        if (isSocketException || isConnectException || isHttpException) {
            Toast.makeText(FingerprintActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(FingerprintActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
