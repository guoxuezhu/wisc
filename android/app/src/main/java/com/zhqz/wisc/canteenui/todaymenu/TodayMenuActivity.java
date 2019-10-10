package com.zhqz.wisc.canteenui.todaymenu;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.canteenui.CanteenMainActivity;
import com.zhqz.wisc.ui.adapter.EnterAdapter;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.utils.ELog;
import com.zhqz.wisc.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhqz.wisc.WiscApplication.context;

/**
 * Created by jingjingtan on 3/29/18.
 */

public class TodayMenuActivity extends BaseActivity implements TodayMenuMvpView {
    @Inject
    TodayMenuPresenter todayMenuPresenter;

    @BindView(R.id.RecyclerView_today)
    RecyclerView recyclerView;

    TodayMenuAdapter todayMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaymenu);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        todayMenuPresenter.attachView(this);

        todayMenuPresenter.getMenu(WiscApplication.prefs.getCardNumber());

        initdata();

    }

    List<TodayMenu> todayMenus;
    private void initdata() {
//        todayMenus = new ArrayList<>();
//        TodayMenu todayMenu = new TodayMenu("111111111","1");
//        TodayMenu todayMenu1 = new TodayMenu("22222","2");
//        todayMenus.add(todayMenu);
//        todayMenus.add(todayMenu1);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        todayMenuAdapter = new TodayMenuAdapter (this, null);
        recyclerView.setAdapter(todayMenuAdapter);
    }

    List<Integer> ids = new ArrayList<>();
    @OnClick(R.id.bt_upload)
    void bt_upload(){
        ids.clear();
        ELog.d(todayMenus.toString()+"=======今日菜单数据4444");
        if (todayMenus==null || todayMenus.size()<=0){
            ELog.d("=======今日菜单数据333");
            return;
        }
        for (int i=0;i < todayMenus.size();i++){
            ELog.d(todayMenus.get(i)+"=======今日菜单数据1111");
            if (todayMenus.get(i).status.equals("1")){
                ids.add(todayMenus.get(i).id);
            }
        }
        ELog.d("=======今日菜单数据333"+ids.toString());
        todayMenuPresenter.Submit(ids);
    }

    @OnClick(R.id.todayback)
    void todayback() {
        startActivity(new Intent(TodayMenuActivity.this, CanteenMainActivity.class));
        finish();
    }

    @Override
    public void showErrorSetingDaka(String s) {
        Toast.makeText(TodayMenuActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setMenuList(List<TodayMenu> todayMenu) {
        ELog.d("=======今日菜单数据2222"+todayMenu.toString());
        todayMenus = todayMenu;
        todayMenuAdapter.setTodayMenu(todayMenu);
    }

    @Override
    public void isSuccess(boolean b) {
        todayback();
    }
}
