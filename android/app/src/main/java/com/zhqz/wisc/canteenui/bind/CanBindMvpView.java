package com.zhqz.wisc.canteenui.bind;



import com.zhqz.wisc.data.model.BindCanTing;
import com.zhqz.wisc.data.model.CanTing;
import com.zhqz.wisc.ui.base.MvpView;

import java.util.List;

public interface CanBindMvpView extends MvpView {

    void showErrorMessage(String s);

    void showCanTing(List<CanTing> strings);

    void showBindMessage(BindCanTing bindCanTing);
}
