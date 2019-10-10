package com.zhqz.wisc.ui.scene;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.model.Classrooms;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.event.GridEvent;
import com.zhqz.wisc.ui.adapter.AppAdapter;
import com.zhqz.wisc.ui.adapter.MyViewPagerAdapter;
import com.zhqz.wisc.ui.adapter.PageControl;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.ui.selectClass.SelectClassActivity;
import com.zhqz.wisc.ui.selectClass.SelectClassMVPView;
import com.zhqz.wisc.ui.selectClass.SelectClassPresenter;
import com.zhqz.wisc.utils.Dialog;
import com.zhqz.wisc.utils.ELog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.HttpException;

/**
 * Created by jingjingtan on 3/27/18.
 */

public class SceneActivity extends BaseActivity implements SceneMvpView {
    @Inject
    ScenePresenter scenePresenter;

    private SceneViewPagerAdapter adapter;
    private PageControl pageControl;

    private Map<Integer, GridView> map;

    @BindView(R.id.scene_viewpager)
    ViewPager viewPager;

    @BindView(R.id.scene_linear_layout)
    LinearLayout linear_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        scenePresenter.attachView(this);
        EventBus.getDefault().register(this);
        scenePresenter.GetSecen();

    }

    public static final double APP_PAGE_SIZE = 52.0;// 每一页装载数据的大小
    private void initViews() {
        Double d= classrooms.size() / APP_PAGE_SIZE;
        final int PageCount = (int) Math.ceil(d);//滑动页数,可以计算
        map = new HashMap<Integer, GridView>();
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(this);
            final SceneAppAdapter adapter = new SceneAppAdapter(this, classrooms, i);
            appPage.setAdapter(adapter);
            appPage.setNumColumns(4);
            appPage.setVerticalSpacing(25);
            appPage.setHorizontalSpacing(25);
            appPage.setGravity(Gravity.CENTER);
            appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            appPage.setPadding(0, 25, 0, 0);
            appPage.setOnItemClickListener(adapter);
            map.put(i, appPage);

        }

        // linear_layout中的负责包裹小圆点的LinearLayout.
        pageControl = new PageControl(this, linear_layout, PageCount,30,30);

    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    View view;
    @Subscribe
    public void onEvent(GridEvent message) {
        //出现提示框，点击确定界面跳转
        view=message.getView();
//        WiscApplication.prefs.setclassRoomId(message.getId());
        ELog.d("====场景切换==="+message.getJumpPath());

        Dialog promptDialog = new Dialog(this, Dialog.BIND_QUEREN);
        promptDialog.show();
        promptDialog.setCanceledOnTouchOutside(false);

        promptDialog.setText("场景选择为："+message.getmMsg());

        promptDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                switch (promptDialog.mIndex1) {
                    case 1://确认
                        if(message.getId()==-1){
                            Toast.makeText(SceneActivity.this, "请选择场景", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //场景通过包名+类名跳转
                        showSecen(message.getJumpPath(),message.getId());
                        break;
                    case 0://取消
                        message.getView().setBackgroundColor(Color.argb(0, 0, 0, 0));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showSecen(String jumpPath, int id) {
        if (jumpPath == null || jumpPath.equals(' ')){
            Toast.makeText(SceneActivity.this, "请联系超级管理员进行路径填写", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = null;
        try {
            intent = new Intent(SceneActivity.this, Class.forName(jumpPath));
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    List<SecenList> classrooms;
    @Override
    public void showClossRooms(List<SecenList> classroomses) {
        if(classroomses==null && classroomses.size()==0){
            Toast.makeText(SceneActivity.this, "没有场景数据，无法绑定", Toast.LENGTH_LONG).show();
            return;
        }
        classrooms=classroomses;
//        Log.d("场景列表", classroomses.toString());
        initViews();
        adapter = new SceneViewPagerAdapter(this, map);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new SceneActivity.MyListener());

    }


    @Override
    public void showErrorMessage(Throwable e) {
        boolean isSocketException = e.getClass().equals(SocketTimeoutException.class);
        boolean isConnectException = e.getClass().equals(ConnectException.class);
        boolean isHttpException = e.getClass().equals(HttpException.class);
        if(isSocketException||isConnectException||isHttpException){
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(SceneActivity.this, msg, Toast.LENGTH_LONG).show();
        view.setBackgroundColor(Color.argb(0, 0, 0, 0));
    }



    class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            pageControl.selectPage(arg0);
        }

    }
}
