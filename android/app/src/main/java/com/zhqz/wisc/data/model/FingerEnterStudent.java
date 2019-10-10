package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FingerEnterStudent {

    @SerializedName("stid")// 姓名
    public int stid; //学生、老师id

    @SerializedName("psType") // 类别
    public int psType; //类型 １老师　２学生

    @SerializedName("name")// 姓名
    public String name;

    @SerializedName("number")
    public String number;

    @SerializedName("contexts")
    public List<String> contexts; //指纹内容

    @SerializedName("schoolId")// 学校Id
    public int schoolId;

    @SerializedName("status")// 状态
    public int status;// 0.未录入， 1.已经录入


    @Override
    public String toString() {
        return "FingerEnterStudent{" +
                "stid=" + stid +
                ", psType=" + psType +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", contexts=" + contexts +
                ", schoolId=" + schoolId +
                ", status=" + status +
                '}';
    }
}
