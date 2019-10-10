package com.zhqz.wisc.canteenui.todaymenu;

import com.zhqz.wisc.canteenui.CanteenMvpView;
import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.utils.ELog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;

/**
 * Created by jingjingtan on 3/29/18.
 */

public class TodayMenuPresenter implements Presenter<TodayMenuMvpView> {

    private static TodayMenuMvpView mMvpView;
    private static WiscClient canteenClient;
    private Disposable disposableSeting;

    @Inject
    public TodayMenuPresenter(WiscClient client) {
        this.canteenClient = client;
    }

    @Override
    public void attachView(TodayMenuMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (disposableSeting != null && !disposableSeting.isDisposed()) {
            disposableSeting.dispose();
        }
    }

    public void getMenu(String dateNum) { // 模式
        canteenClient.getMenu(dateNum)
                .doOnNext(listHttpResult -> {

                })
                .map(listHttpResult -> listHttpResult.getData())
                .subscribe(new Observer<List<TodayMenu>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSeting = d;
                    }

                    @Override
                    public void onNext(List<TodayMenu> todayMenus) {
                        ELog.i("============getSeting===今日菜单===onNext=======" + todayMenus.toString());
                        mMvpView.setMenuList(todayMenus);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableSeting.dispose();
                        ELog.i("============getSeting===今日菜单===onError=======" + e);
                        mMvpView.showErrorSetingDaka(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        disposableSeting.dispose();
                    }
                });
    }

    public void Submit(List<Integer> ids) { // 今日菜单提交
        canteenClient.submit(ids)
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSeting = d;
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        ELog.i("============今日菜单提交======onNext=======" + httpResult.toString());
                        if (httpResult.code.toString().equals("200")) {
                            mMvpView.isSuccess(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableSeting.dispose();
                        ELog.i("============今日菜单提交======onError=======" + e);
                        mMvpView.showErrorSetingDaka(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        disposableSeting.dispose();
                    }
                });
    }
}
