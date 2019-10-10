package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 16-11-15.
 */

public class VersionCheck {
    @SerializedName("id")
    public int id;
    @SerializedName("bbh")//版本号（数字）
    public int bbh;
    @SerializedName("name")//版本号（数字）
    public String name;

    @SerializedName("apkUrl")//版本号（数字）
    public String apkUrl;

    @SerializedName("updateTime")//版本号（数字）
    public String updateTime;

    @Override
    public String toString() {
        return "VersionCheck{" +
                "id='" + id + '\'' +
                "bbh='" + bbh + '\'' +
                "name='" + name + '\'' +
                "apkUrl='" + apkUrl + '\'' +
                "updateTime='" + updateTime + '\'' +
                '}';
    }
}
