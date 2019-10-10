package com.zhqz.wisc.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by guoxuezhu on 17-3-18.
 */
@Entity
public class NoWifiData {

    public String code;//交易编码(前台生成的唯一编码)
    public String cardNum;//ic卡号
    public int timesId;
    public int num;
    public String transactionTime;//交易时间 2017-08-09 09:00:00

    @Generated(hash = 602443890)
    public NoWifiData(String code, String cardNum, int timesId, int num,
                      String transactionTime) {
        this.code = code;
        this.cardNum = cardNum;
        this.timesId = timesId;
        this.num = num;
        this.transactionTime = transactionTime;
    }

    @Generated(hash = 2006643161)
    public NoWifiData() {
    }

    @Override
    public String toString() {
        return "NoWifiData{" +
                "code='" + code + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", timesId=" + timesId +
                ", num=" + num +
                ", transactionTime='" + transactionTime + '\'' +
                '}';
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int getTimesId() {
        return this.timesId;
    }

    public void setTimesId(int timesId) {
        this.timesId = timesId;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTransactionTime() {
        return this.transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }
}
