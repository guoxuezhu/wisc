package com.zhqz.wisc.data;

import com.google.gson.annotations.SerializedName;

import javax.sql.DataSource;

public class HttpResult<T> {
    @SerializedName("success")
    public boolean result;
    @SerializedName("message")
    public String msg;
    @SerializedName("code")
    public String code;

    @SerializedName("data")
    T data;

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
