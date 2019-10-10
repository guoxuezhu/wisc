package com.zhqz.wisc.ui.FingerprintEntry;

import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.data.model.FingerEnterStudent;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.ui.fingerprint.FingerprintMvpView;
import com.zhqz.wisc.utils.ELog;

import java.util.List;

import javax.inject.Inject;

import android_serialport_api.DeviceMonitorService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FingerprintEntryPresenter implements Presenter<FingerprintEntryMvpView> {

    private FingerprintEntryMvpView mMvpView;
    private WiscClient wiscClient;
    private Disposable enterStudentDisposable;


    @Inject
    public FingerprintEntryPresenter(WiscClient wiscClient) {
        this.wiscClient = wiscClient;
    }

    @Override
    public void attachView(FingerprintEntryMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (enterStudentDisposable != null && !enterStudentDisposable.isDisposed()) {
            enterStudentDisposable.dispose();
        }
    }

    public void getFingerEnterStudent(String cardNumber) {
        wiscClient.getfingerEnterStudent(cardNumber)
                .subscribe(new Observer<HttpResult<List<FingerEnterStudent>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        enterStudentDisposable = d;
                    }

                    @Override
                    public void onNext(HttpResult<List<FingerEnterStudent>> listHttpResult) {
                        ELog.i("========getFingerEnterStudent===========onNext===" + listHttpResult.toString());
                        if (listHttpResult.getData() != null && listHttpResult.getData().size() != 0) {
                            mMvpView.fingerEnterStudents(listHttpResult.getData());
                        } else {
                            mMvpView.setFingerMessage("没有学生");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        enterStudentDisposable.dispose();
                        mMvpView.setFingerMessage(e.toString());
                        ELog.i("========getFingerEnterStudent============onError===" + e.toString());
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
                                mMvpView.fingerEnter(kahao.trim().toString());
                            }
                        }
                );
    }

}
