package com.zhqz.wisc.ui.selectClass;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.Classrooms;
import com.zhqz.wisc.data.model.Room;
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
 * Created by guoxuezhu on 16-11-30.
 */

public class SelectClassPresenter implements Presenter<SelectClassMVPView> {


    private SelectClassMVPView mMvpView;

    private WiscClient wiscClient;


    @Inject
    public SelectClassPresenter(WiscClient client) {
        this.wiscClient = client;
    }

    @Override
    public void attachView(SelectClassMVPView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public void classRooms() {//教室列表
        wiscClient.classromms()
                .doOnNext(listHttpResult -> {
                    if (!listHttpResult.code.equals("200")) {
                        throw Exceptions.propagate(new ClientRuntimeException(listHttpResult.code, listHttpResult.msg));
                    }
                }).map(listHttpResult -> listHttpResult.getData())
                .subscribe(new Observer<List<Classrooms>>() {

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
                        ELog.i("====教室列表==onError========" + e.toString());
                        mMvpView.showErrorMessage(e);
                    }

                    @Override
                    public void onNext(List<Classrooms> classroomses) {
                        ELog.i("====教室列表==onNext========" + classroomses.toString());
                        mMvpView.showClossRooms(classroomses);
                    }
                });
    }

    public void BindRooms(int roomId) {//绑定教室
        ELog.d("====绑定教室===33333");
        wiscClient.bindRoom(roomId)
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
                        WiscApplication.prefs.setIsSelectKaoQing(true);
                        WiscApplication.prefs.setSceneId(2);
                        if (mRoom != null && mRoom.id != 0) {
                            WiscApplication.prefs.setdeviceId(mRoom.id);
                            mMvpView.showIsSuccess(mRoom);
                        }
                    }
                });
    }
}
