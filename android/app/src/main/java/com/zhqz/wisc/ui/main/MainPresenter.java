package com.zhqz.wisc.ui.main;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.Datas;
import com.zhqz.wisc.data.DbDao.AttendanceDataDao;
import com.zhqz.wisc.data.DbDao.FingerTeacherUsersDao;
import com.zhqz.wisc.data.DbDao.FingerUsersDao;
import com.zhqz.wisc.data.DbDao.StudentDao;
import com.zhqz.wisc.data.DbDao.TeacherDao;
import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.AttendanceData;
import com.zhqz.wisc.data.model.AttendanceDataTwo;
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
import com.zhqz.wisc.data.model.Student;
import com.zhqz.wisc.data.model.StudentLeaveReason;
import com.zhqz.wisc.data.model.StudentLeaveSpainner;
import com.zhqz.wisc.data.model.Teacher;
import com.zhqz.wisc.data.model.TeacherIntroduction;
import com.zhqz.wisc.data.model.ThisClassStudents;
import com.zhqz.wisc.data.model.Tianqi;
import com.zhqz.wisc.data.model.TianqiAQI;
import com.zhqz.wisc.data.model.VersionCheck;
import com.zhqz.wisc.data.model.ZuoPin;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.DisplayTools;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.PromptDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by jingjingtan on 11/17/16.
 */

public class MainPresenter implements Presenter<MainMvpView> {
    private final static int timeMS = 1000 * 60 * 8;
    private final static int timeMSZuopin = 1000 * 60 * 2;
    private static MainMvpView mMvpView;
    private static WiscClient wiscClient;
    private static Scheduler.Worker workerCourseList = Schedulers.io().createWorker();
    private Scheduler.Worker workerAQI = Schedulers.io().createWorker();
    private Scheduler.Worker workerWeather = Schedulers.io().createWorker();
    private Scheduler.Worker workerNotice = Schedulers.io().createWorker();
    private Scheduler.Worker workerZuoPin = Schedulers.io().createWorker();
    private Scheduler.Worker workerStudentList = Schedulers.io().createWorker();


    private Disposable disposableNotice;
    private Disposable disposableZuoPin;
    private Disposable disposableNoZuoPin;
    private Disposable disposableBanhui;
    private Disposable disposableAQI;
    private Disposable disposableWeather;
    private static Disposable disposableCourse;
    private Disposable disposableStudents;
    private static Disposable disposableCourses;
    private static Disposable disposableJianjie;
    private static Disposable disposableTeacherIntroduction;
    private static Disposable disposableAttendance;
    private Disposable disposableVersion;
    private Disposable disposableRoom;
    private Disposable disposableThisClassStudents;
    private Disposable disposableMeals;


    private Disposable disposableLeaves;
    private Disposable disposableStudentLeaveSpainner;
    private Disposable disposableStudentLeaveReasonSpainner;
    private Disposable disposableStudentLeaveSubmit;
    private Disposable disposableSeting;
    private Disposable cabinetPersonDisposable;
    private Disposable cabinteClassDisposable;
    private Disposable disposableUnlock;
    private Disposable disposableLocking;
    private Disposable getFingerUsersDisposable;
    private Disposable disposableTeacherAttendance;
    private Disposable getFingerTeacherUsersDisposable;


    @Inject
    public MainPresenter(WiscClient client) {
        this.wiscClient = client;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (!workerCourseList.isDisposed()) {
            workerCourseList.dispose();
        }
        if (!workerAQI.isDisposed()) {
            workerAQI.dispose();
        }
        if (!workerWeather.isDisposed()) {
            workerWeather.dispose();
        }
        if (!workerNotice.isDisposed()) {
            workerNotice.dispose();
        }
        if (!workerZuoPin.isDisposed()) {
            workerZuoPin.dispose();
        }
        if (!workerStudentList.isDisposed()) {
            workerStudentList.dispose();
        }

        if (disposableTeacherAttendance != null && !disposableTeacherAttendance.isDisposed()) {
            disposableTeacherAttendance.dispose();
        }

        if (getFingerUsersDisposable != null && !getFingerUsersDisposable.isDisposed()) {
            getFingerUsersDisposable.dispose();
        }
        if (getFingerTeacherUsersDisposable != null && !getFingerTeacherUsersDisposable.isDisposed()) {
            getFingerTeacherUsersDisposable.dispose();
        }
        if (disposableNotice != null && !disposableNotice.isDisposed()) {
            disposableNotice.dispose();
        }
        if (disposableZuoPin != null && !disposableZuoPin.isDisposed()) {
            disposableZuoPin.dispose();
        }
        if (disposableNoZuoPin != null && !disposableNoZuoPin.isDisposed()) {
            disposableNoZuoPin.dispose();
        }
        if (disposableBanhui != null && !disposableBanhui.isDisposed()) {
            disposableBanhui.dispose();
        }
        if (disposableAQI != null && !disposableAQI.isDisposed()) {
            disposableAQI.dispose();
        }
        if (disposableWeather != null && !disposableWeather.isDisposed()) {
            disposableWeather.dispose();
        }
        if (disposableCourse != null && !disposableCourse.isDisposed()) {
            disposableCourse.dispose();
        }
        if (disposableStudents != null && !disposableStudents.isDisposed()) {
            disposableStudents.dispose();
        }
        if (disposableCourses != null && !disposableCourses.isDisposed()) {
            disposableCourses.dispose();
        }
        if (disposableJianjie != null && !disposableJianjie.isDisposed()) {
            disposableJianjie.dispose();
        }
        if (disposableTeacherIntroduction != null && !disposableTeacherIntroduction.isDisposed()) {
            disposableTeacherIntroduction.dispose();
        }
        if (disposableAttendance != null && !disposableAttendance.isDisposed()) {
            disposableAttendance.dispose();
        }
        if (disposableVersion != null && !disposableVersion.isDisposed()) {
            disposableVersion.dispose();
        }
        if (disposableRoom != null && !disposableRoom.isDisposed()) {
            disposableRoom.dispose();
        }
        if (disposableThisClassStudents != null && !disposableThisClassStudents.isDisposed()) {
            disposableThisClassStudents.dispose();
        }
        if (disposableMeals != null && !disposableMeals.isDisposed()) {
            disposableMeals.dispose();
        }
        if (disposableLeaves != null && !disposableLeaves.isDisposed()) {
            disposableLeaves.dispose();
        }
        if (cabinetPersonDisposable != null && !cabinetPersonDisposable.isDisposed()) {
            cabinetPersonDisposable.dispose();
        }
        if (disposableStudentLeaveSpainner != null && !disposableStudentLeaveSpainner.isDisposed()) {
            disposableStudentLeaveSpainner.dispose();
        }
        if (disposableStudentLeaveReasonSpainner != null && !disposableStudentLeaveReasonSpainner.isDisposed()) {
            disposableStudentLeaveReasonSpainner.dispose();
        }
        if (disposableStudentLeaveSubmit != null && !disposableStudentLeaveSubmit.isDisposed()) {
            disposableStudentLeaveSubmit.dispose();
        }
        if (disposableSeting != null && !disposableSeting.isDisposed()) {
            disposableSeting.dispose();
        }
        if (cabinteClassDisposable != null && !cabinteClassDisposable.isDisposed()) {
            cabinteClassDisposable.dispose();
        }
        if (disposableUnlock != null && !disposableUnlock.isDisposed()) {
            disposableUnlock.dispose();
        }
        if (disposableLocking != null && !disposableLocking.isDisposed()) {
            disposableLocking.dispose();
        }
    }



    private void setTimerNotice() {
        if (workerNotice.isDisposed()) {
            workerNotice = Schedulers.io().createWorker();
        }
        workerNotice.schedule(new Runnable() {
            @Override
            public void run() {
                workerNotice.dispose();
                getNoticeList();
            }
        }, timeMS, TimeUnit.MILLISECONDS);
    }

    public void getNoticeList() {
        if (!workerNotice.isDisposed()) {
            workerNotice.dispose();
        }
        wiscClient.getNoticeList(WiscApplication.prefs.getdeviceId())
                .doOnNext(noticeListHttpResult -> {
                    if (!noticeListHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(noticeListHttpResult.code,
                                noticeListHttpResult.msg));

                    }
                })
                .map(noticeListHttpResult -> noticeListHttpResult.getData())
                .subscribe(new Observer<List<NoticeList>>() {

                    @Override
                    public void onError(Throwable e) {
                        disposableNotice.dispose();
                        ELog.i("========通知列表=onError===" + e.toString());
                        if (isNetworkException(e)) {
                            setTimerNotice();
                        } else {
                            mMvpView.showErrorMessage(e);
                        }

                    }

                    @Override
                    public void onComplete() {
                        disposableNotice.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableNotice = d;
                    }

                    @Override
                    public void onNext(List<NoticeList> noticeList) {
                        ELog.i("=========通知列表======onNext============" + noticeList.toString());
                        mMvpView.showNoticeList(noticeList);
                    }
                });
    }

    private void setTimerZuoPin(int activityCourseId) {
        if (workerZuoPin.isDisposed()) {
            workerZuoPin = Schedulers.io().createWorker();
        }
        workerZuoPin.schedule(new Runnable() {
            @Override
            public void run() {
                workerZuoPin.dispose();
                if (activityCourseId == -1) {
                    getMeiKeZuoPin();
                } else {
                    getZuoPin(activityCourseId);
                }
            }
        }, timeMSZuopin, TimeUnit.MILLISECONDS);
    }

    public void getZuoPin(int courseId) {
        if (!workerZuoPin.isDisposed()) {
            workerZuoPin.dispose();
        }

        wiscClient.getZuoPin(courseId)
                .doOnNext(courseListHttpResult -> {

                    if (!courseListHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(courseListHttpResult.code,
                                courseListHttpResult.msg));

                    }
                })
                .subscribe(new Observer<HttpResult<List<ZuoPin>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableZuoPin = d;
                    }

                    @Override
                    public void onNext(HttpResult<List<ZuoPin>> listHttpResult) {
                        if (listHttpResult.getData() != null) {
                            ELog.i("======ZuoPin======onNext=======" + listHttpResult.getData().toString());
                            ELog.i("======ZuoPin======onNext=======" + listHttpResult.getData().size());
                            mMvpView.showZuoPinList(listHttpResult.getData());
                        } else {
                            ELog.i("======ZuoPin===null======onNext=======");
                            getMeiKeZuoPin();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableZuoPin.dispose();
                        ELog.i("==============ZuoPin===onError========" + e.toString());
                        if (isNetworkException(e)) {
                            setTimerZuoPin(courseId);
                        } else {
                            mMvpView.showZuoPinError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        disposableZuoPin.dispose();
                    }
                });
    }

    public void getMeiKeZuoPin() {
        if (!workerZuoPin.isDisposed()) {
            workerZuoPin.dispose();
        }
        wiscClient.getMeikeZuoPin()
                .doOnNext(myCourseHttpResult -> {

                    if (!myCourseHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(myCourseHttpResult.msg,
                                myCourseHttpResult.msg));
                    }
                })
                .map(myCourseHttpResult -> myCourseHttpResult.getData())
                .subscribe(new Observer<List<ZuoPin>>() {

                    @Override
                    public void onComplete() {
                        disposableNoZuoPin.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableNoZuoPin = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableNoZuoPin.dispose();
                        ELog.i("=============getMeikeZuoPin====onError========" + e.toString());
                        if (isNetworkException(e)) {
                            setTimerZuoPin(-1);
                        } else {
                            mMvpView.showZuoPinError();
                        }
                    }

                    @Override
                    public void onNext(List<ZuoPin> zuoPins) {
                        if (zuoPins.size() != 0) {
                            ELog.i("=============getMeikeZuoPin====onNext=======" + zuoPins.toString());
                            ELog.i("=============getMeikeZuoPin====onNext===size====" + zuoPins.size());
                            mMvpView.showZuoPinList(zuoPins);
                        } else {
                            ELog.i("=============getMeikeZuoPin====onNext===null====");
                            mMvpView.showZuoPinError();
                        }
                    }
                });

    }

    public void getBanhui() {

        wiscClient.getBanhui()
                .doOnNext(courseListHttpResult -> {

                    if (!courseListHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(courseListHttpResult.code,
                                courseListHttpResult.msg));

                    }
                })
                .subscribe(new Observer<HttpResult>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableBanhui = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        ELog.i("==============11===onNext=banhui=======" + httpResult.toString());
                        if (httpResult.getData() != null) {
                            mMvpView.showBanHui(httpResult.getData() + "");
                        } else {
                            mMvpView.showBanHui(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableBanhui.dispose();
                        ELog.i("==============11===onError=banhui=======" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableBanhui.dispose();
                    }
                });
    }

    private void setTimerAQI() {
        if (workerAQI.isDisposed()) {
            workerAQI = Schedulers.io().createWorker();
        }
        workerAQI.schedule(new Runnable() {
            @Override
            public void run() {
                workerAQI.dispose();
                getAQI();
            }
        }, timeMS, TimeUnit.MILLISECONDS);
    }

    public void getAQI() {
        if (!workerAQI.isDisposed()) {
            workerAQI.dispose();
        }
        wiscClient.gettianqiAQI()
                .doOnNext(tianqiAQIHttpResult -> {

                    if (!tianqiAQIHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(tianqiAQIHttpResult.code,
                                tianqiAQIHttpResult.msg));

                    }
                })
                .subscribe(new Observer<HttpResult>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableAQI = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        if (httpResult.getData() != null) {
                            Gson gson = new Gson();
                            TianqiAQI tianqiAQI = gson.fromJson(httpResult.getData().toString(), TianqiAQI.class);
                            ELog.i("==============getAQI===onNext====" + tianqiAQI.toString());
                            if (tianqiAQI != null && tianqiAQI.data != null && tianqiAQI.data.aqi != null && tianqiAQI.data.aqi.value != null) {
                                mMvpView.showTianqiAqi(tianqiAQI);
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableAQI.dispose();
                        ELog.i("==============getAQI===onError====" + e.toString());
                        if (isNetworkException(e)) {
                            setTimerAQI();
                        }
                    }

                    @Override
                    public void onComplete() {
                        disposableAQI.dispose();
                    }
                });

    }

    private void setTimerWeather() {
        if (workerWeather.isDisposed()) {
            workerWeather = Schedulers.io().createWorker();
        }
        workerWeather.schedule(new Runnable() {
            @Override
            public void run() {
                workerWeather.dispose();
                getWeather();
            }
        }, timeMS, TimeUnit.MILLISECONDS);
    }

    public void getWeather() {
        if (!workerWeather.isDisposed()) {
            workerWeather.dispose();
        }
        wiscClient.gettianQi()
                .doOnNext(tianqiHttpResult -> {

                    if (!tianqiHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(tianqiHttpResult.code,
                                tianqiHttpResult.msg));

                    }
                })
                .subscribe(new Observer<HttpResult>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableWeather = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        if (httpResult.getData() != null) {
                            Gson gson = new Gson();
                            Tianqi tianqi = gson.fromJson(httpResult.getData().toString(), Tianqi.class);
                            ELog.i("==============getWeather===onNext====" + tianqi.toString());
                            if (tianqi != null && tianqi.data != null && tianqi.data.condition != null) {
                                mMvpView.setTianqi(tianqi);
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableWeather.dispose();
                        ELog.i("==============getWeather===onError====" + e.toString());
                        if (isNetworkException(e)) {
                            setTimerWeather();
                        }
                    }

                    @Override
                    public void onComplete() {
                        disposableWeather.dispose();
                    }
                });

    }

    private static void setTimerCourseList() {
        if (workerCourseList.isDisposed()) {
            workerCourseList = Schedulers.io().createWorker();
        }
        workerCourseList.schedule(new Runnable() {
            @Override
            public void run() {
                workerCourseList.dispose();
                getCourseList();
            }
        }, timeMS, TimeUnit.MILLISECONDS);
    }

    //这天本教室课程列表
    public static void getCourseList() {
        if (!workerCourseList.isDisposed()) {
            workerCourseList.dispose();
        }
        wiscClient.getCourseList()
                .doOnNext(courseListHttpResult -> {

                    if (!courseListHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(courseListHttpResult.code,
                                courseListHttpResult.msg));

                    }
                })
                .map(courseListHttpResult -> courseListHttpResult.getData())
                .subscribe(new Observer<List<Course>>() {

                    @Override
                    public void onComplete() {
                        disposableCourse.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableCourse = d;

                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableCourse.dispose();
                        ELog.i("====这天本教室课程列表==onError========" + e.toString());
                        if (isNetworkException(e)) {
                            setTimerCourseList();
                        }
                        mMvpView.showCoursesListError(e);

                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        ELog.i("======这天本教室课程列表==onNext=======" + courses.toString());
                        mMvpView.showDayCourseList(courses);
                    }
                });
    }

    private static boolean isNetworkException(Throwable e) {
        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
        boolean isConnectException = e.getClass().equals(ConnectException.class);
        boolean isHttpException = e.getClass().equals(HttpException.class);
        if (isSocketException || isConnectException || isHttpException) {
            return true;
        } else {
            return false;
        }
    }

    private void setTimerStudentList(int activityCourseId, int periodId) {
        if (workerStudentList.isDisposed()) {
            workerStudentList = Schedulers.io().createWorker();
        }
        workerStudentList.schedule(new Runnable() {
            @Override
            public void run() {
                workerStudentList.dispose();
                getStudentList(activityCourseId, periodId);
            }
        }, timeMSZuopin, TimeUnit.MILLISECONDS);
    }

    public void getStudentList(int activityCourseId, int periodId) {
        if (!workerStudentList.isDisposed()) {
            workerStudentList.dispose();
        }
        wiscClient.getStudents(activityCourseId, periodId)
                .doOnNext(studentHttpResult -> {

                    if (!studentHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(studentHttpResult.code,
                                studentHttpResult.msg));

                    }
                })
                .map(studentHttpResult -> studentHttpResult.getData())
                .subscribe(new Observer<AttendanceDetails>() {

                    @Override
                    public void onComplete() {
                        disposableStudents.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableStudents = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableStudents.dispose();
                        ELog.i("==========AttendanceDetails=======onError========" + e.toString());
                        if (isNetworkException(e)) {
                            setTimerStudentList(activityCourseId, periodId);
                        } else {
                            mMvpView.showStudentList(null);
                        }
                    }

                    @Override
                    public void onNext(AttendanceDetails attendanceDetails) {
                        ELog.i("=============AttendanceDetails=====onNext=======" + attendanceDetails.toString());
                        mMvpView.showStudentList(attendanceDetails);
                    }
                });
    }

    public static void getMyCourseList(String kahao) {

//        if (null != subscription) {
//            return;
//        }

        wiscClient.getMyCourseList(kahao)
                .doOnNext(myCourseHttpResult -> {

                    if (!myCourseHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(myCourseHttpResult.code,
                                myCourseHttpResult.msg));

                    }
                })
                .map(myCourseHttpResult -> myCourseHttpResult.getData())
                .subscribe(new Observer<Courses>() {

                    @Override
                    public void onComplete() {
                        disposableCourses.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableCourses = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableCourses.dispose();
                        ELog.i("==========个人课程=======onError========" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onNext(Courses courses) {
                        ELog.i("===========个人课程=======onNext=======" + courses.toString());
                        if (courses != null) {
                            mMvpView.showCoursesList(courses);
                        }
                    }
                });


    }

    /*
     * 课程简介
     * */
    public static void courseIntroduction(String activityCourseId) {

//        if (null != subscription) {
//            return;
//        }

        wiscClient.courseIntroduction(activityCourseId)
                .doOnNext(stringHttpResult -> {
                    if (!stringHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(stringHttpResult.code,
                                stringHttpResult.msg));

                    }
                })
                .map(stringHttpResult -> stringHttpResult.getData())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onComplete() {
                        disposableJianjie.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableJianjie = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableJianjie.dispose();
                        mMvpView.showErrorMessage(e);
                        ELog.i("=======onError===课程简介====" + e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        ELog.i("=======onNext===课程简介=====" + s.length() + "");
                        if (s.trim() != null && s.trim().length() != 0) {
                            mMvpView.showCourseIntroduction(s);
                        }


                    }
                });
    }

    /*
     * 课程简介
     * */
    public static void teacherIntroduction(String activityCourseId) {

//        if (null != subscription) {
//            return;
//        }

        wiscClient.teacherIntroduction(activityCourseId)
                .doOnNext(teacherIntroductionHttpResult -> {
                    if (!teacherIntroductionHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(teacherIntroductionHttpResult.code,
                                teacherIntroductionHttpResult.msg));

                    }
                })
                .map(teacherIntroductionHttpResult -> teacherIntroductionHttpResult.getData())
                .subscribe(new Observer<TeacherIntroduction>() {

                    @Override
                    public void onComplete() {
                        disposableTeacherIntroduction.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableTeacherIntroduction = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableTeacherIntroduction.dispose();
                        mMvpView.showErrorMessage(e);
                        ELog.i("=======onError===老师简介===" + e.toString());
                    }

                    @Override
                    public void onNext(TeacherIntroduction teacherIntroduction) {
                        ELog.i("======onNext====老师简介==" + teacherIntroduction.toString());
                        if (teacherIntroduction != null && teacherIntroduction.id != 0) {
                            mMvpView.showTeacherIntroduction(teacherIntroduction);
                        }
                    }
                });
    }


    void sendshibaiAttendanceData() {
        List<AttendanceDataTwo> attendanceDataTwos = new ArrayList<AttendanceDataTwo>();
        AttendanceDataDao attendanceDataDao = WiscApplication.getDaoSession().getAttendanceDataDao();
        if (attendanceDataDao.loadAll().size() != 0) {
            for (int i = 0; i < attendanceDataDao.loadAll().size(); i++) {
                if (attendanceDataDao.loadAll().get(i).attendancedate != null) {
                    attendanceDataTwos.add(new AttendanceDataTwo(attendanceDataDao.loadAll().get(i).activityCourseId,
                            attendanceDataDao.loadAll().get(i).periodId,
                            attendanceDataDao.loadAll().get(i).attendancedate,
                            (int) attendanceDataDao.loadAll().get(i).attendanceId,
                            attendanceDataDao.loadAll().get(i).status,
                            attendanceDataDao.loadAll().get(i).cardNumber));
                }
            }
            if (attendanceDataTwos.size() != 0) {
                uploadRollCallResult(attendanceDataTwos);
            }
        }
    }

    static void uploadRollCallResult(List<AttendanceDataTwo> attendanceDatas) {
        AttendanceDataDao attendanceDataDao = WiscApplication.getDaoSession().getAttendanceDataDao();
        wiscClient.uploadAttendence(attendanceDatas)
                .doOnNext(booleanHttpResult -> {
                            if (!booleanHttpResult.code.equals("200")) {
                                throw Exceptions.propagate(new ClientRuntimeException(booleanHttpResult.code,
                                        booleanHttpResult.msg));

                            }
                        }
                ).map(booleanHttpResult1 -> booleanHttpResult1.getData())
                .subscribe(new Observer<List<AttendanceDataTwo>>() {

                    @Override
                    public void onComplete() {
                        disposableAttendance.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableAttendance = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableAttendance.dispose();
                        ELog.i("===========刷卡签到一起提交======onError========" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onNext(List<AttendanceDataTwo> attendanceDataTwos) {
                        ELog.i("============刷卡签到一起提交======onNext=======" + attendanceDataTwos);
                        if (attendanceDataTwos.size() != 0) {
                            for (int i = 0; i < attendanceDataTwos.size(); i++) {
                                try {
                                    attendanceDataDao.deleteByKey((long) attendanceDataTwos.get(i).getAttendanceIdTwo());
                                } catch (Exception e) {
                                }
                            }
                        }
                        ELog.i("============刷卡签到一起提交======onNext==22=====" + attendanceDataDao.loadAll().size());
                    }
                });
    }

    void VersionCheck() {


        wiscClient.VersionCheck(DisplayTools.getVersionCode() + "", DisplayTools.getVersionName())
                .map(versionCheckHttpResult -> versionCheckHttpResult)
                .subscribe(new Observer<HttpResult<VersionCheck>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableVersion = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<VersionCheck> versionCheckHttpResult) {
                        VersionCheck versionCheck = versionCheckHttpResult.getData();
                        if (versionCheck != null && versionCheck.bbh > DisplayTools.getVersionCode()) {
                            //WiscApplication.prefs.setVersionName(versionCheck.name);
                            WiscApplication.prefs.setVersionUrl(versionCheck.apkUrl);
                            //WiscApplication.prefs.setVersionCode(versionCheck.bbh);
                            mMvpView.isUpdate(true);
                            ELog.i("=====versionCheck===2==" + versionCheck.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableVersion.dispose();
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableVersion.dispose();
                    }
                });

    }


    public void getbindRoom(boolean isMqtt) {

        wiscClient.getbindRoom(WiscApplication.prefs.getdeviceId())
                .doOnNext(bindRoomHttpResult -> {

                    if (!bindRoomHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(bindRoomHttpResult.code,
                                bindRoomHttpResult.msg));

                    }
                })
                .subscribe(new Observer<HttpResult<Room>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableRoom = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<Room> roomHttpResult) {
                        ELog.i("============查看绑定教室======onNext=======" + roomHttpResult.toString());
                        if (roomHttpResult != null) {
                            mMvpView.bindNewRoom(roomHttpResult.getData(), isMqtt);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableRoom.dispose();
                        ELog.i("============查看绑定教室======onError=======" + e.toString());
                        mMvpView.makeError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableRoom.dispose();
                    }
                });
    }


    public void getbenbanStudent(String cardNumber) {
        wiscClient.benbanstudent(cardNumber)
                .doOnNext(benbanstudentHttpResult -> {
                    if (!benbanstudentHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(benbanstudentHttpResult.code,
                                benbanstudentHttpResult.msg));
                    }
                })
                .map(benbanstudentHttpResult -> benbanstudentHttpResult.getData())
                .subscribe(new Observer<List<ThisClassStudents>>() {

                    @Override
                    public void onComplete() {
                        disposableThisClassStudents.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableThisClassStudents = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableThisClassStudents.dispose();
                        ELog.i("============本班学生======onError=======" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onNext(List<ThisClassStudents> thisClassStudentses) {
                        ELog.i("============本班学生======onNext=======" + thisClassStudentses.toString());
                        mMvpView.showbenbanStudnts(thisClassStudentses);
                    }
                });
    }

    public void getMeals(String cardNumber) {


        wiscClient.appgetMeals(cardNumber)
                .doOnNext(mealsHttpResult -> {
                    if (!mealsHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(mealsHttpResult.code,
                                mealsHttpResult.msg));
                    }
                })
                .subscribe(new Observer<HttpResult<Meals>>() {

                    @Override
                    public void onError(Throwable e) {
                        disposableMeals.dispose();
                        ELog.i("============余额======onError=======" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableMeals.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableMeals = d;
                    }

                    @Override
                    public void onNext(HttpResult<Meals> mealsHttpResult) {
                        ELog.i("============余额======onNext=======" + mealsHttpResult.toString());
                        mMvpView.showMeals(mealsHttpResult.getData());
                    }
                });
    }

    public void getLeave(String cardNumber) {
        wiscClient.getLeave(cardNumber)
                .doOnNext(leaveHttpResult -> {
                    if (!leaveHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(leaveHttpResult.code,
                                leaveHttpResult.msg));
                    }
                })
                .map(leaveHttpResult -> leaveHttpResult.getData())
                .subscribe(new Observer<Leave>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableLeaves = d;
                    }

                    @Override
                    public void onNext(@NonNull Leave leave) {
                        ELog.i("============请假======onNext=======" + leave.toString());
                        mMvpView.setLeave(leave);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableLeaves.dispose();
                        ELog.i("============请假======onError=======" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableLeaves.dispose();
                    }
                });
    }


    public void StudentLeaveSpainner(String time, int userid, int num) { //num=1 开始时间 num=2结束时间
        wiscClient.getStudentLeaveSpainner(time, userid)
                .doOnNext(listHttpResult -> {
                    if (!listHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(listHttpResult.code,
                                listHttpResult.msg));
                    }
                })
                .map(listHttpResult -> listHttpResult.getData())
                .subscribe(new Observer<List<StudentLeaveSpainner>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableStudentLeaveSpainner = d;
                    }

                    @Override
                    public void onNext(@NonNull List<StudentLeaveSpainner> studentLeaveSpainners) {
                        ELog.i("============请假======onNext==studentLeaveSpainners=====" + studentLeaveSpainners.toString());
                        mMvpView.setSpainner(studentLeaveSpainners, num);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableStudentLeaveSpainner.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposableStudentLeaveSpainner.dispose();
                    }
                });
    }

    void getStudentLeaveReasonSpainner() {
        wiscClient.getStudentLeaveReasonSpainner()
                .doOnNext(listHttpResult -> {
                    if (!listHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(listHttpResult.code,
                                listHttpResult.msg));
                    }
                })
                .map(listHttpResult -> listHttpResult.getData())
                .subscribe(new Observer<List<StudentLeaveReason>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableStudentLeaveReasonSpainner = d;
                    }

                    @Override
                    public void onNext(@NonNull List<StudentLeaveReason> studentLeaveReasons) {
                        ELog.i("============请假======onNext==StudentLeaveReason=====" + studentLeaveReasons.toString());
                        mMvpView.setLeaveReason(studentLeaveReasons);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableStudentLeaveReasonSpainner.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposableStudentLeaveReasonSpainner.dispose();
                    }
                });
    }

    void getStudentLeaveSubmit(int userId, String psType, String startDate, String endDate, int beginNum, int endNum, String beginLessonNum, String endLessonNum, int typeId, String leaveName) {
        wiscClient.getStudentLeaveSubmit(userId, psType, startDate, endDate, beginNum, endNum, beginLessonNum, endLessonNum, typeId, leaveName)
                .doOnNext(booleanHttpResult -> {

                })
                .map(booleanHttpResult -> booleanHttpResult.code)
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableStudentLeaveSubmit = d;
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        if (s.equals("200")) {
                            mMvpView.setLeaveBoolean(true);
                        } else {
                            mMvpView.setLeaveBoolean(false);
                        }
                        ELog.i("============请假======onNext==提交=====" + s + "");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableStudentLeaveSubmit.dispose();
                        mMvpView.setLeaveBoolean(false);
                    }

                    @Override
                    public void onComplete() {
                        disposableStudentLeaveSubmit.dispose();
                    }
                });

    }

    public void getSeting(String cardNumber, int num) {
        wiscClient.getSeting(cardNumber).subscribe(new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableSeting = d;
            }

            @Override
            public void onNext(HttpResult httpResult) {
                ELog.i("============getSeting======onNext=======" + httpResult.toString());
                if (httpResult.code.toString().equals("200")) {
                    mMvpView.showSeting(true, num);
                }
            }

            @Override
            public void onError(Throwable e) {
                disposableSeting.dispose();
                ELog.i("============getSeting======onError=======" + e);
                mMvpView.showErrorMessage(e);
            }

            @Override
            public void onComplete() {
                disposableSeting.dispose();
            }
        });
    }


    public void getCabinte(String cardNumber) { // 0个人 1本班
        wiscClient.getCabinetPerson(cardNumber)
                .map(booleanHttpResult -> booleanHttpResult.getData())
                .subscribe(new Observer<CabinetPerson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        cabinetPersonDisposable = d;
                    }

                    @Override
                    public void onNext(CabinetPerson cabinetPerson) {
                        ELog.i("============柜子======onNext=======" + cabinetPerson.toString());
                        WiscApplication.prefs.setisClassTeacher(cabinetPerson.isClassTeacher);
                        mMvpView.setCabint(cabinetPerson.sm, cardNumber);
                    }

                    @Override
                    public void onError(Throwable e) {
                        cabinetPersonDisposable.dispose();
                        ELog.i("============柜子======onError=======" + e.toString());
                        mMvpView.setCabint(null, cardNumber);
                    }

                    @Override
                    public void onComplete() {
                        cabinetPersonDisposable.dispose();
                    }
                });
    }


    public void getCabinteClass(String cabintcardNumber) {

        wiscClient.getCabinetClass(cabintcardNumber)
                .subscribe(new Observer<HttpResult<Datas<List<CabinetInfo>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        cabinteClassDisposable = d;
                    }

                    @Override
                    public void onNext(HttpResult<Datas<List<CabinetInfo>>> datasHttpResult) {
                        ELog.i("============本班柜子======onNext=======" + datasHttpResult.toString());
                        if (datasHttpResult.getData() != null && datasHttpResult.getData().getDatas() != null) {
                            mMvpView.setCabintList(datasHttpResult.getData().getDatas(), cabintcardNumber);
                        } else {
                            mMvpView.setCabintList(null, cabintcardNumber);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        cabinteClassDisposable.dispose();
                        ELog.i("============本班柜子======onError=======" + e.toString());
                        mMvpView.setCabintList(null, cabintcardNumber);
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        cabinteClassDisposable.dispose();
                    }
                });

    }

    public void getUnlockClicks(int lockerNoNumber, String cabintcardNumber) {
        wiscClient.getUnlockClicks(lockerNoNumber, cabintcardNumber)
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableUnlock = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        ELog.i("============紧急开柜======onNext=======" + httpResult.toString());
                        if (httpResult.code.toString().equals("200")) {
                            mMvpView.setIsUnlock(true);
                        } else {
                            mMvpView.setIsUnlock(false);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ELog.i("============紧急开柜======onError=====" + e.toString());
                        mMvpView.makeError(e);
                        disposableUnlock.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposableUnlock.dispose();
                    }
                });


    }

    public void getlocking(int id, int status, String cabintcardNumber) {
        wiscClient.getlocking(id, status, cabintcardNumber)
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableLocking = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        ELog.i("============锁定======onNext=======" + httpResult.toString());
                        if (httpResult.code.toString().equals("200")) {
                            mMvpView.setIsLock(true, status);
                        } else {
                            mMvpView.setIsLock(false, 5);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableLocking.dispose();
                        ELog.i("============锁定======onError=======" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        disposableLocking.dispose();
                    }
                });
    }

    public void test(){
//        mMvpView.test();
    }
    public void getFingerUsers(boolean isWriteFinger) {
        wiscClient.getFingerList()
                .subscribe(new Observer<HttpResult<List<FingerUsers>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getFingerUsersDisposable = d;
                    }

                    @Override
                    public void onNext(HttpResult<List<FingerUsers>> listHttpResult) {
                        ELog.i("============getFingerUsers======onNext=======" + listHttpResult.toString());
                        if (listHttpResult.getData() != null && listHttpResult.getData().size() != 0) {
                            ELog.i("============getFingerUsers======onNext=======" + listHttpResult.getData().size());
                            mMvpView.writeTemplate(isWriteFinger, listHttpResult.getData());
                        } else {
                            mMvpView.writeTemplate(isWriteFinger, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getFingerUsersDisposable.dispose();
                        ELog.i("============getFingerUsers======onError=======" + e.toString());
                        mMvpView.writeTemplate(isWriteFinger, null);
                    }

                    @Override
                    public void onComplete() {
                        getFingerUsersDisposable.dispose();
                    }
                });


    }

    public void getFingerTeacherUserList (boolean isWriteFinger) {
        wiscClient.getFingerTeacherUserList()
                .subscribe(new Observer<HttpResult<List<FingerTeacherUsers>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getFingerTeacherUsersDisposable = d;
                    }

                    @Override
                    public void onNext(HttpResult<List<FingerTeacherUsers>> listHttpResult) {
                        ELog.i("============getFingerUsers======onNext=======" + listHttpResult.toString());
                        if (listHttpResult.getData() != null && listHttpResult.getData().size() != 0) {
                            mMvpView.writeTeacherTemplate(isWriteFinger, listHttpResult.getData());
                        } else {
                            mMvpView.writeTeacherTemplate(isWriteFinger, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getFingerTeacherUsersDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        getFingerTeacherUsersDisposable.dispose();
                    }
                });
    }

    public void teacherKaoqin(String cardNumber,int stid, int psType) {
        wiscClient.teacherAttendance(cardNumber,stid,psType)
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableTeacherAttendance = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<String> httpResult) {
                        ELog.i("============teacherAttendance======onNext=======" + httpResult.toString());
                        if (httpResult.code.toString().equals("200") && httpResult.getData() != null) {
                            if (httpResult.getData().toString().length() != 0) {
                                mMvpView.showTeacherKaoqin(true, httpResult.getData().toString());
                            } else {
                                mMvpView.showTeacherKaoqin(false, "请使用老师卡考勤");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableTeacherAttendance.dispose();
                        ELog.i("============teacherAttendance======onError=======" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableTeacherAttendance.dispose();
                    }
                });
    }

    public void showMessage(int msg, int id) {
        if (mMvpView != null) {
            mMvpView.showMessage(msg, id);
        }
    }
    /*
     * 卡号和时间签到
     * */
    public void daka(int fingerId,Handler handler) {
        ELog.e("成功＝＝＝" + fingerId);
        FingerTeacherUsersDao fingerTeacherUsersDao = WiscApplication.getDaoSession().getFingerTeacherUsersDao();
        //老师
        if (fingerTeacherUsersDao != null && fingerTeacherUsersDao.loadAll().size() != 0) {
            List<FingerTeacherUsers> fingerUser = fingerTeacherUsersDao.queryBuilder()
                    .where(FingerTeacherUsersDao.Properties.Id.eq(fingerId))
                    .orderAsc(FingerTeacherUsersDao.Properties.Id)
                    .list();

            if (fingerUser.size() != 0) {
                ELog.e("成功＝＝＝" + fingerUser.get(0).psType +"===id====" +fingerUser.get(0).id+"===name====" +fingerUser.get(0).name+"==stid=="+fingerUser.get(0).stid);
                if (fingerUser.get(0).psType == 1){
                    teacherKaoqin(fingerUser.get(0).number,fingerUser.get(0).stid,fingerUser.get(0).psType);
                } else {
                    ELog.e("成功＝＝＝" + "没有比较的指纹");
                }
            }
        }
        //学生
        FingerUsersDao fingersDao = WiscApplication.getDaoSession().getFingerUsersDao();
        if (fingersDao != null && fingersDao.loadAll().size() != 0) {
            List<FingerUsers> fingerUser = fingersDao.queryBuilder()
                    .where(FingerUsersDao.Properties.Id.eq(fingerId))
                    .orderAsc(FingerUsersDao.Properties.Id)
                    .list();
            if (fingerUser.size() != 0) {
                ELog.e("成功＝＝＝" + "学生暂时不能在此界面签到"+fingerUser.get(0).psType +"===id====" +fingerUser.get(0).id+"===name====" +fingerUser.get(0).name+"==stid=="+fingerUser.get(0).stid);
                if (fingerUser.get(0).psType == 2){
                    ELog.e("成功＝＝＝" + "学生暂时不能在此界面签到");
                    setFingerStudentKaoqin(handler,fingerUser.get(0).number, DateUtil.getCurrentTime());
                }
            }
        }
    }

    /*
    * 学生考勤数据提交
    * */
    private void setFingerStudentKaoqin(Handler handler,String cardnumber,String CurrentTime) {
        if (WiscApplication.prefs.getCourseStartTime() == null) {
            ELog.e("成功＝2＝＝" + "现在不需要考勤");
            Message msg = new Message();
            msg.obj = "现在不需要考勤";
            msg.what = WiscClient.tishi;
            handler.sendMessage(msg);
            return;
        }
        if (DateUtil.compare_date(System.currentTimeMillis(), WiscApplication.prefs.getCourseStartTime()) <= 15 * 1000 * 60) {
            if (WiscApplication.getDaoSession().getFingerUsersDao().loadAll().size() != 0) {
                //学生考勤数据提交
                //发消息过去用handler
                Message msg = new Message();
                msg.obj = cardnumber;
                msg.what = WiscClient.FingerStudent;
                handler.sendMessage(msg);
            } else {
                ELog.e("成功＝1＝＝" + "无指纹数据，请选择其他操作");
                Message msg = new Message();
                msg.obj = "无指纹数据，请选择其他操作";
                msg.what = WiscClient.tishi;
                handler.sendMessage(msg);
            }
        } else {
            ELog.e("成功＝2＝＝" + "请在上课前15分钟内考勤");
            Message msg = new Message();
            msg.obj = "请在上课前15分钟内考勤";
            msg.what = WiscClient.tishi;
            handler.sendMessage(msg);
        }
    }
    /*
     * 读取到的指纹数据
     * */

    public void sendZhiwenData(String str) {
        ELog.e("tjj读取到的指纹数据" + str);
//        mMvpView.sendZhiwenDatas(str);
    }
}
