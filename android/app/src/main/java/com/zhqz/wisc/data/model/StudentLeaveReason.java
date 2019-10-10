package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 11/22/17.
 */

public class StudentLeaveReason {
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public int id;
    @SerializedName("status")
    public int status;

    @Override
    public String toString() {
        return "StudentLeaveReason{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
