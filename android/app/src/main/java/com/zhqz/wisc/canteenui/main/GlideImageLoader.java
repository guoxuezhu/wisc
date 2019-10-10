package com.zhqz.wisc.canteenui.main;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;


public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context)
                .load(path)//图片地址
                .crossFade()
                .into(imageView);
    }
//    Glide.with(WiscApplication.context).load(student.url)
//                .placeholder(R.mipmap.me)
//                .error(R.mipmap.me)
//                .dontAnimate()
//                .into(holder.userAvatar);
}
