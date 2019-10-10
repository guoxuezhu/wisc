package com.zhqz.wisc.canteenui.todaymenu;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.zhqz.wisc.R;

/**
 * Created by jingjingtan on 3/29/18.
 */

public class TodayMenu {
    @SerializedName("name")
    public String name;

    @SerializedName("status")
    public String status;

    @SerializedName("attendanceId")
    public String attendanceId;

    @SerializedName("url")
    public String url;

    @SerializedName("schoolId")
    public int schoolId;

    @SerializedName("id")
    public int id;


    public int attendanceResId() {
        int resId = R.id.radio_late;
        if (!TextUtils.isEmpty(status)) {
            if (status.equals("1")) {
                resId = R.id.radio_late;
            }  else if (status.equals("2")){
                resId = R.id.radio_present;
            } else {

            }
        }
        return resId;
    }

    @Override
    public String toString() {
        return "TodayMenu{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", attendanceId='" + attendanceId + '\'' +
                ", url='" + url + '\'' +
                ", schoolId=" + schoolId +
                ", id=" + id +
                '}';
    }
}
