package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 17-1-16.
 * 临时保存考勤数据
 */

public class AttendanceDataTwo {
    @SerializedName("activityCourseId")
    public int activityCourseIdTwo;
    @SerializedName("periodId")
    public int periodIdTwo;
    @SerializedName("attendancedate")
    public String attendancedateTwo;
    @SerializedName("attendanceId")
    public int attendanceIdTwo;
    @SerializedName("status")
    public int statusTwo;
    @SerializedName("cardNumber")
    public String cardNumberTwo;

    public AttendanceDataTwo(int activityCourseIdTwo, int periodIdTwo, String attendancedateTwo, int attendanceIdTwo, int statusTwo, String cardNumberTwo) {
        this.activityCourseIdTwo = activityCourseIdTwo;
        this.periodIdTwo = periodIdTwo;
        this.attendancedateTwo = attendancedateTwo;
        this.attendanceIdTwo = attendanceIdTwo;
        this.statusTwo = statusTwo;
        this.cardNumberTwo = cardNumberTwo;
    }

    @Override
    public String toString() {
        return "AttendanceDataTwo{" +
                "activityCourseIdTwo=" + activityCourseIdTwo +
                ", periodIdTwo=" + periodIdTwo +
                ", attendancedateTwo='" + attendancedateTwo + '\'' +
                ", attendanceIdTwo=" + attendanceIdTwo +
                ", statusTwo=" + statusTwo +
                ", cardNumberTwo='" + cardNumberTwo + '\'' +
                '}';
    }

    public void setActivityCourseIdTwo(int activityCourseIdTwo) {
        this.activityCourseIdTwo = activityCourseIdTwo;
    }

    public void setPeriodIdTwo(int periodIdTwo) {
        this.periodIdTwo = periodIdTwo;
    }

    public void setAttendancedateTwo(String attendancedateTwo) {
        this.attendancedateTwo = attendancedateTwo;
    }

    public void setAttendanceIdTwo(int attendanceIdTwo) {
        this.attendanceIdTwo = attendanceIdTwo;
    }

    public void setStatusTwo(int statusTwo) {
        this.statusTwo = statusTwo;
    }

    public void setCardNumberTwo(String cardNumberTwo) {
        this.cardNumberTwo = cardNumberTwo;
    }

    public int getActivityCourseIdTwo() {
        return activityCourseIdTwo;
    }

    public int getPeriodIdTwo() {
        return periodIdTwo;
    }

    public String getAttendancedateTwo() {
        return attendancedateTwo;
    }

    public int getAttendanceIdTwo() {
        return attendanceIdTwo;
    }

    public int getStatusTwo() {
        return statusTwo;
    }

    public String getCardNumberTwo() {
        return cardNumberTwo;
    }
}
