package com.zhqz.wisc.ui.main;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.receiver.SchedulePowerOffReceiver;
import com.zhqz.wisc.receiver.SchedulePowerOnReceiver;
import com.zhqz.wisc.utils.ELog;

import java.util.Calendar;

/**
 * Created by jingjingtan on 2/13/17.
 */

public class MainTools {

    public static void ShowImgage(ImageView imageWeather, String img) {
        if (img.equals("晴")) {
            imageWeather.setImageResource(R.mipmap.image_00);
        } else if (img.equals("多云")) {
            imageWeather.setImageResource(R.mipmap.image_01);
        } else if (img.equals("阴")) {
            imageWeather.setImageResource(R.mipmap.image_02);
        } else if (img.equals("阵雨")) {
            imageWeather.setImageResource(R.mipmap.image_03);
        } else if (img.equals("雷阵雨")) {
            imageWeather.setImageResource(R.mipmap.image_04);
        } else if (img.equals("雷阵雨伴有冰雹")) {
            imageWeather.setImageResource(R.mipmap.image_05);
        } else if (img.equals("雨夹雪")) {
            imageWeather.setImageResource(R.mipmap.image_06);
        } else if (img.equals("小雨")) {
            imageWeather.setImageResource(R.mipmap.image_07);
        } else if (img.equals("中雨")) {
            imageWeather.setImageResource(R.mipmap.image_08);
        } else if (img.equals("大雨")) {
            imageWeather.setImageResource(R.mipmap.image_09);
        } else if (img.equals("暴雨")) {
            imageWeather.setImageResource(R.mipmap.image_10);
        } else if (img.equals("大暴雨")) {
            imageWeather.setImageResource(R.mipmap.image_11);
        } else if (img.equals("特大暴雨")) {
            imageWeather.setImageResource(R.mipmap.image_12);
        } else if (img.equals("阵雪")) {
            imageWeather.setImageResource(R.mipmap.image_13);
        } else if (img.equals("小雪")) {
            imageWeather.setImageResource(R.mipmap.image_14);
        } else if (img.equals("中雪")) {
            imageWeather.setImageResource(R.mipmap.image_15);
        } else if (img.equals("大雪")) {
            imageWeather.setImageResource(R.mipmap.image_16);
        } else if (img.equals("暴雪")) {
            imageWeather.setImageResource(R.mipmap.image_17);
        } else if (img.equals("雾")) {
            imageWeather.setImageResource(R.mipmap.image_18);
        } else if (img.equals("冻雨")) {
            imageWeather.setImageResource(R.mipmap.image_19);
        } else if (img.equals("沙尘暴")) {
            imageWeather.setImageResource(R.mipmap.image_20);
        } else if (img.equals("小到中雨")) {
            imageWeather.setImageResource(R.mipmap.image_21);
        } else if (img.equals("中到大雨")) {
            imageWeather.setImageResource(R.mipmap.image_22);
        } else if (img.equals("大到暴雨")) {
            imageWeather.setImageResource(R.mipmap.image_23);
        } else if (img.equals("暴雨到大暴雨")) {
            imageWeather.setImageResource(R.mipmap.image_24);
        } else if (img.equals("大暴雨到特大暴雨")) {
            imageWeather.setImageResource(R.mipmap.image_25);
        } else if (img.equals("小到中雪")) {
            imageWeather.setImageResource(R.mipmap.image_26);
        } else if (img.equals("中到大雪")) {
            imageWeather.setImageResource(R.mipmap.image_27);
        } else if (img.equals("大到暴雪")) {
            imageWeather.setImageResource(R.mipmap.image_28);
        } else if (img.equals("浮尘")) {
            imageWeather.setImageResource(R.mipmap.image_29);
        } else if (img.equals("扬沙")) {
            imageWeather.setImageResource(R.mipmap.image_30);
        } else if (img.equals("强沙尘暴")) {
            imageWeather.setImageResource(R.mipmap.image_31);
        } else if (img.equals("霾")) {
            imageWeather.setImageResource(R.mipmap.image_53);
        } else if (img.equals("雨")) {
            imageWeather.setImageResource(R.mipmap.image_301);
        } else if (img.equals("雪")) {
            imageWeather.setImageResource(R.mipmap.image_302);
        }
    }

    static PendingIntent poweroffSender;
    static PendingIntent poweronSender;

    /*
    * 定时关机
    * */
    @SuppressLint("NewApi")
    public static void enableAlertPowerOff(Context context, String shutDownTime) {
        AlarmManager localAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        long l = calculateAlarm(paramAlarm);
        String hour = shutDownTime.substring(0, 2);
        String minute = shutDownTime.substring(3, 5);
        ELog.i("========定时关机====" + hour + "=======" + minute);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        calendar.set(Calendar.SECOND, 0);
//        calendar.add(Calendar.DAY_OF_WEEK,7);
        ELog.i("定时关机 time = " + calendar.getTimeInMillis() + "===" + calendar.getTime());
        if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);//天数加一，为-1的话是天数减1
            ELog.i("定时关机 time = 当前时间比较的" + calendar.getTimeInMillis() + "===" + calendar.getTime());
        }
        disableAlertPowerOff(context);
        Intent localIntent = new Intent(context, SchedulePowerOffReceiver.class);
        localIntent.putExtra("off", "OffReceiver");
        poweroffSender = PendingIntent.getBroadcast(context, 1, localIntent, PendingIntent.FLAG_ONE_SHOT);//268435456
        ELog.i("定时关机 time =111111111 当前时间比较的" + calendar.getTimeInMillis() + "===" + calendar.getTime());
        localAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), poweroffSender);

    }

    /*
 * 定时开机
 * */
    @SuppressLint({"NewApi", "WrongConstant"})
    public static void enableAlertPowerOn(Context context, String bootTime) {
        AlarmManager localAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        disableAlertPowerOn(context);
//        long l = calculateAlarm(paramAlarm);
        String hour = bootTime.substring(0, 2);
        String minute = bootTime.substring(3, 5);
        System.out.println("========定时开机====" + hour + "=======" + minute);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        calendar.set(Calendar.SECOND, 0);
        System.out.println("===============定时开机 time ======= " + calendar.getTimeInMillis() + "=======" + calendar.getTime());
        if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);//天数加一，为-1的话是天数减1
            System.out.println("==========定时开机 time======= 当前时间比较的" + calendar.getTimeInMillis() + "========" + calendar.getTime());
        }

        //设置开机
        Intent localIntent = new Intent(context, SchedulePowerOnReceiver.class);
        poweronSender = PendingIntent.getBroadcast(context, 0, localIntent, PendingIntent.FLAG_ONE_SHOT);//268435456
        localAlarmManager.setExact(4, calendar.getTimeInMillis(), poweronSender);

    }

    /*
    * 取消开机
    * */

    public static void disableAlertPowerOn(Context context) {
        if (poweronSender != null) {
            new Intent(context, SchedulePowerOnReceiver.class);
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(poweronSender);
        }
    }

    /*
    * 取消定时关机
    * */

    public static void disableAlertPowerOff(Context context) {
        if (poweroffSender != null) {
            new Intent(context, SchedulePowerOffReceiver.class);
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(poweroffSender);
        }
    }


}
