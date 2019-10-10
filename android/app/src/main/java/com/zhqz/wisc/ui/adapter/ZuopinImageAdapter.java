package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.model.ZuoPin;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ZuopinImageAdapter extends RecyclerView.Adapter<ZuopinImageAdapter.ViewHolder> {

    int mSelect = 0;   //选中项
    Context context;
    private List<ZuoPin> zuoPins;
    private CallBack mCallback;
    public ZuopinImageAdapter(Context context, List<ZuoPin> zuoPin, CallBack cb) {
        this.context = context;
        this.zuoPins = zuoPin;
        this.mCallback = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zuopin_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ZuoPin zuoPin = zuoPins.get(position);

        if (zuoPin.fileType.equals("JPEG")||zuoPin.fileType.equals("PNG")) {
            Glide.with(WiscApplication.context).load(zuoPin.thumbnailPath)
                    .placeholder(R.mipmap.zuopin)
                    .error(R.mipmap.zuopin)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iv);
        }else if (zuoPin.fileType.equals("MP4")||zuoPin.fileType.equals("AVI")||zuoPin.fileType.equals("MOV")) {
            Glide.with(WiscApplication.context).load(zuoPin.thumbnailPath)
                    .placeholder(R.mipmap.zuopin)
                    .error(R.mipmap.zuopin)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iv);
        }
        if (mSelect == position) {
            holder.mView.setBackgroundResource(R.color.kechengbg);  //选中项背景
        } else {
            holder.mView.setBackgroundResource(R.color.colorwhite);  //其他项背景
        }
        holder.setItem(zuoPin);
    }

    @Override
    public int getItemCount() {
        return zuoPins != null ? zuoPins.size() : 0;
    }

    public void setZuoPins(List<ZuoPin> zuoPins) {
        this.zuoPins = zuoPins;
        notifyDataSetChanged();
    }

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
    }
    public interface CallBack {
        void onZuopinImageItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView iv;

        View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        ZuoPin item;

        public void setItem(ZuoPin item) {
            this.item = item;
        }

        @OnClick(R.id.image)
        void onItemClicked() {
            if (mCallback != null && item != null) {
                mCallback.onZuopinImageItemClicked(getPosition());
                mView.setBackgroundResource(R.color.kechengbg);  //选中项背景
            }

        }
    }
}
