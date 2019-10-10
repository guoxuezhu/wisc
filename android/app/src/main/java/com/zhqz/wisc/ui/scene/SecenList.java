package com.zhqz.wisc.ui.scene;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingjingtan on 3/30/18.
 */

public class SecenList {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("jumpPath")
    public String jumpPath;

    @SerializedName("url")
    public String url;
    @SerializedName("schoolId")
    public int schoolId;

    @SerializedName("status")
    public int status;

    @SerializedName("parentId")
    public int parentId;


//    private Integer id; // id
//    private String name; // 场景名称
//    private String url; // 图片
//    private String jumpPath; // 跳转路径
//    private Integer schoolId;
//    private Integer parentId; //父id
//    private Integer status; //'状态  1.未开启 2，已经开启  3.禁用   4.中航对学校进行禁用',


    @Override
    public String toString() {
        return "Classrooms{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jumpPath='" + jumpPath + '\'' +
                ", url='" + url + '\'' +
                ", schoolId=" + schoolId +
                ", status=" + status +
                ", parentId=" + parentId +
                '}';
    }
}
