package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 17-3-8.
 */

public class NoticeList {

    @SerializedName("id")
    public int id;
    @SerializedName("level")
    public int level;
    @SerializedName("title")
    public String title;
    @SerializedName("url")
    public String url;
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endTime")
    public String endTime;

    @Override
    public String toString() {
        return "NoticeList{" +
                "id=" + id +
                ", level=" + level +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
