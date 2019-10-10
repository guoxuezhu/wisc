package com.zhqz.wisc.ui.faceScann;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.faceEnter.EnterActivity;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.FileUtils;
import com.zhqz.wisc.utils.PromptDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by jingjingtan on 11/3/17.
 * <p>
 * 签到
 */

public class FaceScannActivity extends BaseActivity implements FaceScannMvpView, TakePictureSurfaceCallback.CallBack {

    @Inject
    FaceScannPresenter faceScannPresenter;

    @BindView(R.id.show_tishi)
    TextView show_tishi;
    @BindView(R.id.facetime)
    TextView facetime;
    @BindView(R.id.face_title)
    TextView face_title;
    @BindView(R.id.face_timeBack)
    TextView face_timeBack;
    @BindView(R.id.saomiao_LL)
    LinearLayout saomiao_LL;
    @BindView(R.id.lvru_LL)
    LinearLayout lvru_LL;

    @BindView(R.id.face_camera_surfaceview)
    SurfaceView surfaceView;


    private int recLen1 = 4;
    private int timeBack = 30;
    private Scheduler.Worker faceworker = Schedulers.io().createWorker();

    Handler faceHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WiscClient.FACE_HANDLER:
                    String errorMsg = (String) msg.obj;
                    show_tishi.setVisibility(View.VISIBLE);
                    facetime.setVisibility(View.GONE);
                    face_timeBack.setVisibility(View.GONE);
                    show_tishi.setText(errorMsg);
                    break;
                case WiscClient.FACE_TIME_HANDLER:
                    show_tishi.setVisibility(View.GONE);
                    facetime.setVisibility(View.VISIBLE);
                    face_timeBack.setVisibility(View.GONE);
                    facetime.setText(recLen1 + "");
                    if (recLen1 < 1) {
                        facetime.setText("不要乱动了哦");
                    }
                    break;
                case WiscClient.FACE_TIMEBACK_HANDLER:
                    show_tishi.setVisibility(View.VISIBLE);
                    facetime.setVisibility(View.GONE);
                    face_timeBack.setVisibility(View.VISIBLE);
                    face_timeBack.setText(timeBack + " 秒后无操作自动退出");
                    break;
            }
        }
    };
    private int stId;
    private String psType;
    private int status;
    private boolean isRuning = false;//正在提交打卡数据
    private PromptDialog kaoqinFaceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facescanning);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        faceScannPresenter.attachView(this);

        initListener();

        if (WiscClient.isEnter == true) {
            face_title.setText("人脸录入");
            stId = getIntent().getIntExtra("stId", 0);
            psType = getIntent().getStringExtra("psType");
            status = getIntent().getIntExtra("status", 0);
            saomiao_LL.setVisibility(View.GONE);
            lvru_LL.setVisibility(View.GONE);
            face_timeBack.setVisibility(View.GONE);
            setFaceTimer();
        } else {
            face_title.setText("人脸识别");
            saomiao_LL.setVisibility(View.VISIBLE);
            lvru_LL.setVisibility(View.VISIBLE);
            face_timeBack.setVisibility(View.GONE);
            if (WiscClient.isDaKaQianDao) {
                if (DateUtil.compare_date(System.currentTimeMillis(), WiscApplication.prefs.getCourseStartTime()) <= 15 * 1000 * 60) {
                    setFaceTimer();
                } else {
                    show_tishi.setText("请在上课前15分钟内考勤");
                    setFaceBackTimer();
                }
            } else {
                show_tishi.setText("现在不需要考勤，请选择其他操作");
                setFaceBackTimer();
            }
        }
    }

    private void setFaceBackTimer() {
        if (faceworker.isDisposed()) {
            faceworker = Schedulers.io().createWorker();
        }
        timeBack = 30;
        faceworker.schedulePeriodically(new Runnable() {
            @Override
            public void run() {
                if (timeBack < 1) {
                    faceworker.dispose();
                    startActivity(new Intent(FaceScannActivity.this, MainActivity.class));
                    finish();
                }
                timeBack--;
                faceHander.sendEmptyMessage(WiscClient.FACE_TIMEBACK_HANDLER);
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);

    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setFixedSize(1280, 720);
        holder.setKeepScreenOn(true);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        TakePictureSurfaceCallback.number = 100;
        holder.addCallback(new TakePictureSurfaceCallback(this));

    }

    private void setFaceTimer() {
        if (faceworker.isDisposed()) {
            faceworker = Schedulers.io().createWorker();
        }
        recLen1 = 4;
        faceworker.schedulePeriodically(new Runnable() {
            @Override
            public void run() {
                if (recLen1 < 1) {
                    faceworker.dispose();
                    TakePictureSurfaceCallback.number = 0;
                }
                recLen1--;
                faceHander.sendEmptyMessage(WiscClient.FACE_TIME_HANDLER);
            }
        }, 2000, 1000, TimeUnit.MILLISECONDS);

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        faceScannPresenter.detachView();
        if (!faceworker.isDisposed()) {
            faceworker.dispose();
        }
        TakePictureSurfaceCallback.doStopCamera();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!faceworker.isDisposed()) {
            faceworker.dispose();
        }
        TakePictureSurfaceCallback.doStopCamera();
    }

    @OnClick(R.id.back_LL)
    void FaceScan_back() {
        if (isRuning) {
            Toast.makeText(this, "正在处理数据，请稍等", Toast.LENGTH_SHORT).show();
        } else {
            FileUtils.deleteDir();
            if (WiscClient.isEnter == true) {
                startActivity(new Intent(FaceScannActivity.this, EnterActivity.class));
            } else {
                startActivity(new Intent(FaceScannActivity.this, MainActivity.class));
            }
            finish();
        }

    }

    /*
    * 录入
    * */
    @OnClick(R.id.lvru_LL)
    void entering() {
        if (isRuning) {
            Toast.makeText(this, "正在处理数据，请稍等", Toast.LENGTH_SHORT).show();
        } else {
            WiscClient.isEnter = true;
            if (!faceworker.isDisposed()) {
                faceworker.dispose();
            }
            WiscApplication.prefs.setCardNumber(null);
            FileUtils.deleteDir();
            //WiscClient.enterStudents.clear();
            TakePictureSurfaceCallback.doStopCamera();
            Intent intent = new Intent(FaceScannActivity.this, EnterActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void detectFace(boolean b, String s) {
        if (b == true) { //人脸检测成功
            if (WiscClient.isEnter == true) {
                faceScannPresenter.updataImage(stId, psType, status, WiscApplication.prefs.getSchoolId(), FileUtils.ReadBitmap());
            } else {
                faceScannPresenter.FaceIdentify(WiscApplication.prefs.getPeriodId(), FileUtils.ReadBitmap());
                Message msg = new Message();
                msg.obj = "人脸检测成功，正在识别";
                msg.what = WiscClient.FACE_HANDLER;
                faceHander.sendMessage(msg);
            }
        } else {
            Message msg = new Message();
            msg.obj = "未检测到人脸，请将正脸对准区域框";
            msg.what = WiscClient.FACE_HANDLER;
            faceHander.sendMessage(msg);
            setFaceTimer();
            FileUtils.deleteDir();
        }
    }


    @Override
    public void faceUpdataImage(boolean isSuccess) {
        if (isSuccess) {
            Intent intent = new Intent(FaceScannActivity.this, EnterActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "人脸图像上传失败，请重新录入", Toast.LENGTH_SHORT).show();
            Message msg = new Message();
            msg.obj = "人脸图像上传失败，请重新录入";
            msg.what = WiscClient.FACE_HANDLER;
            faceHander.sendMessage(msg);
            setFaceTimer();
            FileUtils.deleteDir();
        }
    }

    @Override
    public void IsFaceIdentify(boolean b, String e) {
        //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        if (b) {
            isRuning = true;
            if (kaoqinFaceDialog != null && kaoqinFaceDialog.isShowing()) {
                kaoqinFaceDialog.dismiss();
            }
        } else {
            Message msg = new Message();
            msg.obj = e;
            msg.what = WiscClient.FACE_HANDLER;
            faceHander.sendMessage(msg);
            setFaceTimer();
            FileUtils.deleteDir();
        }

    }

    @Override
    public void faceDaKaXinXi(String userImage, int integer, boolean isQinjia) {
        if (integer == PromptDialog.DAO) {
            kaoqinFaceDialog = new PromptDialog(FaceScannActivity.this, PromptDialog.DAO);
            isRuning = true;
        } else if (integer == PromptDialog.CHIDAO) {
            kaoqinFaceDialog = new PromptDialog(FaceScannActivity.this, PromptDialog.CHIDAO);
            isRuning = true;
        } else if (integer == PromptDialog.CHONGFU) {
            kaoqinFaceDialog = new PromptDialog(FaceScannActivity.this, PromptDialog.CHONGFU);
            isRuning = false;
        } else if (integer == PromptDialog.CUOWU) {
            kaoqinFaceDialog = new PromptDialog(FaceScannActivity.this, PromptDialog.CUOWU);
            isRuning = false;
        }
        kaoqinFaceDialog.show();
        kaoqinFaceDialog.setImage(userImage);
        FileUtils.deleteDir();
        setFaceBackTimer();
    }

    @Override
    public void faceErrorMessage(Throwable e) {
        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
        boolean isConnectException = e.getClass().equals(ConnectException.class);
        boolean isHttpException = e.getClass().equals(HttpException.class);
        if (isSocketException || isConnectException || isHttpException) {
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        isRuning = false;
    }


    @Override
    public void faceDakaOK() {
        isRuning = false;
    }

    @Override
    public void image(Bitmap bitmap) {
        show_tishi.setVisibility(View.VISIBLE);
        facetime.setVisibility(View.GONE);
        face_timeBack.setVisibility(View.GONE);
        show_tishi.setText("人脸正在检测中，请稍等...");
        FileUtils.saveBitmap(bitmap);
        bitmap.recycle();
        FileUtils.Recycled(bitmap);
        faceScannPresenter.detectface(FileUtils.ReadBitmap());

    }


}