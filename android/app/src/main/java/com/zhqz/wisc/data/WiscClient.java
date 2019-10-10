package com.zhqz.wisc.data;

import android.os.Build;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.canteenui.todaymenu.TodayMenu;
import com.zhqz.wisc.data.model.BindCanTing;
import com.zhqz.wisc.data.model.CanTing;
import com.zhqz.wisc.data.model.Canteen;
import com.zhqz.wisc.data.model.DakaInfo;
import com.zhqz.wisc.data.model.DakaRequest;
import com.zhqz.wisc.data.model.FingerTeacherUsers;
import com.zhqz.wisc.data.model.TransactionDetailSM;
import com.zhqz.wisc.data.model.AttendanceDataTwo;
import com.zhqz.wisc.data.model.AttendanceDetails;
import com.zhqz.wisc.data.model.CabinetInfo;
import com.zhqz.wisc.data.model.CabinetPerson;
import com.zhqz.wisc.data.model.Classrooms;
import com.zhqz.wisc.data.model.Course;
import com.zhqz.wisc.data.model.Courses;
import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.data.model.FingerEnterStudent;
import com.zhqz.wisc.data.model.FingerUsers;
import com.zhqz.wisc.data.model.Leave;
import com.zhqz.wisc.data.model.Meals;
import com.zhqz.wisc.data.model.NoticeList;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.data.model.StudentLeaveReason;
import com.zhqz.wisc.data.model.StudentLeaveSpainner;
import com.zhqz.wisc.data.model.TeacherIntroduction;
import com.zhqz.wisc.data.model.ThisClassStudents;
import com.zhqz.wisc.data.model.VersionCheck;
import com.zhqz.wisc.data.model.ZuoPin;
import com.zhqz.wisc.data.remote.JsonRequest;
import com.zhqz.wisc.data.remote.WiscService;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.libraryui.main.LibraryModel;
import com.zhqz.wisc.libraryui.main.LibraryReshuModel;
import com.zhqz.wisc.ui.bind.Person;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.DisplayTools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Query;


@Singleton
public class WiscClient {
    public static final int DUKA_HANDLER = 15;
    public static final int TIME_HANDLER = 16;
    public static final int SPLASH_HANDLER = 2;


    public final static int COURSE_HANDLER = 0;
    public final static int CLOSED_DRAWER_HANDLER = 1;
    public final static int CLOSED_DRAWER_HANDLERt = 14;
    public final static int ZUOPIN_HANDLER = 2;
    public final static int KAOQIN_TONGJI_HANDLER = 3;
    public final static int FINGER_TIMEBACK_HANDLER = 4;
    public final static int SHIPIN_SEEKBAR_HANDLER = 5;
    public final static int DUKAHAO_HANDLER = 100;
    public final static int fail = 101;
    public final static int tishi = 102;
    public final static int FingerStudent = 103;
    public final static int FINGER_NAME_HANDLER = 1011;
    public final static int FINGER_HANDLER = 6;
    public final static int FINGER_EMPTY_NAME_HANDLER = 12;
    public final static int FINGER_DEVICE_HANDLER = 13;
    public final static int CLOSED_DIALOG_HANDLER = 7;
    public final static int NONGLI_HANDLER = 8;
    public final static int FACE_HANDLER = 9;
    public final static int FACE_TIME_HANDLER = 10;
    public final static int FACE_TIMEBACK_HANDLER = 11;
    public final static int CLOSED_LibraryDRAWER_HANDLER = 17;
    public final static int NONGLI_HANDLER_Library = 18;
    public static boolean isDaKaQianDao = false;//是否打卡签到
    public static boolean isChaKanKeCheng = false;//查看个人课程
    public static boolean isThisClassStudents = false;//班级学生
    public static boolean isLeave = false;//请假
    public static boolean isMeals = false;//余额
    public static boolean isEnter = false;//是否点击录入
    public static boolean isSetting = false;//是否设置
    public static boolean isCabint = false;//是否设置
    public static boolean isWriteFinger = true;//是否写指纹数据
    public static boolean isScene = false;//是否场景切换
    public static boolean isCanteenScene = false;//是否食堂场景切换
    public static boolean istoday_menu = false;//今日菜单
    public static boolean isLibraryScene = false;//是否场景切换
    public static boolean isLibrarySetting = false;//是否设置
    public static int Isseting = 1;//1设置 2场景切换
    public static boolean isFinger = false;//是否点击指纹识别按钮

    @SuppressWarnings("unchecked")
    private static ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return ((Observable<HttpResult>) upstream).subscribeOn(Schedulers.io())
                    .timeout(30, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(httpResult -> {
                        if (!httpResult.code.equals("200")) {
                            throw Exceptions.propagate(new ClientRuntimeException(httpResult.code,
                                    httpResult.msg));

                        }
                    });
        }
    };

    private WiscService wiscService;

    @Inject
    public WiscClient(WiscService wiscService) {
        this.wiscService = wiscService;
    }

    @SuppressWarnings("unchecked")
    private static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }

    public Observable<HttpResult<List<FingerEnterStudent>>> getfingerEnterStudent(String cardNumber) {
        return wiscService
                .getfingerStudents(cardNumber, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<FingerEnterStudent>>>applySchedulers());
    }

    public Observable<HttpResult<List<EnterStudent>>> getEnterStudent(String cardNumber) {
        return wiscService
                .getStudents(cardNumber, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<EnterStudent>>>applySchedulers());
    }

    public Observable<HttpResult> saveImage(int stId, String psType, int status, int schoolId, String path) {
        File file = new File(path);
        //组装partMap对象
        Map<String, RequestBody> partMap = new HashMap<>();

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        partMap.put("files\"; filename=\"" + file.getName() + "", fileBody);

        return wiscService
                .updataImage(stId, psType, status, schoolId, partMap)
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> FaceIdentify(int courseRoomTimeId, String path) {
        File file = new File(path);
        //组装partMap对象
        Map<String, RequestBody> partMap = new HashMap<>();

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        partMap.put("files\"; filename=\"" + file.getName() + "", fileBody);

        return wiscService
                .FaceIdentify(courseRoomTimeId, WiscApplication.prefs.getSchoolId(), partMap)
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> detectface(String path) {
        File file = new File(path);
        //组装partMap对象
        Map<String, RequestBody> partMap = new HashMap<>();

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        partMap.put("files\"; filename=\"" + file.getName() + "", fileBody);

        return wiscService
                .detectface(partMap)
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> getSeting(String cardNum) {
        return wiscService
                .getSeting(cardNum, WiscApplication.prefs.getdeviceId(), WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> submit(List<Integer> ids) {
        return wiscService
                .submit(JsonRequest.Factory.sendSubmitMenu(ids))
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> getMode() {
        return wiscService
                .getMode(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult<List<TodayMenu>>> getMenu(String number) {
        return wiscService
                .getMenu(number,WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<TodayMenu>>>applySchedulers());
    }
    public Observable<HttpResult<List<TodayMenu>>> getTodayMenu() {
        return wiscService
                .getTodayMenu(WiscApplication.prefs.getCanteenId(),WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<TodayMenu>>>applySchedulers());
    }


    public Observable<HttpResult<Room>> bindRoom(int roomId) {
        return wiscService
                .bindRoom(JsonRequest.Factory.bindRoomReq(DisplayTools.getUdid(WiscApplication.prefs), "Android_" + Build.VERSION.RELEASE, roomId, WiscApplication.prefs.getSchoolId()))
                .compose(this.<HttpResult<Room>>applySchedulers());
    }

    public Observable<HttpResult<Room>> bindlibrary(int roomId) {
        return wiscService
                .bindlibrary(JsonRequest.Factory.bindRoomReq(DisplayTools.getUdid(WiscApplication.prefs), "Android_" + Build.VERSION.RELEASE, roomId, WiscApplication.prefs.getSchoolId()))
                .compose(this.<HttpResult<Room>>applySchedulers());
    }



    public Observable<HttpResult<List<AttendanceDataTwo>>> uploadAttendence(List<AttendanceDataTwo> attendanceDatas) {

        return wiscService.uploadAttendence(JsonRequest.Factory.rollCallSubmitReq(attendanceDatas))
                .compose(this.<HttpResult<List<AttendanceDataTwo>>>applySchedulers());

    }

    public Observable<HttpResult> gettianQi() {
        return wiscService
                .getTianqi()
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> gettianqiAQI() {
        return wiscService
                .getTianqiAQI()
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult<List<Course>>> getCourseList() {
        return wiscService
                .courseList(WiscApplication.prefs.getdeviceId() + "", DateUtil.getTimeyyyyMMdd(), WiscApplication.prefs.getSchoolId())//"2016-10-12"//DateUtil.getTimeyyyyMMdd()
                .compose(this.<HttpResult<List<Course>>>applySchedulers());
    }


    public Observable<HttpResult<Meals>> appgetMeals(String cardNumber) {
        return wiscService
                .getMeals(cardNumber, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<Meals>>applySchedulers());
    }

    public Observable<HttpResult<Leave>> getLeave(String cardNumber) {
        return wiscService
                .getLeave(cardNumber, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<Leave>>applySchedulers());
    }

    public Observable<HttpResult<Boolean>> getStudentLeaveSubmit(int userid, String psType, String startDate, String endDate, int beginNum, int endNum, String beginLessonNum, String endLessonNum, int typeId, String leaveName) {
        return wiscService.getStudentLeaveSubmit(userid, "2", startDate, endDate, beginNum, endNum, beginLessonNum, endLessonNum, typeId, leaveName, null, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<Boolean>>applySchedulers());
    }

    public Observable<HttpResult<List<StudentLeaveSpainner>>> getStudentLeaveSpainner(String time, int userid) {
        return wiscService
                .getStudentLeaveSpainner(time, userid, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<StudentLeaveSpainner>>>applySchedulers());
    }

    public Observable<HttpResult<List<StudentLeaveReason>>> getStudentLeaveReasonSpainner() {
        return wiscService.getStudentLeaveReasonSpainner(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<StudentLeaveReason>>>applySchedulers());
    }

    public Observable<HttpResult<AttendanceDetails>> getStudents(int activityCourseId, int periodId) {
        return wiscService
                .studentList(activityCourseId, periodId, WiscApplication.prefs.getSchoolId())//10009
                .compose(this.<HttpResult<AttendanceDetails>>applySchedulers());
    }

    public Observable<HttpResult> getBanhui() {
        return wiscService
                .banhui(WiscApplication.prefs.getdeviceId() + "", WiscApplication.prefs.getSchoolId())//10009
                .compose(this.<HttpResult>applySchedulers());
    }
    public Observable<HttpResult> getbanhuilibrary() {
        return wiscService
                .banhuilibrary(WiscApplication.prefs.getdeviceId() + "", WiscApplication.prefs.getSchoolId())//10009
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult<LibraryModel>> getLibraryIntroduction() {
        return wiscService
                .getLibraryIntroduction(WiscApplication.prefs.getdeviceId() + "", WiscApplication.prefs.getSchoolId())//10009
                .compose(this.<HttpResult<LibraryModel>>applySchedulers());
    }


    public Observable<HttpResult<List<ZuoPin>>> getZuoPin(int activityCourseId) {
        return wiscService
                .zuopin(activityCourseId, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<ZuoPin>>>applySchedulers());
    }

    public Observable<HttpResult<List<ZuoPin>>> getMeikeZuoPin() {
        return wiscService
                .meikezuopin(WiscApplication.prefs.getdeviceId() + "", WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<ZuoPin>>>applySchedulers());
    }


    public Observable<HttpResult<List<NoticeList>>> getNoticeList(int deviceId) {
        return wiscService
                .noticeList(WiscApplication.prefs.getUdid(), WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<NoticeList>>>applySchedulers());
    }

    public Observable<HttpResult<List<ThisClassStudents>>> benbanstudent(String cardNumber) {
        return wiscService
                .benbanstudent(cardNumber, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<ThisClassStudents>>>applySchedulers());
    }

    public Observable<HttpResult<Courses>> getMyCourseList(String kahao) {
        return wiscService
                .myCourseList(kahao, DateUtil.getTimeyyyyMMdd(), WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<Courses>>applySchedulers());
    }


    public Observable<HttpResult<List<Classrooms>>> classromms() {
        return wiscService
                .classrooms(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<Classrooms>>>applySchedulers());
    }
    public Observable<HttpResult<List<Classrooms>>> librarymenu() {
        return wiscService
                .librarymenu(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<Classrooms>>>applySchedulers());
    }

    public Observable<HttpResult<List<SecenList>>> GetSecen() {
        return wiscService
                .GetSecen(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<SecenList>>>applySchedulers());
    }

    public Observable<HttpResult<List<LibraryReshuModel>>> LibraryReshuList() {
        return wiscService
                .LibraryReshuList(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<LibraryReshuModel>>>applySchedulers());
    }

    public Observable<HttpResult<List<Person>>> schoolList() {
        return wiscService
                .schoolList()
                .compose(this.<HttpResult<List<Person>>>applySchedulers());
    }

    public Observable<HttpResult<Room>> getbindRoom(int deviceId) {
        return wiscService
                .getbindRoom(WiscApplication.prefs.getUdid(), WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<Room>>applySchedulers());
    }
    public Observable<HttpResult<Room>> getbindlibrary(int deviceId) {
        return wiscService
                .getbindlibrary(WiscApplication.prefs.getUdid(), WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<Room>>applySchedulers());
    }


    public boolean loadUserIfAvailble() {
        if (WiscApplication.prefs.getdeviceId() != 0 && WiscApplication.prefs.getisBind()) {
            return true;
        }
        return false;
    }

    public Observable<HttpResult<VersionCheck>> VersionCheck(String code, String name) {
        return wiscService
                .versionCheck(code, name, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<VersionCheck>>applySchedulers());
    }

    public Observable<HttpResult<String>> courseIntroduction(String activityCourseId) {
        return wiscService
                .courseIntroduction(activityCourseId, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<String>>applySchedulers());
    }

    public Observable<HttpResult<TeacherIntroduction>> teacherIntroduction(String activityCourseId) {
        return wiscService
                .teacherIntroduction(activityCourseId, WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<TeacherIntroduction>>applySchedulers());
    }

    public Observable<HttpResult<CabinetPerson>> getCabinetPerson(String scardNum) {
        return wiscService
                .getCabinetPerson(scardNum, "1", "100", WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<CabinetPerson>>applySchedulers());
    }

    public Observable<HttpResult<Datas<List<CabinetInfo>>>> getCabinetClass(String scardNum) {
        return wiscService
                .getCabinetClass(scardNum, "1", "100", WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<Datas<List<CabinetInfo>>>>applySchedulers());
    }

    public Observable<HttpResult<String>> teacherAttendance(String cardNumber,int stid, int psType) {
        return wiscService.teacherKaoqin(cardNumber,WiscApplication.prefs.getSchoolId(),stid,psType)
                .compose(this.<HttpResult<String>>applySchedulers());
    }

    public Observable<HttpResult> getUnlockClicks(int number, String scardNum) {
        return wiscService.getUnlockClicks(scardNum, number, " ", WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> getlocking(int id, int status, String scardNum) {
        return wiscService.getlocking(WiscApplication.prefs.getSchoolId(), status, id, scardNum)
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult> sendTemplateData1(int psType, int stid, String context) {
        return wiscService.sendTemplateData(WiscApplication.prefs.getSchoolId(), psType, stid, context)
                .compose(this.<HttpResult>applySchedulers());
    }

    public Observable<HttpResult<List<FingerUsers>>> getFingerList() {
        return wiscService
                .getFingerUserList(WiscApplication.prefs.getRoomId(), DateUtil.getTimeyyyyMMdd(), WiscApplication.prefs.getSchoolId())//"2016-10-12"//DateUtil.getTimeyyyyMMdd()
                .compose(this.<HttpResult<List<FingerUsers>>>applySchedulers());
    }
    public Observable<HttpResult<List<FingerTeacherUsers>>> getFingerTeacherUserList() {
        return wiscService
                .getFingerTeacherUserList(WiscApplication.prefs.getSchoolId())//"2016-10-12"//DateUtil.getTimeyyyyMMdd()
                .compose(this.<HttpResult<List<FingerTeacherUsers>>>applySchedulers());
    }

    /*
    *
    * 食堂接口
    *
    * */
    public Observable<HttpResult<Canteen>> getCanteen() {
        return wiscService.getCanteen(WiscApplication.prefs.getCanteenPostId())
                .compose(this.<HttpResult<Canteen>>applySchedulers());
    }

    public Observable<HttpResult<DakaRequest>> sendTransactionDetail(TransactionDetailSM transactionDetailSMs, List<TransactionDetailSM> noWifiDatas) {
        return wiscService
                .sendTransactionDetail(JsonRequest.Factory.sendSubmitRequest(transactionDetailSMs,noWifiDatas))
                .compose(this.<HttpResult<DakaRequest>>applySchedulers());
    }

    public Observable<HttpResult<List<DakaInfo>>> syndata() {
        return wiscService.syndata(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<DakaInfo>>>applySchedulers());
    }

//    public Observable<HttpResult> getSeting(String cardNum) {
//        return wiscService.getSeting(cardNum, 0, WiscApplication.prefs.getSchoolId())
//                .compose(this.<HttpResult>applySchedulers());
//    }

    public Observable<HttpResult<List<CanTing>>> canting() {
        return wiscService.getCantings(WiscApplication.prefs.getSchoolId())
                .compose(this.<HttpResult<List<CanTing>>>applySchedulers());
    }

    public Observable<HttpResult<BindCanTing>> bindCanTing(int canteenId) {
        return wiscService
                .bindCanTing(JsonRequest.Factory.deviceReq(canteenId, DisplayTools.getUdid(WiscApplication.prefs), "Android_" + Build.VERSION.RELEASE))
                .compose(this.<HttpResult<BindCanTing>>applySchedulers());
    }
    public void setDevice(int deviceId) {
        WiscService.Factory.setDeviceId(deviceId);
    }
}
