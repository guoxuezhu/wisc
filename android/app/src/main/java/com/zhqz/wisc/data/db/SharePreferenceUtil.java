package com.zhqz.wisc.data.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 共享参数类
 */
@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {
    private SharedPreferences sp;
    private Editor editor;

    /**
     * 构造函数
     */
    public SharePreferenceUtil(Context context, String file) {
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        // 利用edit()方法获取Editor对象。
        editor = sp.edit();
    }

    public void setisBind(boolean isBind) {
        editor.putBoolean("isBind", isBind);
        editor.commit();
    }

    public boolean getisBind() {
        return sp.getBoolean("isBind", false);
    }
    public void setisLibarayBind(boolean isBind) {
        editor.putBoolean("isBindLibaray", isBind);
        editor.commit();
    }

    public boolean getisLibarayBind() {
        return sp.getBoolean("isBindLibaray", false);
    }

    /**
     * @param udid
     */
    public void setUdid(String udid) {
        editor.putString("udid", udid);
        editor.commit();
    }

    public String getUdid() {
        return sp.getString("udid", null);
    }

    /**
     * @param deviceId
     */
    public void setdeviceId(int deviceId) {
        editor.putInt("deviceId", deviceId);
        editor.commit();
    }

    public int getdeviceId() {
        return sp.getInt("deviceId", 0);
    }


    /**
     * @param classId
     */
    public void setclassRoomId(int classId) {
        editor.putInt("classId", classId);
        editor.commit();
    }

    public int getclassRoomId() {
        return sp.getInt("classId", 0);
    }

    public void setRoomId(int roomId) {
        editor.putInt("roomId", roomId);
        editor.commit();
    }

    public int getRoomId() {
        return sp.getInt("roomId", -1);
    }


    public void setBindRoom(String classRoom) {
        editor.putString("classRoom", classRoom);
        editor.commit();
    }

    public String getBindRoom() {
        return sp.getString("classRoom", null);
    }


    public void setBindRoomEnglishName(String englishName) {
        editor.putString("englishName", englishName);
        editor.commit();
    }

    public String getBindRoomEnglishName() {
        return sp.getString("englishName", null);
    }

    public void setPeriodId(int periodId) {
        editor.putInt("periodId", periodId);
        editor.commit();
    }

    public int getPeriodId() {
        return sp.getInt("periodId", 0);
    }

    public void setVersionUrl(String VersionUrl) {
        editor.putString("VersionUrl", VersionUrl);
        editor.commit();
    }

    public String getVersionUrl() {
        return sp.getString("VersionUrl", "");
    }

    public void setSchoolId(int SchoolId) {
        editor.putInt("SchoolId", SchoolId);
        editor.commit();
    }

    public int getSchoolId() {
        return sp.getInt("SchoolId", -1);
    }

    public void setCourseStartTime(String courseStartTime) {
        editor.putString("courseStartTime", courseStartTime);
        editor.commit();
    }

    public String getCourseStartTime() {
        return sp.getString("courseStartTime", null);
    }

    public void setActivityCourseId(int activityCourseId) {
        editor.putInt("activityCourseId", activityCourseId);
        editor.commit();
    }

    public int getActivityCourseId() {
        return sp.getInt("activityCourseId", -1);
    }


    public void setCardNumber(String cardNumber) {
        editor.putString("cardNumber", cardNumber);
        editor.commit();
    }

    public String getCardNumber() {
        return sp.getString("cardNumber", null);
    }

    public void setisClassTeacher(Boolean isClassTeacher) {
        editor.putBoolean("isClassTeacher", isClassTeacher);
        editor.commit();
    }

    public boolean getisClassTeacher() {
        return sp.getBoolean("isClassTeacher", false);
    }

    public void setIsInitSyntax(Boolean InitSyntax) {
        editor.putBoolean("InitSyntax", InitSyntax);
        editor.commit();
    }

    public boolean getIsInitSyntax() {
        return sp.getBoolean("InitSyntax", false);
    }




    /*
    * 食堂1
    * 选课2
    * 图书馆 3
    * */
    public void setSceneId(int SceneId) {
        editor.putInt("SceneId", SceneId);
        editor.commit();
    }

    public int getSceneId() {
        return sp.getInt("SceneId", -1);
    }

    /*
     * 是否选了图书馆场景
     * */
    public void setIsSelectLibrary(boolean SceneId) {
        editor.putBoolean("SelectLibrary", SceneId);
        editor.commit();
    }

    public Boolean getIsSelectLibrary() {
        return sp.getBoolean("SelectLibrary", false);
    }

    /*
    * 是否选了食堂场景
    * */
    public void setIsSelectCanteen(boolean SceneId) {
        editor.putBoolean("SelectCanteen", SceneId);
        editor.commit();
    }

    public Boolean getIsSelectCanteen() {
        return sp.getBoolean("SelectCanteen", false);
    }

    /*
    * 是否选了考勤场景
    * */
    public void setIsSelectKaoQing(boolean SceneId) {
        editor.putBoolean("SelectKaoQing", SceneId);
        editor.commit();
    }

    public Boolean getIsSelectKaoQing() {
        return sp.getBoolean("SelectKaoQing", false);
    }

    /*
    *
    * 食堂
    *
    * */

    public void setCanteenId(int canteenId) {
        editor.putInt("canteenId", canteenId);
        editor.commit();
    }

    public int getCanteenId() {
        return sp.getInt("canteenId", -1);
    }

    public void setCanteenPostId(int canteenPostId) {
        editor.putInt("canteenPostId", canteenPostId);
        editor.commit();
    }

    public int getCanteenPostId() {
        return sp.getInt("canteenPostId", -1);
    }

//    /**
//     * @param udid
//     */
//    public void setUdid(String udid) {
//        editor.putString("udid", udid);
//        editor.commit();
//    }
//
//    public String getUdid() {
//        return sp.getString("udid", null);
//    }

    public void setZhongcishu(int zhongcishu) {
        editor.putInt("zhongcishu", zhongcishu);
        editor.commit();
    }

    public int getZhongcishu() {
        return sp.getInt("zhongcishu", 0);
    }


    public void setZaocanStartTime(String zaocanStartTime) {
        editor.putString("zaocanStartTime", zaocanStartTime);
        editor.commit();
    }

    public String getZaocanStartTime() {
        return sp.getString("zaocanStartTime", null);
    }


    public void setZaocanEndTime(String zaocanEndTime) {
        editor.putString("zaocanEndTime", zaocanEndTime);
        editor.commit();
    }


    public String getZaocanEndTime() {
        return sp.getString("zaocanEndTime", null);
    }


    public void setZhongcanStartTime(String zhongcanStartTime) {
        editor.putString("zhongcanStartTime", zhongcanStartTime);
        editor.commit();
    }


    public String getZhongcanStartTime() {
        return sp.getString("zhongcanStartTime", null);
    }


    public void setZhongcanEndTime(String zhongcanEndTime) {
        editor.putString("zhongcanEndTime", zhongcanEndTime);
        editor.commit();
    }


    public String getZhongcanEndTime() {
        return sp.getString("zhongcanEndTime", null);
    }


    public void setZaocanTimesId(int zaocanTimesId) {
        editor.putInt("zaocanTimesId", zaocanTimesId);
        editor.commit();
    }

    public int getZaocanTimesId() {
        return sp.getInt("zaocanTimesId", -1);
    }



    public void setZhongcanTimesId(int zhongcanTimesId) {
        editor.putInt("zhongcanTimesId", zhongcanTimesId);
        editor.commit();
    }

    public int getZhongcanTimesId() {
        return sp.getInt("zhongcanTimesId", -1);
    }

    public void setZhongcishuTiem(String zhongcishuTiem) {
        editor.putString("zhongcishuTiem", zhongcishuTiem);
        editor.commit();
    }

    public String getZhongcishuTiem() {
        return sp.getString("zhongcishuTiem", null);
    }


    public void setCanteenName(String canteenName) {
        editor.putString("canteenName", canteenName);
        editor.commit();
    }

    public String getCanteenName() {
        return sp.getString("canteenName", null);
    }

    public void setWancanStartTime(String wancanStartTime) {
        editor.putString("wancanStartTime", wancanStartTime);
        editor.commit();
    }
    public String getWancanStartTime() {
        return sp.getString("wancanStartTime", null);
    }

    public void setWancanEndTime(String wancanEndTime) {
        editor.putString("wancanEndTime", wancanEndTime);
        editor.commit();
    }

    public String getWancanEndTime() {
        return sp.getString("wancanEndTime", null);
    }

    public void setWancanTimesId(int wancanTimesId) {
        editor.putInt("wancanTimesId", wancanTimesId);
        editor.commit();
    }

    public int getWancanTimesId() {
        return sp.getInt("wancanTimesId", -1);
    }




}
