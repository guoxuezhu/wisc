package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 12/9/16.
 */
public class Classrooms {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("englishName")
    public String englishName;


    @SerializedName("status")
    public int status;


    @Override
    public String toString() {
        return "Classrooms{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", englishName='" + englishName + '\'' +
                ", status=" + status +
                '}';
    }
}
