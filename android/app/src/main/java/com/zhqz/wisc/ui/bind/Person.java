package com.zhqz.wisc.ui.bind;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 8/11/17.
 */

public class Person {
    
    @SerializedName("id")
    public int id;
    @SerializedName("value")
    public String value;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}