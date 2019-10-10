package com.zhqz.wisc.ui.faceScann;

import com.zhqz.wisc.ui.base.MvpView;

/**
 * Created by jingjingtan on 11/6/17.
 */

public interface FaceScannMvpView extends MvpView {

    void detectFace(boolean b, String s);

    void IsFaceIdentify(boolean b, String errorMsg);

    void faceDaKaXinXi(String userImage, int type, boolean isQinjia);

    void faceErrorMessage(Throwable e);

    void faceUpdataImage(boolean isSuccess);

    void faceDakaOK();
}
