package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 12/9/16.
 */
public class Room {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("englishName")
    public String englishName;

    @SerializedName("bootTime")
    public String bootTime;

    @SerializedName("shutDownTime")
    public String shutDownTime;

    @SerializedName("isMute")
    public int isMute;

    @SerializedName("deviceId")
    public int deviceId;

    @SerializedName("roomId")
    public int roomId;

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", englishName='" + englishName + '\'' +
                ", bootTime='" + bootTime + '\'' +
                ", shutDownTime='" + shutDownTime + '\'' +
                ", isMute=" + isMute +
                ", deviceId=" + deviceId +
                ", roomId=" + roomId +
                '}';
    }
}
