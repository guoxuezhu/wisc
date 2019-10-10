package com.zhqz.wisc.libraryui;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Librarymodel implements Serializable {
    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public int id;

    public Librarymodel(String name,int id) {
        this.name = name;
        this.id = id;
    }


    @Override
    public String toString() {
        return "Librarymodel{" +
                "name='" + name + '\'' +
                '}';
    }
}
