package com.zhqz.wisc.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.remote.WiscService;
import com.zhqz.wisc.mqtt.MqttManager;
import com.zhqz.wisc.utils.DisplayTools;
import com.zhqz.wisc.utils.ELog;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * USB 打卡签到
 */

public class MQTTServicer extends Service {

    public static final String URL = "tcp://" + WiscService.URLIP + ":61613";
    public static final String TOPIC = "classBrand";
    private String userName = "admin";
    private String password = "password";
    private String clientId = DisplayTools.getUdid(WiscApplication.prefs);//每个客户端唯一标识

    public Timer t;

    private Scheduler mScheduler = Schedulers.from(Executors.newFixedThreadPool(1));
    private int mTimeMS = 1000 * 60 * 10;
    private int MAX_TIME_INTERVAL = 10 * 60 * 1000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ELog.i("启动 TimerServicer");
        CreateTimer();
    }

    private void mqttConnect() {
        Observable<Boolean> onSubscribeDefer = Observable.defer(new Callable<ObservableSource<? extends Boolean>>() {
            @Override
            public ObservableSource<? extends Boolean> call() throws Exception {
                return Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return MqttManager.getInstance().creatConnect(URL, userName, password, clientId);
                    }
                });
            }
        });
        onSubscribeDefer.observeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("============mqttConnect======onError=======" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        ELog.i("============mqttConnect======onNext=======" + aBoolean);
                        if (aBoolean) {
                            Subscribe();

                        }
                    }
                });

    }


    private void Subscribe() {
        Observable<Boolean> onSubscribeDefer = Observable.defer(new Callable<ObservableSource<? extends Boolean>>() {
            @Override
            public ObservableSource<? extends Boolean> call() throws Exception {
                return Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return MqttManager.getInstance().subscribe(TOPIC, 2);
                    }
                });
            }
        });
        onSubscribeDefer.observeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("============mqttSubscribe======onError=======" + e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        ELog.i("============mqttSubscribe======onNext=======" + aBoolean);
                    }
                });
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    private void CreateTimer() {
        stopTimer();
        t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                mqttConnect();
            }
        }, 0, mTimeMS);
    }


    /**
     * 关闭定时器
     */
    private void stopTimer() {
        if (t != null) {
            t.cancel();
        }
    }

    /**
     * 停止销毁服务当中所有东西
     */
    private void stop() {
        stopTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
}
