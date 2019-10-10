package com.zhqz.wisc.ui.FingerprintEntry;

import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.data.model.FingerEnterStudent;
import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

public interface FingerprintEntryMvpView extends MvpView {


    void fingerEnter(String s);

    void setFingerMessage(String s);

    void fingerEnterStudents(List<FingerEnterStudent> data);
}
