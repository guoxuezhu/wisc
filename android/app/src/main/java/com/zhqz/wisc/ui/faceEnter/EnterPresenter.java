package com.zhqz.wisc.ui.faceEnter;

import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.utils.ELog;

import java.util.List;

import javax.inject.Inject;

import android_serialport_api.DeviceMonitorService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jingjingtan on 11/6/17.
 */

public class EnterPresenter implements Presenter<EnterMvpView> {
    private EnterMvpView mMvpView;
    private WiscClient wiscClient;
    private Disposable enterStudentDisposable;
    private Disposable updataImageDisposable;

    @Inject
    public EnterPresenter(WiscClient wiscClient) {
        this.wiscClient = wiscClient;
    }

    @Override
    public void attachView(EnterMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (enterStudentDisposable != null && !enterStudentDisposable.isDisposed()) {
            enterStudentDisposable.dispose();
        }
        if (updataImageDisposable != null && !updataImageDisposable.isDisposed()) {
            updataImageDisposable.dispose();
        }
    }

    void getEnterStudent(String cardNumber) {
        wiscClient.getEnterStudent(cardNumber)
                .subscribe(new Observer<HttpResult<List<EnterStudent>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        enterStudentDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<List<EnterStudent>> listHttpResult) {
                        ELog.i("========getEnterStudent===========onNext===" + listHttpResult.toString());
                        mMvpView.EnterStudents(listHttpResult.getData());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        enterStudentDisposable.dispose();
                        mMvpView.setMessage(e.toString());
                        ELog.i("========getEnterStudent============onError===" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        enterStudentDisposable.dispose();
                    }
                });
    }

    public void getkahaornter() {
        DeviceMonitorService.flow1().subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((kahao) -> {
                            if (mMvpView != null) {
                                mMvpView.enter(kahao.trim().toString());
                            }
                        }
                );
    }
}
