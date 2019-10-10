package com.zhqz.wisc.canteenui;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.canteenui.todaymenu.TodayMenu;
import com.zhqz.wisc.data.DbDao.CardInfoDataDao;
import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.Canteen;
import com.zhqz.wisc.data.model.CardInfoData;
import com.zhqz.wisc.data.model.DakaInfo;
import com.zhqz.wisc.data.model.DakaRequest;
import com.zhqz.wisc.data.model.TransactionDetailSM;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.utils.ELog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import retrofit2.HttpException;

/**
 * Created by jingjingtan on 3/22/18.
 */

public class CanteenPresenter implements Presenter<CanteenMvpView> {


    private static CanteenMvpView mMvpView;
    private static WiscClient canteenClient;
    private Disposable disposableSeting;

    @Inject
    public CanteenPresenter(WiscClient client) {
        this.canteenClient = client;
    }

    @Override
    public void attachView(CanteenMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (disposableSeting != null && !disposableSeting.isDisposed()) {
            disposableSeting.dispose();
        }
    }

    public void getCanteen(boolean b) {

        canteenClient.getCanteen()
                .doOnNext(cantingHttpResult -> {

                    if (!cantingHttpResult.code.equals("200") || cantingHttpResult.getData() == null) {
                        throw Exceptions.propagate(new ClientRuntimeException(cantingHttpResult.code, cantingHttpResult.msg));
                    }
                })
                .map(deviceHttpResult -> deviceHttpResult.getData())
                .subscribe(new Observer<Canteen>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Canteen canteen) {
                        ELog.i("==============Canteen==onNext=======" + canteen.toString());
                        if (mMvpView != null) {
                            if (canteen != null && canteen.canteenId != 0) {
                                mMvpView.showCanteenInfo(canteen, b);
                            } else {
                                mMvpView.bindReset();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("==============Canteen==onError=======" + e.toString());
                        mMvpView.showCanteenInfoError(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /*
    * transactionDetailSM 有网传参
    * noWifiDatas 没网传参
    * */

    public void daka(TransactionDetailSM transactionDetailSM, List<TransactionDetailSM> noWifiDatas) {//
        canteenClient.sendTransactionDetail(transactionDetailSM, noWifiDatas)
                .doOnNext(cantingHttpResult -> {

                    if (!cantingHttpResult.code.equals("200") || cantingHttpResult.getData() == null) {
                        throw Exceptions.propagate(new ClientRuntimeException(cantingHttpResult.code, cantingHttpResult.msg));
                    }
                })
                .map(deviceHttpResult -> deviceHttpResult.getData())
                .subscribe(new Observer<DakaRequest>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DakaRequest dakaRequest) {
                        ELog.i("==============daka==onNext=======" + dakaRequest.toString());
                        if (dakaRequest != null && dakaRequest.dakaInfo != null) {
                            mMvpView.showDakaInfo(dakaRequest.dakaInfo);
                        }
                        if (dakaRequest != null && dakaRequest.synchroCode != null && dakaRequest.synchroCode.equals("200")) {
                            mMvpView.showNoWifiDakaInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("==============daka==onError=======" + e.toString());
                        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
                        boolean isConnectException = e.getClass().equals(ConnectException.class);
                        boolean isHttpException = e.getClass().equals(HttpException.class);
                        if (isSocketException || isConnectException || isHttpException) {
                            mMvpView.showDakaConnectException(transactionDetailSM, noWifiDatas);
                        } else {
                            mMvpView.showErrorDaka(e.toString());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void syndata() {
        canteenClient.syndata()
                .doOnNext(syndataHttpResult -> {

                    if (!syndataHttpResult.code.equals("200") || syndataHttpResult.getData() == null) {
                        throw Exceptions.propagate(new ClientRuntimeException(syndataHttpResult.code, syndataHttpResult.msg));
                    }
                })
                .map(syndataHttpResult -> syndataHttpResult.getData())
                .subscribe(new Observer<List<DakaInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<DakaInfo> dakaInfos) {
                        ELog.i("==============syndata==onNext=======" + dakaInfos.size());
                        if (dakaInfos.size() != 0) {
                            syndatas(dakaInfos);
                        } else {
                            mMvpView.syndataSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("==============syndata==onError=======" + e.toString());
                        mMvpView.syndataSuccess();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void syndatas(List<DakaInfo> dakaInfos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CardInfoDataDao cardInfoDataDao = WiscApplication.getDaoSession().getCardInfoDataDao();
                if (cardInfoDataDao.loadAll().size() != 0) {
                    cardInfoDataDao.deleteAll();
                }
                for (int i = 0; i < dakaInfos.size(); i++) {
                    if (dakaInfos.get(i).wallets1 != null && dakaInfos.get(i).wallets2 != null && dakaInfos.get(i).wallets3 != null) {
                        int zaoSumNum = dakaInfos.get(i).wallets1.sumNum;
                        int zaototalNum = dakaInfos.get(i).wallets1.totalNum;
                        int zaolimitNum = dakaInfos.get(i).wallets1.limitNum;

                        int zhongSumNum = dakaInfos.get(i).wallets2.sumNum;
                        int zhongtotalNum = dakaInfos.get(i).wallets2.totalNum;
                        int zhonglimitNum = dakaInfos.get(i).wallets2.limitNum;

                        int wanSumNum = dakaInfos.get(i).wallets3.sumNum;
                        int wantotalNum = dakaInfos.get(i).wallets3.totalNum;
                        int wanlimitNum = dakaInfos.get(i).wallets3.limitNum;

                        try {
                            cardInfoDataDao.insert(new CardInfoData(Long.parseLong(dakaInfos.get(i).cardNum), dakaInfos.get(i).customerId, dakaInfos.get(i).name, dakaInfos.get(i).number,
                                    dakaInfos.get(i).className, dakaInfos.get(i).cardNum,
                                    zaoSumNum, zaototalNum, zaolimitNum,
                                    zhongSumNum, zhongtotalNum, zhonglimitNum,
                                    wanSumNum, wantotalNum, wanlimitNum));
                        } catch (Exception e) {
                            ELog.i("==============cardInfoDataDao===1111====" + dakaInfos.get(i).toString());
//                            String error = "同步数据 bug : " + dakaInfos.get(i).toString() + "==11==" + e.toString();
//                            MobclickAgent.reportError(CanteenApplication.context, error.toString());
                        }
                    } else {
                        try {
                            cardInfoDataDao.insert(new CardInfoData(Long.parseLong(dakaInfos.get(i).cardNum), dakaInfos.get(i).customerId, dakaInfos.get(i).name,
                                    dakaInfos.get(i).number, dakaInfos.get(i).className, dakaInfos.get(i).cardNum,
                                    0, 0, 0, 0, 0, 0, 0, 0, 0));
                        } catch (Exception e) {
                            ELog.i("==============cardInfoDataDao===2222====" + dakaInfos.get(i).toString());
//                            String error = "同步数据 bug : " + dakaInfos.get(i).toString() + "==22==" + e.toString();
//                            MobclickAgent.reportError(CanteenApplication.context, error.toString());
                        }
                    }
                }
                ELog.i("==============cardInfoDataDao===3333====" + cardInfoDataDao.loadAll().size());
                mMvpView.syndataSuccess();
            }
        }).start();


    }

    public void getToday_menu() {

//        mMvpView.setTodayMenu(null,false);
        canteenClient.getTodayMenu()
                .subscribe(new Observer<HttpResult<List<TodayMenu>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<TodayMenu>> listHttpResult) {
                        ELog.i("============getSeting==tjj=今日菜单===onNext=======" + listHttpResult.toString());
                        if (mMvpView != null && listHttpResult != null) {
                            mMvpView.setTodayMenu(listHttpResult.getData(), true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("============getSeting===今日菜单===onError=======" + e);
                        mMvpView.setTodayMenu(null, false);
                        mMvpView.showErrorSetingDaka(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                    @Override
//                    public void onSubscribe(Disposable d) {
////                        disposableSeting = d;
//                    }
//
//                    @Override
//                    public void onNext(List<TodayMenu> todayMenus) {
//                        ELog.i("============getSeting==tjj=今日菜单===onNext=======");
//                        ELog.i("============getSeting===今日菜单===onNext=======" + todayMenus.toString());
//                        mMvpView.setTodayMenu(todayMenus, true);
////                        mMvpView.setTodayMenu(null,false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mMvpView.setTodayMenu(null, false);
////                        ELog.i("============getSeting===今日菜单===onError=======" + e);
////                        disposableSeting.dispose();
//                        mMvpView.showErrorSetingDaka(e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        disposableSeting.dispose();
//                    }
//                });

    }


    public void getSeting(String cardNumber, int num) { // 1设置 2场景切换
        canteenClient.getSeting(cardNumber)
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSeting = d;
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        ELog.i("============getSeting======onNext=======" + httpResult.toString());
                        if (mMvpView != null && httpResult.code.toString().equals("200")) {
                            mMvpView.showSeting(true, num);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableSeting.dispose();
                        ELog.i("============getSeting======onError=======" + e);
                        if (mMvpView != null) {
                            mMvpView.showErrorSetingDaka(e.toString());
                        }
                    }

                    @Override
                    public void onComplete() {
                        disposableSeting.dispose();
                    }
                });
    }


    public void getMode() { // 模式
        canteenClient.getMode()
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSeting = d;
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        ELog.i("============getSeting===模式===onNext========" + httpResult.toString());
                        if (httpResult.code.toString().equals("200")) {
                            mMvpView.modelNum(httpResult.getData() + "");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableSeting.dispose();
                        ELog.i("============getSeting===模式===onError=======" + e);
                        mMvpView.showErrorSetingDaka(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        disposableSeting.dispose();
                    }
                });
    }
}
