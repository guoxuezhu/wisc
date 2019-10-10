package com.zhqz.wisc.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by guoxuezhu on 16-11-15.
 */

@Entity
public class Student {

    public int studentId;
    public String studentName;
    public String cardNumber;
    public String studentNumber;
    public String attendancedate;
    @Id
    public long attendanceId;
    public int status;
    public String picture;

    @Generated(hash = 1339746076)
    public Student(int studentId, String studentName, String cardNumber,
            String studentNumber, String attendancedate, long attendanceId,
            int status, String picture) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.cardNumber = cardNumber;
        this.studentNumber = studentNumber;
        this.attendancedate = attendancedate;
        this.attendanceId = attendanceId;
        this.status = status;
        this.picture = picture;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", attendancedate='" + attendancedate + '\'' +
                ", attendanceId=" + attendanceId +
                ", status=" + status +
                ", picture='" + picture + '\'' +
                '}';
    }

    public int getStudentId() {
        return this.studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStudentNumber() {
        return this.studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
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
