package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by guoxuezhu on 18-1-30.
 */

public class CabinetPerson {

    @SerializedName("isClassTeacher")
    public Boolean isClassTeacher;

    @SerializedName("sm")
    public List<CabinetClassItem> sm;


    public static class CabinetClassItem {
        @SerializedName("name")
        public String name;
        @SerializedName("lockerNoNumber")
        public int lockerNoNumber;
        @SerializedName("position")
        public String position;
        @SerializedName("distribution")
        public int distribution;


        @Override
        public String toString() {
            return "CabinetClassItem{" +
                    "name='" + name + '\'' +
                    ", lockerNoNumber=" + lockerNoNumber +
                    ", position='" + position + '\'' +
                    ", distribution=" + distribution +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CabinetPerson{" +
                "isClassTeacher=" + isClassTeacher +
                ", sm=" + sm +
                '}';
    }
}
//    private Integer id; // id
//    private Integer lockAllotClasssId; // 主表id
//    private Integer classId; // 班级id
//    private Integer schoolId; // 学校id
//    private Integer stid; // 学生或老师id
//    private Integer psType; // 人员类别 1.老师 2.学生
//    private String number; // 人员学号或老师教职工号
//    private Integer lockerNoNumber; // 柜号number
//    private Integer distribution; // 分配情况 0，未分配 1.已分配
//    private String name; //人员姓名
//    private String position; //位置
//    private String ts; //分配时间