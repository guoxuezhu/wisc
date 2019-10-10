package com.zhqz.wisc.canteenui.bind;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.canteenui.CanteenMainActivity;
import com.zhqz.wisc.canteenui.CanteenPromptDialog;
import com.zhqz.wisc.canteenui.DakaErrorDialog;
import com.zhqz.wisc.data.model.BindCanTing;
import com.zhqz.wisc.data.model.CanTing;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.utils.ELog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CanBindActivity extends BaseActivity implements CanBindMvpView {


    @Inject
    BindPresenter mBindPresenter;
    @BindView(R.id.canting_lv)
    ListView canting_lv;
    private int position = 0;
    private List<CanTing> mcanting;
    private CanteenPromptDialog promptDialog;
    private DakaErrorDialog dakaErrorDialog;
    private Handler mErrorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_activity_bind);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        mBindPresenter.attachView(this);
        ELog.d("====绑定教室=tjj1==" + WiscApplication.prefs.getIsSelectCanteen());

        if (WiscApplication.prefs.getIsSelectCanteen()){
            WiscApplication.prefs.setSceneId(1);
            ELog.d("====绑定教室=tjj1=111=" + WiscApplication.prefs.getIsSelectCanteen());
            startActivity(new Intent(this, CanteenMainActivity.class));
            finish();
            return;
        }
        mBindPresenter.getCanTings();

    }

    @Override
    public void showErrorMessage(String s) {
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        if (dakaErrorDialog == null) {
            dakaErrorDialog = new DakaErrorDialog(this);
        }
        if (dakaErrorDialog != null) {
            dakaErrorDialog.show();
            dakaErrorDialog.setContents("请查看网络是否连接,或者联系后台查看");
            mErrorHandler = new Handler();
            mErrorHandler.postDelayed(() -> {
                dakaErrorDialog.dismiss();
                dakaErrorDialog = null;
                mErrorHandler = null;
            }, 3300);
        }
    }

    @Override
    public void showCanTing(List<CanTing> canting) {
        mcanting = canting;
        CanTingAdapter canTingAdapter = new CanTingAdapter(this, mcanting);
        canting_lv.setAdapter(canTingAdapter);
        canting_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                canTingAdapter.changeSelected(i);
                position = i;
            }
        });
    }

    @OnClick(R.id.bind_bt)
    public void bind_bt() {
        if (mcanting != null) {
            if (promptDialog == null) {
                promptDialog = new CanteenPromptDialog(this, CanteenPromptDialog.BIND);
            }
            if (promptDialog != null) {
                promptDialog.show();
                promptDialog.setCanceledOnTouchOutside(false);
                promptDialog.setContentTV(mcanting.get(position).canteenName);
                promptDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        switch (promptDialog.mIndex) {
                            case 1://确认
                                promptDialog = null;
                                mBindPresenter.bindCanTing(mcanting.get(position).canteenId);
                                break;
                            case 2://取消
                                promptDialog = null;
                                break;
                        }
                    }
                });
            }
        }

    }

    @Override
    public void showBindMessage(BindCanTing bindCanTing) {
        if (bindCanTing.canteenPostId != -1) {
            Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
            bindCanTing();
        } else {
            Toast.makeText(this, "绑定失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindCanTing() {
        startActivity(new Intent(this, CanteenMainActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        mBindPresenter.detachView();//？
        super.onDestroy();
    }
}
