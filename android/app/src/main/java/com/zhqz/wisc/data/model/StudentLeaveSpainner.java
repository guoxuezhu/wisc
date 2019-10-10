package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 11/21/17.
 */

public class StudentLeaveSpainner {

    @SerializedName("name")
    public String name;

    @SerializedName("lessonNum")
    public int lessonNum;

    @Override
    public String toString() {
        return "StudentLeaveSpainner{" +
                "name='" + name + '\'' +
                ", lessonNum=" + lessonNum +
                '}';
    }
}
