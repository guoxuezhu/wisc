package com.zhqz.wisc.ui.scene;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.Classrooms;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.exception.ClientRuntimeException;
import com.zhqz.wisc.ui.base.Presenter;
import com.zhqz.wisc.ui.selectClass.SelectClassMVPView;
import com.zhqz.wisc.utils.ELog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;

/**
 * Created by jingjingtan on 3/27/18.
 */

public class ScenePresenter implements Presenter<SceneMvpView> {

    private SceneMvpView mMvpView;

    private WiscClient wiscClient;

    @Inject
    public ScenePresenter(WiscClient client) {
        this.wiscClient = client;
    }


    @Override
    public void attachView(SceneMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public void GetSecen() {//场景列表
        wiscClient.GetSecen()
                .doOnNext(listHttpResult -> {
                    if (!listHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(listHttpResult.code, listHttpResult.msg));
                    }
                }).map(listHttpResult -> listHttpResult.getData())
                .subscribe(new Observer<List<SecenList>>() {

                    public Disposable disposableClassrooms;

                    @Override
                    public void onComplete() {
                        disposableClassrooms.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableClassrooms = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposableClassrooms.dispose();
                        ELog.i("====场景列表==onError========" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onNext(List<SecenList> classroomses) {
                        ELog.i("====场景列表==onNext========" + classroomses.toString());
                        mMvpView.showClossRooms(classroomses);
                    }
                });
    }
}
