package com.zhqz.wisc.ui.faceScann;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.DbDao.AttendanceDataDao;
import com.zhqz.wisc.data.DbDao.StudentDao;
import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.AttendanceData;
import com.zhqz.wisc.data.model.AttendanceDataTwo;
import com.zhqz.wisc.data.model.Student;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;

/**
 * Created by jingjingtan on 11/6/17.
 */

public class FaceScannPresenter implements Presenter<FaceScannMvpView> {
    private FaceScannMvpView mMvpView;
    private WiscClient wiscClient;
    private Disposable detectfaceDisposable;
    private Disposable disposableFaceIdentify;
    private Disposable faceAttendanceDisposable;
    private Disposable updataImageDisposable;


    @Inject
    public FaceScannPresenter(WiscClient wiscClient) {
        this.wiscClient = wiscClient;
    }

    @Override
    public void attachView(FaceScannMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (detectfaceDisposable != null && !detectfaceDisposable.isDisposed()) {
            detectfaceDisposable.dispose();
        }
        if (disposableFaceIdentify != null && !disposableFaceIdentify.isDisposed()) {
            disposableFaceIdentify.dispose();
        }
        if (faceAttendanceDisposable != null && !faceAttendanceDisposable.isDisposed()) {
            faceAttendanceDisposable.dispose();
        }
        if (updataImageDisposable != null && !updataImageDisposable.isDisposed()) {
            updataImageDisposable.dispose();
        }
    }

    void detectface(String path) {
        wiscClient.detectface(path)
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        detectfaceDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        ELog.i("============人脸检测======onNext=======" + httpResult.toString());
                        if (httpResult.code.toString().equals("200")) {
                            mMvpView.detectFace(true, null);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        detectfaceDisposable.dispose();
                        ELog.i("============人脸检测======onError=======" + e.toString());
                        mMvpView.detectFace(false, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        detectfaceDisposable.dispose();
                    }
                });
    }

    void updataImage(int stId, String psType, int status, int schoolId, String path) {
        wiscClient.saveImage(stId, psType, status, schoolId, path)
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        updataImageDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        ELog.i("============人脸录入======onNext=======" + httpResult.toString());
                        if (httpResult.code.toString().equals("200")) {
                            mMvpView.faceUpdataImage(true);
                        } else {
                            mMvpView.faceUpdataImage(false);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        updataImageDisposable.dispose();
                        ELog.i("============人脸录入======onError=======" + e.toString());
                        mMvpView.faceUpdataImage(false);
                    }

                    @Override
                    public void onComplete() {
                        updataImageDisposable.dispose();
                    }
                });
    }


    void FaceIdentify(int courseRoomTimeId, String path) {
        wiscClient.FaceIdentify(courseRoomTimeId, path)
                .doOnNext(httpResult -> {

                })
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableFaceIdentify = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        ELog.i("============人脸识别cardNumber======onNext=======" + httpResult.toString());
                        if (httpResult.code.toString().equals("200")) {
                            if (httpResult.getData() != null) {
                                mMvpView.IsFaceIdentify(true, httpResult.getData().toString());
                                kaoqin(httpResult.getData().toString(), DateUtil.getCurrentTime());
                            } else {
                                mMvpView.IsFaceIdentify(false, "请重新检测");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableFaceIdentify.dispose();
                        ELog.i("============人脸识别cardNumber======onError=======" + e.toString());//165
                        mMvpView.IsFaceIdentify(false, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        disposableFaceIdentify.dispose();
                    }
                });
    }

    private void kaoqin(String cardNumber, String time) {
        ELog.i("==========人脸识别cardNumber======" + cardNumber);
        List<AttendanceDataTwo> attendanceDataTwos = new ArrayList<AttendanceDataTwo>();
        attendanceDataTwos.clear();

        StudentDao studentDao = WiscApplication.getDaoSession().getStudentDao();
        AttendanceDataDao attendanceDataDao = WiscApplication.getDaoSession().getAttendanceDataDao();

        if (studentDao != null && studentDao.loadAll().size() != 0) {
            List<Student> students = studentDao.queryBuilder()
                    .where(StudentDao.Properties.CardNumber.eq(cardNumber))
                    .orderAsc(StudentDao.Properties.CardNumber)
                    .list();
            if (students.size() != 0) {
                if (students.get(0).status == -1 || students.get(0).status == 3 || students.get(0).status == 4 || students.get(0).status == 5) {
                    if (DateUtil.compare_date_two(time, WiscApplication.prefs.getCourseStartTime()) <= 0) {

                        if (students.get(0).status == 4 || students.get(0).status == 5) {
                            mMvpView.faceDaKaXinXi(students.get(0).picture, PromptDialog.DAO, true);
                        } else {
                            mMvpView.faceDaKaXinXi(students.get(0).picture, PromptDialog.DAO, false);
                        }
                        students.get(0).setStatus(1);
                        students.get(0).setAttendancedate(time);

                        try {
                            studentDao.update(students.get(0));
                        } catch (Exception e) {
                        }

                        List<AttendanceData> attendanceDatas = attendanceDataDao.queryBuilder()
                                .where(AttendanceDataDao.Properties.CardNumber.eq(cardNumber), AttendanceDataDao.Properties.PeriodId.eq(WiscApplication.prefs.getPeriodId()))
                                .orderAsc(AttendanceDataDao.Properties.CardNumber)
                                .list();

                        if (attendanceDatas.size() != 0) {
                            attendanceDatas.get(0).setStatus(1);
                            attendanceDatas.get(0).setAttendancedate(time);
                            try {
                                attendanceDataDao.update(attendanceDatas.get(0));
                            } catch (Exception e) {
                            }
                        }

                        attendanceDataTwos.add(new AttendanceDataTwo(WiscApplication.prefs.getActivityCourseId(),
                                WiscApplication.prefs.getPeriodId(),
                                students.get(0).attendancedate,
                                (int) students.get(0).attendanceId,
                                students.get(0).status,
                                students.get(0).cardNumber
                        ));

                        faceUploadRollCallResult(attendanceDataTwos);

                    } else if (DateUtil.compare_date_two(time, WiscApplication.prefs.getCourseStartTime()) > 0) {

                        if (students.get(0).status == 4 || students.get(0).status == 5) {
                            mMvpView.faceDaKaXinXi(students.get(0).picture, PromptDialog.CHIDAO, true);
                        } else {
                            mMvpView.faceDaKaXinXi(students.get(0).picture, PromptDialog.CHIDAO, false);
                        }

                        students.get(0).setStatus(2);
                        students.get(0).setAttendancedate(time);
                        try {
                            studentDao.update(students.get(0));
                        } catch (Exception e) {
                        }
                        List<AttendanceData> attendanceDatas = attendanceDataDao.queryBuilder()
                                .where(AttendanceDataDao.Properties.CardNumber.eq(cardNumber), AttendanceDataDao.Properties.PeriodId.eq(WiscApplication.prefs.getPeriodId()))
                                .orderAsc(AttendanceDataDao.Properties.CardNumber)
                                .list();

                        if (attendanceDatas.size() != 0) {
                            attendanceDatas.get(0).setStatus(2);
                            attendanceDatas.get(0).setAttendancedate(time);
                            try {
                                attendanceDataDao.update(attendanceDatas.get(0));
                            } catch (Exception e) {
                            }
                        }

                        attendanceDataTwos.add(new AttendanceDataTwo(WiscApplication.prefs.getActivityCourseId(),
                                WiscApplication.prefs.getPeriodId(),
                                students.get(0).attendancedate,
                                (int) students.get(0).attendanceId,
                                students.get(0).status,
                                students.get(0).cardNumber
                        ));
                        faceUploadRollCallResult(attendanceDataTwos);

                    }
                } else if (students.get(0).status == 1 || students.get(0).status == 2) {
                    mMvpView.faceDaKaXinXi(students.get(0).picture, PromptDialog.CHONGFU, false);
                }
            } else {
                mMvpView.faceDaKaXinXi(null, PromptDialog.CUOWU, false);
            }
        }
    }

    private void faceUploadRollCallResult(List<AttendanceDataTwo> attendanceDatas) {

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
                        faceAttendanceDisposable.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        faceAttendanceDisposable = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        faceAttendanceDisposable.dispose();
                        ELog.i("===========刷卡签到一起提交======onError========" + e.toString());
                        mMvpView.faceErrorMessage(e);
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
                        mMvpView.faceDakaOK();
                        ELog.i("============刷卡签到一起提交======onNext==22=====" + attendanceDataDao.loadAll().size());
                    }
                });
    }


}
