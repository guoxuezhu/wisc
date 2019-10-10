package com.zhqz.wisc.data.model;

import java.util.List;

/**
 * Created by guoxuezhu on 17-1-11.
 */

public class MqttMsg {
    public int action;  //  1是更新  2是删除
    public String content;//通知 班徽 作品的id
    public List<String> target;//设备id
    public int type;// 消息类型

    public int getAction() {
        return action;
    }

    public List<String> getTarget() {
        return target;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MqttMsg{" +
                "action=" + action +
                ", content=" + content +
                ", target=" + target +
                ", type=" + type +
                '}';
    }
}
