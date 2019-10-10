package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;
import com.zhqz.wisc.data.model.Canteen;

/**
 * Created by guoxuezhu on 16-11-15.
 */
public class BindCanTing {

    @SerializedName("canteenPostId")
    public int canteenPostId;
    @SerializedName("canteenId")
    public int canteenId;
    @SerializedName("canTingSM")
    public Canteen canteen;

    @Override
    public String toString() {
        return "BindCanTing{" +
                "canteenPostId=" + canteenPostId +
                ", canteenId=" + canteenId +
                ", canteen=" + canteen +
                '}';
    }
}
