package com.zhqz.wisc.ui.splash;

import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.ui.base.Presenter;

import javax.inject.Inject;

public class SplashPresenter implements Presenter<SplashMvpView> {
    private SplashMvpView mMvpView;

    private WiscClient wiscClient;

    @Inject
    public SplashPresenter(WiscClient client) {
        this.wiscClient = client;
    }

    @Override
    public void attachView(SplashMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean hasValidUserStored() {
        return wiscClient.loadUserIfAvailble();
    }
}
