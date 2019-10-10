package com.zhqz.wisc.libraryui.main;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LibraryModel implements Serializable{
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("code")
    public String code;

    @SerializedName("logo")
    public String logo;

    @SerializedName("roomName")
    public String roomName;

    @SerializedName("summary")
    public String summary;

    @SerializedName("summaryUrl")
    public String summaryUrl;

    @SerializedName("roomId")
    public int roomId;

    @SerializedName("status")
    public int status;

    @SerializedName("schoolId")
    public int schoolId;

    @Override
    public String toString() {
        return "LibraryModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", logo='" + logo + '\'' +
                ", roomName='" + roomName + '\'' +
                ", summary='" + summary + '\'' +
                ", summaryUrl='" + summaryUrl + '\'' +
                ", roomId=" + roomId +
                ", status=" + status +
                ", schoolId=" + schoolId +
                '}';
    }
}
//    private Integer id;
//    private String name; // '名称',
//    private String code; // '编码',
//    private String logo; // logo
//    private Integer roomId; // '教室id',
//    private String roomName; // '教室名称',
//    private String summary; // '简介',
//    private Integer status; // '1. 开启 2.关闭',
//    private Integer schoolId;
//    private String summaryUrl;