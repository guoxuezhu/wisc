package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 11/9/16.
 */

public class TianqiAQI {

    @SerializedName("code")
    public int code;
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("city")
        public City city;

        @SerializedName("aqi")
        public AQI aqi;

        public class City {
            @SerializedName("cityId")
            public String cityId;

            @SerializedName("counname")
            public String counname;//国家名称

            @SerializedName("name")
            public String name;//城市名称

            @SerializedName("pname")
            public String pname;//上级区域名称

            @Override
            public String toString() {
                return "City{" +
                        "cityId='" + cityId + '\'' +
                        ", counname='" + counname + '\'' +
                        ", name='" + name + '\'' +
                        ", pname='" + pname + '\'' +
                        '}';
            }
        }

        public class AQI {
            @SerializedName("cityName")
            public String cityName;//城市名称

            @SerializedName("pubtime")
            public String pubtime;//发布时间

            @SerializedName("value")
            public String value;//空气质量指数值

            @Override
            public String toString() {
                return "AQI{" +
                        "cityName='" + cityName + '\'' +
                        ", pubtime='" + pubtime + '\'' +
                        ", value='" + value + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                    "city=" + city +
                    ", aqi=" + aqi +
                    '}';
        }
    }

    @SerializedName("msg")
    public String msg;
    @SerializedName("rc")
    public Rc rc;

    public class Rc {
        @SerializedName("c")
        public int c;
        @SerializedName("p")
        public String p;

        @Override
        public String toString() {
            return "Rc{" +
                    "c=" + c +
                    ", p='" + p + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "Tianqi{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", rc=" + rc +
                '}';
    }
}
