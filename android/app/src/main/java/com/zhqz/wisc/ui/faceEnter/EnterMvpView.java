package com.zhqz.wisc.ui.faceEnter;

import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

/**
 * Created by jingjingtan on 11/6/17.
 */

public interface EnterMvpView extends MvpView {
    void EnterStudents(List<EnterStudent> enterStudent);

    void enter(String s);

    void setMessage(String s);
}
