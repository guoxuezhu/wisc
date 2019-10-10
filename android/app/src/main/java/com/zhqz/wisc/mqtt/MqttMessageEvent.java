package com.zhqz.wisc.mqtt;

import com.zhqz.wisc.data.model.MqttMsg;

/**
 * Created by guoxuezhu on 16-11-11.
 */

public class MqttMessageEvent {
    public MqttMsg mMsg;

    public MqttMessageEvent(MqttMsg msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }

    public MqttMsg getMsg() {
        return mMsg;
    }


    @Override
    public String toString() {
        return "MqttMessageEvent{" +
                "mMsg=" + mMsg +
                '}';
    }
}
