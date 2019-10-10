package com.zhqz.wisc.ui.bind;

import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

/**
 * Created by guoxuezhu on 16-11-16.
 */

public interface BindMvpView extends MvpView {
    void showSchoolList(List<Person> persons);
    void showMessage(String msg);
}
