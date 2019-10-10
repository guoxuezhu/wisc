package com.zhqz.wisc.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jingjingtan on 2/21/17.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mViews;
    String[] titles = {"今天","明天","昨天"};

    public ViewPagerAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();
//        return Integer.MAX_VALUE;//循环时修改
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        循环时View向左滑动需要屏蔽掉removeView
//        container.removeView(mViews.get(position));

    }

//    //返回标题
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titles[position%titles.length];
//    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //ViewPager的循环
        Log.e("===view22===",position%mViews.size()+"");
        View view = mViews.get(position%mViews.size());
        if (view.getParent() !=null){
            container.removeView(view);//如果前边有View的话，需要先removeView掉View然后再add上
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
