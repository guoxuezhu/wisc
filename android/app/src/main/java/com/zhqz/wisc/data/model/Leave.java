package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jingjingtan on 11/23/17.
 */

public class Leave {
    @SerializedName("userId")
    public int userId;

    @SerializedName("list")
    public List<LeaveList> data;

    public class LeaveList {
        @SerializedName("id")
        public int id;

        @SerializedName("name")// 请假人姓名
        public String leave_user_name;

        @SerializedName("psType")// 2.学生 3.家长
        public String psType;

        @SerializedName("startDate")// 开始时间
        public String startDate;

        @SerializedName("endDate")// 结束时间
        public String endDate;

        @SerializedName("beginLessonNum")// 开始时间的第几节
        public String beginLessonNum;

        @SerializedName("endLessonNum")// 结束时间的第几节
        public String endLessonNum;

        @SerializedName("leave_item_title")
        public String leave_item_title;

        @SerializedName("typeId") // 请假类型 4事假 5病假
        public int typeId;

        @SerializedName("leaveName") // 请假类型原因
        public String leaveName;

        @SerializedName("reason") // 请假原因
        public String leave_reason;

        @SerializedName("schoolId")// 学校id
        public int schoolId;

        @SerializedName("userId")//班主任userid
        public int userId;

        @SerializedName("progress")// 进度 （0.发起申请，1.同意申请，2.申请驳回）
        public int progress;

        @SerializedName("agreeId")// 同意人id或驳回人id
        public int agreeId;

        @SerializedName("sex")//性别  0、男  1、女',
        public int sex;

        @Override
        public String toString() {
            return "LeaveList{" +
                    "id=" + id +
                    ", leave_user_name='" + leave_user_name + '\'' +
                    ", psType='" + psType + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", beginLessonNum='" + beginLessonNum + '\'' +
                    ", endLessonNum='" + endLessonNum + '\'' +
                    ", leave_item_title='" + leave_item_title + '\'' +
                    ", typeId=" + typeId +
                    ", leaveName='" + leaveName + '\'' +
                    ", leave_reason='" + leave_reason + '\'' +
                    ", schoolId=" + schoolId +
                    ", userId=" + userId +
                    ", progress=" + progress +
                    ", agreeId=" + agreeId +
                    ", sex=" + sex +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Leave{" +
                "userId=" + userId +
                ", data=" + data +
                '}';
    }
}
