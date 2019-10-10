package com.zhqz.wisc.canteenui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.transformer.CubeOutTransformer;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.canteenui.bind.CanBindActivity;
import com.zhqz.wisc.canteenui.main.GlideImageLoader;
import com.zhqz.wisc.canteenui.todaymenu.TodayMenu;
import com.zhqz.wisc.canteenui.todaymenu.TodayMenuActivity;
import com.zhqz.wisc.data.DbDao.CardInfoDataDao;
import com.zhqz.wisc.data.DbDao.NoWifiDataDao;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.Canteen;
import com.zhqz.wisc.data.model.CardInfoData;
import com.zhqz.wisc.data.model.DakaInfo;
import com.zhqz.wisc.data.model.NoWifiData;
import com.zhqz.wisc.data.model.TransactionDetailSM;
import com.zhqz.wisc.data.model.TransactionTimes;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.scene.SceneActivity;
import com.zhqz.wisc.utils.DateUtil;
import com.zhqz.wisc.utils.DisplayTools;
import com.zhqz.wisc.utils.ELog;
import com.zxy.recovery.core.Recovery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import android_serialport_api.DeviceMonitorService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jingjingtan on 3/22/18.
 * https://github.com/youth5201314/banner
 */

public class CanteenMainActivity extends BaseActivity implements CanteenMvpView {


    @Inject
    CanteenPresenter mMainPresenter;

    @BindView(R.id.wucan_image)
    ImageView wucan_image;
    @BindView(R.id.item_tv)
    TextView item_tv;
    @BindView(R.id.zaocan_tv)
    TextView zaocan_tv;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_cishu)
    TextView tv_cishu;
    @BindView(R.id.tv_xuehao)
    TextView tv_xuehao;
    @BindView(R.id.canting_name)
    TextView canting_name;
    @BindView(R.id.tv_zongcishu)
    TextView tv_zongcishu;

    @BindView(R.id.tv_zongcishu2)
    TextView tv_zongcishu2;
    @BindView(R.id.wucan_image2)
    ImageView wucan_image2;
    @BindView(R.id.item_tv2)
    TextView item_tv2;
    @BindView(R.id.zaocan_tv2)
    TextView zaocan_tv2;
    @BindView(R.id.tv_name2)
    TextView tv_name2;
    @BindView(R.id.tv_cishu2)
    TextView tv_cishu2;
    @BindView(R.id.tv_xuehao2)
    TextView tv_xuehao2;

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.caidanbg)
    LinearLayout caidanbg;
    @BindView(R.id.caidanbg2)
    LinearLayout caidanbg2;


    @BindView(R.id.canteen_today_tishi1)
    TextView canteen_today_tishi;
    @BindView(R.id.canteen_secen_tishi1)
    TextView canteen_secen_tishi1;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WiscClient.TIME_HANDLER:
                    item_tv.setText(DateUtil.getTimeHHmmss());
                    item_tv2.setText(DateUtil.getTimeHHmmss());
                    break;
                case WiscClient.DUKA_HANDLER:
                    String kahao = msg.obj.toString();
                    ELog.i("=====kahao===" + kahao);
                    getCardNumber(kahao);

                    break;
            }
        }
    };

    private void getCardNumber(String kahao) {
        if (kahao != null && kahao.length() == 10) {
            if (isSeting) {
                mMainPresenter.getSeting(kahao, 1);
                return;
            } else if (WiscClient.isCanteenScene) {
                ELog.i("=====场景切换请求数据===" + kahao);
                mMainPresenter.getSeting(kahao, 2);
                return;
            } else if (WiscClient.istoday_menu) {
                WiscApplication.prefs.setCardNumber(kahao);
                mMainPresenter.getSeting(kahao, 3);
                ELog.i("=====场景切换今日菜单===" + kahao);
                return;
            }
            tv_name.setText("");
            tv_xuehao.setText("");
            tv_cishu.setText("");
            tv_name2.setText("");
            tv_xuehao2.setText("");
            tv_cishu2.setText("");
            if (isDaka) {
                duqukahao(kahao);
            } else {
                Toast.makeText(CanteenMainActivity.this, "现在不是吃饭时间", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private CanteenPromptDialog promptDialog;
    private MediaPlayer mp;
    private TransactionTimes zaocan;
    private TransactionTimes zhongcan;
    private int mtimesId;
    private int mtype;
    private boolean isDaka = false;
    private List<TransactionDetailSM> transactionDetailSMs;
    private int zhongcishu;
    private long oldTime = 0;
    private DakaErrorDialog dakaErrorDialog;
    private SyndataDialog syndataDialog;
    private TransactionTimes wancan;
    private boolean isSeting;
    private Handler chaxunHandler;
    private Runnable chaxunRunnable;
    public static List<?> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    public static List<String> imageslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_activity_main);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        mMainPresenter.attachView(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        WiscClient.istoday_menu = false;
        WiscClient.isCanteenScene = false;

        //模式
        mMainPresenter.getMode();

        setViewInfo();


    }

    /*
     * 场景切换
     * */
    @OnClick(R.id.canteen_button_secen)
    void canteen_button_secen() {
        ELog.d("场景切换===食堂=弹框");
        ELog.i("==========场景切换=====2222====");
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        WiscClient.isCanteenScene = true;
        WiscClient.istoday_menu = false;
        canteen_secen_tishi1.setVisibility(View.GONE);
        canteen_today_tishi.setVisibility(View.GONE);
        chaxunDelayed();
//        Message msg = new Message();
//        msg.obj = "1111111111";
//        msg.what = WiscClient.DUKA_HANDLER;
//        handler.sendMessage(msg);

    }


    /*
     * 今天菜单
     * */
    @OnClick(R.id.today_menu_canteen)
    void MenuOnClick() {
        ELog.d("今日菜单===食堂=dddddddd");
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        WiscClient.istoday_menu = true;
        WiscClient.isCanteenScene = false;
        canteen_today_tishi.setVisibility(View.VISIBLE);
        canteen_secen_tishi1.setVisibility(View.GONE);
        chaxunDelayed();
//        Message msg = new Message();
//        msg.obj = "1111111111";
//        msg.what = WiscClient.DUKA_HANDLER;
//        handler.sendMessage(msg);
    }


    private void initBanner() {
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(CanteenMainActivity.class)
                .init(this);

//        String[] urls = getResources().getStringArray(R.array.url);
//        String[] tips = getResources().getStringArray(R.array.title);
//        List list = Arrays.asList(urls);
//        images = new ArrayList<>(list);
//        titles= Arrays.asList(tips);

//        ArrayList<String> titles = new ArrayList<>(Arrays.asList(new String[]{"first title", "second title", "third title", "fourth title", "第五张图片", "第6张图片", "第7张图片", "第8张图片", "第9张图片", "第10张图片"}));
        mBanner.setImages(images)
                .setBannerAnimation(CubeOutTransformer.class)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setBannerTitles(titles)
                .setImageLoader(new GlideImageLoader())
                .start();

    }

    private void synData() {
        mMainPresenter.syndata();
        if (syndataDialog == null) {
            syndataDialog = new SyndataDialog(this);
        }
        if (syndataDialog != null) {
            syndataDialog.show();
            syndataDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            syndataDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    @Override
    public void syndataSuccess() {
        if (syndataDialog != null) {
            syndataDialog.dismiss();
            syndataDialog = null;
        }
    }

    private void setViewInfo() {
        CanteenTimerUtils.getTime(handler);
        mMainPresenter.getCanteen(false);
        initMusic();
        setserialport();
        transactionDetailSMs = new ArrayList<TransactionDetailSM>();
    }

    private void setserialport() {
        DeviceMonitorService.flow1().subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((kahao) -> {
                    Message msg = new Message();
                    msg.obj = kahao.trim().toString();
                    msg.what = WiscClient.DUKA_HANDLER;
                    handler.sendMessage(msg);
                });
    }


    private void sendNoWifiData() {
        NoWifiDataDao noWifiDataDao = WiscApplication.getDaoSession().getNoWifiDataDao();
        if (noWifiDataDao.loadAll().size() != 0) {
            transactionDetailSMs.clear();
            for (int i = 0; i < noWifiDataDao.loadAll().size(); i++) {
                transactionDetailSMs.add(new TransactionDetailSM(noWifiDataDao.loadAll().get(i).code,
                        WiscApplication.prefs.getCanteenPostId(),
                        WiscApplication.prefs.getCanteenId(),
                        noWifiDataDao.loadAll().get(i).cardNum,
                        noWifiDataDao.loadAll().get(i).timesId,
                        noWifiDataDao.loadAll().get(i).num,
                        noWifiDataDao.loadAll().get(i).transactionTime));
            }
            ELog.i("==============sendNoWifiData=========" + transactionDetailSMs.size());
            mMainPresenter.daka(null, transactionDetailSMs);
        }

    }

    @Override
    public void showCanteenInfoError(String s) {
        tv_name.setText("");
        tv_xuehao.setText("");
        tv_cishu.setText("");
        tv_name2.setText("");
        tv_xuehao2.setText("");
        tv_cishu2.setText("");
        String error = "获取食堂详情 bug : " + s;
        MobclickAgent.reportError(CanteenMainActivity.this, error.toString());
        //Toast.makeText(CanteenMainActivity.this, s, Toast.LENGTH_LONG).show();
        canting_name.setText("欢迎光临" + WiscApplication.prefs.getCanteenName());
        if (DateUtil.comparetime(WiscApplication.prefs.getZaocanStartTime(), DateUtil.getTimeHHmmss()) > 0) {
            zhongcishu = 0;
            WiscApplication.prefs.setZhongcishu(zhongcishu);
            zaocan_tv.setText("");
            zaocan_tv2.setText("");
            wucan_image.setVisibility(View.GONE);
            wucan_image2.setVisibility(View.GONE);
            CanteenTimerUtils.getTimer(mMainPresenter, WiscApplication.prefs.getZaocanStartTime());
            isDaka = false;
            sendNoWifiData();
        } else if (DateUtil.comparetime(WiscApplication.prefs.getZaocanStartTime(), DateUtil.getTimeHHmmss()) <= 0
                && DateUtil.comparetime(WiscApplication.prefs.getZaocanEndTime(), DateUtil.getTimeHHmmss()) >= 0) {
            zaocan_tv.setText("早餐");
            zaocan_tv2.setText("早餐");
            wucan_image.setVisibility(View.VISIBLE);
            wucan_image2.setVisibility(View.VISIBLE);
            mtimesId = WiscApplication.prefs.getZaocanTimesId();
            mtype = 1;
            isDaka = true;
            if (WiscApplication.prefs.getZhongcishu() != 0 && WiscApplication.prefs.getZhongcishuTiem() != null) {
                if (DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZaocanStartTime()) >= 0
                        && DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZaocanEndTime()) <= 0) {
                    zhongcishu = WiscApplication.prefs.getZhongcishu();
                } else {
                    zhongcishu = 0;
                }
            } else {
                zhongcishu = 0;
            }
            CanteenTimerUtils.getTimer(mMainPresenter, WiscApplication.prefs.getZaocanEndTime());
            synData();
        } else if (DateUtil.comparetime(WiscApplication.prefs.getZaocanEndTime(), DateUtil.getTimeHHmmss()) < 0
                && DateUtil.comparetime(WiscApplication.prefs.getZhongcanStartTime(), DateUtil.getTimeHHmmss()) > 0) {
            zhongcishu = 0;
            WiscApplication.prefs.setZhongcishu(zhongcishu);
            zaocan_tv.setText("");
            zaocan_tv2.setText("");
            wucan_image.setVisibility(View.GONE);
            wucan_image2.setVisibility(View.GONE);
            isDaka = false;
            CanteenTimerUtils.getTimer(mMainPresenter, WiscApplication.prefs.getZhongcanStartTime());
            sendNoWifiData();
        } else if (DateUtil.comparetime(WiscApplication.prefs.getZhongcanStartTime(), DateUtil.getTimeHHmmss()) <= 0
                && DateUtil.comparetime(WiscApplication.prefs.getZhongcanEndTime(), DateUtil.getTimeHHmmss()) >= 0) {
            zaocan_tv.setText("午餐");
            zaocan_tv2.setText("午餐");
            wucan_image.setVisibility(View.VISIBLE);
            wucan_image2.setVisibility(View.VISIBLE);
            mtimesId = WiscApplication.prefs.getZhongcanTimesId();
            mtype = 2;
            isDaka = true;
            if (WiscApplication.prefs.getZhongcishu() != 0 && WiscApplication.prefs.getZhongcishuTiem() != null) {
                if (DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZhongcanStartTime()) >= 0
                        && DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZhongcanEndTime()) <= 0) {
                    zhongcishu = WiscApplication.prefs.getZhongcishu();
                } else {
                    zhongcishu = 0;
                }
            } else {
                zhongcishu = 0;
            }
            CanteenTimerUtils.getTimer(mMainPresenter, WiscApplication.prefs.getZhongcanEndTime());
            synData();
        } else if (DateUtil.comparetime(WiscApplication.prefs.getZhongcanEndTime(), DateUtil.getTimeHHmmss()) < 0
                && DateUtil.comparetime(WiscApplication.prefs.getWancanStartTime(), DateUtil.getTimeHHmmss()) > 0) {
            zhongcishu = 0;
            WiscApplication.prefs.setZhongcishu(zhongcishu);
            zaocan_tv.setText("");
            zaocan_tv2.setText("");
            wucan_image.setVisibility(View.GONE);
            wucan_image2.setVisibility(View.GONE);
            isDaka = false;
            CanteenTimerUtils.getTimer(mMainPresenter, WiscApplication.prefs.getWancanStartTime());
            sendNoWifiData();
        } else if (DateUtil.comparetime(WiscApplication.prefs.getWancanStartTime(), DateUtil.getTimeHHmmss()) <= 0
                && DateUtil.comparetime(WiscApplication.prefs.getWancanEndTime(), DateUtil.getTimeHHmmss()) >= 0) {
            zaocan_tv.setText("晚餐");
            zaocan_tv2.setText("晚餐");
            wucan_image.setVisibility(View.VISIBLE);
            wucan_image2.setVisibility(View.VISIBLE);
            mtimesId = WiscApplication.prefs.getWancanTimesId();
            mtype = 3;
            isDaka = true;
            if (WiscApplication.prefs.getZhongcishu() != 0 && WiscApplication.prefs.getZhongcishuTiem() != null) {
                if (DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getWancanStartTime()) >= 0
                        && DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getWancanEndTime()) <= 0) {
                    zhongcishu = WiscApplication.prefs.getZhongcishu();
                } else {
                    zhongcishu = 0;
                }
            } else {
                zhongcishu = 0;
            }
            CanteenTimerUtils.getTimer(mMainPresenter, WiscApplication.prefs.getWancanEndTime());
            synData();
        } else if (DateUtil.comparetime(WiscApplication.prefs.getWancanEndTime(), DateUtil.getTimeHHmmss()) < 0) {
            zhongcishu = 0;
            WiscApplication.prefs.setZhongcishu(zhongcishu);
            zaocan_tv.setText("");
            zaocan_tv2.setText("");
            wucan_image.setVisibility(View.GONE);
            wucan_image2.setVisibility(View.GONE);
            isDaka = false;
            CanteenTimerUtils.getTimer(mMainPresenter, WiscApplication.prefs.getZaocanStartTime());
            sendNoWifiData();
        }
        tv_zongcishu.setText("总第" + zhongcishu + "次");
        tv_zongcishu2.setText("总第" + zhongcishu + "次");
    }

    @Override
    public void showCanteenInfo(Canteen canteen, boolean b) {
        tv_name.setText("");
        tv_xuehao.setText("");
        tv_cishu.setText("");
        tv_name2.setText("");
        tv_xuehao2.setText("");
        tv_cishu2.setText("");
        WiscApplication.prefs.setCanteenName(canteen.canteenName);
        canting_name.setText("欢迎光临" + WiscApplication.prefs.getCanteenName());
        if (canteen.transactionTimes.size() != 0) {
            for (int i = 0; i < canteen.transactionTimes.size(); i++) {
                if (canteen.transactionTimes.get(i).num == 1) {
                    zaocan = canteen.transactionTimes.get(i);
                    WiscApplication.prefs.setZaocanStartTime(zaocan.startTime);
                    WiscApplication.prefs.setZaocanEndTime(zaocan.endTime);
                    WiscApplication.prefs.setZaocanTimesId(zaocan.timesId);
                } else if (canteen.transactionTimes.get(i).num == 2) {
                    zhongcan = canteen.transactionTimes.get(i);
                    WiscApplication.prefs.setZhongcanStartTime(zhongcan.startTime);
                    WiscApplication.prefs.setZhongcanEndTime(zhongcan.endTime);
                    WiscApplication.prefs.setZhongcanTimesId(zhongcan.timesId);
                } else if (canteen.transactionTimes.get(i).num == 3) {
                    wancan = canteen.transactionTimes.get(i);
                    WiscApplication.prefs.setWancanStartTime(wancan.startTime);
                    WiscApplication.prefs.setWancanEndTime(wancan.endTime);
                    WiscApplication.prefs.setWancanTimesId(wancan.timesId);
                }
            }
            if (DateUtil.comparetime(zaocan.startTime, DateUtil.getTimeHHmmss()) > 0) {
                zhongcishu = 0;
                WiscApplication.prefs.setZhongcishu(zhongcishu);
                zaocan_tv.setText("");
                zaocan_tv2.setText("");
                wucan_image.setVisibility(View.GONE);
                wucan_image2.setVisibility(View.GONE);
                CanteenTimerUtils.getTimer(mMainPresenter, zaocan.startTime);//定时器作用，判断早晨、午餐、晚餐什么时候开始结束
                isDaka = false;
                sendNoWifiData();
            } else if (DateUtil.comparetime(zaocan.startTime, DateUtil.getTimeHHmmss()) <= 0 && DateUtil.comparetime(zaocan.endTime, DateUtil.getTimeHHmmss()) >= 0) {
                zaocan_tv.setText("早餐");
                zaocan_tv2.setText("早餐");
                wucan_image.setVisibility(View.VISIBLE);
                wucan_image2.setVisibility(View.VISIBLE);
                mtimesId = zaocan.timesId;
                mtype = zaocan.num;
                if (b) {
                    showqueren(0);
                }
                isDaka = true;
                if (WiscApplication.prefs.getZhongcishu() != 0 && WiscApplication.prefs.getZhongcishuTiem() != null) {
                    if (DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZaocanStartTime()) >= 0
                            && DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZaocanEndTime()) <= 0) {
                        zhongcishu = WiscApplication.prefs.getZhongcishu();
                    } else {
                        zhongcishu = 0;
                    }
                } else {
                    zhongcishu = 0;
                }
                CanteenTimerUtils.getTimer(mMainPresenter, zaocan.endTime);
                synData();
            } else if (DateUtil.comparetime(zaocan.endTime, DateUtil.getTimeHHmmss()) < 0 && DateUtil.comparetime(zhongcan.startTime, DateUtil.getTimeHHmmss()) > 0) {
                zhongcishu = 0;
                WiscApplication.prefs.setZhongcishu(zhongcishu);
                zaocan_tv.setText("");
                zaocan_tv2.setText("");
                wucan_image.setVisibility(View.GONE);
                wucan_image2.setVisibility(View.GONE);
                isDaka = false;
                CanteenTimerUtils.getTimer(mMainPresenter, zhongcan.startTime);
                sendNoWifiData();
            } else if (DateUtil.comparetime(zhongcan.startTime, DateUtil.getTimeHHmmss()) <= 0 && DateUtil.comparetime(zhongcan.endTime, DateUtil.getTimeHHmmss()) >= 0) {
                zaocan_tv.setText("午餐");
                zaocan_tv2.setText("午餐");
                wucan_image.setVisibility(View.VISIBLE);
                wucan_image2.setVisibility(View.VISIBLE);
                mtimesId = zhongcan.timesId;
                mtype = zhongcan.num;
                if (b) {
                    showqueren(1);
                }
                isDaka = true;
                if (WiscApplication.prefs.getZhongcishu() != 0 && WiscApplication.prefs.getZhongcishuTiem() != null) {
                    if (DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZhongcanStartTime()) >= 0
                            && DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getZhongcanEndTime()) <= 0) {
                        zhongcishu = WiscApplication.prefs.getZhongcishu();
                    } else {
                        zhongcishu = 0;
                    }
                } else {
                    zhongcishu = 0;
                }
                CanteenTimerUtils.getTimer(mMainPresenter, zhongcan.endTime);
                synData();
            } else if (DateUtil.comparetime(zhongcan.endTime, DateUtil.getTimeHHmmss()) < 0 && DateUtil.comparetime(wancan.startTime, DateUtil.getTimeHHmmss()) > 0) {
                zhongcishu = 0;
                WiscApplication.prefs.setZhongcishu(zhongcishu);
                zaocan_tv.setText("");
                wucan_image.setVisibility(View.GONE);
                zaocan_tv2.setText("");
                wucan_image2.setVisibility(View.GONE);
                isDaka = false;
                CanteenTimerUtils.getTimer(mMainPresenter, wancan.startTime);
                sendNoWifiData();
            } else if (DateUtil.comparetime(wancan.startTime, DateUtil.getTimeHHmmss()) <= 0 && DateUtil.comparetime(wancan.endTime, DateUtil.getTimeHHmmss()) >= 0) {
                zaocan_tv.setText("晚餐");
                wucan_image.setVisibility(View.VISIBLE);
                zaocan_tv2.setText("晚餐");
                wucan_image2.setVisibility(View.VISIBLE);
                mtimesId = wancan.timesId;
                mtype = wancan.num;
                if (b) {
                    showqueren(2);
                }
                isDaka = true;
                if (WiscApplication.prefs.getZhongcishu() != 0 && WiscApplication.prefs.getZhongcishuTiem() != null) {
                    if (DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getWancanStartTime()) >= 0
                            && DateUtil.compare_date_two(WiscApplication.prefs.getZhongcishuTiem(), DateUtil.getTimeyyyyMMdd() + " " + WiscApplication.prefs.getWancanEndTime()) <= 0) {
                        zhongcishu = WiscApplication.prefs.getZhongcishu();
                    } else {
                        zhongcishu = 0;
                    }
                } else {
                    zhongcishu = 0;
                }
                CanteenTimerUtils.getTimer(mMainPresenter, wancan.endTime);
                synData();
            } else if (DateUtil.comparetime(wancan.endTime, DateUtil.getTimeHHmmss()) < 0) {
                zhongcishu = 0;
                WiscApplication.prefs.setZhongcishu(zhongcishu);
                zaocan_tv.setText("");
                wucan_image.setVisibility(View.GONE);
                zaocan_tv2.setText("");
                wucan_image2.setVisibility(View.GONE);
                isDaka = false;
                CanteenTimerUtils.getTimer(mMainPresenter, zaocan.startTime);
                sendNoWifiData();
            }
            tv_zongcishu.setText("总第" + zhongcishu + "次");
            tv_zongcishu2.setText("总第" + zhongcishu + "次");
        } else {
            isDaka = false;
            zhongcishu = 0;
            WiscApplication.prefs.setZhongcishuTiem(DateUtil.getCurrentTime());
            WiscApplication.prefs.setZhongcishu(0);
            tv_zongcishu.setText("总第" + zhongcishu + "次");
            tv_zongcishu2.setText("总第" + zhongcishu + "次");
        }
    }

    private void initMusic() {
        mp = MediaPlayer.create(this, R.raw.chenggong);//这时就不用调用setDataSource了
    }


    private void showqueren(int i) {
        if (promptDialog == null) {
            if (i == 0) {
                promptDialog = new CanteenPromptDialog(this, CanteenPromptDialog.ZAOCAN);
            } else if (i == 1) {
                promptDialog = new CanteenPromptDialog(this, CanteenPromptDialog.WUCAN);
            } else if (i == 2) {
                promptDialog = new CanteenPromptDialog(this, CanteenPromptDialog.WANCAN);
            }
        }
        if (promptDialog != null) {
            promptDialog.show();
            promptDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    switch (promptDialog.mIndex) {
                        case 1://确认
                            promptDialog = null;
                            break;
                    }
                }
            });
        }

    }

    private void duqukahao(String cardNumber) {
        if (System.currentTimeMillis() - oldTime < 1000) {
            return;
        }
        oldTime = System.currentTimeMillis();
        if (dakaErrorDialog != null) {
            dakaErrorDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }
        TransactionDetailSM transactionDetailSM = new TransactionDetailSM(UUID.randomUUID().toString(),
                WiscApplication.prefs.getCanteenPostId(),
                WiscApplication.prefs.getCanteenId(),
                cardNumber, mtimesId, mtype, DateUtil.getCurrentTime());
        if (DisplayTools.isNetworkConnected(WiscApplication.context)) {
            NoWifiDataDao noWifiDataDao = WiscApplication.getDaoSession().getNoWifiDataDao();
            if (noWifiDataDao.loadAll().size() != 0) {
                transactionDetailSMs.clear();
                for (int i = 0; i < noWifiDataDao.loadAll().size(); i++) {
                    transactionDetailSMs.add(new TransactionDetailSM(noWifiDataDao.loadAll().get(i).code,
                            WiscApplication.prefs.getCanteenPostId(),
                            WiscApplication.prefs.getCanteenId(),
                            noWifiDataDao.loadAll().get(i).cardNum,
                            noWifiDataDao.loadAll().get(i).timesId,
                            noWifiDataDao.loadAll().get(i).num,
                            noWifiDataDao.loadAll().get(i).transactionTime));
                }
                mMainPresenter.daka(transactionDetailSM, transactionDetailSMs);
            } else {
                mMainPresenter.daka(transactionDetailSM, null);
            }
        } else {
//            meiWifi(transactionDetailSM);
            showMeiWifidata(cardNumber,transactionDetailSM);
        }

    }

    private void showMeiWifidata(String cardNumber,TransactionDetailSM transactionDetailSM) {
        CardInfoDataDao cardInfoDataDao = WiscApplication.getDaoSession().getCardInfoDataDao();
        if (cardInfoDataDao.loadAll().size() != 0) {
            List<CardInfoData> attendance = cardInfoDataDao.queryBuilder()
                    .where(CardInfoDataDao.Properties.CardNum.eq(cardNumber))
                    .orderAsc(CardInfoDataDao.Properties.CustomerId)
                    .list();
            if (attendance.size() != 0) {
                tv_name.setText(attendance.get(0).getName());
                tv_name2.setText(attendance.get(0).getName());
                if (attendance.get(0).getClassName() == null) {
                    tv_xuehao.setText(cardNumber);
                    tv_xuehao2.setText(cardNumber);
                } else {
                    tv_xuehao.setText(attendance.get(0).getClassName());
                    tv_xuehao2.setText(attendance.get(0).getClassName());
                }
                if (mtype == 1) {
                    attendance.get(0).setZaoSumNum(attendance.get(0).getZaoSumNum() + 1);
                    tv_cishu.setText(attendance.get(0).getZaoSumNum() + "次");
                    tv_cishu2.setText(attendance.get(0).getZaoSumNum() + "次");
                } else if (mtype == 2) {
                    attendance.get(0).setZhongSumNum(attendance.get(0).getZhongSumNum() + 1);
                    tv_cishu.setText(attendance.get(0).getZhongSumNum() + "次");
                    tv_cishu2.setText(attendance.get(0).getZhongSumNum() + "次");
                } else if (mtype == 3) {
                    attendance.get(0).setWanSumNum(attendance.get(0).getWanSumNum() + 1);
                    tv_cishu.setText(attendance.get(0).getWanSumNum() + "次");
                    tv_cishu2.setText(attendance.get(0).getWanSumNum() + "次");
                }
                meiWifi(transactionDetailSM);//把有卡号的数据存储到数据库,等待下次打卡的时候上传
            } else {
                tv_xuehao.setText(cardNumber);
                tv_name.setText("此卡存在问题");
                tv_xuehao2.setText(cardNumber);
                tv_name2.setText("此卡存在问题");
            }
        } else {
            tv_xuehao.setText(cardNumber);
            tv_xuehao2.setText(cardNumber);
        }
        zhongcishu++;
        WiscApplication.prefs.setZhongcishuTiem(DateUtil.getCurrentTime());
        WiscApplication.prefs.setZhongcishu(zhongcishu);
        tv_zongcishu.setText("总第" + zhongcishu + "次");
        tv_zongcishu2.setText("总第" + zhongcishu + "次");
    }

    private void meiWifi(TransactionDetailSM transactionDetailSMs) {
        NoWifiDataDao noWifiDataDao = WiscApplication.getDaoSession().getNoWifiDataDao();
        noWifiDataDao.insert(new NoWifiData(transactionDetailSMs.code,
                transactionDetailSMs.cardNum,
                transactionDetailSMs.timesId,
                transactionDetailSMs.num,
                transactionDetailSMs.transactionTime));
    }


    private void setMusic() {
        try {
            if (mp.isPlaying()) {
                mp.pause();
                mp.seekTo(0);
            }
            mp.start();
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    mp.reset();
                    return true;
                }
            });
        } catch (Exception e) {
            mp.reset();
        }

    }

    @Override
    public void showNoWifiDakaInfo() {
        NoWifiDataDao noWifiDataDao = WiscApplication.getDaoSession().getNoWifiDataDao();
        noWifiDataDao.deleteAll();
    }

    @Override
    public void showDakaInfo(DakaInfo dakaInfo) {
        zhongcishu++;
        WiscApplication.prefs.setZhongcishuTiem(DateUtil.getCurrentTime());
        WiscApplication.prefs.setZhongcishu(zhongcishu);
        tv_name.setText(dakaInfo.name);
        tv_name2.setText(dakaInfo.name);
        if (dakaInfo.className == null) {
            tv_xuehao.setText(dakaInfo.cardNum);
            tv_xuehao2.setText(dakaInfo.cardNum);
        } else {
            tv_xuehao.setText(dakaInfo.className);
            tv_xuehao2.setText(dakaInfo.className);
        }

        if (1 == mtype) {
            tv_cishu.setText(dakaInfo.wallets1.sumNum + "次");
            tv_cishu2.setText(dakaInfo.wallets1.sumNum + "次");
        } else if (2 == mtype) {
            tv_cishu.setText(dakaInfo.wallets2.sumNum + "次");
            tv_cishu2.setText(dakaInfo.wallets2.sumNum + "次");
        } else if (3 == mtype) {
            tv_cishu.setText(dakaInfo.wallets3.sumNum + "次");
            tv_cishu2.setText(dakaInfo.wallets3.sumNum + "次");
        }

        tv_zongcishu.setText("总第" + zhongcishu + "次");
        tv_zongcishu2.setText("总第" + zhongcishu + "次");
    }

    @Override
    public void showDakaConnectException(TransactionDetailSM transactionDetailSM, List<TransactionDetailSM> noWifiDatas) {
        if (transactionDetailSM != null) {
//            meiWifi(transactionDetailSM);
            showMeiWifidata(transactionDetailSM.cardNum,transactionDetailSM);
        }
    }

    @Override
    public void showErrorDaka(String s) {
        String error = "打卡 bug : " + s;
        MobclickAgent.reportError(CanteenMainActivity.this, error.toString());
        setMusic();
        //Toast.makeText(CanteenMainActivity.this, s, Toast.LENGTH_SHORT).show();
        if (dakaErrorDialog == null) {
            dakaErrorDialog = new DakaErrorDialog(this);
        }
        if (dakaErrorDialog != null) {
            dakaErrorDialog.show();
            dakaErrorDialog.setContents(s);
        }

    }

    @Override
    public void bindReset() {
        WiscApplication.prefs.setCanteenId(-1);
        WiscApplication.prefs.setCanteenPostId(-1);
        WiscApplication.prefs.setZhongcishu(0);
        WiscApplication.prefs.setIsSelectCanteen(false);
        startActivity(new Intent(CanteenMainActivity.this, CanBindActivity.class));
        finish();
    }

    @OnClick(R.id.canting_name)
    void canting_nameseting() {
        ELog.i("==========场景切换==设置界面===2222====");
        isSeting = true;
        WiscClient.istoday_menu = false;
        WiscClient.isCanteenScene = false;
        canteen_today_tishi.setVisibility(View.GONE);
        canteen_secen_tishi1.setVisibility(View.GONE);
        chaxunDelayed();
    }

    private void chaxunDelayed() {
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        chaxunHandler = new Handler();
        chaxunRunnable = new Runnable() {
            @Override
            public void run() {
                isSeting = false;
                canteen_today_tishi.setVisibility(View.GONE);
//                canteen_secen_tishi.setVisibility(View.GONE);
                canteen_secen_tishi1.setVisibility(View.GONE);
                WiscClient.istoday_menu = false;
                WiscClient.isCanteenScene = false;
                chaxunHandler = null;
            }
        };
        chaxunHandler.postDelayed(chaxunRunnable, 5000);
    }

    @Override
    public void showSeting(boolean b, int num) { // 1设置界面 2场景切换弹框请求数据
        if (num == 1) {
            //ACTION_DISPLAY_SETTINGS显示界面;ACTION_SETTINGS设置界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            CanteenMainActivity.this.finish();
            Process.killProcess(Process.myPid());//杀死进程，防止dialog.show()出现错误
        } else if (num == 2) {
//            mMainPresenter.GetSecen();
            startActivity(new Intent(CanteenMainActivity.this, SceneActivity.class));
            finish();
        } else if (num == 3) { // 今日菜单
            ELog.i("==========场景切换==今日菜单===2222====");
            startActivity(new Intent(CanteenMainActivity.this, TodayMenuActivity.class));
            finish();
        }


    }

    @Override
    public void showErrorSetingDaka(String s) {
        Toast.makeText(CanteenMainActivity.this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setTodayMenu(List<TodayMenu> b, boolean isSucess) {
//        ELog.i("==========场景切换==显示今日菜单===2222===="+b.toString());
        images.clear();
        titles.clear();
        imageslist.clear();
        if (b == null || b.size() <= 0) {//默认图图片
            ELog.i("==========场景切换==显示今日菜单===eee====");
            Integer[] qq = {R.mipmap.splash_a, R.mipmap.splash_b};
            images = new ArrayList<>(Arrays.asList(qq));
            titles.add("默认菜单标题一");
            titles.add("默认菜单标题二");
//            for (int j = 0; j < images.size(); j++) {
//                ELog.i("==========场景切换==显示今日菜单===eee====" + images.get(j));
//            }

        } else {
            ELog.i("==========场景切换==显示今日菜单==rrrr====");
            for (int i = 0; i < b.size(); i++) {
                titles.add(b.get(i).name);
                imageslist.add(b.get(i).url);
            }
            images = new ArrayList<>(imageslist);
//            for (int j = 0; j < images.size(); j++) {
//                ELog.i("==========场景切换==显示今日菜单===rrrr====" + images.get(j));
//            }
        }
        initBanner(); // 食堂菜单自动播放

    }


    @SuppressLint("NewApi")
    @Override
    public void modelNum(String num) {
        if (num.equals("2.0") || num.equals("2")) {//图片
            ELog.i("============getSeting===模式===2.0=======" + num);
            caidanbg.setVisibility(View.VISIBLE);
            caidanbg2.setVisibility(View.GONE);
            mMainPresenter.getToday_menu();

        } else if (num.equals("1.0") || num.equals("1")) { // 文字
            ELog.i("============getSeting===模式===1.0=======" + num);
            caidanbg.setVisibility(View.GONE);
            caidanbg2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        if (chaxunHandler != null) {
            chaxunHandler.removeCallbacks(chaxunRunnable);
            chaxunHandler = null;
        }
        mMainPresenter.detachView();//？
        mBanner.stopAutoPlay();
        handler.removeCallbacks(null);
        super.onDestroy();
    }
}