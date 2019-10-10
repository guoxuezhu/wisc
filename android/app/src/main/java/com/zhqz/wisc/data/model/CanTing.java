package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 16-11-15.
 */
public class CanTing {
    @SerializedName("canteenId")
    public int canteenId;
    @SerializedName("name")
    public String canteenName;

    @Override
    public String toString() {
        return "CanTing{" +
                "canteenId=" + canteenId +
                ", canteenName='" + canteenName + '\'' +
                '}';
    }
}
