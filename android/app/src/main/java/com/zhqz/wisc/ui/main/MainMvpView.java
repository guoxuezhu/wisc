package com.zhqz.wisc.ui.main;

import com.zhqz.wisc.data.model.AttendanceDetails;
import com.zhqz.wisc.data.model.CabinetInfo;
import com.zhqz.wisc.data.model.CabinetPerson;
import com.zhqz.wisc.data.model.Course;
import com.zhqz.wisc.data.model.Courses;
import com.zhqz.wisc.data.model.FingerTeacherUsers;
import com.zhqz.wisc.data.model.FingerUsers;
import com.zhqz.wisc.data.model.Leave;
import com.zhqz.wisc.data.model.Meals;
import com.zhqz.wisc.data.model.NoticeList;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.data.model.StudentLeaveReason;
import com.zhqz.wisc.data.model.StudentLeaveSpainner;
import com.zhqz.wisc.data.model.TeacherIntroduction;
import com.zhqz.wisc.data.model.ThisClassStudents;
import com.zhqz.wisc.data.model.Tianqi;
import com.zhqz.wisc.data.model.TianqiAQI;
import com.zhqz.wisc.data.model.ZuoPin;
import com.zhqz.wisc.ui.base.MvpView;
import com.zhqz.wisc.ui.scene.SecenList;

import java.util.List;

/**
 * Created by jingjingtan on 11/17/16.
 */

public interface MainMvpView extends MvpView {

    void showStudentList(AttendanceDetails attendanceDetails);

    void showCoursesList(Courses courses);

    void showDayCourseList(List<Course> courses);

    void showErrorMessage(Throwable s);

    void showBanHui(String imgUrl);

    void showCoursesListError(Throwable e);

    void showZuoPinList(List<ZuoPin> zuoPins);

    void showZuoPinError();

    void isUpdate(boolean b);

    void showCourseIntroduction(String introduction);

    void showTeacherIntroduction(TeacherIntroduction teacherIntroduction);

    void showNoticeList(List<NoticeList> noticeList);

    void bindNewRoom(Room room, boolean isMqtt);

    void showbenbanStudnts(List<ThisClassStudents> thisClassStudentses);

    void showMeals(Meals meals);

    void makeError(Throwable e);

    void showTianqiAqi(TianqiAQI tianqiAQI);

    void setTianqi(Tianqi tianqi);

    void setLeave(Leave leave);

    void setSpainner(List<StudentLeaveSpainner> studentLeaveSpainners, int num);

    void setLeaveReason(List<StudentLeaveReason> studentLeaveReasons);

    void setLeaveBoolean(Boolean aBoolean);

    void showSeting(boolean b, int num);

    void setCabint(List<CabinetPerson.CabinetClassItem> sm, String cardNumber);

    void setCabintList(List<CabinetInfo> cabinetInfos, String cabintcardNumber);

    void setIsUnlock(boolean b);

    void setIsLock(boolean b, int status);

    void writeTemplate(boolean isWriteFinger, List<FingerUsers> data);

    void showTeacherKaoqin(boolean b, String msg);

    void showMessage(int msg, int id);

    void writeTeacherTemplate(boolean isWriteFinger, List<FingerTeacherUsers> data);
    void showTishiMessage(String s);
}
