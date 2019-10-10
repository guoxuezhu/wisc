package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 16-11-15.
 */
public class DakaRequest {
    @SerializedName("customer")
    public DakaInfo dakaInfo;
    @SerializedName("synchroCode")
    public String synchroCode;

    @Override
    public String toString() {
        return "DakaRequest{" +
                "dakaInfo=" + dakaInfo +
                ", synchroCode='" + synchroCode + '\'' +
                '}';
    }
}
