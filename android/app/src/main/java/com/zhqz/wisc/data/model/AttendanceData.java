package com.zhqz.wisc.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by guoxuezhu on 16-11-15.
 */
@Entity
public class AttendanceData {

    @Id
    public long attendanceId;

    public int activityCourseId;
    public int periodId;
    public String attendancedate;
    public int status;
    public String cardNumber;


    @Generated(hash = 518961601)
    public AttendanceData(long attendanceId, int activityCourseId, int periodId,
                          String attendancedate, int status, String cardNumber) {
        this.attendanceId = attendanceId;
        this.activityCourseId = activityCourseId;
        this.periodId = periodId;
        this.attendancedate = attendancedate;
        this.status = status;
        this.cardNumber = cardNumber;
    }


    @Generated(hash = 466875570)
    public AttendanceData() {
    }


    @Override
    public String toString() {
        return "AttendanceData{" +
                "attendanceId=" + attendanceId +
                ", activityCourseId=" + activityCourseId +
                ", periodId=" + periodId +
                ", attendancedate='" + attendancedate + '\'' +
                ", status=" + status +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }


    public long getAttendanceId() {
        return this.attendanceId;
    }


    public void setAttendanceId(long attendanceId) {
        this.attendanceId = attendanceId;
    }


    public int getActivityCourseId() {
        return this.activityCourseId;
    }


    public void setActivityCourseId(int activityCourseId) {
        this.activityCourseId = activityCourseId;
    }


    public int getPeriodId() {
        return this.periodId;
    }


    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }


    public String getAttendancedate() {
        return this.attendancedate;
    }


    public void setAttendancedate(String attendancedate) {
        this.attendancedate = attendancedate;
    }


    public int getStatus() {
        return this.status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public String getCardNumber() {
        return this.cardNumber;
    }


    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

   
}
