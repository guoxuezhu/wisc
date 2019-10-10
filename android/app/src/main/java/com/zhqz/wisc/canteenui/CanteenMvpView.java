package com.zhqz.wisc.canteenui;

import com.zhqz.wisc.canteenui.todaymenu.TodayMenu;
import com.zhqz.wisc.data.model.Canteen;
import com.zhqz.wisc.data.model.DakaInfo;
import com.zhqz.wisc.data.model.TransactionDetailSM;
import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

/**
 * Created by jingjingtan on 3/22/18.
 */

public interface CanteenMvpView extends MvpView {

    void showCanteenInfo(Canteen canteen, boolean b);

    void showNoWifiDakaInfo();

    void showErrorDaka(String s);

    void showDakaInfo(DakaInfo dakaInfo);

    void showCanteenInfoError(String s);

    void showDakaConnectException(TransactionDetailSM transactionDetailSM, List<TransactionDetailSM> noWifiDatas);

    void bindReset();

    void syndataSuccess();

    void showSeting(boolean b,int num);

    void showErrorSetingDaka(String s);

    void setTodayMenu(List<TodayMenu> b,boolean issuceess);

    void modelNum(String num);
}