package com.zhqz.wisc.libraryui.selectLibrary;

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
import com.zhqz.wisc.libraryui.main.LibraryMainActivity;
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


public class SelectLibraryActivity extends BaseActivity implements SelectLibraryMVPView {


    @Inject
    SelectLibraryPresenter selectLibraryPresenter;
    private MyViewPagerAdapter adapter;
    private PageControl pageControl;
    private Map<Integer, GridView> map;

    @BindView(R.id.myviewpager)
    ViewPager viewPager;

    @BindView(R.id.linear_layout)
    LinearLayout linear_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        selectLibraryPresenter.attachView(this);
        EventBus.getDefault().register(this);

        if (WiscApplication.prefs.getisLibarayBind()){
            if (WiscApplication.prefs.getIsSelectLibrary() ) {
                WiscApplication.prefs.setSceneId(3);
                ELog.d("====绑定教室==tjj2222=" + WiscApplication.prefs.getIsSelectLibrary());

                startActivity(new Intent(SelectLibraryActivity.this, LibraryMainActivity.class));
                finish();
                return;
            }
        } else {
            selectLibraryPresenter.classRooms();
        }

    }

    public static final double APP_PAGE_SIZE = 52.0;// 每一页装载数据的大小
    private void initViews() {
        Double d= classrooms.size() / APP_PAGE_SIZE;
        final int PageCount = (int) Math.ceil(d);//滑动页数,可以计算
        map = new HashMap<Integer, GridView>();
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(this);
            final AppAdapter adapter = new AppAdapter(this, classrooms, i);
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
        WiscApplication.prefs.setclassRoomId(message.getId());
        Dialog promptDialog = new Dialog(this, Dialog.BIND_QUEREN);
        promptDialog.show();
        promptDialog.setCanceledOnTouchOutside(false);
        ELog.d("====绑定教室===");
        promptDialog.setText("你将此班牌绑定为 "+ message.getmMsg());

        promptDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                switch (promptDialog.mIndex1) {
                    case 1://确认
                        if(WiscApplication.prefs.getclassRoomId()==-1){
                            Toast.makeText(SelectLibraryActivity.this, getResources().getString(R.string.no_select_room), Toast.LENGTH_LONG).show();
                            return;
                        }
                        ELog.d("====绑定教室==1111=");
                        selectLibraryPresenter.BindRooms(WiscApplication.prefs.getclassRoomId());
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

    List<Classrooms> classrooms;
    @Override
    public void showClossRooms(List<Classrooms> classroomses) {
        if(classroomses == null || classroomses.size()==0){
            Toast.makeText(SelectLibraryActivity.this, getResources().getString(R.string.no_find_roomlist), Toast.LENGTH_LONG).show();
            return;
        }
        classrooms=classroomses;
        Log.d("教室列表", classroomses.toString());
        initViews();
        adapter = new MyViewPagerAdapter(this, map);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new MyListener());

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
        Toast.makeText(SelectLibraryActivity.this, msg, Toast.LENGTH_LONG).show();
        view.setBackgroundColor(Color.argb(0, 0, 0, 0));
    }

    @Override
    public void showIsSuccess(Room mRoom) {
        if (mRoom != null && mRoom.name != null) {
            WiscApplication.prefs.setisLibarayBind(true);
            WiscApplication.prefs.setBindRoom(mRoom.name);
            if(mRoom.englishName!=null){
                WiscApplication.prefs.setBindRoomEnglishName(mRoom.englishName);
            }
            WiscApplication.prefs.setRoomId(mRoom.roomId);
            startActivity(new Intent(SelectLibraryActivity.this, LibraryMainActivity.class));
            finish();
        }else {
            Toast.makeText(this, getResources().getString(R.string.bind_roomfailure), Toast.LENGTH_SHORT).show();
            view.setBackgroundColor(Color.argb(0, 0, 0, 0));
        }

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
