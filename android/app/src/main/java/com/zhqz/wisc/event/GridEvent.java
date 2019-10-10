package com.zhqz.wisc.event;

import android.view.View;

/**
 * Created by guoxuezhu on 16-11-16.
 */

public class GridEvent {

    public String mMsg;
    public int id;
    public View view;

    public String jumpPath;

    public int getId() {
        return id;
    }

    public GridEvent(String msg, int id, View view) {
        this.mMsg = msg;
        this.id = id;
        this.view=view;
    }

    public GridEvent(int id, View view, String jumpPath,String mMsg) {
        this.id = id;
        this.view = view;
        this.jumpPath = jumpPath;
        this.mMsg = mMsg;
    }

    public String getmMsg() {
        return mMsg;
    }

    public View getView() {
        return view;
    }

    public String getJumpPath() {
        return jumpPath;
    }
}
