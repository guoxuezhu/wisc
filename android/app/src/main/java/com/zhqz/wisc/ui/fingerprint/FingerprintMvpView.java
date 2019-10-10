package com.zhqz.wisc.ui.fingerprint;

import com.zhqz.wisc.data.model.FingerUsers;
import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

public interface FingerprintMvpView extends MvpView {


    void showMessage(int msg, int id);

    void sendTemplateDatas(boolean isSuccess,String s);

    void sendZhiwenDatas(String str);

    void fingerDaKaXinXi(String userImg, int dao, String name, boolean isQingjia);

    void fingerErrorMessage(Throwable e);

    void fingerDakaOK();

}
