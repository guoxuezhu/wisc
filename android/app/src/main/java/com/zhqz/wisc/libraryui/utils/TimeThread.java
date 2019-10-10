package com.zhqz.wisc.libraryui.utils;

import android.os.Handler;
import android.os.Message;

import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.libraryui.main.LibraryMainPresenter;
import com.zhqz.wisc.ui.main.MainPresenter;
import com.zhqz.wisc.utils.DateUtil;

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

    public static void getWeather(LibraryMainPresenter libraryMainPresenter) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                libraryMainPresenter.getWeather();
                libraryMainPresenter.getAQI();
            }
        }, 1500, 1000 * 60 * 30);
    }

    public static void getTime1(LibraryMainPresenter libraryMainPresenter, String time) {
        stopTimer1();
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time.equals(DateUtil.getCurrentTime())) {
                    libraryMainPresenter.getNoticeList();
                    stopTimer1();
                }
            }
        }, 1, 1000);
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
                    msg.what = WiscClient.CLOSED_LibraryDRAWER_HANDLER;
                    handler.sendMessage(msg);
                    slidingMiaoshu--;
                } else {
                    slidingClosedTime();
                }
            }
        }, 1, 1000);
    }

    public static void getCourses(Handler handler, LibraryMainPresenter mainPresenter) {
        if (tCourses != null) {
            tCourses.cancel();
        }
        tCourses = new Timer();
        tCourses.schedule(new TimerTask() {
            @Override
            public void run() {
                if (DateUtil.getTimeHHmmss().equals("04:00:00")) {
                    handler.sendEmptyMessage(WiscClient.NONGLI_HANDLER_Library);
                }
            }
        }, 1, 1000);
    }

    public static void slidingClosedTime() {
        if (slidingTimer != null) {
            slidingTimer.cancel();
        }
        slidingMiaoshu = 60;
    }

    public static void stopTimer1() {
        if (timer1 != null) {
            timer1.cancel();
        }
    }
    public static void setAllClosed() {
        if (timer != null) {
            timer.cancel();
        }
        if (timer1 != null) {
            timer1.cancel();
        }
        if (slidingTimer != null) {
            slidingTimer.cancel();
        }
        if (tCourses != null) {
            tCourses.cancel();
        }

    }
}
