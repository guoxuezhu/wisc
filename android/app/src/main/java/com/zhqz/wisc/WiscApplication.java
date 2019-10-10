package com.zhqz.wisc;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;
import com.zhqz.wisc.data.DbDao.DaoMaster;
import com.zhqz.wisc.data.DbDao.DaoSession;
import com.zhqz.wisc.data.db.SharePreferenceUtil;
import com.zhqz.wisc.injection.component.ApplicationComponent;
import com.zhqz.wisc.injection.component.DaggerApplicationComponent;
import com.zhqz.wisc.injection.module.ApplicationModule;
import com.zhqz.wisc.ui.splash.SplashActivity;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.FileSizeUtil;
import com.zxy.recovery.core.Recovery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WiscApplication extends MultiDexApplication {
    private ApplicationComponent mApplicationComponent;

    public static WiscApplication get(Context context) {
        return (WiscApplication) context.getApplicationContext();
    }

    public static SharePreferenceUtil prefs;
    public static Context context;
    public static DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        setupComponent();
        prefs = new SharePreferenceUtil(this, "saveDates");


        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "wisc.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();

        FileSizeUtil.createFileLog();
        ELog.getLogcat();
//        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程  以下用来捕获程序崩溃异常

//        voide(); // 语音导航

    }

//    private void voide() {
//        // 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
//        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
//        // 参数间使用“,”分隔。
//        // 设置你申请的应用appid
//
//        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
//
//        StringBuffer param = new StringBuffer();
//        param.append("appid="+getString(R.string.app_id));
//        param.append(",");
//        // 设置使用v5+
//        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
//        SpeechUtility.createUtility(WiscApplication.this, param.toString());
//    }

    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            MobclickAgent.reportError(context, "程序崩溃: " + ex);
            restartApp();//发生崩溃异常时,重启应用
        }
    };

    public void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    private void setupComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

}
