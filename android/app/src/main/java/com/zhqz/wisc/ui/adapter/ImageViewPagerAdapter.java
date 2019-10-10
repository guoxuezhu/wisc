package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.model.ZuoPin;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by guoxuezhu on 17-2-15.
 */

public class ImageViewPagerAdapter extends PagerAdapter {

    private LinkedList<View> recycledViews = new LinkedList<View>();
    private Context mcontext;
    private List<ZuoPin> zuoPins;

    public ImageViewPagerAdapter(Context context, List<ZuoPin> zuoPins) {
        this.mcontext = context;
        this.zuoPins = zuoPins;

    }

    @Override
    public int getCount() {
        return zuoPins.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewGroup) container).removeView((View) object);
        if (object != null) {
            recycledViews.addLast((View) object);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = null;
        if (recycledViews != null && recycledViews.size() > 0) {
            imageView = (ImageView) recycledViews.getFirst();
            recycledViews.removeFirst();
        } else {
            imageView = new ImageView(mcontext);
        }
        Glide.with(WiscApplication.context).load(zuoPins.get(position).thumbnailPath)
                .placeholder(R.mipmap.zuopin)
                .error(R.mipmap.zuopin)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
        container.addView(imageView);
        return imageView;
    }
}
