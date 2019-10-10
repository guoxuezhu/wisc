package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jingjingtan on 4/6/17.
 */

public class Meals {
    @SerializedName("totalNum")
    public int totalNum;//总次数

    @SerializedName("breakfastCount")
    public int breakfastCount;

    @SerializedName("breakfastUseCount")
    public int breakfastUseCount;

    @SerializedName("breakfastSurplusCount")
    public int breakfastSurplusCount;

    @SerializedName("lunchCount")
    public int lunchCount;

    @SerializedName("lunchUseCount")
    public int lunchUseCount;

    @SerializedName("lunchSurplusCount")
    public int lunchSurplusCount;

    @SerializedName("dinnerCount")
    public int dinnerCount;

    @SerializedName("dinnerUseCount")
    public int dinnerUseCount;

    @SerializedName("dinnerSurplusCount")
    public int dinnerSurplusCount;

    @SerializedName("details")
    public List<Mealsdetali> details;


    public class Mealsdetali {
        @SerializedName("canteenName")
        public String canteenName;
        @SerializedName("mealsName")
        public String mealsName;
        @SerializedName("mealTime")
        public String mealTime;
        @SerializedName("mealsCount")
        public int mealsCount;

        @Override
        public String toString() {
            return "Mealsdetali{" +
                    "canteenName='" + canteenName + '\'' +
                    ", mealsName='" + mealsName + '\'' +
                    ", mealTime='" + mealTime + '\'' +
                    ", mealsCount=" + mealsCount +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Meals{" +
                "totalNum=" + totalNum +
                ", breakfastCount=" + breakfastCount +
                ", breakfastUseCount=" + breakfastUseCount +
                ", breakfastSurplusCount=" + breakfastSurplusCount +
                ", lunchCount=" + lunchCount +
                ", lunchUseCount=" + lunchUseCount +
                ", lunchSurplusCount=" + lunchSurplusCount +
                ", dinnerCount=" + dinnerCount +
                ", dinnerUseCount=" + dinnerUseCount +
                ", dinnerSurplusCount=" + dinnerSurplusCount +
                ", details=" + details +
                '}';
    }
}
