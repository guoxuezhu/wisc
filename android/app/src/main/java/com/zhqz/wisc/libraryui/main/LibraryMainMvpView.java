package com.zhqz.wisc.libraryui.main;

import com.zhqz.wisc.data.model.NoticeList;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.data.model.Tianqi;
import com.zhqz.wisc.data.model.TianqiAQI;
import com.zhqz.wisc.ui.base.MvpView;
import com.zhqz.wisc.ui.scene.SecenList;

import java.util.List;

public interface LibraryMainMvpView extends MvpView{
    void setTianqi(Tianqi tianqi);

    void showTianqiAqi(TianqiAQI tianqiAQI);

    void showErrorMessage(Throwable e);

    void showNoticeList(List<NoticeList> noticeList);

    void showSeting(boolean b, int num);

    void showLibraryReshu(List<LibraryReshuModel> classroomses);

    void bindNewRoom(Room data, boolean isMqtt);

    void showBanHui(String s);

    void makeError(Throwable e);

    void LibraryIntroduction(LibraryModel libraryModel);
}
