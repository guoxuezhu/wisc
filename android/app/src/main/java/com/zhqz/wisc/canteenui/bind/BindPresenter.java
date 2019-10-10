package com.zhqz.wisc.canteenui.bind;



import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.BindCanTing;
import com.zhqz.wisc.data.model.CanTing;
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


public class BindPresenter implements Presenter<CanBindMvpView> {

    private CanBindMvpView mMvpView;
    private WiscClient canteenClient;


    @Inject
    public BindPresenter(WiscClient client) {
        this.canteenClient = client;
    }

    @Override
    public void attachView(CanBindMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }


    public void getCanTings() {

        canteenClient.canting()
                .doOnNext(cantingHttpResult -> {

                    if (!cantingHttpResult.code.equals("200") || cantingHttpResult.getData() == null) {
                        throw Exceptions.propagate(new ClientRuntimeException(cantingHttpResult.code, cantingHttpResult.msg));
                    }
                })
                .map(cantingHttpResult -> cantingHttpResult.getData())
                .subscribe(new Observer<List<CanTing>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CanTing> canTings) {
                        ELog.i("==============canting==onNext=======" + canTings.toString());
                        if (canTings.size() != 0) {
                            mMvpView.showCanTing(canTings);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("==============canting==onError=======" + e.toString());
                        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
                        boolean isConnectException = e.getClass().equals(ConnectException.class);
                        if (isSocketException || isConnectException) {
                            mMvpView.showErrorMessage(e.toString());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public void bindCanTing(int canteenId) {

       canteenClient.bindCanTing(canteenId)
                .doOnNext(bindCanTingHttpResult -> {

                    if (!bindCanTingHttpResult.code.equals("200") || bindCanTingHttpResult.getData() == null) {
                        throw Exceptions.propagate(new ClientRuntimeException(bindCanTingHttpResult.code, bindCanTingHttpResult.msg));
                    }
                })
                .map(deviceHttpResult -> deviceHttpResult.getData())
                .subscribe(new Observer<BindCanTing>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BindCanTing bindCanTing) {
                        ELog.i("================bindCanTing==onNext=======" + bindCanTing.toString());
                        if(bindCanTing!=null){
                            WiscApplication.prefs.setCanteenPostId(bindCanTing.canteenPostId);
                            WiscApplication.prefs.setCanteenId(bindCanTing.canteenId);
                            WiscApplication.prefs.setIsSelectCanteen(true);
                            WiscApplication.prefs.setSceneId(1);
                            canteenClient.setDevice(bindCanTing.canteenPostId);
                            mMvpView.showBindMessage(bindCanTing);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ELog.i("===============bindCanTing==onError========" + e.toString());
                        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
                        boolean isConnectException = e.getClass().equals(ConnectException.class);
                        if (isSocketException || isConnectException) {
                            mMvpView.showErrorMessage(e.toString());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}
