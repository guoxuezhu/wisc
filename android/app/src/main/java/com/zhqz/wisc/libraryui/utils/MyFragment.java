package com.zhqz.wisc.libraryui.utils;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.libraryui.Librarymodel;
import com.zhqz.wisc.libraryui.main.LibraryAppAdapter;
import com.zhqz.wisc.libraryui.main.LibraryMainActivity;
import com.zhqz.wisc.libraryui.main.LibraryMainPresenter;
import com.zhqz.wisc.libraryui.main.LibraryModel;
import com.zhqz.wisc.libraryui.main.LibraryReshuModel;
import com.zhqz.wisc.libraryui.main.LibraryViewPagerAdapter;
import com.zhqz.wisc.ui.adapter.PageControl;
import com.zhqz.wisc.ui.scene.SceneActivity;
import com.zhqz.wisc.ui.scene.SceneAppAdapter;
import com.zhqz.wisc.ui.scene.SceneViewPagerAdapter;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.utils.ELog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {

    public static final String TAG = "MyFragment";
    public static final String LibraryIntroduction = "LibraryIntroduction";
    private String str;

    LibraryMainActivity l;
    public static List<?> images=new ArrayList<>();
    public static List<String> titles=new ArrayList<>();
    public static List<String> imageslist=new ArrayList<>();
    LibraryMainPresenter lPresenter;
    private LibraryViewPagerAdapter adapter;


    public MyFragment(LibraryMainActivity libraryMainActivity, LibraryMainPresenter libraryMainPresenter) {
        this.l = libraryMainActivity;
        this.lPresenter = libraryMainPresenter;
    }
    WebView tv_title;
    ViewPager viewPager;
    LinearLayout linear_layout;
    RelativeLayout library_re;
    TextView tv_show;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.myfragment, null);
        tv_title = (WebView) view.findViewById(R.id.tv_title);
        viewPager = (ViewPager) view.findViewById(R.id.library_viewpager);
        linear_layout = (LinearLayout) view.findViewById(R.id.library_linear_layout);
        library_re = (RelativeLayout) view.findViewById(R.id.library_re);
        ELog.d("==stopAutoPlay=="+"点击了onCreateView");
        tv_show = (TextView) view.findViewById(R.id.tv_show);
        tv_show.setVisibility(View.GONE);
        //得到数据
        Librarymodel librarymodel = (Librarymodel) getArguments().getSerializable(TAG);

        tv_title.loadUrl("");
        tv_title.clearHistory();

        if (librarymodel.id == 2){
            library_re.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.GONE);

            lPresenter.LibraryReshuList();

        }else {
            ELog.d("==stopAutoPlay=="+"点击了onCreateView111");
            library_re.setVisibility(View.GONE);
            lPresenter.getLibraryIntroduction();

        }
        return view;
    }

    public void setjianjie(LibraryModel libraryModel){
//        LibraryModel libraryModelintroduction = (LibraryModel) getArguments().getSerializable(LibraryIntroduction);
        ELog.d("==stopAutoPlay=="+"setjianjie"+libraryModel.toString());

        ELog.d("==stopAutoPlay=="+"setjianjie====="+libraryModel.summary);
        if(libraryModel.summary != null){
            tv_show.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setWebViewClient(new webViewClient());
            tv_title.loadUrl(libraryModel.summaryUrl);
        } else {
            tv_show.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.GONE);
            tv_show.setText("暂时没有简介，请上传");
        }
    }
    public void sedata(List<LibraryReshuModel> classroomses) {
        classrooms = classroomses;
        if(classroomses == null){
            tv_show.setVisibility(View.VISIBLE);
            library_re.setVisibility(View.GONE);
            tv_show.setText("暂时没有推荐内容");
            return;
        }
        library_re.setVisibility(View.VISIBLE);
        tv_show.setVisibility(View.GONE);
        initViews();
        adapter = new LibraryViewPagerAdapter(l, map);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new MyListener());
    }
    List<LibraryReshuModel> classrooms;
    public static final double APP_PAGE_SIZE = 6.0;// 每一页装载数据的大小
    private PageControl pageControl;

    private Map<Integer, GridView> map;
    private void initViews() {
        Double d= classrooms.size() / APP_PAGE_SIZE;
        final int PageCount = (int) Math.ceil(d);//滑动页数,可以计算
        map = new HashMap<Integer, GridView>();
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(l);
            final LibraryAppAdapter adapter = new LibraryAppAdapter(l, classrooms, i);
            appPage.setAdapter(adapter);
            appPage.setNumColumns(3);
            appPage.setVerticalSpacing(25);
            appPage.setHorizontalSpacing(25);
            appPage.setGravity(Gravity.CENTER);
            appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            appPage.setPadding(0, 25, 0, 0);
//            appPage.setOnItemClickListener(adapter);
            map.put(i, appPage);

        }

        // linear_layout中的负责包裹小圆点的LinearLayout.
        pageControl = new PageControl(l, linear_layout, PageCount,10,10);


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

    class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }
    }
}
