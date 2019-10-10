package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;
import com.zhqz.wisc.data.model.TransactionTimes;

import java.util.List;

/**
 * Created by guoxuezhu on 17-3-19.
 */

public class Canteen {

    @SerializedName("name")
    public String canteenName;
    @SerializedName("canteenId")
    public int canteenId;

    @SerializedName("canTingTimeSMs")
    public List<TransactionTimes> transactionTimes;

    @Override
    public String toString() {
        return "Canteen{" +
                "canteenName='" + canteenName + '\'' +
                ", canteenId=" + canteenId +
                ", transactionTimes=" + transactionTimes +
                '}';
    }
}
