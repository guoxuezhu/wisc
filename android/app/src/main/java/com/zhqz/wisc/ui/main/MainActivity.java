package com.zhqz.wisc.ui.main;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.don.clockviewlibrary.ClockView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;
import com.sprylab.android.widget.TextureVideoView;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.DbDao.AttendanceDataDao;
import com.zhqz.wisc.data.DbDao.CourseDao;
import com.zhqz.wisc.data.DbDao.CourseTwoDao;
import com.zhqz.wisc.data.DbDao.FingerTeacherUsersDao;
import com.zhqz.wisc.data.DbDao.FingerUsersDao;
import com.zhqz.wisc.data.DbDao.StudentDao;
import com.zhqz.wisc.data.DbDao.TeacherDao;
import com.zhqz.wisc.data.DbDao.ZuoPinDbDao;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.AttendanceData;
import com.zhqz.wisc.data.model.AttendanceDataTwo;
import com.zhqz.wisc.data.model.AttendanceDetails;
import com.zhqz.wisc.data.model.CabinetInfo;
import com.zhqz.wisc.data.model.CabinetPerson;
import com.zhqz.wisc.data.model.Course;
import com.zhqz.wisc.data.model.CourseTwo;
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
import com.zhqz.wisc.data.model.ZuoPin;
import com.zhqz.wisc.data.model.ZuoPinDb;
import com.zhqz.wisc.mqtt.MqttMessageEvent;
import com.zhqz.wisc.speech.util.FucUtil;
import com.zhqz.wisc.speech.util.JsonParser;
import com.zhqz.wisc.ui.FingerprintEntry.FingerprintEntryActivity;
import com.zhqz.wisc.ui.adapter.EightNoticeAdapter;
import com.zhqz.wisc.ui.adapter.ImageAdapter;
import com.zhqz.wisc.ui.adapter.ImageViewPagerAdapter;
import com.zhqz.wisc.ui.adapter.NoticeAdapter;
import com.zhqz.wisc.ui.adapter.ZuopinImageAdapter;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.faceScann.FaceScannActivity;
import com.zhqz.wisc.ui.fingerprint.FingerprintActivity;
import com.zhqz.wisc.ui.fingerprint.fingerprintUtils.FingerUtil;
import com.zhqz.wisc.ui.scene.SceneActivity;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.ui.selectClass.SelectClassActivity;
import com.zhqz.wisc.ui.splash.SplashActivity;
import com.zhqz.wisc.ui.view.MyClockView;
import com.zhqz.wisc.ui.view.MyTextView;
import com.zhqz.wisc.utils.AllCourseDialog;
import com.zhqz.wisc.utils.AttendanceDialog;
import com.zhqz.wisc.utils.CabintDialog;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.DisplayTools;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.FileSizeUtil;
import com.zhqz.wisc.utils.FileUtils;
import com.zhqz.wisc.utils.KeChengDialog;
import com.zhqz.wisc.utils.LeaveDialog;
import com.zhqz.wisc.utils.MealsDialog;
import com.zhqz.wisc.utils.NoDataDialog;
import com.zhqz.wisc.utils.PromptDialog;
import com.zhqz.wisc.utils.ThisClassStudensDialog;
import com.zhqz.wisc.utils.TimeThread;
import com.zhqz.wisc.utils.TimerUtils;
import com.zhqz.wisc.utils.UpdateManger1;
import com.zhqz.wisc.utils.VoideDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import android_serialport_api.DeviceMonitorService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.xhinliang.lunarcalendar.LunarCalendar;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.zhqz.wisc.WiscApplication.context;


public class MainActivity extends BaseActivity implements MainMvpView, ViewPager.OnPageChangeListener,
        EightNoticeAdapter.CallBack, NoticeAdapter.CallBack, ZuopinImageAdapter.CallBack,
        ImageAdapter.CallBack, LeaveDialog.DialogCallback, CabintDialog.DialogCallback {
    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.tv_weather)
    TextView weather;//天气状况

    @BindView(R.id.image_weather)
    ImageView imageWeather;//天气图片

    @BindView(R.id.image_banhui)
    ImageView image_banhui;

    @BindView(R.id.tv_wind)
    TextView tv_wind;//风向
    @BindView(R.id.tv_temperature)
    TextView tv_temperature;//温度
    @BindView(R.id.tv_pm)
    TextView tv_pm;
    @BindView(R.id.tv_air_quality)
    TextView tv_air_quality;

    @BindView(R.id.tishi_shuaka)
    TextView tishi_shuaka;
    @BindView(R.id.benban_tishi_shuaka)
    TextView benban_tishi_shuaka;
    @BindView(R.id.meals_tishi_shuaka)
    TextView meals_tishi_shuaka;
    @BindView(R.id.leave_tishi_shuaka)
    TextView leave_tishi_shuaka;
    @BindView(R.id.cabint_tishi_shuaka)
    TextView cabint_tishi_shuaka;
    @BindView(R.id.scene_tishi_shuaka)
    TextView scene_tishi_shuaka;

    @BindView(R.id.kaoqin_xiangxi)
    LinearLayout kaoqin_xiangxi;

    @BindView(R.id.main_mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.sliding_menu_linearlayout)
    LinearLayout sliding_menu_linearlayout;

    @BindView(R.id.closed_time)
    TextView closed_time;
    @BindView(R.id.close_image)
    ImageView close_image;

    @BindView(R.id.kaoqin_zong_tv)
    TextView kaoqin_zong_tv;
    @BindView(R.id.kaoqin_dao_tv)
    TextView kaoqin_dao_tv;
    @BindView(R.id.kaoqin_chidao_tv)
    TextView kaoqin_chidao_tv;
    @BindView(R.id.kaoqin_queqin_tv)
    TextView kaoqin_queqin_tv;
    @BindView(R.id.kaoqin_qingjia_tv)
    TextView kaoqin_qingjia_tv;

    @BindView(R.id.course_tv)
    MyTextView course_tv;
    @BindView(R.id.course_layout)
    FrameLayout course_layout;

    @BindView(R.id.course_name)
    TextView course_name;
    @BindView(R.id.course_teacher_name)
    TextView course_teacher_name;
    @BindView(R.id.course_time)
    TextView course_time;
    @BindView(R.id.course_juli_endtime)
    TextView course_juli_endtime;

    @BindView(R.id.LL1)
    LinearLayout LL1;
    @BindView(R.id.LL2)
    LinearLayout LL2;

    @BindView(R.id.classroom)
    TextView classroom_tv;
    @BindView(R.id.eg_classroom)
    TextView eg_classroom_tv;

    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.clockView)
    MyClockView clockView;

    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_nongli)
    TextView tv_nongli;

    @BindView(R.id.image_viewPager)
    ViewPager image_viewPager;
    @BindView(R.id.surface_view1)
    TextureVideoView videoView;
    @BindView(R.id.video_layout)
    RelativeLayout video_layout;
    @BindView(R.id.list_view1)
    RecyclerView listView;
    @BindView(R.id.list_view2)
    RecyclerView listView2;
    @BindView(R.id.LL_shipin)
    LinearLayout LL_shipin;
    @BindView(R.id.LL_tupian)
    LinearLayout LL_tupian;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.seekBar_FrameLayout)
    FrameLayout seekBar_FrameLayout;
    @BindView(R.id.current_time)
    TextView current_time;
    @BindView(R.id.duration_time)
    TextView duration_time;

    @BindView(R.id.course_detail_line)
    LinearLayout course_detail_line;
    @BindView(R.id.teacher_detail_line)
    LinearLayout teacher_detail_line;

    @BindView(R.id.course_content)
    TextView course_content;

    @BindView(R.id.teacher_detail_name)
    TextView teacher_detail_name;
    @BindView(R.id.teacher_detail)
    TextView teacher_detail;
    @BindView(R.id.llayout)
    LinearLayout llayout;

    @BindView(R.id.RL2)
    RelativeLayout RL2;
    @BindView(R.id.RL3)
    RelativeLayout RL3;
    @BindView(R.id.RL_notice)
    RelativeLayout RL_notice;
    @BindView(R.id.RL_notice_content)
    RelativeLayout RL_notice_content;
    @BindView(R.id.notice_ListView)
    RecyclerView notice_ListView;
    @BindView(R.id.eightnotice_Listview)
    RecyclerView eightnotice_Listview;
    @BindView(R.id.LL3)
    LinearLayout LL3;
    @BindView(R.id.RL_notice_content_two)
    RelativeLayout RL_notice_content_two;
    @BindView(R.id.notice_close)
    TextView notice_close;
    @BindView(R.id.webview_one)
    WebView webview_one;
    @BindView(R.id.webview_two)
    WebView webview_two;

    private ActionBarDrawerToggle mDrawerToggle;
    Courses coursesList;
    public static boolean openFingerDevice = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WiscClient.COURSE_HANDLER:
                    long newTime = System.currentTimeMillis();
                    if (DateUtil.compare_date(newTime, course.getStartTime()) > 0) {
                        course_name.setText("下一节课: " + course.getName());
                        course_teacher_name.setText("任课老师: " + course.getTeacherName());
                        course_time.setText("上课时间: " + course.getStartTime().substring(10).trim() + " - " + course.getEndTime().substring(10).trim());
                        course_juli_endtime.setText("距离上课: " + DateUtil.compare_date(newTime, course.getStartTime()) / 1000 / 60 + "分钟");
                    } else if (DateUtil.compare_date(newTime, course.getStartTime()) <= 0
                            && DateUtil.compare_date(newTime, course.getEndTime()) > 0) {
                        course_name.setText("正在上课: " + course.getName());
                        course_teacher_name.setText("任课老师: " + course.getTeacherName());
                        course_time.setText("上课时间: " + course.getStartTime().substring(10).trim() + " - " + course.getEndTime().substring(10).trim());
                        course_juli_endtime.setText("距离下课: " + DateUtil.compare_date(newTime, course.getEndTime()) / 1000 / 60 + "分钟");
                    } else if (DateUtil.compare_date(newTime, course.getEndTime()) <= 0) {
                        mainPresenter.sendshibaiAttendanceData();
                        course_juli_endtime.setText("距离下课: " + 0 + "分钟");
                        position = position + 1;
                        if (position < courseDao.loadAll().size()) {
                            setCourseData();
                        } else {
                            position = 0;
                            TimerUtils.stopTimer();
                            setMeiKeView();
                        }

                    }
                    break;
                case WiscClient.CLOSED_DRAWER_HANDLER:
                    int miao = (int) msg.obj;
                    closed_time.setText(miao + "");
                    if (miao == 0 && mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
                        mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
                    }
                    break;
                case WiscClient.ZUOPIN_HANDLER:
                    if (zuoPins.size() != 0 && zuoPins2.size() == 0) {
                        if (index >= listView.getAdapter().getItemCount()) {
                            index = 0;
                        }
                        listView.smoothScrollToPosition(index);
                        imageAdapter.changeSelected(index);
                        SetImageAndVideo(zuoPins.get(index), index);
                        index += 1;
                    } else if (zuoPins.size() != 0 && zuoPins2.size() != 0) {
                        if (index < listView.getAdapter().getItemCount()) {
                            listView.smoothScrollToPosition(index);
                            imageAdapter.changeSelected(index);
                            SetImageAndVideo(zuoPins.get(index), index);
                            index += 1;
                        } else if (index == listView.getAdapter().getItemCount()) {
                            listView2.smoothScrollToPosition(index - listView.getAdapter().getItemCount());
                            imageAdapter2.changeSelected(index - listView.getAdapter().getItemCount());
                            SetImageAndVideo(zuoPins2.get(index - listView.getAdapter().getItemCount()), index);
                            index += 1;
                        } else if (index > listView.getAdapter().getItemCount() &&
                                index < listView.getAdapter().getItemCount() + listView2.getAdapter().getItemCount()) {
                            listView2.smoothScrollToPosition(index - listView.getAdapter().getItemCount());
                            imageAdapter2.changeSelected(index - listView.getAdapter().getItemCount());
                            SetImageAndVideo(zuoPins2.get(index - listView.getAdapter().getItemCount()), index);
                            index += 1;
                        } else if (index >= listView.getAdapter().getItemCount() + listView2.getAdapter().getItemCount()) {
                            index = 0;
                            listView.smoothScrollToPosition(index);
                            imageAdapter.changeSelected(index);
                            SetImageAndVideo(zuoPins.get(index), index);
                            index += 1;
                        }
                    } else if (zuoPins.size() == 0 && zuoPins2.size() != 0) {
                        if (index >= listView2.getAdapter().getItemCount()) {
                            index = 0;
                        }
                        listView2.smoothScrollToPosition(index);
                        imageAdapter2.changeSelected(index);
                        SetImageAndVideo(zuoPins2.get(index), index);
                        index += 1;
                    }
                    break;
                case WiscClient.KAOQIN_TONGJI_HANDLER:
                    if (studentDao != null && studentDao.loadAll().size() != 0) {
                        kaoqin_zong_tv.setText(studentDao.loadAll().size() + "");
                        kaoqin_dao_tv.setText(dao + "");
                        kaoqin_chidao_tv.setText(chidao + "");
                        kaoqin_queqin_tv.setText(queqin + "");
                        kaoqin_qingjia_tv.setText(qingjia + "");
                    } else if (studentDao != null) {
                        kaoqin_zong_tv.setText(studentDao.loadAll().size() + "");
                        kaoqin_dao_tv.setText(dao + "");
                        kaoqin_chidao_tv.setText(chidao + "");
                        kaoqin_queqin_tv.setText(queqin + "");
                        kaoqin_qingjia_tv.setText(qingjia + "");
                    }
                    break;
                case WiscClient.SHIPIN_SEEKBAR_HANDLER:
                    try {
                        int currentPosition = videoView.getCurrentPosition();
                        int duration = videoView.getDuration();
                        current_time.setText(DateUtil.getHHmmss_time(currentPosition + 1000));
                        seekBar.setProgress(currentPosition * 100 / duration);
                    } catch (Exception e) {
                        current_time.setText("00:00");
                        seekBar.setProgress(0);
                    }
                    break;
                case WiscClient.DUKAHAO_HANDLER:
                    String kahao = msg.obj.toString();
                    ELog.i("=====kahao===" + kahao);
                    huoqukahao(kahao);
                    break;
                case WiscClient.NONGLI_HANDLER:
                    tv_date.setText(DateUtil.getTimeyyyyMMdd() + " " + DateUtil.getWeek());
                    LunarCalendar lunarCalender = LunarCalendar.getInstance(DateUtil.getYEAR(), DateUtil.getMONTH(), DateUtil.getDAY_OF_MONTH());
                    tv_nongli.setText("农历 " + lunarCalender.getFullLunarStr());
                    break;
                case WiscClient.fail:
                    Toast.makeText(MainActivity.this,"失败,请重新识别或你没有录入指纹",Toast.LENGTH_SHORT).show();
                    break;
                case WiscClient.tishi:
                    Toast.makeText(MainActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case WiscClient.FingerStudent:
                    dakaqiandao(msg.obj.toString(),DateUtil.getCurrentTime());
                    break;
                case WiscClient.FINGER_DEVICE_HANDLER:
                    ELog.i("====FingerUtil====2222222222==打开指纹设备成功==111112222==");
                    if (msg.arg1 == 0) {
                        Toast.makeText(MainActivity.this, "打开指纹设备失败，请同意USB权限", Toast.LENGTH_SHORT).show();
                        openFingerDevice = false;
                    } else if (msg.arg1 == 1) {
                        Toast.makeText(MainActivity.this, "打开指纹设备成功12", Toast.LENGTH_SHORT).show();
                        if (WiscClient.isWriteFinger) {
//                            fingerUtil.Run_CmdDeleteAll();
                            fingerTeacherUsersDao.deleteAll();
                            fingersDao.deleteAll();
                            mainPresenter.getFingerUsers(WiscClient.isWriteFinger);
//                            mainPresenter.getFingerTeacherUserList(WiscClient.isWriteFinger);
                            isRunWriteTemplate = true;
                        } else {
                            isRunWriteTemplate = false;
                        }
                        openFingerDevice = true;
                    } else if (msg.arg1 == 2) {
                        Toast.makeText(MainActivity.this, "指纹权限被拒绝，无法使用指纹识别", Toast.LENGTH_SHORT).show();
                        openFingerDevice = false;
                    } else if (msg.arg1 == 3) {
                        Toast.makeText(MainActivity.this, "找不到指纹USB设备", Toast.LENGTH_SHORT).show();
                        openFingerDevice = false;
                    } else if (msg.arg1 == 66){
                        Toast.makeText(MainActivity.this, "Fail to receive response!Please check the connection to target", Toast.LENGTH_SHORT).show();
//                        setAgainOpen();
//                        openFingerDevice = false;
                    } else {
                        Toast.makeText(MainActivity.this, "无法连接到指纹设备", Toast.LENGTH_SHORT).show();
//                        setAgainOpen();
                        openFingerDevice = false;
                    }
                    break;
            }


        }
    };

    private void setAgainOpen() {
//        fingerUtil = new FingerUtil(this, handler,mainPresenter);
        fingerUtil.OpenDevice();
    }


    private StudentDao studentDao;
    //    private TeacherDao teacherDao;
    private Handler myHandler;
    private Runnable runnable;
    private CourseTwoDao courseDaoTwo;
    private NoticeAdapter noticeAdapter;
    private Timer videotimer;
    private List<NoticeList> noticeList;
    private List<NoticeList> eightNotices;
    private ZuoPinDbDao zuoPinDbDao;
    private Handler chaxunHandler;
    private Runnable chaxunRunnable;
    private DownloadManager downloadManager;//获取下载管理器
    private Scheduler.Worker bindRoomworker = Schedulers.io().createWorker();
    private String CabintcardNumber = "";
    private CabintDialog cabintDialog;
    private AttendanceDialog attendanceDialog;
    private FingerUsersDao fingersDao;
    private FingerTeacherUsersDao fingerTeacherUsersDao;
    private FingerUtil fingerUtil;
    private Scheduler.Worker fingerworker = Schedulers.io().createWorker();
    private Scheduler.Worker fingerteacherworker = Schedulers.io().createWorker();
    private Scheduler.Worker tese = Schedulers.io().createWorker();
    private boolean isRunWriteTemplate = false;
    private PromptDialog kaoqinDialog;

    /*
     * 获取卡号
     * */
    private void huoqukahao(String kahao) {
        if (kahao != null && kahao.length() == 10) {
            if (kaoqinDialog != null && kaoqinDialog.isShowing()) {
                kaoqinDialog.dismiss();
            }
            if (attendanceDialog != null && attendanceDialog.isShowing()) {
                attendanceDialog.dismiss();
            }
            if (allCourseDialog != null && allCourseDialog.isShowing()) {
                allCourseDialog.dismiss();
            }
            if (keChengDialog != null && keChengDialog.isShowing()) {
                keChengDialog.dismiss();
            }
            if (thisclassstudensDialog != null && thisclassstudensDialog.isShowing()) {
                thisclassstudensDialog.dismiss();
            }
            if (mealsDialog != null && mealsDialog.isShowing()) {
                mealsDialog.dismiss();
            }
            if (leaveDialog != null && leaveDialog.isShowing()) {
                leaveDialog.dismiss();
            }
            if (cabintDialog != null && cabintDialog.isShowing()) {
                cabintDialog.dismiss();
            }
            if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
                mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            }

            if (WiscClient.isDaKaQianDao) {
                dakaqiandao(kahao.toString().trim(), DateUtil.getCurrentTime());
            } else if (WiscClient.isChaKanKeCheng) {//个人课程
                MainPresenter.getMyCourseList(kahao.toString().trim());
            } else if (WiscClient.isThisClassStudents) {
                //本班学生
                mainPresenter.getbenbanStudent(kahao.toString().trim());
            } else if (WiscClient.isMeals) {
                //余额
                mainPresenter.getMeals(kahao.toString().trim());
            } else if (WiscClient.isLeave) {
                //请假
                mainPresenter.getLeave(kahao.toString().trim());
            } else if (WiscClient.isSetting) {
                //设置界面
                mainPresenter.getSeting(kahao.toString().trim(), 1);
            } else if (WiscClient.isCabint) {
                //柜子
                mainPresenter.getCabinte(kahao.toString().trim());//2134242444
            } else if (WiscClient.isScene) {
                //场景切换
                ELog.i("==========场景切换=====00000====");
                mainPresenter.getSeting(kahao.toString().trim(), 2);
            } else {
                //Toast.makeText(MainActivity.this, "此时无课或本节课不需要打卡考勤", Toast.LENGTH_LONG).show();
                mainPresenter.teacherKaoqin(kahao.toString().trim(),0,0);
            }

        }
    }

    /*
     * 打卡签到
     *
     * */

    private void dakaqiandao(String cardNumber, String time) {
        ELog.i("==========dakaqiandao=====00000====" + cardNumber);
        List<AttendanceDataTwo> attendanceDataTwos = new ArrayList<AttendanceDataTwo>();
        attendanceDataTwos.clear();

        if (studentDao != null && studentDao.loadAll().size() != 0) {
            List<Student> students = studentDao.queryBuilder()
                    .where(StudentDao.Properties.CardNumber.eq(cardNumber))
                    .orderAsc(StudentDao.Properties.CardNumber)
                    .list();
            if (students.size() != 0) {
                if (DateUtil.compare_date(System.currentTimeMillis(), course.getStartTime()) <= 15 * 1000 * 60) {
                    if (students.get(0).status == -1 || students.get(0).status == 3 || students.get(0).status == 4 || students.get(0).status == 5) {
                        if (DateUtil.compare_date_two(time, course.startTime) <= 0) {

                            if (students.get(0).status == 4 || students.get(0).status == 5) {
                                showDaKaXinXi(students.get(0).picture, PromptDialog.DAO, false, true);
                            } else {
                                showDaKaXinXi(students.get(0).picture, PromptDialog.DAO, false, false);
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

                            attendanceDataTwos.add(new AttendanceDataTwo(course.activityCourseId,
                                    course.periodId,
                                    students.get(0).attendancedate,
                                    (int) students.get(0).attendanceId,
                                    students.get(0).status,
                                    students.get(0).cardNumber
                            ));
                            MainPresenter.uploadRollCallResult(attendanceDataTwos);
                        } else if (DateUtil.compare_date_two(time, course.startTime) > 0) {
                            if (students.get(0).status == 4 || students.get(0).status == 5) {
                                showDaKaXinXi(students.get(0).picture, PromptDialog.CHIDAO, false, true);
                            } else {
                                showDaKaXinXi(students.get(0).picture, PromptDialog.CHIDAO, false, false);
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

                            attendanceDataTwos.add(new AttendanceDataTwo(course.activityCourseId,
                                    course.periodId,
                                    students.get(0).attendancedate,
                                    (int) students.get(0).attendanceId,
                                    students.get(0).status,
                                    students.get(0).cardNumber
                            ));
                            MainPresenter.uploadRollCallResult(attendanceDataTwos);
                        }
                    } else if (students.get(0).status == 1 || students.get(0).status == 2) {
                        showDaKaXinXi(students.get(0).picture, PromptDialog.CHONGFU, false, false);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "请在上课前15分钟内打卡", Toast.LENGTH_LONG).show();
                }
            } else {
                mainPresenter.teacherKaoqin(cardNumber,0,0);
            }
        } else {
            mainPresenter.teacherKaoqin(cardNumber,0,0);
        }

    }

    private CourseDao courseDao;
    private int position = 0;
    private static Course course;//显示的课程
    private KeChengDialog keChengDialog;
    private int dao = 0;
    private int chidao = 0;
    private int queqin = 0;
    private int qingjia = 0;
    private List<ZuoPin> zuoPins;
    private ZuopinImageAdapter imageAdapter;
    private List<ZuoPin> zuoPins2;
    private ImageAdapter imageAdapter2;

    private int index = 0;//计时器
    private AttendanceDataDao attendanceDataDao;
    private AudioManager mAudioManager;
    private MainActivity baseActivity;
    EightNoticeAdapter eightNoticeAdapter;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityComponent().inject(this);
        mainPresenter.attachView(this);
        ButterKnife.bind(this);
        baseActivity = this;
        initStatus();
        EventBus.getDefault().register(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        setadapter();

        fingerTeacherUsersDao = WiscApplication.getDaoSession().getFingerTeacherUsersDao();
        fingersDao = WiscApplication.getDaoSession().getFingerUsersDao();
        fingerUtil = new FingerUtil(this, handler,mainPresenter);
        fingerUtil.OpenDevice();

//        settest();

        startMQTTService();
        TimeThread.getWeather(mainPresenter);
        setSlidingMenu();
        setserialport();


        clockView.setOnCurrentTimeListener(new ClockView.OnCurrentTimeListener() {
            @Override
            public void currentTime(String time) {
                tv_time.setText(time);
            }
        });
        tv_date.setText(DateUtil.getTimeyyyyMMdd() + " " + DateUtil.getWeek());
        LunarCalendar lunarCalender = LunarCalendar.getInstance(DateUtil.getYEAR(), DateUtil.getMONTH(), DateUtil.getDAY_OF_MONTH());
        tv_nongli.setText("农历 " + lunarCalender.getFullLunarStr());

        mainPresenter.getbindRoom(false);
        mainPresenter.VersionCheck();
        TimeThread.getCourses(handler, mainPresenter);

//        initvoide();


    }

//    private void initvoide() {
//        // 初始化识别对象
//        mAsr = SpeechRecognizer.createRecognizer(this, mInitListener);
//
//        // 初始化语法、命令词
//        mLocalLexicon = "张海羊\n刘婧\n王锋\n";
//        mLocalGrammar = FucUtil.readFile(this, "call.bnf", "utf-8");
//
//        ELog.d("====mLocalGrammar加载本地文件call.bnf=====" + mLocalGrammar + "======" + WiscApplication.prefs.getIsInitSyntax());
//        if (WiscApplication.prefs.getIsInitSyntax()) {
//            ELog.d("====mLocalGrammar加载本地文件call.bnf=1======" + WiscApplication.prefs.getIsInitSyntax());
//        } else {
//            ELog.d("====mLocalGrammar加载本地文件call.bnf=2===" + WiscApplication.prefs.getIsInitSyntax());
//            localGrammar();
//        }
//    }

    private void settest() {
        ELog.e("====tjj指纹有数据===" +WiscApplication.getDaoSession().getFingerTeacherUsersDao().loadAll().size() + openFingerDevice);
        if (openFingerDevice == true) {
            ELog.e("====tjj指纹没有数据==11111=" );
            fingerUtil.Run_CmdIdentifyFree();
        } else {
            ELog.e("====tjj指纹没有数据===" );
        }

    }


    private void initStatus() {
        WiscClient.isCabint = false;
        WiscClient.isDaKaQianDao = false;
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isScene = false;
        WiscClient.isFinger= false;
    }

    String mContent;// 语法、词典临时变量
    int ret = 0;// 函数调用返回值
    // 语音识别对象
    private SpeechRecognizer mAsr;
    // 缓存
    private SharedPreferences mSharedPreferences;
    // 本地语法文件
    private String mLocalGrammar = null;
    // 本地词典
    private String mLocalLexicon = null;
    // 云端语法文件
    private String mCloudGrammar = null;
    // 本地语法构建路径
    private String grmPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/msc/test";
    // 返回结果格式，支持：xml,json
    private String mResultType = "json";

    private final String KEY_GRAMMAR_ABNF_ID = "grammar_abnf_id";
    private final String GRAMMAR_TYPE_ABNF = "abnf";
    private final String GRAMMAR_TYPE_BNF = "bnf";
    private String mEngineType = SpeechConstant.TYPE_LOCAL;

    /*
     *
     * 本地语法构建
     * */
//    void localGrammar() {
//
//        if (null == mAsr) {
//            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
//            Toast.makeText(this, "创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化。", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 本地-构建语法文件，生成语法id
//        if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
//            mContent = new String(mLocalGrammar);
//            mAsr.setParameter(SpeechConstant.PARAMS, null);
//            // 设置文本编码格式
//            mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
//            // 设置引擎类型
//            mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
//            // 设置语法构建路径
//            mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
//            //使用8k音频的时候请解开注释
////					mAsr.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
//            // 设置资源路径
//            mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
//            ELog.d("===本地-构建语法文件===资源路径" + getResourcePath() + "\n" + "mContent" + mContent + "\n"
//                    + "语法构建路径" + grmPath);
//            ret = mAsr.buildGrammar(GRAMMAR_TYPE_BNF, mContent, grammarListener);
//            ELog.d("===本地-构建语法文件===ret=" + ret);
//            if (ret != ErrorCode.SUCCESS) {
//                ELog.d("===本地-构建语法文件===ret=语法构建失败,错误码：" + ret);
//            }
//        }
//        // 在线-构建语法文件，生成语法id
////        else {
////            mContent = new String(mCloudGrammar);
////            // 指定引擎类型
////            mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
////            // 设置文本编码格式
////            mAsr.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");
////            ret = mAsr.buildGrammar(GRAMMAR_TYPE_ABNF, mContent, grammarListener);
////            if(ret != ErrorCode.SUCCESS)
////                ELog.d("===本地-构建语法文件===ret=语法构建失败,错误码：" + ret);
////        }
//    }

    VoideDialog voideDialog;

    /*
     * 语音识别
     * */
    @OnClick(R.id.voide_navigation)
    void voide() {
        Toast.makeText(this, "暂时去除掉此功能 " , Toast.LENGTH_SHORT).show();
//        if (voideDialog == null) {
//            voideDialog = new VoideDialog(baseActivity);
//        }
//
//        if (null == mAsr) {
//            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
//            Toast.makeText(this, "创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化。", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        ELog.d("====开始识别按钮======");
//
//        // 设置参数
//        if (!setParam()) {
//            Toast.makeText(this, "请先构建语法。", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        ;
//        ret = mAsr.startListening(mRecognizerListener);
//        if (ret != ErrorCode.SUCCESS) {
//            Toast.makeText(this, "识别失败,错误码: " + ret, Toast.LENGTH_SHORT).show();
//        }
    }


//    private void xianshi(int errorCode) {
//        if (errorCode == 10114) {
//            Toast.makeText(this, "超时 ", Toast.LENGTH_SHORT).show();
//        } else if (errorCode == 20005) {
//            Toast.makeText(this, "无匹配结果 ", Toast.LENGTH_SHORT).show();
//        } else if (errorCode == 10109) {
//
//            Toast.makeText(this, "无效的数据 ", Toast.LENGTH_SHORT).show();
//        } else if (errorCode == 11212) {
//            Toast.makeText(this, "测试版本授权失败 ", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, errorCode + "", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * 构建语法监听器。
     */
//    private GrammarListener grammarListener = new GrammarListener() {
//        @Override
//        public void onBuildFinish(String grammarId, SpeechError error) {
//            ELog.d("===本地-构建语法监听器===grammarId=====" + grammarId + " ");
////            Toast.makeText(MainActivity.this, "构建语法监听器。" + grammarId, Toast.LENGTH_SHORT).show();
//            if (error == null) {
////                if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
////                    SharedPreferences.Editor editor = mSharedPreferences.edit();
////                    if(!TextUtils.isEmpty(grammarId))
////                        editor.putString(KEY_GRAMMAR_ABNF_ID, grammarId);
////                    editor.commit();
////                }
//                ELog.d("========语法构建成功：" + grammarId);
//                WiscApplication.prefs.setIsInitSyntax(true);
//            } else {
//                WiscApplication.prefs.setIsInitSyntax(false);
//                ELog.d("===本地-构建语法监听器===语法构建失败=====" + grammarId + " ===" + error.getErrorCode());
//                xianshi(error.getErrorCode());
//                ELog.d("=======语法构建失败,错误码：" + error.getErrorCode());
//            }
//        }
//    };
    /**
     * 初始化监听器。
     */
//    private InitListener mInitListener = new InitListener() {
//
//        @Override
//        public void onInit(int code) {
//            ELog.d("====SpeechRecognizer init() code = " + code);
//            if (code != ErrorCode.SUCCESS) {
//                ELog.d("====初始化失败,错误码：" + code);
//            }
//        }
//    };

    /**
     * 识别监听器。
     */
//    private RecognizerListener mRecognizerListener = new RecognizerListener() {
//
//        @Override
//        public void onVolumeChanged(int volume, byte[] data) {
//            ELog.d("====当前正在说话，音量大小：" + volume);
//            ELog.d("===返回音频数据：" + data.length);
//            voideDialog.setData("当前正在说话，音量大小：" + volume);
//        }
//
//        @Override
//        public void onResult(final RecognizerResult result, boolean isLast) {
//            ELog.d("====开始识别按钮==监听器======");
//            voideDialog.cancel();
//            if (null != result && !TextUtils.isEmpty(result.getResultString())) {
//                ELog.d("====开始识别按钮==监听器==recognizer result：" + result.getResultString());
//                String text = "";
//                text = JsonParser.parseGrammarResult(result.getResultString(), mEngineType);
//                ELog.d("====开始识别按钮==监听器=111111====" + text);
//                RecognitionText(text);//识别返回的文字
//                ELog.d("====开始识别按钮==监听器=返回文字之后是否还在监听===" + mAsr.isListening());
//
////                if (!mAsr.isListening()){
////                    Log.d("====开始识别按钮==监听器=返回文字之后再次监听111=","=====" + mAsr.isListening());
////                    ret = mAsr.startListening(mRecognizerListener);
////                }
//            } else {
//                ELog.d("====开始识别按钮==监听器=11111=recognizer result : null");
//            }
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
////            showTip("结束说话");
//            ELog.d("====开始识别按钮==监听器=====结束说话==");
////            Toast.makeText(this,"结束说话。",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onBeginOfSpeech() {
//            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
////            showTip("开始说话");
//            ELog.d("====开始识别按钮==监听器===开始说话==");
//            voideDialog.show();
//        }
//
//        @Override
//        public void onError(SpeechError error) {
//            voideDialog.cancel();
//            xianshi(error.getErrorCode());
////            showTip("onError Code："	+ error.getErrorCode());
//            ELog.d("====开始识别按钮==监听器==onError Code：" + error.getErrorCode());
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//            // 若使用本地能力，会话id为null
//            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//            //	}
//        }
//
//    };

//    private void RecognitionText(String text) {
//        // 显示
//        one_back();
//        if (text.contains("功能")) {
//            ELog.d("====开始识别按钮==监听器=22222=====" + text);
//            one_back();
//        } else if (text.contains("通知")) {
//            noticeOnclick();
//        } else if (text.contains("人脸识别")) {
//            faceDetect();
//        } else if (text.contains("指纹识别")) {
//            fingerprint();
//        } else if (text.contains("考勤统计")) {
//            attendance_count();
//        } else if (text.contains("班级课程")) {
//            class_course();
//        } else if (text.contains("个人课程")) {
//            Toast.makeText(MainActivity.this, "请刷卡", Toast.LENGTH_SHORT).show();
//            personal_course();
//        } else if (text.contains("柜子")) {
//            Toast.makeText(MainActivity.this, "请刷卡", Toast.LENGTH_SHORT).show();
//            cabint_liner();
//        } else if (text.contains("班主任查询")) {
//            Toast.makeText(MainActivity.this, "请刷卡", Toast.LENGTH_SHORT).show();
//            this_class_student();
//        } else if (text.contains("请假")) {
//            Toast.makeText(MainActivity.this, "请刷卡", Toast.LENGTH_SHORT).show();
//            leave();
//        } else if (text.contains("餐费余额")) {
//            Toast.makeText(MainActivity.this, "请刷卡", Toast.LENGTH_SHORT).show();
//            meals();
//        }
//
////        Toast.makeText(MainActivity.this,"结束说话。"+text,Toast.LENGTH_SHORT).show();
//    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
//    public boolean setParam() {
//        ELog.d("====判断是否构建语法了========");
//        boolean result = false;
//        // 清空参数
//        mAsr.setParameter(SpeechConstant.PARAMS, null);
//        // 设置识别引擎
//        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
//        if ("cloud".equalsIgnoreCase(mEngineType)) {
//            String grammarId = mSharedPreferences.getString(KEY_GRAMMAR_ABNF_ID, null);
//            ELog.d("====判断是否构建语法了==cloud===grammarId==" + grammarId);
//            if (TextUtils.isEmpty(grammarId)) {
//                result = false;
//            } else {
//                // 设置返回结果格式
//                mAsr.setParameter(SpeechConstant.RESULT_TYPE, mResultType);
//                // 设置云端识别使用的语法id
//                mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
//                result = true;
//            }
//        } else {
//            ELog.d("====判断是否构建语法了==本地======");
//            // 设置本地识别资源
//            mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
//            // 设置语法构建路径
//            mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
//            // 设置返回结果格式
//            mAsr.setParameter(SpeechConstant.RESULT_TYPE, mResultType);
//            // 设置本地识别使用语法id
//            mAsr.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");
//            // 设置识别的门限值
//            mAsr.setParameter(SpeechConstant.MIXED_THRESHOLD, "30");
//            // 使用8k音频的时候请解开注释
////			mAsr.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
//            result = true;
//        }
//
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mAsr.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mAsr.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/asr.wav");
//        ELog.d("====判断是否构建语法了==1111==" + Environment.getExternalStorageDirectory() + "/msc/asr.wav");
//        return result;
//    }

    //获取识别资源路径
//    private String getResourcePath() {
//        StringBuffer tempBuffer = new StringBuffer();
//        //识别通用资源
//        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
//        //识别8k资源-使用8k的时候请解开注释
////		tempBuffer.append(";");
////		tempBuffer.append(ResourceUtil.generateResourcePath(this, RESOURCE_TYPE.assets, "asr/common_8k.jet"));
//        return tempBuffer.toString();
//    }

    private void setadapter() {

        // 设置布局管理器
        //八条通知adapter
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        eightnotice_Listview.setLayoutManager(llm);
        eightNoticeAdapter = new EightNoticeAdapter(this, null, this);
        eightnotice_Listview.setAdapter(eightNoticeAdapter);

        //通知
        notice_ListView.setLayoutManager(new LinearLayoutManager(context));
        noticeAdapter = new NoticeAdapter(this, null, this);
        notice_ListView.setAdapter(noticeAdapter);



        /*
         * 作品1
         * */
        listView2.setLayoutManager(new LinearLayoutManager(this));
        listView2.setHasFixedSize(true);
        imageAdapter2 = new ImageAdapter(this, null, this);
        listView2.setAdapter(imageAdapter2);

        /*
         * 作品2
         * */
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setHasFixedSize(true);
        imageAdapter = new ZuopinImageAdapter(this, null, this);
        listView.setAdapter(imageAdapter);
    }


    private void setserialport() {
//        Log.d("setserialport111","333333");
        DeviceMonitorService.flow1().subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((kahao) -> {
                    Message msg = new Message();
                    msg.obj = kahao.trim().toString();
                    msg.what = WiscClient.DUKAHAO_HANDLER;
                    handler.sendMessage(msg);
                });
    }

    @Override
    public void bindNewRoom(Room room, boolean isMqtt) {
        ELog.i("============查看绑定教室======onNext=====1111==" + isMqtt);
        if (room != null && room.id != 0 && room.roomId != 0) {
            ELog.i("============查看绑定教室======onNext=====2222==" + isMqtt);
            WiscApplication.prefs.setdeviceId(room.id);
            WiscApplication.prefs.setBindRoom(room.name);
            classroom_tv.setText(WiscApplication.prefs.getBindRoom());
            if (!TextUtils.isEmpty(room.englishName)) {
                WiscApplication.prefs.setBindRoomEnglishName(room.englishName);
                eg_classroom_tv.setText(WiscApplication.prefs.getBindRoomEnglishName());
                eg_classroom_tv.setVisibility(View.VISIBLE);
            } else {
                WiscApplication.prefs.setBindRoomEnglishName(null);
                eg_classroom_tv.setVisibility(View.GONE);
            }
            if (isMqtt) {
                if (WiscApplication.prefs.getRoomId() != room.roomId) {
                    WiscApplication.prefs.setRoomId(room.roomId);
                    mainPresenter.getCourseList();
                    mainPresenter.getBanhui();
                    mainPresenter.getNoticeList();
                }
            } else {
                mainPresenter.getCourseList();
                mainPresenter.getBanhui();
                mainPresenter.getNoticeList();
            }

            if (room.isMute == 1) {
                current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                if (current != 0) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, 0);
                }
            }
            /*
            * bootTime":"08:00:00",
            "shutDownTime":"09:00:00",
            * */

            if (room.bootTime != null && room.bootTime.trim() != null && room.bootTime.trim().length() != 0) {
                //Toast.makeText(MainActivity.this, "设置了定时开机", Toast.LENGTH_LONG).show();
                System.out.println("=====================定时开机==============" + room.bootTime);
                MainTools.enableAlertPowerOn(this, room.bootTime);
            } else {
                System.out.println("=====================取消开机==============");
                //Toast.makeText(MainActivity.this, "取消了定时开机", Toast.LENGTH_LONG).show();
                MainTools.disableAlertPowerOn(this);//取消开机
            }

            if (room.shutDownTime != null && room.shutDownTime.trim() != null && room.shutDownTime.trim().length() != 0) {
                System.out.println("================定时关机===================" + room.shutDownTime);
                //Toast.makeText(MainActivity.this, "设置了定时关机", Toast.LENGTH_LONG).show();
                MainTools.enableAlertPowerOff(this, room.shutDownTime);

            } else {
                System.out.println("===================取消关机================");
                //Toast.makeText(MainActivity.this, "取消了定时关机", Toast.LENGTH_LONG).show();
                MainTools.disableAlertPowerOff(this);//取消关机
            }
        } else {
            ELog.i("============查看绑定教室======onNext=====3333==" + isMqtt);
            //删除该班牌,取消定时开关机任务
            MainTools.disableAlertPowerOff(this);
            MainTools.disableAlertPowerOn(this);
            WiscApplication.prefs.setisBind(false);
            startActivity(new Intent(this, SelectClassActivity.class));
            MainActivity.this.finish();
            Process.killProcess(Process.myPid());//杀死进程，防止dialog.show()出现错误

        }
    }

    @Override
    public void showZuoPinError() {
        zuoPins = new ArrayList<ZuoPin>();
        zuoPins.clear();
        zuoPins2 = new ArrayList<ZuoPin>();
        zuoPins2.clear();
        zuoPins.add(new ZuoPin("", "", "", "", "JPEG", 0, ""));


        imageAdapter.setZuoPins(zuoPins);
        video_layout.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        image_viewPager.setVisibility(View.VISIBLE);
        LL_tupian.setVisibility(View.VISIBLE);
        LL_shipin.setVisibility(View.GONE);

        image_viewPager.setAdapter(new ImageViewPagerAdapter(this, zuoPins));
        image_viewPager.setOnPageChangeListener(this);

        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        if (myHandler != null) {
            myHandler.removeCallbacks(runnable);
            myHandler = null;
        }
        if (videotimer != null) {
            videotimer.cancel();
        }
        videotimer = null;


    }

    @Override
    public void showZuoPinList(List<ZuoPin> zuoPins) {
        zuoPinDbDao = WiscApplication.getDaoSession().getZuoPinDbDao();
        //创建文件夹 ，在存储卡下
        FileSizeUtil.createFile();
        FileSizeUtil.delete(downloadManager);
        if (zuoPins.size() != 0) {
            index = 0;
            setListAdapter(zuoPins);
        }
    }

    private void setListAdapter(List<ZuoPin> zuopins) {
        zuoPins = new ArrayList<ZuoPin>();
        zuoPins2 = new ArrayList<ZuoPin>();
        for (int i = 0; i < zuopins.size(); i++) {
            ZuoPin zuopin = zuopins.get(i);
            if (zuopin.fileType.equals("JPEG") || zuopin.fileType.equals("PNG")) {
                zuoPins.add(zuopin);
            } else if (zuopin.fileType.equals("MP4") || zuopin.fileType.equals("AVI") || zuopin.fileType.equals("MOV")) {
                zuoPins2.add(zuopin);
            }
        }

        if (zuoPins.size() != 0) {
            LL_tupian.setVisibility(View.VISIBLE);
            //创建默认的线性LayoutManager
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            listView.setLayoutManager(mLayoutManager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            listView.setHasFixedSize(true);
            //创建并设置Adapter
            imageAdapter = new ZuopinImageAdapter(this, zuoPins, this);
            imageAdapter.notifyDataSetChanged();
            listView.setAdapter(imageAdapter);

            image_viewPager.setAdapter(new ImageViewPagerAdapter(this, zuoPins));
            image_viewPager.setOnPageChangeListener(this);
        } else {
            LL_tupian.setVisibility(View.GONE);
        }

        if (zuoPins2.size() != 0) {
            LL_shipin.setVisibility(View.VISIBLE);
            imageAdapter2.setZuoPins(zuoPins2);
            for (int i = 0; i < zuoPins2.size(); i++) {
                String[] namelist = zuoPins2.get(i).filePath.toString().split("/");
                if (namelist.length != 0) {
                    String filename = namelist[namelist.length - 1];
                    List<ZuoPinDb> zuoPinDB = zuoPinDbDao.queryBuilder()
                            .where(ZuoPinDbDao.Properties.FileName.eq(filename))
                            .orderAsc(ZuoPinDbDao.Properties.DownloadTime)
                            .list();
                    zuoPins2.get(i).setFileName(filename);
                    if (zuoPinDB.size() != 0) {
                        if (zuoPinDB.get(0).getDownloadStatus() != 1) {
                            int status = checkDownloadStatus(zuoPinDB.get(0).downloadTaskId);
                            ELog.i("=========下载=======11111=====" + status);
                            if (status == -1) {
                                downloadManager.remove(zuoPinDB.get(0).downloadTaskId);
                                ELog.i("=========下载========222222=====" + status);
                                huancun(zuoPins2.get(i), filename);
                            }
                            if (status == 1) {
                                zuoPinDB.get(0).setDownloadStatus(1);
                                ELog.i("=========下载========222222==111===" + status);
                            }
                        }
                        zuoPinDB.get(0).setDownloadTime(System.currentTimeMillis());
                        zuoPinDbDao.update(zuoPinDB.get(0));
                    } else {
                        huancun(zuoPins2.get(i), filename);
                        ELog.i("=========下载==========333333=====" + zuoPins2.get(i).toString());
                    }

                }
            }
            ELog.i("=========下载======zuoPinDbDao.loadAll()=========" + zuoPinDbDao.loadAll().toString());
        } else {
            LL_shipin.setVisibility(View.GONE);
        }
        handler.sendEmptyMessage(WiscClient.ZUOPIN_HANDLER);

    }

    /*
     * 图片视频轮播
     * */
    @Override
    public void onZuopinImageItemClicked(int position) {
        imageAdapter.changeSelected(position);
        index = position;
        SetImageAndVideo(zuoPins.get(position), index);
        index++;
    }

    @Override
    public void onZuopinVideoItemClicked(int position) {
        imageAdapter2.changeSelected(position);
        index = position + zuoPins.size();
        SetImageAndVideo(zuoPins2.get(position), index);
        index++;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //ELog.i("======onPageSelected=======" + position);
        listView.smoothScrollToPosition(position);
        imageAdapter.changeSelected(position);
        index = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //ELog.i("======onPageScrollStateChanged======="+state);
    }


    private void SetImageAndVideo(ZuoPin zuoPin, int index) {
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        if (myHandler != null) {
            myHandler.removeCallbacks(runnable);
            myHandler = null;
        }
        if (videotimer != null) {
            videotimer.cancel();
        }
        videotimer = null;
        if (zuoPin.fileType.equals("JPEG") || zuoPin.fileType.equals("PNG")) {
            video_layout.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            image_viewPager.setVisibility(View.VISIBLE);
            image_viewPager.setCurrentItem(index);
            myHandler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(WiscClient.ZUOPIN_HANDLER);
                    myHandler = null;
                }
            };
            myHandler.postDelayed(runnable, 10000);

        } else if (zuoPin.fileType.equals("MP4") || zuoPin.fileType.equals("AVI") || zuoPin.fileType.equals("MOV")) {
            video_layout.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            image_viewPager.setVisibility(View.GONE);

            List<ZuoPinDb> zuoPinDB = zuoPinDbDao.queryBuilder()
                    .where(ZuoPinDbDao.Properties.FileName.eq(zuoPin.fileName))
                    .orderAsc(ZuoPinDbDao.Properties.DownloadTime)
                    .list();

            if (zuoPinDB.size() != 0) {
                if (zuoPinDB.get(0).getDownloadStatus() != 1) {
                    int sta = checkDownloadStatus(zuoPinDB.get(0).downloadTaskId);
                    if (sta == 1) {
                        //Toast.makeText(this,"本地本地本地",Toast.LENGTH_SHORT).show();
                        zuoPinDB.get(0).setDownloadTime(System.currentTimeMillis());
                        zuoPinDB.get(0).setDownloadStatus(1);
                        zuoPinDbDao.update(zuoPinDB.get(0));
                        videoView.setVideoPath(FileSizeUtil.createFile() + zuoPin.getFileName());
                    } else if (sta == 2) {
                        bofangVideo(zuoPin);
                    } else if (sta == -1) {
                        downloadManager.remove(zuoPinDB.get(0).downloadTaskId);
                        bofangVideo(zuoPin);
                        String[] namelist = zuoPin.filePath.toString().split("/");
                        if (namelist.length != 0) {
                            String filename = namelist[namelist.length - 1];
                            huancun(zuoPin, filename);
                        }
                    }
                } else {
                    videoView.setVideoPath(FileSizeUtil.createFile() + zuoPin.getFileName());
                }

            } else {
                bofangVideo(zuoPin);
            }

            // videoView.setMediaController(new MediaController(this));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    current_time.setText("00:00");
                    try {
                        duration_time.setText(DateUtil.getHHmmss_time(videoView.getDuration()));
                    } catch (Exception e) {
                        duration_time.setText("00:00");
                    }
                    videoView.start();
                    videoView.requestFocus();
                    //videoView.animate().rotationBy(360.0f).setDuration(videoView.getDuration()).start();//旋转动画
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (videotimer != null) {
                        videotimer.cancel();
                    }
                    videotimer = null;
                    handler.sendEmptyMessage(WiscClient.ZUOPIN_HANDLER);
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if (videotimer != null) {
                        videotimer.cancel();
                    }
                    videotimer = null;
                    handler.sendEmptyMessage(WiscClient.ZUOPIN_HANDLER);
                    return true;
                }
            });
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    seekBarSetVisibility();
                    seekBar_FrameLayout.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (videotimer != null) {
                        videotimer.cancel();
                    }
                    videotimer = null;
                    if (videoView.isPlaying()) {
                        videoView.pause();
                    }
                    int weizhi = 0;
                    try {
                        weizhi = seekBar.getProgress() * videoView.getDuration() / 100;
                    } catch (Exception e) {
                        weizhi = 0;
                    }
                    current_time.setText(DateUtil.getHHmmss_time(weizhi + 1000));
                    videoView.seekTo(weizhi);
                    videoView.start();
                    startVideotimer();
                }
            });

            startVideotimer();
            seekBarSetVisibility();
        } else {
            handler.sendEmptyMessage(WiscClient.ZUOPIN_HANDLER);
        }
    }

    private void bofangVideo(ZuoPin zuoPin) {
        if (DisplayTools.isNetworkAvailable(MainActivity.this)) {
            videoView.setVideoPath(zuoPin.filePath);
        } else {
            if (videotimer != null) {
                videotimer.cancel();
            }
            videotimer = null;
            handler.sendEmptyMessage(WiscClient.ZUOPIN_HANDLER);
        }
    }

    private void seekBarSetVisibility() {
        if (myHandler != null) {
            myHandler.removeCallbacks(runnable);
            myHandler = null;
        }
        myHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar_FrameLayout.setVisibility(View.GONE);
                myHandler = null;
            }
        };
        myHandler.postDelayed(runnable, 6000);
    }

    private void startVideotimer() {
        videotimer = new Timer();
        videotimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WiscClient.SHIPIN_SEEKBAR_HANDLER);
            }
        }, 0, 1000);
    }


    private void setSlidingMenu() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                TimeThread.slidingClosedTime();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                TimeThread.slidingOpenedTime(handler);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @OnClick(R.id.close_image)
    void closeImageClick() {
        WiscClient.isFinger= false;
        mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTianqi(Tianqi tianqi) {
        showTianqi(tianqi);
    }


    private void showTianqi(Tianqi tianqi) {
        if (tianqi.data.condition != null) {
            if (tianqi.data.condition.icon != null && tianqi.data.condition.condition != null && tianqi.data.condition.temp != null
                    && tianqi.data.condition.windDir != null && tianqi.data.condition.windLevel != null) {
                MainTools.ShowImgage(imageWeather, tianqi.data.condition.condition);
                weather.setText(tianqi.data.condition.condition);
                tv_temperature.setText(tianqi.data.condition.temp + "°C");
                tv_wind.setText(tianqi.data.condition.windDir + "　" + tianqi.data.condition.windLevel + "级");
            }
        }
    }

    @Override
    public void showTianqiAqi(TianqiAQI tianqiAQI) {
        showTianqiAQI(tianqiAQI);
    }

    private void showTianqiAQI(TianqiAQI tianqiAQI) {
        if (tianqiAQI.data.aqi != null) {
            if (tianqiAQI.data.aqi.value != null) {
                tv_pm.setText(tianqiAQI.data.aqi.value + " ");
                int aqi = Integer.parseInt(tianqiAQI.data.aqi.value);
                if (aqi >= 0 && aqi <= 50) {
                    tv_air_quality.setText("优");
                } else if (aqi >= 51 && aqi <= 100) {
                    tv_air_quality.setText("良");
                } else if (aqi >= 101 && aqi <= 150) {
                    tv_air_quality.setText("轻度污染");
                } else if (aqi >= 151 && aqi <= 200) {
                    tv_air_quality.setText("中度污染");
                } else if (aqi >= 201 && aqi <= 300) {
                    tv_air_quality.setText("重度污染");
                } else if (aqi >= 301) {
                    tv_air_quality.setText("严重污染");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        // 关闭相关线程
        if (!bindRoomworker.isDisposed()) {
            bindRoomworker.dispose();
        }
        if (!fingerworker.isDisposed()) {
            fingerworker.dispose();
        }
        if (!fingerteacherworker.isDisposed()) {
            fingerteacherworker.dispose();
        }

        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        if (myHandler != null) {
            myHandler.removeCallbacks(runnable);
            myHandler = null;
        }
        if (videotimer != null) {
            videotimer.cancel();
            videotimer = null;
        }
        TimeThread.setAllClosed();
        TimerUtils.closedAllTimer();
        mainPresenter.detachView();//？
        handler.removeCallbacks(null);
//        mAsr.cancel();
//        mAsr.stopListening();
        fingerUtil.Run_CmdDeleteAll();
        fingerUtil.Run_CmdCancel();
//        fingerUtil.CloseDevice();模拟出现黑屏
        ELog.e("onDestroy");
        if (webview_one != null) {
            webview_one.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_one.clearHistory();

            ((ViewGroup) webview_one.getParent()).removeView(webview_one);
            webview_one.destroy();
            webview_one = null;
        }
        if (webview_two != null) {
            webview_two.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_two.clearHistory();

            ((ViewGroup) webview_two.getParent()).removeView(webview_two);
            webview_two.destroy();
            webview_two = null;
        }
        super.onDestroy();
    }


    @Override
    public void showBanHui(String imgUrl) {
        if (imgUrl != null) {
            Glide.with(context).load(imgUrl)
                    .placeholder(R.mipmap.class_logo)
                    .error(R.mipmap.class_logo)
                    .fitCenter()
                    .dontAnimate()
                    .into(image_banhui);
        } else {
            image_banhui.setImageResource(R.mipmap.class_logo);
        }
    }


    @Override
    public void showErrorMessage(Throwable e) {
        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
        boolean isConnectException = e.getClass().equals(ConnectException.class);
        boolean isHttpException = e.getClass().equals(HttpException.class);
        if (isSocketException || isConnectException || isHttpException) {
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void makeError(Throwable e) {
        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
        boolean isConnectException = e.getClass().equals(ConnectException.class);
        boolean isHttpException = e.getClass().equals(HttpException.class);
        if (isSocketException || isConnectException || isHttpException) {
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
            if (WiscApplication.prefs.getBindRoom() != null) {
                classroom_tv.setText(WiscApplication.prefs.getBindRoom());
            }
            if (WiscApplication.prefs.getBindRoomEnglishName() != null) {
                eg_classroom_tv.setText(WiscApplication.prefs.getBindRoomEnglishName());
                eg_classroom_tv.setVisibility(View.VISIBLE);
            } else {
                eg_classroom_tv.setVisibility(View.GONE);
            }
            if (bindRoomworker.isDisposed()) {
                bindRoomworker = Schedulers.io().createWorker();
            }
            bindRoomworker.schedule(new Runnable() {
                @Override
                public void run() {
                    mainPresenter.getbindRoom(false);
                    bindRoomworker.dispose();
                }
            }, 1000 * 60 * 2, TimeUnit.MILLISECONDS);
        } else {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * dialog 出现android.view.WindowManager$BadTokenException: Unable to add window -- token andr
     * http://stackoverflow.com/questions/1561803/android-progressdialog-show-crashes-with-getapplicationcontext
     * */
    private void showDaKaXinXi(String userImg, Integer integer, boolean isTeacher, boolean isQingjia) {
        ELog.i("=======showDaKaXinXi===" + MainActivity.this.isFinishing());
        if (integer == PromptDialog.DAO) {
            if (isTeacher == false) {
                if (isQingjia == true) {
                    dao++;
                    qingjia--;
                } else {
                    dao++;
                    queqin--;
                }
            }
            kaoqinDialog = new PromptDialog(baseActivity, PromptDialog.DAO);
        } else if (integer == PromptDialog.CHIDAO) {
            if (isTeacher == false) {
                if (isQingjia == true) {
                    chidao++;
                    qingjia--;
                } else {
                    chidao++;
                    queqin--;
                }
            }
            kaoqinDialog = new PromptDialog(baseActivity, PromptDialog.CHIDAO);
        } else if (integer == PromptDialog.CHONGFU) {
            kaoqinDialog = new PromptDialog(baseActivity, PromptDialog.CHONGFU);
        } else if (integer == PromptDialog.CUOWU) {
            kaoqinDialog = new PromptDialog(baseActivity, PromptDialog.CUOWU);
        }
        kaoqinDialog.show();
        if (userImg != null) {
            kaoqinDialog.setImage(userImg);
        }
        handler.sendEmptyMessage(WiscClient.KAOQIN_TONGJI_HANDLER);
    }

    @Override
    public void showTeacherKaoqin(boolean b, String msg) {
        if (b) {
            kaoqinDialog = new PromptDialog(baseActivity, PromptDialog.KAOQIN);
            kaoqinDialog.show();
            kaoqinDialog.setkaoqingdata(msg);
        } else {
            Toast.makeText(baseActivity, msg, Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void showCoursesListError(Throwable e) {
        setMeiKeView();
        // mainPresenter.getCourseList();
    }

    @Override
    public void showMessage(int type, int fingerId) {
        ELog.e(type + "===thh===="+fingerId);
        switch (type) {
            case 1:
                ELog.e("录入成功");

                fingerUtil.Run_CmdReadTemplate(0);
                break;
            case -1:
                ELog.e("失败,请重新录入");
//                msg.obj = "失败,请重新录入";
//                msg.what = WiscClient.FINGER_HANDLER;
                break;
            case 2:
                if (fingerId != 0) {
                    ELog.e("tjj识别指纹成功");
//                    msg.obj = "识别指纹成功";
//                    msg.what = WiscClient.FINGER_HANDLER;
                    mainPresenter.daka(fingerId,handler);//卡号和时间签到
                } else {
                    ELog.e("tjj失败,请重新识别或今天不需要在此教室上课");
                    Message msg = new Message();
                    msg.obj = "失败,请重新识别或你没有录入指纹";
                    msg.what = WiscClient.fail;
                    handler.sendMessage(msg);
//                    msg.obj = "失败,请重新识别或今天不需要在此教室上课";
//                    msg.what = WiscClient.FINGER_EMPTY_NAME_HANDLER;
                }
                break;
            case 4:
                ELog.e("tjj请按下你的手指");
                break;
        }
    }
    /*
    * 保存请求下来的学生指纹信息
    * */
    @Override
    public void writeTemplate(boolean isWriteFinger, List<FingerUsers> data) {

        fingersDao.deleteAll();
        if (data != null && data.size() != 0 && openFingerDevice == true) {
            ELog.i("======tjj======FingerUsers===========" + data.size());

            if (fingerworker.isDisposed()) {
                fingerworker = Schedulers.io().createWorker();
            }
            ELog.i("======tjj======fingersDao===id====开始==");
            fingerworker.schedule(new Runnable() {
                @Override
                public void run() {
                    fingerworker.dispose();
                    for (int i = 0; i < data.size(); i++) {
                        fingersDao.insert(new FingerUsers(data.get(i).id, data.get(i).stid,
                                data.get(i).psType, data.get(i).name, data.get(i).context,
                                data.get(i).schoolId, data.get(i).ts, data.get(i).status,
                                data.get(i).number));
                        if (isWriteFinger) {
                            ELog.i("====tjj========fingersDao===id===开始２===" + data.get(i).id + "==psType==" + data.get(i).psType + "=name=="+data.get(i).name);
                            fingerUtil.Run_CmdWriteTemplate(data.get(i).id, data.get(i).context);
                        }
                    }
                    isRunWriteTemplate = false;
                    mainPresenter.getFingerTeacherUserList(WiscClient.isWriteFinger);
                }
            }, 1000, TimeUnit.MILLISECONDS);

            ELog.i("=========tjj===fingersDao===id====开始３==");
        } else {
            isRunWriteTemplate = false;
            mainPresenter.getFingerTeacherUserList(WiscClient.isWriteFinger);
        }
    }


    @Override
    public void writeTeacherTemplate(boolean isWriteFinger, List<FingerTeacherUsers> data) {

        fingerTeacherUsersDao.deleteAll();
        if (data != null && data.size() != 0 && openFingerDevice == true) {
            ELog.i("========tjj====fingerTeacherUsersDao===========" + data.size());
            for (int i = 0; i < data.size(); i++) {
                fingerTeacherUsersDao.insert(new FingerTeacherUsers(data.get(i).id, data.get(i).stid,
                        data.get(i).psType, data.get(i).name, data.get(i).context,
                        data.get(i).schoolId, data.get(i).ts, data.get(i).status,
                        data.get(i).number));
                if (isWriteFinger) {
                    ELog.i("=======tjj=====fingerTeacherUsersDao===id======" + data.get(i).id+"==="+data.get(i).context);
                    fingerUtil.Run_CmdWriteTemplate(data.get(i).id, data.get(i).context);
                }
            }
//            mainPresenter.getFingerUsers(WiscClient.isWriteFinger);
            isRunWriteTemplate = false;
            fingerUtil.Run_CmdIdentifyFree();

//            if (fingerteacherworker.isDisposed()) {
//                fingerteacherworker = Schedulers.io().createWorker();
//            }
//            fingerteacherworker.schedule(new Runnable() {
//                @Override
//                public void run() {
//                    fingerteacherworker.dispose();
//                    fingerUtil.Run_CmdIdentifyFree();
//                    isRunWriteTemplate = false;
//                }
//            }, 1000, TimeUnit.MILLISECONDS);
        } else {
            isRunWriteTemplate = false;
        }

    }


    @Override
    public void showDayCourseList(List<Course> courses) {
        position = 0;
        courseDao = WiscApplication.getDaoSession().getCourseDao();
        courseDaoTwo = WiscApplication.getDaoSession().getCourseTwoDao(); // 只有点击查看课程表的用途
        courseDao.deleteAll();
        courseDaoTwo.deleteAll();
        if (courses != null && courses.size() != 0) {
            for (int i = 0; i < courses.size(); i++) {
                //判断课程是否结束，课程没结束
                if (DateUtil.compare_date(System.currentTimeMillis(), courses.get(i).getEndTime()) >= 0) {
                    try {
                        courseDao.insert(courses.get(i));
                    } catch (Exception e) {
                    }
                }
                try {
                    courseDaoTwo.insert(new CourseTwo(courses.get(i).courseId, courses.get(i).name, courses.get(i).activityCourseId, courses.get(i).periodId,
                            courses.get(i).teacherName, courses.get(i).teacherId, courses.get(i).startTime, courses.get(i).endTime,
                            courses.get(i).courseforshort, courses.get(i).getLessonNum()));
                } catch (Exception e) {
                }

            }
            if (courseDao.loadAll() != null && courseDao.loadAll().size() != 0) {//课程没结束，请求有课作品
                setCourseData();
                TimerUtils.getTime(handler);
            } else { //课程结束，请求没课作品
                setMeiKeView();
            }

        } else {
            setMeiKeView();
        }
    }

    private void setMeiKeView() {
        LL1.setVisibility(View.GONE);
        LL2.setVisibility(View.VISIBLE);
        kaoqin_xiangxi.setVisibility(View.GONE);
        course = null;
        course_layout.setVisibility(View.GONE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        WiscClient.isDaKaQianDao = false;
        mainPresenter.getMeiKeZuoPin();
        if (studentDao != null && studentDao.loadAll().size() != 0) {
            studentDao.deleteAll();
        }
//        if (teacherDao != null && teacherDao.loadAll().size() != 0) {
//            teacherDao.deleteAll();
//        }
    }

    private void setCourseData() {
        LL1.setVisibility(View.VISIBLE);
        LL2.setVisibility(View.GONE);
        kaoqin_xiangxi.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        course = courseDao.loadAll().get(position);
        WiscApplication.prefs.setPeriodId(course.getPeriodId());
        course_name.setText("下一节课: " + course.getName());
        course_teacher_name.setText("任课老师: " + course.getTeacherName());
        course_time.setText("上课时间: " + course.getStartTime().substring(10).trim() + " - " + course.getEndTime().substring(10).trim());
        course_juli_endtime.setText("距离上课: " + 0 + "分钟");
        mainPresenter.getStudentList(course.getCourseId(), course.getPeriodId());
        mainPresenter.getZuoPin(course.getCourseId());
        WiscClient.isDaKaQianDao = true;
        setcourse_tvText();
    }

    /*
     *
     * 得到 查看个人课程 数据
     * */
    @Override
    public void showCoursesList(Courses courses) {
        coursesList = courses;
        getkecheng();

    }


    private void getkecheng() {
        if (keChengDialog == null) {
            keChengDialog = new KeChengDialog(this);
        }
        if (keChengDialog != null) {
            keChengDialog.show();
            keChengDialog.setData(coursesList);
            keChengDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    TimeThread.setClosed();
                    keChengDialog = null;
                }
            });
        }

    }

    private void startMQTTService() {
        Intent mIntent = new Intent();
        mIntent.setAction("com.zhqz.wisc.data.service.MQTTServicer");
        mIntent.setPackage(this.getPackageName());//这里你需要设置你应用的包名
        this.startService(mIntent);
    }


    @Override
    public void showStudentList(AttendanceDetails attendanceDetails) {
        studentDao = WiscApplication.getDaoSession().getStudentDao();
        studentDao.deleteAll();
//        teacherDao = WiscApplication.getDaoSession().getTeacherDao();
//        teacherDao.deleteAll();
        if (attendanceDetails != null && attendanceDetails.students != null && attendanceDetails.teachers != null
                && attendanceDetails.students.size() != 0 && attendanceDetails.teachers.size() != 0) {
            kaoqin_xiangxi.setVisibility(View.VISIBLE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            WiscClient.isDaKaQianDao = true;
            ELog.i("====attendanceDetails==11==" + attendanceDetails.toString());
            attendanceDataDao = WiscApplication.getDaoSession().getAttendanceDataDao();
            if (attendanceDataDao.loadAll().size() != 0) {
                for (int j = 0; j < attendanceDetails.students.size(); j++) {
                    List<AttendanceData> attendance = attendanceDataDao.queryBuilder()
                            .where(AttendanceDataDao.Properties.AttendanceId.eq(attendanceDetails.students.get(j).attendanceId))
                            .orderAsc(AttendanceDataDao.Properties.AttendanceId)
                            .list();
                    if (attendance.size() != 0) {
                        if (attendanceDetails.students.get(j).attendancedate != null) {
                            if (DateUtil.compare_date_two(attendanceDetails.students.get(j).attendancedate, attendance.get(0).getAttendancedate()) >= 0) {
                                try {
                                    attendanceDataDao.deleteByKey(attendance.get(0).attendanceId);
                                } catch (Exception e) {
                                }
                            } else {
                                attendanceDetails.students.get(j).setAttendancedate(attendance.get(0).getAttendancedate());
                                attendanceDetails.students.get(j).setStatus(attendance.get(0).getStatus());
                            }
                        } else {
                            attendanceDetails.students.get(j).setAttendancedate(attendance.get(0).getAttendancedate());
                            attendanceDetails.students.get(j).setStatus(attendance.get(0).getStatus());
                        }
                    }
                }

                for (int j = 0; j < attendanceDetails.teachers.size(); j++) {
                    List<AttendanceData> attendance = attendanceDataDao.queryBuilder()
                            .where(AttendanceDataDao.Properties.AttendanceId.eq(attendanceDetails.teachers.get(j).attendanceId))
                            .orderAsc(AttendanceDataDao.Properties.AttendanceId)
                            .list();
                    if (attendance.size() != 0) {
                        if (attendanceDetails.teachers.get(j).attendancedate != null) {
                            if (DateUtil.compare_date_two(attendanceDetails.teachers.get(j).attendancedate, attendance.get(0).getAttendancedate()) >= 0) {
                                try {
                                    attendanceDataDao.deleteByKey(attendance.get(0).attendanceId);
                                } catch (Exception e) {
                                }
                            } else {
                                attendanceDetails.teachers.get(j).setAttendancedate(attendance.get(0).getAttendancedate());
                                attendanceDetails.teachers.get(j).setStatus(attendance.get(0).getStatus());
                            }
                        } else {
                            attendanceDetails.teachers.get(j).setAttendancedate(attendance.get(0).getAttendancedate());
                            attendanceDetails.teachers.get(j).setStatus(attendance.get(0).getStatus());
                        }
                    }
                }
            }

            ELog.i("===错误+没打卡数据库=====" + attendanceDataDao.loadAll().size());
            ELog.i("====attendanceDetails===22=" + attendanceDetails.toString());
            //studentList = attendanceDetails.students;

            for (int j = 0; j < attendanceDetails.students.size(); j++) {
                try {
                    studentDao.insert(new Student(attendanceDetails.students.get(j).studentId,
                            attendanceDetails.students.get(j).studentName,
                            attendanceDetails.students.get(j).cardNumber,
                            attendanceDetails.students.get(j).studentNumber,
                            attendanceDetails.students.get(j).attendancedate,
                            attendanceDetails.students.get(j).attendanceId,
                            attendanceDetails.students.get(j).status,
                            attendanceDetails.students.get(j).picture
                    ));
                } catch (Exception e) {
                }

                if (attendanceDetails.students.get(j).status == -1) {
                    try {
                        attendanceDataDao.insert(new AttendanceData(attendanceDetails.students.get(j).attendanceId,
                                attendanceDetails.activityCourseId,
                                attendanceDetails.periodId,
                                attendanceDetails.students.get(j).attendancedate,
                                attendanceDetails.students.get(j).status,
                                attendanceDetails.students.get(j).cardNumber
                        ));
                    } catch (Exception e) {
                    }
                }

            }
            ELog.i("===studentDao=====" + studentDao.loadAll().size());

            ELog.i("===错误+没打卡数据库==2222===" + attendanceDataDao.loadAll().size());
            ELog.i("===错误+没打卡数据库==2222===" + attendanceDataDao.loadAll().toString());
            setTongji();
        } else {
            kaoqin_xiangxi.setVisibility(View.GONE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            WiscClient.isDaKaQianDao = false;
        }
    }

    /**
     * 1 到
     * 2 迟到
     * 3 缺勤
     * 4 请假
     * -1 初始状态
     */
    private void setTongji() {
        dao = 0;
        chidao = 0;
        queqin = 0;
        qingjia = 0;
        for (int i = 0; i < studentDao.loadAll().size(); i++) {
            int status = studentDao.loadAll().get(i).status;
            if (status == 1) {
                dao++;
            } else if (status == 2) {
                chidao++;
            } else if (status == 3) {
                queqin++;
            } else if (status == 4) {
                qingjia++;
            } else if (status == 5) {
                qingjia++;
            } else if (status == -1) {
                queqin++;
            }
        }
        handler.sendEmptyMessage(WiscClient.KAOQIN_TONGJI_HANDLER);
    }


    //查看个人课程//1713530814
    @OnClick(R.id.personal_course)
    void personal_course() {
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.VISIBLE);
        meals_tishi_shuaka.setVisibility(View.GONE);
        benban_tishi_shuaka.setVisibility(View.GONE);
        leave_tishi_shuaka.setVisibility(View.GONE);
        cabint_tishi_shuaka.setVisibility(View.GONE);
        scene_tishi_shuaka.setVisibility(View.GONE);
        WiscClient.isCabint = false;
        WiscClient.isDaKaQianDao = false;
        WiscClient.isChaKanKeCheng = true;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isScene = false;
        chaxunDelayed();

    }

    //查看班级课程
    @OnClick(R.id.class_course)
    void class_course() {
        if (courseDaoTwo != null && courseDaoTwo.loadAll().size() != 0) {
            if (allCourseDialog == null) {
                allCourseDialog = new AllCourseDialog(this);
            }
            if (allCourseDialog != null) {
                allCourseDialog.show();
                allCourseDialog.setData(courseDaoTwo.loadAll());
                allCourseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        TimeThread.setClosed();
                        allCourseDialog = null;
                    }
                });
            }
            if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
                mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_course), Toast.LENGTH_SHORT).show();
        }

    }

    private AllCourseDialog allCourseDialog;
    private MealsDialog mealsDialog;
    private LeaveDialog leaveDialog;
    private ThisClassStudensDialog thisclassstudensDialog;


    @OnClick(R.id.kaoqin_xiangxi)
    void kaoqinClick() {
        attendance_count();
    }

    //查看考勤统计
    @OnClick(R.id.attendance_count)
    void attendance_count() {
        if (studentDao != null && studentDao.loadAll().size() != 0) {//有数据
            if (attendanceDialog == null) {
                attendanceDialog = new AttendanceDialog(this);
            }
            if (attendanceDialog != null) {
                attendanceDialog.show();
                attendanceDialog.setData(studentDao.loadAll());
                attendanceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        TimeThread.setClosed();
                        attendanceDialog = null;
                    }
                });
            }
            if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
                mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.btn_select)
    void one_back() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
        } else {
            mDrawerLayout.openDrawer(sliding_menu_linearlayout);
        }

    }


    /*
     *
     * 班主任查询  本班学生
     * */
    @OnClick(R.id.this_class_student)
    void this_class_student() {
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.GONE);
        meals_tishi_shuaka.setVisibility(View.GONE);
        benban_tishi_shuaka.setVisibility(View.VISIBLE);
        leave_tishi_shuaka.setVisibility(View.GONE);
        cabint_tishi_shuaka.setVisibility(View.GONE);
        scene_tishi_shuaka.setVisibility(View.GONE);
        WiscClient.isCabint = false;
        WiscClient.isDaKaQianDao = false;
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = true;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isScene = false;
        chaxunDelayed();

    }


    @Override
    public void showbenbanStudnts(List<ThisClassStudents> thisClassStudentses) {

        if (thisClassStudentses != null && thisClassStudentses.size() != 0) {
            if (thisclassstudensDialog == null) {
                thisclassstudensDialog = new ThisClassStudensDialog(this);
            }
            thisclassstudensDialog.show();
            thisclassstudensDialog.setData(thisClassStudentses);
            thisclassstudensDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    TimeThread.setClosed();
                    thisclassstudensDialog = null;
                }
            });

        } else {
            new NoDataDialog(baseActivity, NoDataDialog.No_Data).show();
//            Toast.makeText(this, "没有需要上课学生", Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * 人脸识别
     *
     * */
    @OnClick(R.id.faceDetect)
    void faceDetect() {
        if (isRunWriteTemplate) {
            Toast.makeText(this, "正在写入指纹数据，请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!DisplayTools.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.GONE);
        meals_tishi_shuaka.setVisibility(View.GONE);
        benban_tishi_shuaka.setVisibility(View.GONE);
        leave_tishi_shuaka.setVisibility(View.GONE);
        cabint_tishi_shuaka.setVisibility(View.GONE);
        scene_tishi_shuaka.setVisibility(View.GONE);
        WiscClient.isCabint = false;
        if (studentDao != null && studentDao.loadAll().size() != 0) {//有数据
            WiscClient.isDaKaQianDao = true;
        } else {
            WiscClient.isDaKaQianDao = false;
        }
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isEnter = false;
        WiscClient.isScene = false;

        if (course != null) {
            WiscApplication.prefs.setActivityCourseId(course.getActivityCourseId());
            WiscApplication.prefs.setCourseStartTime(course.getStartTime());
        } else {
            WiscApplication.prefs.setActivityCourseId(-1);
            WiscApplication.prefs.setCourseStartTime(null);
        }
        DeviceMonitorService.stop();
        FileUtils.deleteDir();
        WiscClient.isWriteFinger = false;
        Intent intent = new Intent(MainActivity.this, FaceScannActivity.class);
        startActivity(intent);
        finish();


    }


    /*
     * 指纹识别
     * */
    @OnClick(R.id.fingerprint)
    void fingerprint() {
        if (!openFingerDevice) {
            Toast.makeText(this, "此设备未打开指纹权限，无法进行此操作", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isRunWriteTemplate) {
            Toast.makeText(this, "正在写入指纹数据，请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!DisplayTools.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.GONE);
        meals_tishi_shuaka.setVisibility(View.GONE);
        benban_tishi_shuaka.setVisibility(View.GONE);
        leave_tishi_shuaka.setVisibility(View.GONE);
        cabint_tishi_shuaka.setVisibility(View.GONE);
        WiscClient.isCabint = false;
        if (studentDao != null && studentDao.loadAll().size() != 0) {//有数据
            WiscClient.isDaKaQianDao = true;
        } else {
            WiscClient.isDaKaQianDao = false;
        }
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isEnter = false;

        if (course != null) {
            WiscApplication.prefs.setActivityCourseId(course.getActivityCourseId());
            WiscApplication.prefs.setCourseStartTime(course.getStartTime());
        } else {
            WiscApplication.prefs.setActivityCourseId(-1);
            WiscApplication.prefs.setCourseStartTime(null);
        }
        DeviceMonitorService.stop();
        FileUtils.deleteDir();
        WiscClient.isWriteFinger = false;
        WiscClient.isFinger= false;
        WiscClient.isEnter = true;
        fingerUtil.Run_CmdCancel();
        WiscApplication.prefs.setCardNumber(null);
        Intent intent = new Intent(MainActivity.this, FingerprintEntryActivity.class);
        startActivity(intent);
        finish();
    }

    /*
     * 请假
     * */
    @OnClick(R.id.leave)
    void leave() {
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.GONE);
        meals_tishi_shuaka.setVisibility(View.GONE);
        benban_tishi_shuaka.setVisibility(View.GONE);
        leave_tishi_shuaka.setVisibility(View.VISIBLE);
        cabint_tishi_shuaka.setVisibility(View.GONE);
        scene_tishi_shuaka.setVisibility(View.GONE);
        WiscClient.isCabint = false;
        WiscClient.isDaKaQianDao = false;
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = true;
        WiscClient.isSetting = false;
        WiscClient.isScene = false;
        chaxunDelayed();
    }

    /*
     * 柜子详情
     * */
    @OnClick(R.id.cabint_liner)
    void cabint_liner() {
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.GONE);
        meals_tishi_shuaka.setVisibility(View.GONE);
        benban_tishi_shuaka.setVisibility(View.GONE);
        leave_tishi_shuaka.setVisibility(View.GONE);
        cabint_tishi_shuaka.setVisibility(View.VISIBLE);
        scene_tishi_shuaka.setVisibility(View.GONE);
        WiscClient.isDaKaQianDao = false;
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isCabint = true;
        WiscClient.isScene = false;
        chaxunDelayed();
    }

    /*
     *
     * 余额查询
     * */
    @OnClick(R.id.meals)
    void meals() {
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.GONE);
        meals_tishi_shuaka.setVisibility(View.VISIBLE);
        benban_tishi_shuaka.setVisibility(View.GONE);
        leave_tishi_shuaka.setVisibility(View.GONE);
        cabint_tishi_shuaka.setVisibility(View.GONE);
        scene_tishi_shuaka.setVisibility(View.GONE);
        WiscClient.isCabint = false;
        WiscClient.isDaKaQianDao = false;
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = true;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isScene = false;
        chaxunDelayed();
    }

    /*
     * 场景切换
     * */
    @OnClick(R.id.scene_switch)
    void scene_switch() {
        ELog.i("==========场景切换=====11111====");
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        tishi_shuaka.setVisibility(View.GONE);
        meals_tishi_shuaka.setVisibility(View.GONE);
        benban_tishi_shuaka.setVisibility(View.GONE);
        leave_tishi_shuaka.setVisibility(View.GONE);
        cabint_tishi_shuaka.setVisibility(View.GONE);
        scene_tishi_shuaka.setVisibility(View.VISIBLE);
        WiscClient.isCabint = false;
        WiscClient.isDaKaQianDao = false;
        WiscClient.isChaKanKeCheng = false;
        WiscClient.isMeals = false;
        WiscClient.isThisClassStudents = false;
        WiscClient.isLeave = false;
        WiscClient.isSetting = false;
        WiscClient.isScene = true;
        chaxunDelayed();
//        Message msg = new Message();
//        msg.obj = "1111111111";
//        msg.what = WiscClient.DUKAHAO_HANDLER;
//        handler.sendMessage(msg);
    }

    private void chaxunDelayed() {
        chaxunHandler = new Handler();
        chaxunRunnable = new Runnable() {
            @Override
            public void run() {
                benban_tishi_shuaka.setVisibility(View.GONE);
                tishi_shuaka.setVisibility(View.GONE);
                meals_tishi_shuaka.setVisibility(View.GONE);
                leave_tishi_shuaka.setVisibility(View.GONE);
                cabint_tishi_shuaka.setVisibility(View.GONE);
                scene_tishi_shuaka.setVisibility(View.GONE);
                WiscClient.isCabint = false;
                if (studentDao != null && studentDao.loadAll().size() != 0) {//有数据
                    WiscClient.isDaKaQianDao = true;
                } else {
                    WiscClient.isDaKaQianDao = false;
                }
                WiscClient.isChaKanKeCheng = false;
                WiscClient.isMeals = false;
                WiscClient.isThisClassStudents = false;
                WiscClient.isLeave = false;
                WiscClient.isSetting = false;
                WiscClient.isScene = false;
                chaxunHandler = null;
            }
        };
        chaxunHandler.postDelayed(chaxunRunnable, 5000);
    }

    @Override
    public void showMeals(Meals meals) {
        if (meals != null) {
            if (mealsDialog == null) {
                mealsDialog = new MealsDialog(this);
            }
            mealsDialog.show();
            mealsDialog.setData(meals);
            mealsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    TimeThread.setClosed();
                    mealsDialog = null;
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.meals_failure), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showSeting(boolean b, int num) {
        if (num == 1) {
            //ACTION_DISPLAY_SETTINGS显示界面;ACTION_SETTINGS设置界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            MainActivity.this.finish();
            Process.killProcess(Process.myPid());//杀死进程，防止dialog.show()出现错误
        } else {
//            mainPresenter.GetSecen();
            startActivity(new Intent(MainActivity.this, SceneActivity.class));
            finish();
        }
    }


    //连续点击5次，调出系统设置界面
    @OnClick(R.id.set_button)
    void setting() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
        } else {
            if (chaxunHandler != null) {
                chaxunHandler.removeCallbacks(chaxunRunnable);
                chaxunHandler = null;
            }
            tishi_shuaka.setVisibility(View.GONE);
            meals_tishi_shuaka.setVisibility(View.GONE);
            benban_tishi_shuaka.setVisibility(View.GONE);
            leave_tishi_shuaka.setVisibility(View.GONE);
            cabint_tishi_shuaka.setVisibility(View.GONE);
            scene_tishi_shuaka.setVisibility(View.GONE);
            WiscClient.isCabint = false;
            WiscClient.isDaKaQianDao = false;
            WiscClient.isChaKanKeCheng = false;
            WiscClient.isMeals = false;
            WiscClient.isThisClassStudents = false;
            WiscClient.isLeave = false;
            WiscClient.isSetting = true;
            WiscClient.isScene = false;
            chaxunDelayed();
        }

    }


    /*
     * 课程详情
     * */
    @OnClick(R.id.course_detail)
    void course_detail() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        if (course != null && course.getCourseId() != 0) {
            mainPresenter.courseIntroduction(course.getCourseId() + "");
        }
    }

    @Override
    public void showCourseIntroduction(String introduction) {
        llayout.setVisibility(View.GONE);
        course_detail_line.setVisibility(View.VISIBLE);
        teacher_detail_line.setVisibility(View.GONE);
        course_content.setText(introduction);

    }


    /*
     * 老师简介
     * */
    @OnClick(R.id.teacher_introduction)
    void teacher_introduction() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        if (course != null && course.getCourseId() != 0) {
            mainPresenter.teacherIntroduction(course.getCourseId() + "");
        }
    }

    @Override
    public void showTeacherIntroduction(TeacherIntroduction teacherIntroduction) {
        llayout.setVisibility(View.GONE);
        course_detail_line.setVisibility(View.GONE);
        teacher_detail_line.setVisibility(View.VISIBLE);
        teacher_detail_name.setText("姓名 ： " + teacherIntroduction.teachername);
        teacher_detail.setText("简介 ： " + teacherIntroduction.profile);
    }

    /*
     * 课程返回按钮
     * */
    @OnClick(R.id.course_back)
    void course_back() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        llayout.setVisibility(View.VISIBLE);
        course_detail_line.setVisibility(View.GONE);
        teacher_detail_line.setVisibility(View.GONE);
    }

    /*
     * 老师简介返回按钮
     * */
    @OnClick(R.id.teacher_back)
    void teacher_back() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        llayout.setVisibility(View.VISIBLE);
        course_detail_line.setVisibility(View.GONE);
        teacher_detail_line.setVisibility(View.GONE);
    }


    /**
     * action;  //  1是更新  2是删除 9升级
     * content;//通知 班徽 作品的id
     * List<String> target;//设备id
     * type;// 消息类型  1班牌重新绑定  2通知  3班徽   4作品
     */
    @Subscribe
    public void onEventMqtt(MqttMessageEvent mqttMessageEvent) {
        ELog.i("===onEventMqtt==11==" + mqttMessageEvent.getMsg().toString());
        ELog.i("===onEventMqtt==getdeviceId====" + WiscApplication.prefs.getUdid());
        List<String> targetList = mqttMessageEvent.getMsg().getTarget();
        if (targetList != null && targetList.size() > 0) {
            for (int i = 0; i < targetList.size(); i++) {
                ELog.i("=======tjj==有该班牌消息======" + targetList.get(i));
                if (targetList.get(i).equals(WiscApplication.prefs.getUdid() + "")) {
                    ELog.i("=========有该班牌消息======" + mqttMessageEvent.getMsg().type);
                    if (mqttMessageEvent.getMsg().type == 1) {
                        ELog.i("======1===升级apk======");
                        if (mqttMessageEvent.getMsg().action == 9) {

                            mainPresenter.VersionCheck();
                        } else if (mqttMessageEvent.getMsg().action == 1 || mqttMessageEvent.getMsg().action == 7 || mqttMessageEvent.getMsg().action == 8) {
                            mainPresenter.getbindRoom(true);
                        } else if (mqttMessageEvent.getMsg().action == 2) {
                            //删除该班牌,取消定时开关机任务
                            ELog.i("======1===删除该班牌,取消定时开关机任务======");
                            MainTools.disableAlertPowerOff(this);
                            MainTools.disableAlertPowerOn(this);
                            WiscApplication.prefs.setisBind(false);
                            WiscApplication.prefs.setSceneId(-1);
                            startActivity(new Intent(this, SplashActivity.class));
                            MainActivity.this.finish();
                            Process.killProcess(Process.myPid());//杀死进程，防止dialog.show()出现错误
                        } else if (mqttMessageEvent.getMsg().action == 6) {//
                            //删除该班牌,取消定时开关机任务
                            ELog.i("======1===强制关机======");
                            fireShutDown();

                        }
                    } else if (mqttMessageEvent.getMsg().type == 2) {
                        ELog.i("======2===有该班牌通知列表消息======");
                        mainPresenter.getNoticeList();
                    } else if (mqttMessageEvent.getMsg().type == 3) {
                        ELog.i("======3===有该班牌消息======");
                        mainPresenter.getBanhui();
                    } else if (mqttMessageEvent.getMsg().type == 4) {
                        ELog.i("======4===有该班牌消息======");
                        if (course != null && course.getCourseId() != 0) {
                            mainPresenter.getZuoPin(course.getCourseId());
                        } else {
                            mainPresenter.getMeiKeZuoPin();
                        }
                    } else if (mqttMessageEvent.getMsg().type == 5) {
                        ELog.i("======5===有该班牌消息======");
                    }
                }
            }
        }


    }

    /*
     * 强制关机
     * */
    private void fireShutDown() {
        ELog.i("======1===强制关机====2==");
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private UpdateManger1 updateManger1;

    @Override
    public void isUpdate(boolean b) {
        if (b) {
            //这里来检测版本是否需要更新
            ////                        删除缓存的文件夹
            File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            File appCacheDir = new File(new File(dataDir, "com.zhqz.wisc"), "cache");
//            ELog.i("======1===升级apk===否需要更新==="+appCacheDir.toString());
            FileSizeUtil.deleteFile(appCacheDir);
            updateManger1 = new UpdateManger1(this);
            updateManger1.checkUpdateInfo();
        }

    }


    //检查下载状态
    private int checkDownloadStatus(long downloadTaskId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            //有记录
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_PAUSED) {
                ELog.i("================ 下载暂停");
                //downloadManager.remove(downloadTaskId);//删除下载，若下载中取消下载。会同时删除下载文件和记录。
                return 2;
            } else if (status == DownloadManager.STATUS_PENDING) {
                ELog.i("================ 下载延迟");
                //downloadManager.remove(downloadTaskId);
                return 2;
            } else if (status == DownloadManager.STATUS_RUNNING) {
                ELog.i("================ 正在下载");
                return 2;
            } else if (status == DownloadManager.STATUS_FAILED) {
                ELog.i("================ 下载失败");
                //downloadManager.remove(downloadTaskId);
                return -1;
            } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                ELog.i("================ 下载完成");
                return 1;
            }
        }
        // 没有记录
        //downloadManager.remove(downloadTaskId);
        return -1;
    }


    private void huancun(ZuoPin zuoPin, String filename) {

        FileSizeUtil.deletefile(filename);
        ELog.i("=========delete=======");

        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(zuoPin.filePath));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/班牌作品/", filename);
        //在通知栏中显示，默认就是显示的 //显示 VISIBILITY_VISIBLE_NOTIFY_COMPLETED   //隐藏　VISIBILITY_HIDDEN
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法 //将下载任务加入下载队列，否则不会进行下载
        long downloadTaskId = downloadManager.enqueue(request);
        List<ZuoPinDb> zuoPinDB = zuoPinDbDao.queryBuilder()
                .where(ZuoPinDbDao.Properties.FileName.eq(zuoPin.fileName))
                .orderAsc(ZuoPinDbDao.Properties.DownloadTime)
                .list();
        if (zuoPinDB.size() != 0) {
            zuoPinDbDao.deleteByKey(zuoPinDB.get(0).getFileId());
            ELog.i("====huancun==delete 一条===下载=====" + zuoPinDbDao.loadAll().size());
        }
        zuoPinDbDao.insert(new ZuoPinDb(zuoPin.fileId, System.currentTimeMillis(), downloadTaskId, filename, 2));
        ELog.i("====huancun==插入一条===下载=====" + zuoPinDbDao.loadAll().size());

    }

    @Override
    public void showNoticeList(List<NoticeList> noticeLists) {
        if (webview_one != null) {
            webview_one.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_one.clearHistory();
        }
        if (webview_two != null) {
            webview_two.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_two.clearHistory();
        }
        if (noticeLists != null && noticeLists.size() != 0) {
            eightnotice_Listview.setVisibility(View.VISIBLE);
            ELog.i("=========通知列表============" + noticeLists.toString());

            //一级通知、二级通知排序
            List<NoticeList> level_one_new = new ArrayList<>();
            List<NoticeList> level_one_old = new ArrayList<>();
            List<NoticeList> level_two_new = new ArrayList<>();
            List<NoticeList> level_two_old = new ArrayList<>();

            for (int i = 0; i < noticeLists.size(); i++) {
                if (noticeLists.get(i).level == 2) {//一级通知
                    if (DateUtil.compare_date(System.currentTimeMillis(), noticeLists.get(i).endTime) >= 0) {
                        level_one_new.add(noticeLists.get(i));
                    } else {
                        level_one_old.add(noticeLists.get(i));
                    }
                } else {
                    if (DateUtil.compare_date(System.currentTimeMillis(), noticeLists.get(i).endTime) >= 0) {
                        level_two_new.add(noticeLists.get(i));
                    } else {
                        level_two_old.add(noticeLists.get(i));
                    }
                }
            }

            noticeList = new ArrayList<NoticeList>();
            noticeList.clear();
            noticeList.addAll(level_one_new);
            noticeList.addAll(level_two_new);
            noticeList.addAll(level_one_old);
            noticeList.addAll(level_two_old);

            //通知显示前8条
            if (noticeList.size() >= 8) {
                eightNotices = noticeList.subList(0, 8);
            } else {
                eightNotices = noticeList.subList(0, noticeList.size());
            }
            eightNoticeAdapter.setData(eightNotices);
            noticeAdapter.setData(noticeList);

            showOneLVNotice(noticeList.get(0));
        } else {
            ELog.i("======通知列表===null=1111111=====onNext=======");
            eightnotice_Listview.setVisibility(View.GONE);
            guanBiTongZhiShow();
        }

    }

    private void showNoticeLvOne(NoticeList notice) {
        RL2.setVisibility(View.GONE);
        RL3.setVisibility(View.GONE);
        RL_notice.setVisibility(View.GONE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.GONE);
        RL_notice_content_two.setVisibility(View.VISIBLE);
        WebSettings settings = webview_one.getSettings();
        ELog.e("===item.url=2==" + notice.url);
        //webView  加载视频，下面五行必须
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true);//支持js
        settings.setPluginState(WebSettings.PluginState.ON);// 支持插件
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ELog.e("===item.url=2=webview_one=" + notice.url);
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        webview_one.setWebViewClient(new webViewClient());
        webview_one.loadUrl(notice.url);
        showCourseTishi();
    }

    @Override
    public void setLeaveBoolean(Boolean aBoolean) {
        if (aBoolean == true) {
            Toast.makeText(this, "提交成功，等待审核", Toast.LENGTH_SHORT).show();
            leaveDialog.cancel();
        }
    }

    @Override
    public void setLeaveReason(List<StudentLeaveReason> studentLeaveReasons) {
        if (studentLeaveReasons == null && studentLeaveReasons.size() < 0) {
            return;
        }
        leaveDialog.ReasonAdapter(studentLeaveReasons);
    }

    @Override
    public void setLeave(Leave leave) {
        leaveDialog = new LeaveDialog(this, this, leave);
        leaveDialog.show();
        mainPresenter.getStudentLeaveReasonSpainner();
    }

    List<StudentLeaveSpainner> LeaveSpainnersList;

    @Override
    public void setSpainner(List<StudentLeaveSpainner> studentLeaveSpainners, int num) {
        LeaveSpainnersList = studentLeaveSpainners;
        if (num == 1) {//开始时间
            leaveDialog.SatrtDateAdapter(studentLeaveSpainners);
        } else if (num == 2) {//结束时间
            leaveDialog.EndDateAdapter(studentLeaveSpainners);
        } else if (num == 0) {
            leaveDialog.SatrtDateAdapter(studentLeaveSpainners);
            leaveDialog.EndDateAdapter(studentLeaveSpainners);
        }

    }

    @Override
    public void Leave_summit(int UserId, String psType, String startDate, String endDate, int beginNum, int endNum, String beginLessonNum, String endLessonNum, int typeId, String leaveName) {

        if (startDate == null || startDate.equals("")) {
            Toast.makeText(this, "请选择开始日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endDate == null || endDate.equals("")) {
            Toast.makeText(this, "请选择结束日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (beginNum == -1 && beginLessonNum == null) {
            beginNum = 0;
            beginLessonNum = null;
        }
        if (endNum == -1 && endLessonNum == null) {
            endNum = 0;
            endLessonNum = null;
        }
        if (DateUtil.compare_datetwo(startDate, endDate) == 0) {
            if (beginNum > endNum) {
                Toast.makeText(this, "请选择开始、结束请假的节数存在错误，请重新选择", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (DateUtil.compare_datetwo(startDate, endDate) == 1) {
            Toast.makeText(this, "日期选择错误", Toast.LENGTH_SHORT).show();
            return;
        }

        if (typeId == -1 && leaveName == null) {
            Toast.makeText(this, "请选择请假原因", Toast.LENGTH_SHORT).show();
            return;
        }
        //提交请假接口
        mainPresenter.getStudentLeaveSubmit(UserId, "2", startDate, endDate, beginNum, endNum, beginLessonNum, endLessonNum, typeId, leaveName);
    }

    @Override
    public void leave_date_start_dialog(String time, int UserId) {
        if (time.equals("0")) {
            mainPresenter.StudentLeaveSpainner(DateUtil.getNowYMd(), UserId, 0);
            return;
        }
        leaveDialog.cancel();
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (DateUtil.compare_datetwo(DateUtil.getNowYMd(), DateUtil.getTime(date)) == 1) {
                    Toast.makeText(MainActivity.this, "不能选择今天之后的日期", Toast.LENGTH_SHORT).show();
                    leaveDialog.setStartDate(DateUtil.getNowYMd());
                    leaveDialog.show();
                    return;
                }
                leaveDialog.show();
                leaveDialog.setStartDate(DateUtil.getTime(date));
                mainPresenter.StudentLeaveSpainner(DateUtil.getTime(date), UserId, 1);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setTitleSize(20)
                .setTitleText("请假开始时间")
                .setTitleBgColor(Color.parseColor("#ffffff"))//标题背景颜色 Night mode
                .setTitleColor(Color.BLACK)
                .setOutSideCancelable(false)
                .setLineSpacingMultiplier(2.0f)//设置两横线之间的间隔倍数
                .build();

        pvTime.show();
    }

    @Override
    public void leave_date_end_dialog(String time, int UserId) {
        if (time.equals("0")) {
            mainPresenter.StudentLeaveSpainner(DateUtil.getNowYMd(), UserId, 0);
            return;
        }
        leaveDialog.cancel();
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (DateUtil.compare_datetwo(DateUtil.getNowYMd(), DateUtil.getTime(date)) == 1) {
                    Toast.makeText(MainActivity.this, "不能选择今天之后的日期", Toast.LENGTH_SHORT).show();
                    leaveDialog.setEndDate(DateUtil.getNowYMd());
                    leaveDialog.show();
                    return;
                }
                leaveDialog.show();
                leaveDialog.setEndDate(DateUtil.getTime(date));
                mainPresenter.StudentLeaveSpainner(DateUtil.getTime(date), UserId, 2);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setTitleSize(20)
                .setTitleText("请假结束时间")
                .setTitleBgColor(Color.parseColor("#ffffff"))//标题背景颜色 Night mode
                .setTitleColor(Color.BLACK)
                .setLineSpacingMultiplier(2.0f)//设置两横线之间的间隔倍数
                .build();

        pvTime.show();
    }

    class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }
    }

    private void showOneLVNotice(NoticeList notice) {
        TimerUtils.stopTimer1();
        if (notice.level == 2) {
            long timecha1 = DateUtil.compare_date(System.currentTimeMillis(), notice.startTime);
            long timecha2 = DateUtil.compare_date(System.currentTimeMillis(), notice.endTime);
            if (timecha1 > 0) {
                //还没开始
                guanBiTongZhiShow();
                TimerUtils.getTime1(mainPresenter, notice.startTime);
            } else if (timecha1 <= 0 && timecha2 >= 0) {
                //正在显示
                showNoticeLvOne(notice);
                notice_close.setVisibility(View.GONE);
                TimerUtils.getTime1(mainPresenter, notice.endTime);
            } else if (timecha2 < 0) {
                //结束
                guanBiTongZhiShow();
            }
        } else {
            guanBiTongZhiShow();
        }
    }

    @OnClick(R.id.LL_notice)
    void noticeOnclick() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
//            return;
        }
        if (noticeList != null && noticeList.size() != 0) {
            setClickTongzhi();
        }
    }

    @Override
    public void onItemClicked(NoticeList item) {
        setClickTongzhi();
    }

    @Override
    public void onNoticeItemClicked(NoticeList item) {
        //小布局
        RL2.setVisibility(View.GONE);
        RL3.setVisibility(View.GONE);
        RL_notice.setVisibility(View.GONE);
        RL_notice_content.setVisibility(View.VISIBLE);
        WebSettings settings = webview_two.getSettings();
ELog.e("===item.url===" + item.url);
        //webView  加载视频，下面五行必须

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true);//支持js
        settings.setPluginState(WebSettings.PluginState.ON);// 支持插件
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ELog.e("===item.url=2222==" + item.url);
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        webview_two.setWebViewClient(new webViewClient());
        webview_two.loadUrl(item.url);
        showCourseTishi();
    }

    int current = 0;

    private void setClickTongzhi() {

        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        ELog.i("====视频声音===" + current + "");
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        RL2.setVisibility(View.GONE);
        RL3.setVisibility(View.GONE);
        RL_notice.setVisibility(View.VISIBLE);
        RL_notice_content.setVisibility(View.GONE);
        showCourseTishi();
    }

    private void showCourseTishi() {
        if (course != null) {
            course_layout.setVisibility(View.VISIBLE);
            setcourse_tvText();
        } else {
            course_layout.setVisibility(View.GONE);
        }

    }

    private void setcourse_tvText() {
        course_tv.setText("正在上课：" + course.getName() + "                 " + "任课老师：" + course.getTeacherName() + "                 " + "上课时间：" + course.getStartTime().substring(10) + " - " + course.getEndTime().substring(10));
    }

    @OnClick(R.id.notice_close)
    void notice_closeOnclick() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        RL2.setVisibility(View.GONE);
        RL3.setVisibility(View.GONE);
        RL_notice.setVisibility(View.VISIBLE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.VISIBLE);
        RL_notice_content_two.setVisibility(View.GONE);
        if (webview_one != null) {
            webview_one.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_one.clearHistory();
        }

//        //网速太慢的时候，清空历史数据
//        webview_one.loadUrl("");
//        webview_one.clearHistory();
        if (webview_two != null) {
            webview_two.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_two.clearHistory();
        }
//        webview_two.loadUrl("");
//        webview_two.clearHistory();

        showCourseTishi();
    }

    @OnClick(R.id.notice_close_two)
    void notice_close_twoOnclick() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        RL2.setVisibility(View.GONE);
        RL3.setVisibility(View.GONE);
        RL_notice.setVisibility(View.VISIBLE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.VISIBLE);
        RL_notice_content_two.setVisibility(View.GONE);

        if (webview_one != null) {
            webview_one.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_one.clearHistory();
        }

//        //网速太慢的时候，清空历史数据
//        webview_one.loadUrl("");
//        webview_one.clearHistory();
        if (webview_two != null) {
            webview_two.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_two.clearHistory();
        }
//        webview_one.loadUrl("");
//        webview_one.clearHistory();
//
//        webview_two.loadUrl("");
//        webview_two.clearHistory();
        showCourseTishi();
    }

    @OnClick(R.id.notice_close_image)
    void notice_close_imageOnclick() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        ELog.i("====视频声音=11==" + current + "");
        if (current != 0) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, 0);
        }
        guanBiTongZhiShow();
    }

    private void guanBiTongZhiShow() {
        RL2.setVisibility(View.VISIBLE);
        RL3.setVisibility(View.VISIBLE);
        RL_notice.setVisibility(View.GONE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.VISIBLE);
        RL_notice_content_two.setVisibility(View.GONE);
        course_layout.setVisibility(View.GONE);
    }


    //监听手机的物理按键点击事件
    long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {     //KEYCODE_BACK：回退键
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
//                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    public void setCabint(List<CabinetPerson.CabinetClassItem> sm, String cardNumber) {
        ELog.i("=========setCabint=========个人柜子====");
        CabintcardNumber = cardNumber;
        if (cabintDialog == null) {
            cabintDialog = new CabintDialog(this, this);
        }
        if (!cabintDialog.isShowing()) {
            cabintDialog.show();
        }
        cabintDialog.setPersonalData(sm);
    }

    @Override
    public void setCabintList(List<CabinetInfo> cabinetInfos, String cabintcardNumber) {
        ELog.i("=========setCabintList=========本班柜子====");
        CabintcardNumber = cabintcardNumber;
        if (cabintDialog == null) {
            cabintDialog = new CabintDialog(this, this);
        }
        cabintDialog.show();
        cabintDialog.setClassData(cabinetInfos);
    }

    @Override
    public void PersonalCabintOnclick() {
        ELog.i("===Onclick===个人柜子===");
        mainPresenter.getCabinte(CabintcardNumber);
    }

    @Override
    public void ClassCabintOnclick() {
        ELog.i("===Onclick===本班柜子===");
        mainPresenter.getCabinteClass(CabintcardNumber);
    }

    @Override
    public void OnClickItemCabinet_urgent(CabinetInfo item) {
        ELog.i("=====紧急开柜=======");
        if (item.distribution == 0) {
            Toast.makeText(MainActivity.this, "未分配柜子，不能紧急开柜", Toast.LENGTH_SHORT).show();
            return;
        }
        mainPresenter.getUnlockClicks(item.lockerNoNumber, CabintcardNumber);
    }

    @Override
    public void setIsUnlock(boolean isUnlock) {
        if (cabintDialog.isShowing()) {
            cabintDialog.dismiss();
            cabintDialog = null;
            TimeThread.setClosed();
        }
        if (isUnlock == true) {
            Toast.makeText(MainActivity.this, "紧急开柜成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "网络缓慢，紧急开柜失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnClickItemCabinet_locking(CabinetInfo item) {
        ELog.i("======锁定柜子====");
        if (item.distribution == 0) {
            Toast.makeText(MainActivity.this, "未分配柜子，不能锁定", Toast.LENGTH_SHORT).show();
            return;
        }
        mainPresenter.getlocking(item.id, 0, CabintcardNumber);
    }

    @Override
    public void setIsLock(boolean isUnlock, int status) {
        if (cabintDialog.isShowing()) {
            cabintDialog.dismiss();
            cabintDialog = null;
            TimeThread.setClosed();
        }
        if (isUnlock == true) {
            if (status == 1) {
                Toast.makeText(MainActivity.this, "紧急解锁成功", Toast.LENGTH_SHORT).show();
            } else if (status == 0) {
                Toast.makeText(MainActivity.this, "紧急锁定成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "网络缓慢，紧急开柜失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void OnClickItemCabinet_open(CabinetInfo item) {
        ELog.i("======解锁柜子=====");
        if (item.distribution == 0) {
            Toast.makeText(MainActivity.this, "未分配柜子，不能解锁", Toast.LENGTH_SHORT).show();
            return;
        }
        mainPresenter.getlocking(item.id, 1, CabintcardNumber);
    }

    @Override
    public void showTishiMessage(String s){
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
