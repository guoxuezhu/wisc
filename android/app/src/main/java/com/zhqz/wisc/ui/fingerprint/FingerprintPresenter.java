package com.zhqz.wisc.ui.fingerprint;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.DbDao.AttendanceDataDao;
import com.zhqz.wisc.data.DbDao.FingerUsersDao;
import com.zhqz.wisc.data.DbDao.StudentDao;
import com.zhqz.wisc.data.DbDao.TeacherDao;
import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.AttendanceData;
import com.zhqz.wisc.data.model.AttendanceDataTwo;
import com.zhqz.wisc.data.model.FingerUsers;
import com.zhqz.wisc.data.model.Student;
import com.zhqz.wisc.data.model.Teacher;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.ui.main.MainPresenter;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.PromptDialog;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.CancellableDisposable;

public class FingerprintPresenter implements Presenter<FingerprintMvpView> {


    private WiscClient wiscClient;
    private FingerprintMvpView mMvpView;
    private Disposable fingerAttendanceDisposable;
    private Disposable sendTemplateDataDisposable;


    @Inject
    public FingerprintPresenter(WiscClient wiscClient) {
        this.wiscClient = wiscClient;
    }

    @Override
    public void attachView(FingerprintMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (fingerAttendanceDisposable != null && !fingerAttendanceDisposable.isDisposed()) {
            fingerAttendanceDisposable.dispose();
        }
        if (sendTemplateDataDisposable != null && !sendTemplateDataDisposable.isDisposed()) {
            sendTemplateDataDisposable.dispose();
        }
    }


    public void sendTemplateData(int psType, int stid, String m_TemplateData) {
        wiscClient.sendTemplateData1(psType, stid, m_TemplateData)
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        sendTemplateDataDisposable = d;
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        ELog.i("==============11===onNext=sendTemplateData=======" + httpResult.toString());
                        if (httpResult.code.equals("200")) {
                            mMvpView.sendTemplateDatas(true,null);
                        } else {
                            mMvpView.sendTemplateDatas(false,httpResult.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        sendTemplateDataDisposable.dispose();
                        ELog.i("==============11===onError=sendTemplateData=======" + e.toString());
                        mMvpView.sendTemplateDatas(false,e.toString());
                    }

                    @Override
                    public void onComplete() {
                        sendTemplateDataDisposable.dispose();
                    }
                });
    }

    public void showMessage(int msg, int id) {
        if (mMvpView != null) {
            mMvpView.showMessage(msg, id);
        }
    }

    /*
    * 发送指纹数据
    * */

    public void sendZhiwenData(String str) {
        mMvpView.sendZhiwenDatas(str);
    }

    /*
    * 卡号和时间签到
    * */
    public void daka(int fingerId) {
        FingerUsersDao fingersDao = WiscApplication.getDaoSession().getFingerUsersDao();
        if (fingersDao != null && fingersDao.loadAll().size() != 0) {
            List<FingerUsers> fingerUser = fingersDao.queryBuilder()
                    .where(FingerUsersDao.Properties.Id.eq(fingerId))
                    .orderAsc(FingerUsersDao.Properties.Id)
                    .list();

            if (fingerUser.size() != 0) {
                dakaqiandao(fingerUser.get(0).number, DateUtil.getCurrentTime());
            }
        }
    }

    private void dakaqiandao(String cardNumber, String time) {
        List<AttendanceDataTwo> attendanceDataTwos = new ArrayList<AttendanceDataTwo>();
        attendanceDataTwos.clear();

        StudentDao studentDao = WiscApplication.getDaoSession().getStudentDao();
        TeacherDao teacherDao = WiscApplication.getDaoSession().getTeacherDao();
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
                            mMvpView.fingerDaKaXinXi(students.get(0).picture, PromptDialog.DAO, students.get(0).studentName, true);
                        } else {
                            mMvpView.fingerDaKaXinXi(students.get(0).picture, PromptDialog.DAO, students.get(0).studentName, false);
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
                        fingerUploadRollCallResult(attendanceDataTwos);
                    } else if (DateUtil.compare_date_two(time, WiscApplication.prefs.getCourseStartTime()) > 0) {
                        if (students.get(0).status == 4 || students.get(0).status == 5) {
                            mMvpView.fingerDaKaXinXi(students.get(0).picture, PromptDialog.CHIDAO, students.get(0).studentName, true);
                        } else {
                            mMvpView.fingerDaKaXinXi(students.get(0).picture, PromptDialog.CHIDAO, students.get(0).studentName, false);
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
                        fingerUploadRollCallResult(attendanceDataTwos);
                    }
                } else if (students.get(0).status == 1 || students.get(0).status == 2) {
                    mMvpView.fingerDaKaXinXi(students.get(0).picture, PromptDialog.CHONGFU, students.get(0).studentName, false);
                }
            } else if (teacherDao != null && teacherDao.loadAll().size() != 0) {
                List<Teacher> teachers = teacherDao.queryBuilder()
                        .where(TeacherDao.Properties.CardNumber.eq(cardNumber))
                        .orderAsc(TeacherDao.Properties.CardNumber)
                        .list();
                if (teachers.size() != 0) {
                    if (teachers.get(0).status == -1 || teachers.get(0).status == 3 || teachers.get(0).status == 4 || teachers.get(0).status == 5) {
                        if (DateUtil.compare_date_two(time, WiscApplication.prefs.getCourseStartTime()) <= 0) {
                            if (teachers.get(0).status == 4 || teachers.get(0).status == 5) {
                                mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.DAO, teachers.get(0).teacherName, true);
                            } else {
                                mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.DAO, teachers.get(0).teacherName, false);
                            }
                            teachers.get(0).setStatus(1);
                            teachers.get(0).setAttendancedate(time);
                            try {
                                teacherDao.update(teachers.get(0));
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
                                    teachers.get(0).attendancedate,
                                    (int) teachers.get(0).attendanceId,
                                    teachers.get(0).status,
                                    teachers.get(0).cardNumber
                            ));
                            fingerUploadRollCallResult(attendanceDataTwos);
                        } else if (DateUtil.compare_date_two(time, WiscApplication.prefs.getCourseStartTime()) > 0) {
                            if (teachers.get(0).status == 4 || teachers.get(0).status == 5) {
                                mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.CHIDAO, teachers.get(0).teacherName, true);
                            } else {
                                mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.CHIDAO, teachers.get(0).teacherName, false);
                            }
                            teachers.get(0).setStatus(2);
                            teachers.get(0).setAttendancedate(time);
                            try {
                                teacherDao.update(teachers.get(0));
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
                                    teachers.get(0).attendancedate,
                                    (int) teachers.get(0).attendanceId,
                                    teachers.get(0).status,
                                    teachers.get(0).cardNumber
                            ));
                            fingerUploadRollCallResult(attendanceDataTwos);
                        }
                    } else if (teachers.get(0).status == 1 || teachers.get(0).status == 2) {
                        mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.CHONGFU, teachers.get(0).teacherName, false);
                    }
                } else {
                    mMvpView.fingerDaKaXinXi(null, PromptDialog.CUOWU, null, false);
                }
            } else {
                mMvpView.fingerDaKaXinXi(null, PromptDialog.CUOWU, null, false);
            }
        } else if (teacherDao != null && teacherDao.loadAll().size() != 0) {
            List<Teacher> teachers = teacherDao.queryBuilder()
                    .where(TeacherDao.Properties.CardNumber.eq(cardNumber))
                    .orderAsc(TeacherDao.Properties.CardNumber)
                    .list();

            if (teachers.size() != 0) {
                if (teachers.get(0).status == -1 || teachers.get(0).status == 3 || teachers.get(0).status == 4 || teachers.get(0).status == 5) {
                    if (DateUtil.compare_date_two(time, WiscApplication.prefs.getCourseStartTime()) <= 0) {
                        if (teachers.get(0).status == 4 || teachers.get(0).status == 5) {
                            mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.DAO, teachers.get(0).teacherName, true);
                        } else {
                            mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.DAO, teachers.get(0).teacherName, false);
                        }
                        teachers.get(0).setStatus(1);
                        teachers.get(0).setAttendancedate(time);
                        try {
                            teacherDao.update(teachers.get(0));
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
                                teachers.get(0).attendancedate,
                                (int) teachers.get(0).attendanceId,
                                teachers.get(0).status,
                                teachers.get(0).cardNumber
                        ));
                        fingerUploadRollCallResult(attendanceDataTwos);
                    } else if (DateUtil.compare_date_two(time, WiscApplication.prefs.getCourseStartTime()) > 0) {
                        if (teachers.get(0).status == 4 || teachers.get(0).status == 5) {
                            mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.CHIDAO, teachers.get(0).teacherName, true);
                        } else {
                            mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.CHIDAO, teachers.get(0).teacherName, false);
                        }
                        teachers.get(0).setStatus(2);
                        teachers.get(0).setAttendancedate(time);
                        try {
                            teacherDao.update(teachers.get(0));
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
                                teachers.get(0).attendancedate,
                                (int) teachers.get(0).attendanceId,
                                teachers.get(0).status,
                                teachers.get(0).cardNumber
                        ));
                        fingerUploadRollCallResult(attendanceDataTwos);
                    }
                } else if (teachers.get(0).status == 1 || teachers.get(0).status == 2) {
                    mMvpView.fingerDaKaXinXi(teachers.get(0).picture, PromptDialog.CHONGFU, teachers.get(0).teacherName, false);
                }
            } else {
                mMvpView.fingerDaKaXinXi(null, PromptDialog.CUOWU, null, false);
            }
        } else {
            mMvpView.fingerDaKaXinXi(null, PromptDialog.CUOWU, null, false);
        }

    }


    private void fingerUploadRollCallResult(List<AttendanceDataTwo> attendanceDatas) {

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
                        fingerAttendanceDisposable.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        fingerAttendanceDisposable = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        fingerAttendanceDisposable.dispose();
                        ELog.i("===========刷卡签到一起提交======onError========" + e.toString());
                        mMvpView.fingerErrorMessage(e);
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
                        mMvpView.fingerDakaOK();
                        ELog.i("============刷卡签到一起提交======onNext==22=====" + attendanceDataDao.loadAll().size());
                    }
                });
    }


}
