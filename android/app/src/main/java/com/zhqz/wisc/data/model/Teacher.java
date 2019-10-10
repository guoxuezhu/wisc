package com.zhqz.wisc.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by guoxuezhu on 16-11-15.
 */
@Entity
public class Teacher {

    public int teacherId;
    public String teacherName;
    public String cardNumber;
    public String staffNumber;
    public String attendancedate;
    @Id
    public long attendanceId;
    public int status;
    public String picture;

    @Generated(hash = 1074314597)
    public Teacher(int teacherId, String teacherName, String cardNumber,
            String staffNumber, String attendancedate, long attendanceId,
            int status, String picture) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.cardNumber = cardNumber;
        this.staffNumber = staffNumber;
        this.attendancedate = attendancedate;
        this.attendanceId = attendanceId;
        this.status = status;
        this.picture = picture;
    }

    @Generated(hash = 1630413260)
    public Teacher() {
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", teacherName='" + teacherName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", staffNumber='" + staffNumber + '\'' +
                ", attendancedate='" + attendancedate + '\'' +
                ", attendanceId=" + attendanceId +
                ", status=" + status +
                ", picture='" + picture + '\'' +
                '}';
    }

    public int getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return this.teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStaffNumber() {
        return this.staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getAttendancedate() {
        return this.attendancedate;
    }

    public void setAttendancedate(String attendancedate) {
        this.attendancedate = attendancedate;
    }

    public long getAttendanceId() {
        return this.attendanceId;
    }

    public void setAttendanceId(long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
