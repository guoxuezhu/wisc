package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 2/18/17.
 */

public class TeacherIntroduction {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String teachername;

    /**
     * 老师性别: 1男  2女
     */
    @SerializedName("sex")
    public String sex;

    /**
     * 老师简介
     */
    @SerializedName("profile")
    public String profile;

    @Override
    public String toString() {
        return "TeacherIntroduction{" +
                "id=" + id +
                ", name='" + teachername + '\'' +
                ", sex='" + sex + '\'' +
                ", profile=" + profile +
                '}';
    }


}
