package com.zhqz.wisc.canteenui.todaymenu;

import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

/**
 * Created by jingjingtan on 3/29/18.
 */

public interface TodayMenuMvpView extends MvpView{
    void showErrorSetingDaka(String s);
    void setMenuList(List<TodayMenu> todayMenus);

    void isSuccess(boolean b);
}
