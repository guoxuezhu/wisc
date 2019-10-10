package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by guoxuezhu on 17-1-6.
 */

public class AttendanceDetails {

    @SerializedName("activityCourseId")
    public int activityCourseId;
    @SerializedName("periodId")
    public int periodId;

    /**
     * 学生
     */
    public List<Student> students;

    /**
     * 老师
     */
    public List<Teacher> teachers;


    @Override
    public String toString() {
        return "AttendanceDetails{" +
                "activityCourseId=" + activityCourseId +
                ", periodId=" + periodId +
                ", students=" + students +
                ", teachers=" + teachers +
                '}';
    }
}
