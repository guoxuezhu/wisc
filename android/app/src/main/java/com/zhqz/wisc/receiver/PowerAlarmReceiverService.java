package com.zhqz.wisc.receiver;

/**
 * Created by jingjingtan on 3/19/17.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.zhqz.wisc.ui.main.MainTools;

import java.net.URISyntaxException;

public class PowerAlarmReceiverService extends IntentService {
    public PowerAlarmReceiverService() {
        super("PowerAlarmReceiverService");
    }

    public static void processBroadcastIntent(Context paramContext, Intent paramIntent) {
        //ELog.i("PowerAlarmReceiverService" + "start power alarm service 启动电源报警服务");
        Intent localIntent = new Intent(paramContext, PowerAlarmReceiverService.class);
        localIntent.setAction("broadcast_receiver");
        localIntent.putExtra("android.intent.extra.INTENT", paramIntent);
        paramContext.startService(localIntent);
    }

    protected void onHandleIntent(Intent paramIntent) {
        if (!"broadcast_receiver".equals(paramIntent.getAction())) {
            return;
        }
        try {
            paramIntent = Intent.getIntent(((Intent) paramIntent.getParcelableExtra("android.intent.extra.INTENT")).getAction());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // Log.d("PowerAlarmReceiverService", "action= " + paramIntent);
        ////BOOT_COMPLETED 在系统启动完成后，这个动作被广播一次（只有一次）。
        if ("android.intent.action.BOOT_COMPLETED".equals(paramIntent)) {
            MainTools.disableAlertPowerOn(this);
            MainTools.disableAlertPowerOff(this);
            return;
        }
    }


}
