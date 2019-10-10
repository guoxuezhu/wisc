package com.zhqz.wisc.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by guoxuezhu on 17-3-29.
 */
@Entity
public class CardInfoData {

    @Id
    public long cdId;

    public int customerId;

    public String name;

    public String number;//学号/工号

    public String className;

    public String cardNum;

    public int zaoSumNum;
    public int zaototalNum;
    public int zaolimitNum;

    public int zhongSumNum;
    public int zhongtotalNum;
    public int zhonglimitNum;

    public int wanSumNum;
    public int wantotalNum;
    public int wanlimitNum;

    @Generated(hash = 961718020)
    public CardInfoData(long cdId, int customerId, String name, String number,
                        String className, String cardNum, int zaoSumNum, int zaototalNum,
                        int zaolimitNum, int zhongSumNum, int zhongtotalNum, int zhonglimitNum,
                        int wanSumNum, int wantotalNum, int wanlimitNum) {
        this.cdId = cdId;
        this.customerId = customerId;
        this.name = name;
        this.number = number;
        this.className = className;
        this.cardNum = cardNum;
        this.zaoSumNum = zaoSumNum;
        this.zaototalNum = zaototalNum;
        this.zaolimitNum = zaolimitNum;
        this.zhongSumNum = zhongSumNum;
        this.zhongtotalNum = zhongtotalNum;
        this.zhonglimitNum = zhonglimitNum;
        this.wanSumNum = wanSumNum;
        this.wantotalNum = wantotalNum;
        this.wanlimitNum = wanlimitNum;
    }

    @Generated(hash = 835706945)
    public CardInfoData() {
    }

    @Override
    public String toString() {
        return "CardInfoData{" +
                "cdId=" + cdId +
                ", customerId=" + customerId +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", className='" + className + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", zaoSumNum=" + zaoSumNum +
                ", zaototalNum=" + zaototalNum +
                ", zaolimitNum=" + zaolimitNum +
                ", zhongSumNum=" + zhongSumNum +
                ", zhongtotalNum=" + zhongtotalNum +
                ", zhonglimitNum=" + zhonglimitNum +
                ", wanSumNum=" + wanSumNum +
                ", wantotalNum=" + wantotalNum +
                ", wanlimitNum=" + wanlimitNum +
                '}';
    }

    public long getCdId() {
        return this.cdId;
    }

    public void setCdId(long cdId) {
        this.cdId = cdId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int getZaoSumNum() {
        return this.zaoSumNum;
    }

    public void setZaoSumNum(int zaoSumNum) {
        this.zaoSumNum = zaoSumNum;
    }

    public int getZaototalNum() {
        return this.zaototalNum;
    }

    public void setZaototalNum(int zaototalNum) {
        this.zaototalNum = zaototalNum;
    }

    public int getZaolimitNum() {
        return this.zaolimitNum;
    }

    public void setZaolimitNum(int zaolimitNum) {
        this.zaolimitNum = zaolimitNum;
    }

    public int getZhongSumNum() {
        return this.zhongSumNum;
    }

    public void setZhongSumNum(int zhongSumNum) {
        this.zhongSumNum = zhongSumNum;
    }

    public int getZhongtotalNum() {
        return this.zhongtotalNum;
    }

    public void setZhongtotalNum(int zhongtotalNum) {
        this.zhongtotalNum = zhongtotalNum;
    }

    public int getZhonglimitNum() {
        return this.zhonglimitNum;
    }

    public void setZhonglimitNum(int zhonglimitNum) {
        this.zhonglimitNum = zhonglimitNum;
    }

    public int getWanSumNum() {
        return this.wanSumNum;
    }

    public void setWanSumNum(int wanSumNum) {
        this.wanSumNum = wanSumNum;
    }

    public int getWantotalNum() {
        return this.wantotalNum;
    }

    public void setWantotalNum(int wantotalNum) {
        this.wantotalNum = wantotalNum;
    }

    public int getWanlimitNum() {
        return this.wanlimitNum;
    }

    public void setWanlimitNum(int wanlimitNum) {
        this.wanlimitNum = wanlimitNum;
    }
}
