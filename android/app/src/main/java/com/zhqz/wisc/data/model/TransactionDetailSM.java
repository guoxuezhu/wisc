package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 17-3-18.
 */

public class TransactionDetailSM {
    @SerializedName("code")
    public String code;//交易编码(前台生成的唯一编码)
    @SerializedName("canteenPostId")
    int canteenPostId;
    @SerializedName("canteenId")
    int canteenId;
    @SerializedName("cardNum")
    public String cardNum;//ic卡号

    @SerializedName("timesId")
    public int timesId;

    @SerializedName("num")
    public int num;

    @SerializedName("transactionTime")
    public String transactionTime;//交易时间 2017-08-09 09:00:00

    public TransactionDetailSM(String code, int canteenPostId, int canteenId, String cardNum, int timesId, int num, String transactionTime) {
        this.code = code;
        this.canteenPostId = canteenPostId;
        this.canteenId = canteenId;
        this.cardNum = cardNum;
        this.timesId = timesId;
        this.num = num;
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return "TransactionDetailSM{" +
                "code='" + code + '\'' +
                ", canteenPostId=" + canteenPostId +
                ", canteenId=" + canteenId +
                ", cardNum='" + cardNum + '\'' +
                ", timesId=" + timesId +
                ", num=" + num +
                ", transactionTime='" + transactionTime + '\'' +
                '}';
    }
}
