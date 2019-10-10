package com.zhqz.wisc.ui.bind;

import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.utils.ELog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;


/**
 * Created by guoxuezhu on 16-11-16.
 */

public class BindPresenter implements Presenter<BindMvpView> {
    private BindMvpView mMvpView;
    private WiscClient wiscClient;

    @Inject
    public BindPresenter(WiscClient client) {
        this.wiscClient = client;
    }

    @Override
    public void attachView(BindMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public void SchoolId() {//教室列表
        wiscClient.schoolList()
                .doOnNext(listHttpResult -> {
                    if (!listHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(listHttpResult.code, listHttpResult.msg));
                    }
                })
                .map(listHttpResult -> listHttpResult.getData())
                .subscribe(new Observer<List<Person>>() {
                    public Disposable disposablePerson;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposablePerson = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Person> persons) {
                        ELog.i("============学校列表======onNext=======" + persons.toString());
                        mMvpView.showSchoolList(persons);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposablePerson.dispose();
                        ELog.i("============学校列表======onError=======" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        disposablePerson.dispose();
                    }
                });
    }
}
