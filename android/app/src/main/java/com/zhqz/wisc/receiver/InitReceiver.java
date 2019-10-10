package com.zhqz.wisc.receiver;

/**
 * Created by jingjingtan on 3/19/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InitReceiver extends BroadcastReceiver {
    public void onReceive(Context paramContext, Intent paramIntent) {
        //ELog.i("InitReceiver" + "InitReceiver action = " + paramIntent.getAction());
        PowerAlarmReceiverService.processBroadcastIntent(paramContext, paramIntent);
    }
}
