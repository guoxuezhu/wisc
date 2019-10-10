package com.zhqz.wisc.libraryui.main;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.DbDao.LibraryReshuModelDao;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.NoticeList;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.data.model.Tianqi;
import com.zhqz.wisc.data.model.TianqiAQI;
import com.zhqz.wisc.libraryui.LibraryListAdapter;
import com.zhqz.wisc.libraryui.Librarymodel;
import com.zhqz.wisc.libraryui.selectLibrary.SelectLibraryActivity;
import com.zhqz.wisc.libraryui.utils.MyFragment;
import com.zhqz.wisc.libraryui.utils.TimeThread;
import com.zhqz.wisc.mqtt.LibraryMqttMessageEvent;
import com.zhqz.wisc.ui.adapter.EightNoticeAdapter;
import com.zhqz.wisc.ui.adapter.NoticeAdapter;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.ui.main.MainTools;
import com.zhqz.wisc.ui.scene.SceneActivity;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.ui.selectClass.SelectClassActivity;
import com.zhqz.wisc.ui.splash.SplashActivity;
import com.zhqz.wisc.ui.view.MyClockView;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.ELog;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
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
import com.don.clockviewlibrary.ClockView;
import static com.zhqz.wisc.WiscApplication.context;

/**
 * Created by jingjingtan on 11/04/18.
 */
public class LibraryMainActivity extends BaseActivity implements LibraryMainMvpView,EightNoticeAdapter.CallBack, NoticeAdapter.CallBack,LibraryListAdapter.CallBack {
    @Inject
    LibraryMainPresenter libraryMainPresenter;


    @BindView(R.id.classroom_library)
    TextView classroom_tv;
    @BindView(R.id.eg_classroom_library)
    TextView eg_classroom_tv;

    /*
    * 天气
    * */
    @BindView(R.id.tv_weather_library)
    TextView weather;//天气状况

    @BindView(R.id.image_weather_library)
    ImageView imageWeather;//天气图片

    @BindView(R.id.image_banhui_library)
    ImageView image_banhui;

    @BindView(R.id.tv_wind_library)
    TextView tv_wind;//风向
    @BindView(R.id.tv_temperature_library)
    TextView tv_temperature;//温度
//    @BindView(R.id.tv_pm_library)
//    TextView tv_pm;
    @BindView(R.id.tv_air_quality_library)
    TextView tv_air_quality;
    @BindView(R.id.kaoqin_xiangxi)
    LinearLayout kaoqin_xiangxi;

    /*
    时间
    * */
    @BindView(R.id.tv_time_library)
    TextView tv_time;
    @BindView(R.id.clockView_library)
    MyClockView clockView;

    @BindView(R.id.tv_date_library)
    TextView tv_date;
    @BindView(R.id.tv_nongli_library)
    TextView tv_nongli;

    /*
    * 通知
    * */
    @BindView(R.id.RL_notice_content_two_library)
    RelativeLayout RL_notice_content_two;
    @BindView(R.id.notice_close)
    TextView notice_close;
    @BindView(R.id.webview_one)
    WebView webview_one;
    @BindView(R.id.webview_two)
    WebView webview_two;
    @BindView(R.id.RL_notice_library)
    RelativeLayout RL_notice;
    @BindView(R.id.RL_notice_content_library)
    RelativeLayout RL_notice_content;
    @BindView(R.id.notice_ListView)
    RecyclerView notice_ListView;
    @BindView(R.id.eightnotice_Listview)
    RecyclerView eightnotice_Listview;

    @BindView(R.id.LL3_library)
    LinearLayout LL3;
    @BindView(R.id.re_layout_library)
    LinearLayout re_layout_library;

    @BindView(R.id.main_mDrawerLayout_library)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.sliding_menu_linearlayout_library)
    LinearLayout sliding_menu_linearlayout;

    @BindView(R.id.scene_tishi_shuaka)
    TextView scene_tishi_shuaka;

    @BindView(R.id.closed_time)
    TextView closed_time;

    @BindView(R.id.listview)
    RecyclerView listview;

    EightNoticeAdapter eightNoticeAdapter;
    LinearLayoutManager llm;
    private NoticeAdapter noticeAdapter;
    private List<NoticeList> noticeList;
    private List<NoticeList> eightNotices;
    private ActionBarDrawerToggle mDrawerToggle;
    private AudioManager mAudioManager;
    private DownloadManager downloadManager;//获取下载管理器
    int current = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WiscClient.DUKAHAO_HANDLER:
                    String kahao = msg.obj.toString();
                    ELog.i("=====kahao===" + kahao);
                    huoqukahao(kahao);
                    break;
                case WiscClient.CLOSED_LibraryDRAWER_HANDLER:
                    int miao = (int) msg.obj;
                    closed_time.setText(miao + "");
                    if (miao == 0 && mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
                        mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
                    }
                    break;
                case WiscClient.NONGLI_HANDLER_Library:
                    tv_date.setText(DateUtil.getTimeyyyyMMdd() + " " + DateUtil.getWeek());
                    LunarCalendar lunarCalender = LunarCalendar.getInstance(DateUtil.getYEAR(), DateUtil.getMONTH(), DateUtil.getDAY_OF_MONTH());
                    tv_nongli.setText("农历 " + lunarCalender.getFullLunarStr());
                    break;
            }


        }
    };

    /*
     * 获取卡号
     * */
    private void huoqukahao(String kahao) {
        if (kahao != null && kahao.length() == 10) {
            if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
                mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            }

            if (WiscClient.isLibraryScene ) {
                //场景切换
                ELog.i("==========场景切换=====00000====");
                libraryMainPresenter.getSeting(kahao.toString().trim(),2);
            } else if (WiscClient.isLibrarySetting) {
                //设置界面
                libraryMainPresenter.getSeting(kahao.toString().trim(),1);
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_library);
        activityComponent().inject(this);
        libraryMainPresenter.attachView(this);
        ButterKnife.bind(this);
        WiscApplication.prefs.setSceneId(3);
        EventBus.getDefault().register(this);
        libraryMainPresenter.getbindRoom(false);//查看绑定图书馆接口

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        TimeThread.getWeather(libraryMainPresenter);
        TimeThread.getCourses(handler, libraryMainPresenter);
        startMQTTService();
        setSlidingMenu();
        setserialport();

        kaoqin_xiangxi.setVisibility(View.GONE);
//        clockView.setVisibility(View.GONE);
        clockView.setOnCurrentTimeListener(new ClockView.OnCurrentTimeListener() {
            @Override
            public void currentTime(String time) {
                tv_time.setText(time);
            }
        });


        tv_date.setText(DateUtil.getTimeyyyyMMdd() + " " + DateUtil.getWeek());
        LunarCalendar lunarCalender = LunarCalendar.getInstance(DateUtil.getYEAR(), DateUtil.getMONTH(), DateUtil.getDAY_OF_MONTH());
        tv_nongli.setText("农历 " + lunarCalender.getFullLunarStr());



        setView();

        setadapter();

//        getHttpData();

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

    private void startMQTTService() {
        Intent mIntent = new Intent();
        mIntent.setAction("com.zhqz.wisc.data.service.MQTTServicer");
        mIntent.setPackage(this.getPackageName());//这里你需要设置你应用的包名
        this.startService(mIntent);
    }

    private void getHttpData() {


//        libraryMainPresenter.getNoticeList();

//        libraryMainPresenter.getLibraryIntroduction();
    }


    LibraryListAdapter libraryListAdapter;
    private MyFragment myFragment;
    public static int mPosition;

    LibraryModel LibraryIntroductionModel;
    @Override
    public void LibraryIntroduction(LibraryModel libraryModel) {
        ELog.d("==stopAutoPlay=="+"setjianjie22222222"+libraryModel.toString());
        LibraryIntroductionModel =libraryModel;
//        initView(libraryModel);
        myFragment.setjianjie(libraryModel);
    }
    List<Librarymodel> librarymodels = new ArrayList<>();;

    private void setView() {

        Librarymodel m = new Librarymodel("图书馆简介及规则",1);
        librarymodels.add(m);
        Librarymodel m1 = new Librarymodel("热书推荐",2);
        librarymodels.add(m1);

// TODO Auto-generated method stub
        listview.setLayoutManager(new LinearLayoutManager(this));
        libraryListAdapter = new LibraryListAdapter(this,librarymodels,this);
        listview.setAdapter(libraryListAdapter);
    }

    /**
     * 初始化view
     */
    private void initView(LibraryModel libraryModel) {

        //创建MyFragment对象
        myFragment = new MyFragment(this,libraryMainPresenter);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, myFragment);

        //通过bundle传值给MyFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyFragment.TAG, librarymodels.get(mPosition));
        bundle.putSerializable(MyFragment.LibraryIntroduction, LibraryIntroductionModel);
        myFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }


    @Override
    public void onLibraryListItemClicked(int position, Librarymodel item) {
        ELog.d("==stopAutoPlay=="+"点击了");
        mPosition = position;
        //即使刷新adapter
        libraryListAdapter.notifyDataSetChanged();
//        for (int i = 0; i < librarymodels.size(); i++) {
            myFragment = new MyFragment(this, libraryMainPresenter);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, myFragment);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MyFragment.TAG, librarymodels.get(mPosition));
            bundle.putSerializable(MyFragment.LibraryIntroduction, LibraryIntroductionModel);
            myFragment.setArguments(bundle);
            fragmentTransaction.commit();
//        }
    }

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


    }

    @OnClick(R.id.LL_notice_library)
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
    protected void onDestroy() {
        TimeThread.setAllClosed();
        super.onDestroy();
    }
    /**
     * action;  //  1是更新  2是删除 9升级
     * content;//通知 班徽 作品的id
     * List<String> target;//设备id
     * type;// 消息类型  1班牌重新绑定  2通知  3班徽   4作品
     */
    @Subscribe
    public void onEventMqtt(LibraryMqttMessageEvent mqttMessageEvent) {
        ELog.i("===LibraryMqttMessageEvent==11==" + mqttMessageEvent.getMsg().toString());
        ELog.i("===LibraryMqttMessageEvent==getdeviceId====" + WiscApplication.prefs.getUdid());
        List<String> targetList = mqttMessageEvent.getMsg().getTarget();
        if (targetList != null && targetList.size() > 0) {
            for (int i = 0; i < targetList.size(); i++) {
                ELog.i("=======LibraryMqttMessageEvent==有该班牌消息======" + targetList.get(i));
                if (targetList.get(i).equals(WiscApplication.prefs.getUdid() + "")) {
                    ELog.i("=========有该班牌消息======" + mqttMessageEvent.getMsg().type);
                    if (mqttMessageEvent.getMsg().type == 2) {
                        ELog.i("======2===有该班牌通知列表消息======");
                        libraryMainPresenter.getNoticeList();
                    }  else if (mqttMessageEvent.getMsg().type == 5) {
                        ELog.i("======5===有该班牌消息======");
                    } else if (mqttMessageEvent.getMsg().type == 1) {
                        ELog.i("======1===升级apk======");
                        if (mqttMessageEvent.getMsg().action == 9) {

//                            mainPresenter.VersionCheck();
                        } else if (mqttMessageEvent.getMsg().action == 1 || mqttMessageEvent.getMsg().action == 7 || mqttMessageEvent.getMsg().action == 8) {
                            libraryMainPresenter.getbindRoom(true);
                        } else if (mqttMessageEvent.getMsg().action == 2) {
                            //删除该班牌,取消定时开关机任务
                            ELog.i("======1===删除该班牌,取消定时开关机任务======");
                            MainTools.disableAlertPowerOff(this);
                            MainTools.disableAlertPowerOn(this);
                            WiscApplication.prefs.setisLibarayBind(false);
                            WiscApplication.prefs.setSceneId(-1);
                            startActivity(new Intent(this, SplashActivity.class));
                            LibraryMainActivity.this.finish();
                            Process.killProcess(Process.myPid());//杀死进程，防止dialog.show()出现错误
                        } else if (mqttMessageEvent.getMsg().action == 6) {//
                            //删除该班牌,取消定时开关机任务
                            ELog.i("======1===强制关机======");
                            fireShutDown();

                        }
                    } else if (mqttMessageEvent.getMsg().type == 3) {
                        ELog.i("======3===有该班牌消息==班徽====");
                        libraryMainPresenter.getBanhui();
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
    public void showNoticeList(List<NoticeList> noticeLists) {
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

    @Override
    public void showSeting(boolean b, int num) {
        if (num ==1){
            //ACTION_DISPLAY_SETTINGS显示界面;ACTION_SETTINGS设置界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            LibraryMainActivity.this.finish();
            Process.killProcess(Process.myPid());//杀死进程，防止dialog.show()出现错误
        }else {
//            mainPresenter.GetSecen();
            startActivity(new Intent(LibraryMainActivity.this, SceneActivity.class));
            finish();
        }
    }
    LibraryReshuModelDao modelDao;
    @Override
    public void showLibraryReshu(List<LibraryReshuModel> libraryReshuModels) {
        modelDao = WiscApplication.getDaoSession().getLibraryReshuModelDao();
        modelDao.deleteAll();
        modelDao.insertInTx(libraryReshuModels);

        myFragment.sedata(libraryReshuModels);
    }

    @Override
    public void bindNewRoom(Room room, boolean isMqtt) {
        initView(null);
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
                    libraryMainPresenter.getBanhui();
                    libraryMainPresenter.getNoticeList();
                    libraryMainPresenter.getLibraryIntroduction();
                }
            } else {
                libraryMainPresenter.getBanhui();
                libraryMainPresenter.getNoticeList();
                libraryMainPresenter.getLibraryIntroduction();
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
            WiscApplication.prefs.setisLibarayBind(false);
            startActivity(new Intent(this, SelectLibraryActivity.class));
            LibraryMainActivity.this.finish();
            Process.killProcess(Process.myPid());//杀死进程，防止dialog.show()出现错误

        }
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

    private void guanBiTongZhiShow() {
        RL_notice.setVisibility(View.GONE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.VISIBLE);
        RL_notice_content_two.setVisibility(View.GONE);
    }

    private void showOneLVNotice(NoticeList notice) {
        TimeThread.stopTimer1();
        if (notice.level == 2) {
            long timecha1 = DateUtil.compare_date(System.currentTimeMillis(), notice.startTime);
            long timecha2 = DateUtil.compare_date(System.currentTimeMillis(), notice.endTime);
            if (timecha1 > 0) {
                //还没开始
                guanBiTongZhiShow();
                TimeThread.getTime1(libraryMainPresenter, notice.startTime);
            } else if (timecha1 <= 0 && timecha2 >= 0) {
                //正在显示
                showNoticeLvOne(notice);
                notice_close.setVisibility(View.GONE);
                TimeThread.getTime1(libraryMainPresenter, notice.endTime);
            } else if (timecha2 < 0) {
                //结束
                guanBiTongZhiShow();
            }
        } else {
            guanBiTongZhiShow();
        }
    }
    private void showNoticeLvOne(NoticeList notice) {
        RL_notice.setVisibility(View.GONE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.GONE);
        RL_notice_content_two.setVisibility(View.VISIBLE);
        webview_one.setWebViewClient(new webViewClient());
        webview_one.loadUrl(notice.url);
//        showCourseTishi();
    }

    private void showTianqiAQI(TianqiAQI tianqiAQI) {
        if (tianqiAQI.data.aqi != null) {
            if (tianqiAQI.data.aqi.value != null) {
//                tv_pm.setText(tianqiAQI.data.aqi.value + " ");
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
    public void onItemClicked(NoticeList item) {
        setClickTongzhi();
    }

    private void setClickTongzhi() {
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        re_layout_library.setVisibility(View.GONE);
        RL_notice.setVisibility(View.VISIBLE);
        RL_notice_content.setVisibility(View.GONE);
    }

    @Override
    public void onNoticeItemClicked(NoticeList item) {
        RL_notice.setVisibility(View.GONE);
        RL_notice_content.setVisibility(View.VISIBLE);
        webview_two.setWebViewClient(new webViewClient());
        webview_two.loadUrl(item.url);
    }

    @OnClick(R.id.notice_close)
    void notice_closeOnclick() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        re_layout_library.setVisibility(View.VISIBLE);
        RL_notice.setVisibility(View.VISIBLE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.VISIBLE);
        RL_notice_content_two.setVisibility(View.GONE);

        //网速太慢的时候，清空历史数据
        webview_one.loadUrl("");
        webview_one.clearHistory();

        webview_two.loadUrl("");
        webview_two.clearHistory();

    }

    @OnClick(R.id.notice_close_two)
    void notice_close_twoOnclick() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        re_layout_library.setVisibility(View.GONE);
        RL_notice.setVisibility(View.VISIBLE);
        RL_notice_content.setVisibility(View.GONE);
        LL3.setVisibility(View.VISIBLE);
        RL_notice_content_two.setVisibility(View.GONE);
        webview_one.loadUrl("");
        webview_one.clearHistory();

        webview_two.loadUrl("");
        webview_two.clearHistory();
    }

    @OnClick(R.id.notice_close_image)
    void notice_close_imageOnclick() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
            return;
        }
        re_layout_library.setVisibility(View.VISIBLE);
        guanBiTongZhiShow();
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

    @OnClick(R.id.btn_select)
    void one_back() {
        if (mDrawerLayout.isDrawerOpen(sliding_menu_linearlayout)) {
            mDrawerLayout.closeDrawer(sliding_menu_linearlayout);
        } else {
            mDrawerLayout.openDrawer(sliding_menu_linearlayout);
        }

    }
    private Handler chaxunHandler;
    private Runnable chaxunRunnable;

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

            scene_tishi_shuaka.setVisibility(View.GONE);
            WiscClient.isLibrarySetting = true;
            WiscClient.isLibraryScene = false;
            chaxunDelayed();
        }

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
        scene_tishi_shuaka.setVisibility(View.VISIBLE);
        WiscClient.isLibraryScene = true;
        WiscClient.isLibrarySetting = false;
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

                scene_tishi_shuaka.setVisibility(View.GONE);
                WiscClient.isLibrarySetting = false;
                WiscClient.isLibraryScene = false;
                chaxunHandler = null;
            }
        };
        chaxunHandler.postDelayed(chaxunRunnable, 5000);
    }
    private Scheduler.Worker bindRoomworker = Schedulers.io().createWorker();
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
                    libraryMainPresenter.getbindRoom(false);
                    bindRoomworker.dispose();
                }
            }, 1000 * 60 * 2, TimeUnit.MILLISECONDS);
        } else {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
