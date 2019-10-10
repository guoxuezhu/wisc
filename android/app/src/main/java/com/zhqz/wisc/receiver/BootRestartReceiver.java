package com.zhqz.wisc.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhqz.wisc.ui.splash.SplashActivity;

/**
 * Created by guoxuezhu on 16-11-16.
 */
public class BootRestartReceiver extends BroadcastReceiver {
    private final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent intent2 = new Intent(context, SplashActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
           // ELog.i("开机自动服务自动启动...");
        }

    }
}