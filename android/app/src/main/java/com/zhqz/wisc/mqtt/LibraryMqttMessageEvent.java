package com.zhqz.wisc.mqtt;

import com.zhqz.wisc.data.model.MqttMsg;

/**
 * Created by guoxuezhu on 16-11-11.
 */

public class LibraryMqttMessageEvent {
    public MqttMsg mMsg;

    public LibraryMqttMessageEvent(MqttMsg msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }

    public MqttMsg getMsg() {
        return mMsg;
    }


    @Override
    public String toString() {
        return "LibraryMqttMessageEvent{" +
                "mMsg=" + mMsg +
                '}';
    }
}
