package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 4/6/17.
 */

public class ThisClassStudents {
    @SerializedName("className")
    public String className;

    @SerializedName("studentName")
    public String studentName;

    @SerializedName("attendanceStatus")
    public String attendanceStatus;

    @SerializedName("courseName")
    public String courseName;

    @SerializedName("roomsName")
    public String roomsName;

    @SerializedName("courseTeacherName")
    public String courseTeacherName;

    @SerializedName("classTeacherName")
    public String classTeacherName;

    @Override
    public String toString() {
        return "ThisClassStudents{" +
                "className='" + className + '\'' +
                ", studentName='" + studentName + '\'' +
                ", attendanceStatus='" + attendanceStatus + '\'' +
                ", courseName='" + courseName + '\'' +
                ", roomsName='" + roomsName + '\'' +
                ", courseTeacherName='" + courseTeacherName + '\'' +
                ", classTeacherName='" + classTeacherName + '\'' +
                '}';
    }
}
