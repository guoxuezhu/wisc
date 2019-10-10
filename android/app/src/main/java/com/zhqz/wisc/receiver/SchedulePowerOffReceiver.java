package com.zhqz.wisc.receiver;

/**
 * Created by jingjingtan on 3/19/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SchedulePowerOffReceiver extends BroadcastReceiver {
    public void onReceive(Context paramContext, Intent paramIntent) {
        String off = paramIntent.getExtras().get("off").toString();
        if (off.equals("OffReceiver")) {
            paramIntent = new Intent(paramContext, ShutdownActivity.class);
            paramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//268435456
            paramContext.startActivity(paramIntent);
        }

    }

}