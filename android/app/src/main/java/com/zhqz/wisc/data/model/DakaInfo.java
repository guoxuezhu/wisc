package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;
import com.zhqz.wisc.data.model.WalletSM;

/**
 * Created by guoxuezhu on 17-3-19.
 */

public class DakaInfo {

    @SerializedName("id")
    public int customerId;
    @SerializedName("name")
    public String name;
    @SerializedName("number")
    public String number;//学号/工号
    @SerializedName("className")
    public String className;
    @SerializedName("cardNum")
    public String cardNum;
    @SerializedName("walletDetailSMs1")
    public WalletSM wallets1;
    @SerializedName("walletDetailSMs2")
    public WalletSM wallets2;
    @SerializedName("walletDetailSMs3")
    public WalletSM wallets3;

    @Override
    public String toString() {
        return "DakaInfo{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", className='" + className + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", wallets1=" + wallets1 +
                ", wallets2=" + wallets2 +
                ", wallets3=" + wallets3 +
                '}';
    }
}
