package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 17-3-19.
 */

public class TransactionTimes {

    @SerializedName("timesId")//用餐时段顺序:1:代表第一时段 2:代表第二时段
    public int timesId;
    @SerializedName("name")
    public String name;
    @SerializedName("num")
    public int num;//用餐时段顺序:1:代表第一时段 2:代表第二时段
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endTime")
    public String endTime;

    @Override
    public String toString() {
        return "TransactionTimes{" +
                "timesId=" + timesId +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
