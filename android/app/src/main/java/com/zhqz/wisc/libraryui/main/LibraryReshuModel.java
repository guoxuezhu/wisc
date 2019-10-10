package com.zhqz.wisc.libraryui.main;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LibraryReshuModel {
    public int id;

    public String name;

    public String author;

    public String coverUrl;

    public String number;

    public String synopsis;

    public double sorting;

    public int status;

    public int schoolId;

    @Generated(hash = 15602175)
    public LibraryReshuModel(int id, String name, String author, String coverUrl,
            String number, String synopsis, double sorting, int status,
            int schoolId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.coverUrl = coverUrl;
        this.number = number;
        this.synopsis = synopsis;
        this.sorting = sorting;
        this.status = status;
        this.schoolId = schoolId;
    }

    @Generated(hash = 1946318618)
    public LibraryReshuModel() {
    }

    @Override
    public String toString() {
        return "LibraryReshuModel{" +
                "id=" + id +
                ", name=" + name +
                ", author=" + author +
                ", coverUrl=" + coverUrl +
                ", number=" + number +
                ", synopsis=" + synopsis +
                ", sorting=" + sorting +
                ", status=" + status +
                ", schoolId=" + schoolId +
                '}';
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getSorting() {
        return this.sorting;
    }

    public void setSorting(double sorting) {
        this.sorting = sorting;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSchoolId() {
        return this.schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
//    private Integer id;
//    private String name; // 书名
//    private String author; // 作者
//    private String coverUrl; // 封面路径
//    private String number; // 编号
//    private String synopsis; // 简介， 大纲
//    private Double sorting; // 排序
//    private Integer status; // 状态 1.推书 2.禁推
//    private Integer schoolId; // 学校id