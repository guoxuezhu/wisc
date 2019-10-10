package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 11/9/16.
 */

public class Tianqi {

    @SerializedName("code")
    public int code;
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("city")
        public City city;

        @SerializedName("condition")
        public Conditions condition;

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

        public class Conditions {
            @SerializedName("condition")
            public String condition;//实时天气状况

            @SerializedName("humidity")
            public String humidity;//湿度

            @SerializedName("icon")
            public String icon;//天气icon

            @SerializedName("temp")
            public String temp;//实时温度

            @SerializedName("updatetime")
            public String updatetime;//发布时间

            @SerializedName("windDir")
            public String windDir;//风向

            @SerializedName("windLevel")
            public String windLevel;//风级

            @Override
            public String toString() {
                return "Conditions{" +
                        "condition='" + condition + '\'' +
                        ", humidity='" + humidity + '\'' +
                        ", icon='" + icon + '\'' +
                        ", temp='" + temp + '\'' +
                        ", updatetime='" + updatetime + '\'' +
                        ", windDir='" + windDir + '\'' +
                        ", windLevel='" + windLevel + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                    "city=" + city +
                    ", condition=" + condition +
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
