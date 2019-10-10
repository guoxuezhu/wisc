package com.zhqz.wisc.utils;

import android.os.Handler;

import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.ui.main.MainPresenter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guoxuezhu on 16-12-21.
 */

public class TimerUtils {
    static Timer timer;

    public static void getTime(Handler handler) {
        stopTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WiscClient.COURSE_HANDLER);
            }
        }, 1, 1000);
    }

    public static void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    static Timer timer1;

    public static void getTime1(MainPresenter mainPresenter, String time) {
        stopTimer1();
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time.equals(DateUtil.getCurrentTime())) {
                    mainPresenter.getNoticeList();
                    stopTimer1();
                }
            }
        }, 1, 1000);
    }

    public static void stopTimer1() {
        if (timer1 != null) {
            timer1.cancel();
        }
    }

    public static void closedAllTimer() {
        if (timer1 != null) {
            timer1.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
