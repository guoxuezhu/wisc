package com.zhqz.wisc.libraryui.selectLibrary;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.HttpResult;
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
 * Created by guoxuezhu on 16-11-30.
 */

public class SelectLibraryPresenter implements Presenter<SelectLibraryMVPView> {


    private SelectLibraryMVPView mMvpView;

    private WiscClient wiscClient;


    @Inject
    public SelectLibraryPresenter(WiscClient client) {
        this.wiscClient = client;
    }

    @Override
    public void attachView(SelectLibraryMVPView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public void classRooms() {//教室列表
        wiscClient.librarymenu()
                .doOnNext(listHttpResult -> {
                    if (!listHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(listHttpResult.code, listHttpResult.msg));
                    }
                })
                .subscribe(new Observer<HttpResult<List<Classrooms>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HttpResult<List<Classrooms>> listHttpResult) {
                        ELog.i("====教室列表==onNext========" + listHttpResult.toString());
                        if(mMvpView!=null && listHttpResult!=null){
                            mMvpView.showClossRooms(listHttpResult.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("====教室列表==onError========" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void BindRooms(int roomId) {//绑定教室
        ELog.d("====绑定教室===33333");
        wiscClient.bindlibrary(roomId)
                .doOnNext(bindRoomHttpResult -> {
                    if (!bindRoomHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(bindRoomHttpResult.code, bindRoomHttpResult.msg));
                    }
                })
                .map(bindRoomHttpResult -> bindRoomHttpResult.getData())
                .subscribe(new Observer<Room>() {

                    public Disposable disposableRoom;

                    @Override
                    public void onError(Throwable e) {
                        disposableRoom.dispose();
                        ELog.i("====绑定教室==onError========" + e.toString());
                        mMvpView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        disposableRoom.dispose();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableRoom = d;
                    }

                    @Override
                    public void onNext(Room mRoom) {
                        ELog.i("====绑定教室==onNext========" + mRoom.toString());
                        WiscApplication.prefs.setIsSelectLibrary(true);
                        WiscApplication.prefs.setSceneId(3);
                        if (mRoom != null && mRoom.id != 0) {
                            WiscApplication.prefs.setdeviceId(mRoom.id);
                            mMvpView.showIsSuccess(mRoom);
                        }
                    }
                });
    }
}
