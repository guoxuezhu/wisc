package com.zhqz.wisc.ui.scene;

import com.zhqz.wisc.data.model.Classrooms;
import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

/**
 * Created by jingjingtan on 3/27/18.
 */

public interface SceneMvpView extends MvpView {
    void showClossRooms(List<SecenList> classroomses);

    void showErrorMessage(Throwable e);

    void showMessage(String msg);
}
