package com.zhqz.wisc.data.remote;

import com.google.gson.annotations.SerializedName;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.model.TransactionDetailSM;
import com.zhqz.wisc.data.model.AttendanceDataTwo;

import java.util.ArrayList;
import java.util.List;

public class JsonRequest {
    static public class Factory {
        static public BindRoomRequest bindRoomReq(String uuid, String osType, int roomId, int schoolId) {
            return new BindRoomRequest(uuid, osType, roomId, schoolId);
        }

        public static RollCallSubmitRequest rollCallSubmitReq(List<AttendanceDataTwo> attendanceData) {
            return new RollCallSubmitRequest(attendanceData);
        }

        static public IsLoginRequest IsLoginReq(String userName, String pwd, String schoolId, String grant_type) {
            return new IsLoginRequest(userName, pwd, schoolId, grant_type);
        }

        static public InputInformationRequest InputInfoReq(String name, String number, int schoolId) {
            return new InputInformationRequest(name, number, schoolId);
        }

        public static SendSubmitRequest sendSubmitRequest(TransactionDetailSM transactionDetailSMs,List<TransactionDetailSM> noWifiDatas) {
            return new SendSubmitRequest(transactionDetailSMs,noWifiDatas);
        }

        static public DeviceRequest deviceReq(int canteenId,String uuid, String osType) {
            return new DeviceRequest(canteenId,uuid, osType);
        }
        static public SendSubmitMenu sendSubmitMenu(List<Integer> ids) {
            return new SendSubmitMenu(ids);
        }

    }

    static public class InputInformationRequest {
        @SerializedName("name")
        String name;
        @SerializedName("number")
        String number;
        @SerializedName("schoolId")
        int schoolId;

        public InputInformationRequest(String name, String number, int schoolId) {
            this.name = name;
            this.number = number;
            this.schoolId = schoolId;
        }
    }

    static public class IsLoginRequest {
        @SerializedName("userName")
        String userName;
        @SerializedName("pwd")
        String pwd;
        @SerializedName("schoolId")
        String schoolId;
        @SerializedName("grant_type")
        String grant_type;


        public IsLoginRequest(String userName, String pwd, String schoolId, String grant_type) {
            this.userName = userName;
            this.pwd = pwd;
            this.schoolId = schoolId;
            this.grant_type = grant_type;
        }
    }

    static public class BindRoomRequest {
        @SerializedName("uuid")
        String uuid;
        @SerializedName("osType")
        String osType;
        @SerializedName("roomId")
        int roomId;
        @SerializedName("schoolId")
        int schoolId;

        public BindRoomRequest(String uuid, String osType, int roomId, int schoolId) {
            this.uuid = uuid;
            this.osType = osType;
            this.roomId = roomId;
            this.schoolId = schoolId;
        }
    }


    static public class RollCallSubmitRequest {
        @SerializedName("deviceId")
        int deviceId;
        @SerializedName("schoolId")
        int schoolId;
        @SerializedName("data")
        List<AttendanceDeta> attendanceDetas;

        RollCallSubmitRequest(List<AttendanceDataTwo> attendanceData) {
            deviceId = WiscApplication.prefs.getdeviceId();
            schoolId = WiscApplication.prefs.getSchoolId();
            attendanceDetas = new ArrayList<AttendanceDeta>();
            for (int i = 0; i < attendanceData.size(); i++) {
                attendanceDetas.add(new AttendanceDeta(attendanceData.get(i)));
            }
        }
    }

    static class AttendanceDeta {
        @SerializedName("attendanceId")
        int attendanceId;
        @SerializedName("attendancedate")
        String attendancedate;
        @SerializedName("activityCourseId")
        int activityCourseId;
        @SerializedName("periodId")
        int periodId;
        @SerializedName("cardNumber")
        String cardNumber;
        @SerializedName("status")
        int status;

        public AttendanceDeta(AttendanceDataTwo attendanceData) {
            this.attendanceId = attendanceData.getAttendanceIdTwo();
            this.attendancedate = attendanceData.getAttendancedateTwo();
            this.activityCourseId = attendanceData.getActivityCourseIdTwo();
            this.periodId = attendanceData.getPeriodIdTwo();
            this.cardNumber = attendanceData.getCardNumberTwo();
            this.status = attendanceData.getStatusTwo();
        }
    }


    /*
    * 食堂
    * */

    static public class SendSubmitRequest {
        @SerializedName("schoolId")
        int schoolId;
        @SerializedName("canteenPostId")
        int canteenPostId;
        @SerializedName("canteenId")
        int canteenId;
        @SerializedName("data")
        TransactionDetailSM transactionDetailSMs;
        @SerializedName("synchroDatas")
        List<TransactionDetailSM> synchroDatas;

        SendSubmitRequest(TransactionDetailSM attendanceData,List<TransactionDetailSM> noWifiDatas) {
            canteenPostId= WiscApplication.prefs.getCanteenPostId();
            canteenId= WiscApplication.prefs.getCanteenId();
            transactionDetailSMs = attendanceData;
            synchroDatas=noWifiDatas;
            schoolId = WiscApplication.prefs.getSchoolId();
        }


    }

     /*
    * 食堂
    * */

    static public class SendSubmitMenu {
        @SerializedName("schoolId")
        int schoolId;

        @SerializedName("canteenId")
        int canteenId;

        @SerializedName("ids")
        List<Integer> ids;
        SendSubmitMenu(List<Integer> idlist) {
            canteenId= WiscApplication.prefs.getCanteenId();
            ids=idlist;
            schoolId = WiscApplication.prefs.getSchoolId();
        }


    }


    static public class DeviceRequest {
        @SerializedName("canteenId")
        int canteenId;
        @SerializedName("uuid")
        String uuid;
        @SerializedName("osType")
        String osType;

        public DeviceRequest(int canteenId, String uuid, String osType) {
            this.canteenId = canteenId;
            this.uuid = uuid;
            this.osType = osType;
        }
    }

}
