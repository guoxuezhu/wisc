package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by guoxuezhu on 16-11-15.
 */

public class Courses {


    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("roleId")
    public String roleId;

    public List<Today> today;

    public class Today {

        @SerializedName("classSection")
        public String classSection;//第几节课
        @SerializedName("courseName")
        public String courseName;
        @SerializedName("teacherName")
        public String teacherName;
        @SerializedName("classTime")
        public String classTime;
        @SerializedName("classRoom")
        public String classRoom;

        @Override
        public String toString() {
            return "Today{" +
                    "classSection='" + classSection + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", teacherName='" + teacherName + '\'' +
                    ", classTime='" + classTime + '\'' +
                    ", classRoom='" + classRoom + '\'' +
                    '}';
        }
    }

    public List<Yesterday> yesterday;

    public class Yesterday {

        @SerializedName("classSection")
        public String classSection;//第几节课
        @SerializedName("courseName")
        public String courseName;
        @SerializedName("teacherName")
        public String teacherName;
        @SerializedName("classTime")
        public String classTime;
        @SerializedName("classRoom")
        public String classRoom;

        @Override
        public String toString() {
            return "Yesterday{" +
                    "classSection='" + classSection + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", teacherName='" + teacherName + '\'' +
                    ", classTime='" + classTime + '\'' +
                    ", classRoom='" + classRoom + '\'' +
                    '}';
        }
    }

    public List<Tomorrow> tomorrow;

    public class Tomorrow {

        @SerializedName("classSection")
        public String classSection;//第几节课
        @SerializedName("courseName")
        public String courseName;
        @SerializedName("teacherName")
        public String teacherName;
        @SerializedName("classTime")
        public String classTime;
        @SerializedName("classRoom")
        public String classRoom;

        @Override
        public String toString() {
            return "Tomorrow{" +
                    "classSection='" + classSection + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", teacherName='" + teacherName + '\'' +
                    ", classTime='" + classTime + '\'' +
                    ", classRoom='" + classRoom + '\'' +
                    '}';
        }
    }

    //    @SerializedName("name")
//    public String name;
//    @SerializedName("courseName")
//    public String courseName;
//    @SerializedName("teacherName")
//    public String teacherName;
//    @SerializedName("classTime")
//    public String classTime;
//    @SerializedName("classRoom")
//    public String classRoom;
//
    @Override
    public String toString() {
        return "Courses{" +
                "name='" + name + '\'' +
                ", roleId='" + roleId + '\'' +
                ", id='" + id + '\'' +
                ", Today='" + today + '\'' +
                ", Yesterday='" + yesterday + '\'' +
//                ", Tomorrow='" + tomorrow + '\'' +
                '}';
    }
}
