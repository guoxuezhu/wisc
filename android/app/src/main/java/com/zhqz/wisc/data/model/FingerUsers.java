package com.zhqz.wisc.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by guoxuezhu on 16-11-15.
 */

@Entity
public class FingerUsers {

    public int id;
    public int stid;
    public int psType;
    public String name;
    public String context;
    public int schoolId;
    public String ts;
    public int status;  //0.未录入  1.已经录入
    public String number; // 卡号

    @Generated(hash = 481339449)
    public FingerUsers(int id, int stid, int psType, String name, String context,
            int schoolId, String ts, int status, String number) {
        this.id = id;
        this.stid = stid;
        this.psType = psType;
        this.name = name;
        this.context = context;
        this.schoolId = schoolId;
        this.ts = ts;
        this.status = status;
        this.number = number;
    }

    @Generated(hash = 1329917544)
    public FingerUsers() {
    }

    @Override
    public String toString() {
        return "FingerUsers{" +
                "id=" + id +
                ", stid=" + stid +
                ", psType=" + psType +
                ", name='" + name + '\'' +
                ", context='" + context + '\'' +
                ", schoolId=" + schoolId +
                ", ts='" + ts + '\'' +
                ", status=" + status +
                ", number='" + number + '\'' +
                '}';
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStid() {
        return this.stid;
    }

    public void setStid(int stid) {
        this.stid = stid;
    }

    public int getPsType() {
        return this.psType;
    }

    public void setPsType(int psType) {
        this.psType = psType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return this.context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getSchoolId() {
        return this.schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
