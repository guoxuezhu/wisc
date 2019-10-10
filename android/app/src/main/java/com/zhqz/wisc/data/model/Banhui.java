package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 16-11-15.
 */

public class Banhui {

    @SerializedName("id")
    public int id;
    @SerializedName("imgUrl")
    public String imgUrl;

    @Override
    public String toString() {
        return "Banhui{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
