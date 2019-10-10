package com.zhqz.wisc.libraryui.main;

import com.google.gson.Gson;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.DbDao.LibraryReshuModelDao;
import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.FingerUsers;
import com.zhqz.wisc.data.model.NoticeList;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.data.model.Tianqi;
import com.zhqz.wisc.data.model.TianqiAQI;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.ui.main.MainMvpView;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.utils.ELog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
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
 * Created by jingjingtan on 11/04/18.
 */
public class LibraryMainPresenter implements Presenter<LibraryMainMvpView> {

    private final static int timeMS = 1000 * 60 * 8;


    private static WiscClient wiscClient;
    private static LibraryMainMvpView mMvpView;
    private Scheduler.Worker workerAQI = Schedulers.io().createWorker();
    private Scheduler.Worker workerWeather = Schedulers.io().createWorker();
    private Scheduler.Worker workerNotice = Schedulers.io().createWorker();

    private Disposable disposableAQI;
    private Disposable disposableWeather;
    private Disposable disposableNotice;
    private Disposable disposableSeting;
    private Disposable disposableRoom;
    private Disposable disposableBanhui;
    private Disposable LibraryIntroduction;


    @Inject
    public LibraryMainPresenter(WiscClient client) {
        this.wiscClient = client;
    }

    @Override
    public void attachView(LibraryMainMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (!workerAQI.isDisposed()) {
            workerAQI.dispose();
        }
        if (!workerWeather.isDisposed()) {
            workerWeather.dispose();
        }
        if (!workerNotice.isDisposed()) {
            workerNotice.dispose();
        }
        if (disposableNotice != null && !disposableNotice.isDisposed()) {
            disposableNotice.dispose();
        }
        if (LibraryIntroduction != null && !LibraryIntroduction.isDisposed()) {
            LibraryIntroduction.dispose();
        }

    }



/*
* 天气
* */
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

    /*
    * 通知
    * */

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
                    mMvpView.showSeting(true,num);
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

    public void getbindRoom(boolean isMqtt) {

        wiscClient.getbindlibrary(WiscApplication.prefs.getdeviceId())
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
                            ELog.i("============查看绑定教室======onNext=111======" + roomHttpResult.getData().id);
                            WiscApplication.prefs.setdeviceId(roomHttpResult.getData().id);
                            mMvpView.bindNewRoom(roomHttpResult.getData(),isMqtt);
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

    public void LibraryReshuList() {//场景列表
        wiscClient.LibraryReshuList()
                .doOnNext(listHttpResult -> {
                    if (!listHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(listHttpResult.code, listHttpResult.msg));
                    }
                })
                .map(listHttpResult -> listHttpResult.getData())
                .subscribe(new Observer<List<LibraryReshuModel>>() {
                    public Disposable disposableClassrooms;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableClassrooms = d;
                    }

                    @Override
                    public void onNext(List<LibraryReshuModel> libraryReshuModels) {
                        ELog.i("====热书列表==onNext========" + libraryReshuModels.toString());
                        mMvpView.showLibraryReshu(libraryReshuModels);
                    }

                    @Override
                    public void onError(Throwable e) {

                        if(WiscApplication.getDaoSession().getLibraryReshuModelDao().loadAll().size() != 0){
                            mMvpView.showLibraryReshu(WiscApplication.getDaoSession().getLibraryReshuModelDao().loadAll());
                        }
                        disposableClassrooms.dispose();
                        ELog.i("====热书列表==onError========" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableClassrooms.dispose();
                    }
                });
    }

    public void getBanhui() {

        wiscClient.getbanhuilibrary()
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
    public void getLibraryIntroduction() {

        wiscClient.getLibraryIntroduction()
                .doOnNext( libraryModelHttpResult-> {

                    if (!libraryModelHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(libraryModelHttpResult.code,
                                libraryModelHttpResult.msg));

                    }
                })
                .map(libraryModelHttpResult->libraryModelHttpResult.getData())
                .subscribe(new Observer<LibraryModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LibraryIntroduction = d;
                    }

                    @Override
                    public void onNext(LibraryModel libraryModel) {
                        ELog.i("==============11===onNext=图书馆简介=======" + libraryModel.toString());
                        if(libraryModel != null){
                            mMvpView.LibraryIntroduction(libraryModel);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LibraryIntroduction.dispose();
                        ELog.i("==============11===onError=图书馆简介=======" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {
                        LibraryIntroduction.dispose();
                    }
                });
    }
}
