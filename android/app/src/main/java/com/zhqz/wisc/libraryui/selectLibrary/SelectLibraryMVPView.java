package com.zhqz.wisc.libraryui.selectLibrary;

import com.zhqz.wisc.data.model.Classrooms;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

/**
 * Created by guoxuezhu on 16-11-30.
 */

public interface SelectLibraryMVPView extends MvpView {
    void showClossRooms(List<Classrooms> classroomses);
    void showMessage(String msg);

    void showIsSuccess(Room mRoom);

    void showErrorMessage(Throwable s);
}
