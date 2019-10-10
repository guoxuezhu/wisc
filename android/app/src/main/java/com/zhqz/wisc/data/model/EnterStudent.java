package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 11/14/17.
 */

public class EnterStudent {
    @SerializedName("name")// 姓名
    public String name;
    @SerializedName("number")// 学号/教职工号
    public String number;
    @SerializedName("url")
    public String url;
    @SerializedName("status")// 状态
    public int status;// 0 未录入 1已录入

    @SerializedName("stId")// 主键
    public int stId;
    @SerializedName("psType") // 类别
    public String psType;
    @SerializedName("schoolId")// 学校Id
    public int schoolId;
    @SerializedName("classId")// 班级Id
    public int classId;

    @Override
    public String toString() {
        return "EnterStudent{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", stId=" + stId +
                ", psType='" + psType + '\'' +
                ", schoolId=" + schoolId +
                ", classId=" + classId +
                '}';
    }
}
