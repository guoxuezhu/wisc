package com.zhqz.wisc.canteenui;

import android.os.Handler;

import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.utils.DateUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jingjingtan on 3/22/18.
 */

public class CanteenTimerUtils {
    static Timer timeTimer=new Timer();
    public static void getTime(Handler handler) {
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WiscClient.TIME_HANDLER);
            }
        }, 1, 1000);
    }

    static Timer timer= new Timer();

    public static void getKahao(Handler handler) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WiscClient.DUKA_HANDLER);
            }
        }, 1, 1500);
    }

    static Timer timer1;

    public static void getTimer(CanteenPresenter mainPresenter, String time) {
        stopTimer1();
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if(time.equals(DateUtil.getTimeHHmmss())){
                    mainPresenter.getCanteen(false);
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




}
