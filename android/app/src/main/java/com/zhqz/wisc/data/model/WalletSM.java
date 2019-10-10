package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 17-3-23.
 */
public class WalletSM {
    @SerializedName("num")
    public int num;
    @SerializedName("totalNum")
    public int totalNum;//能打的总次数
    @SerializedName("limitNum")
    public int limitNum;//每时段每天能打几次
    @SerializedName("sumNum")
    public int sumNum;

    @Override
    public String toString() {
        return "WalletSM{" +
                "num=" + num +
                ", totalNum=" + totalNum +
                ", limitNum=" + limitNum +
                ", sumNum=" + sumNum +
                '}';
    }
}
