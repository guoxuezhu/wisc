package com.zhqz.wisc.receiver;

/**
 * Created by jingjingtan on 3/19/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShutdownActivity extends Activity {
//    public static CountDownTimer mCountDownTimer = null;


    //    关机，发送SHUTDOWN广告，典型用法如下：
    private void fireShutDown()
    {
        Intent localIntent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        localIntent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        localIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);//8388608
        localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//268435456
        startActivity(localIntent);
    }
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        ShutdownActivity.this.fireShutDown();
//        ShutdownActivity.mCountDownTimer = null;

    }

}
