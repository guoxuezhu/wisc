package com.zhqz.wisc.ui.splash;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.canteenui.bind.CanBindActivity;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.libraryui.main.LibraryMainActivity;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.bind.BindActivity;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.ui.selectClass.SelectClassActivity;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.FileSizeUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashPresenter mSplashPresenter;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        mSplashPresenter.attachView(this);
        FileSizeUtil.delete((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE));
        WiscClient.isWriteFinger = true;
        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            setTiaozhuan();
            mHandler = null;
        }, 3000);

    }

    private void setTiaozhuan() {
        ELog.i( "=====bind==" + WiscApplication.prefs.getisBind() + "====" + WiscApplication.prefs.getSceneId());
        if (WiscApplication.prefs.getSceneId() == 1){
            startActivity(new Intent(this, CanBindActivity.class));
        } else if (WiscApplication.prefs.getSceneId() == 2) {
            if (mSplashPresenter.hasValidUserStored()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, BindActivity.class));
            }
        } else if (WiscApplication.prefs.getSceneId() == 3) {
            startActivity(new Intent(this, LibraryMainActivity.class));
        } else {
            startActivity(new Intent(this, BindActivity.class));
        }

//        if (mSplashPresenter.hasValidUserStored()) {
//            startActivity(new Intent(this, MainActivity.class));
//        } else {
//            startActivity(new Intent(this, BindActivity.class));
//        }
        finish();
    }


    @Override
    protected void onDestroy() {
        mSplashPresenter.detachView();//？
        super.onDestroy();
    }

}
