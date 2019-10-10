package com.zhqz.wisc.mqtt;


import com.google.gson.Gson;
import com.zhqz.wisc.data.model.MqttMsg;
import com.zhqz.wisc.utils.ELog;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

/**
 * 使用EventBus分发事件
 * Created by guoxuezhu on 16-11-11.
 */
public class MqttCallbackBus implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        ELog.i("=====connectionLost==" + cause.toString());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        ELog.i("=====messageArrived==" + message.toString());
        Gson gson = new Gson();
        MqttMsg mqttMsg = gson.fromJson(message.toString(), MqttMsg.class);
        EventBus.getDefault().post(new LibraryMqttMessageEvent(mqttMsg));
        EventBus.getDefault().post(new MqttMessageEvent(mqttMsg));


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}
