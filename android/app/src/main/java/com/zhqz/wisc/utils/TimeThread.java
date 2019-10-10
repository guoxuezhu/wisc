package com.zhqz.wisc.utils;

import android.os.Handler;
import android.os.Message;

import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.ui.main.MainPresenter;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by jingjingtan on 11/24/16.
 */
public class TimeThread {


    private static Timer timer, timer1, slidingTimer, tCourses,slidingTimert;
    private static int miaoshu = 60;
    private static int slidingMiaoshu = 60;
    private static int slidingMiaoshut = 60;

    public static void getWeather(MainPresenter mainPresenter) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainPresenter.getWeather();
                mainPresenter.getAQI();
            }
        }, 1500, 1000 * 60 * 30);
    }

    public static void slidingOpenedTimet(Handler handler) {
        slidingClosedTime();
        slidingTimert = new Timer();
        slidingTimert.schedule(new TimerTask() {
            @Override
            public void run() {
                if (slidingMiaoshut >= 0) {
                    Message msg = new Message();
                    msg.obj = slidingMiaoshut;
                    msg.what = WiscClient.CLOSED_DRAWER_HANDLERt;
                    handler.sendMessage(msg);
                    slidingMiaoshut--;
                } else {
                    slidingClosedTime();
                }
            }
        }, 1, 1000);
    }

    public static void slidingClosedTimet() {
        if (slidingTimert != null) {
            slidingTimert.cancel();
        }
        slidingMiaoshut = 60;
    }

    public static void slidingOpenedTime(Handler handler) {
        slidingClosedTime();
        slidingTimer = new Timer();
        slidingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (slidingMiaoshu >= 0) {
                    Message msg = new Message();
                    msg.obj = slidingMiaoshu;
                    msg.what = WiscClient.CLOSED_DRAWER_HANDLER;
                    handler.sendMessage(msg);
                    slidingMiaoshu--;
                } else {
                    slidingClosedTime();
                }
            }
        }, 1, 1000);
    }

    public static void slidingClosedTime() {
        if (slidingTimer != null) {
            slidingTimer.cancel();
        }
        slidingMiaoshu = 60;
        WiscClient.isFinger= false;
    }

    public static void setOpenedTime(Handler handler) {
        setClosed();
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (miaoshu >= 0) {
                    Message msg = new Message();
                    msg.obj = miaoshu;
                    msg.what = WiscClient.CLOSED_DIALOG_HANDLER;
                    handler.sendMessage(msg);
                    miaoshu--;
                } else {
                    setClosed();
                }
            }
        }, 1, 1000);
    }

    public static void setClosed() {
        if (timer1 != null) {
            timer1.cancel();
        }
        miaoshu = 60;
    }

    public static void setAllClosed() {
        if (timer1 != null) {
            timer1.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (tCourses != null) {
            tCourses.cancel();
        }
    }

    public static void getCourses(Handler handler, MainPresenter mainPresenter) {
        if (tCourses != null) {
            tCourses.cancel();
        }
        tCourses = new Timer();
        tCourses.schedule(new TimerTask() {
            @Override
            public void run() {
                if (DateUtil.getTimeHHmmss().equals("04:00:00")) {
                    handler.sendEmptyMessage(WiscClient.NONGLI_HANDLER);
                    mainPresenter.getCourseList();
                    WiscClient.isWriteFinger = true;
//                    mainPresenter.test();
                }
            }
        }, 1, 1000);
    }
}
