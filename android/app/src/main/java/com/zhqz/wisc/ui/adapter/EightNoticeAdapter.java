package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.NoticeList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guoxuezhu on 17-2-17.
 */

public class EightNoticeAdapter extends RecyclerView.Adapter<EightNoticeAdapter.ViewHolder> {

    Context context;
    List<NoticeList> noticeList;
    private CallBack mCallback;

    public EightNoticeAdapter(Context context, List<NoticeList> noticeList, CallBack mCallback) {
        this.context = context;
        this.noticeList = noticeList;
        this.mCallback = mCallback;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eightnotice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoticeList notice = noticeList.get(position);
        holder.notice_tv_content.setText(notice.title);
        if(notice.level==2){
            holder.notice_dengji.setImageResource(R.mipmap.red);
        }else {
            holder.notice_dengji.setImageResource(R.mipmap.gray);
        }
        holder.setItem(notice);
    }

    @Override
    public int getItemCount() {
        return noticeList != null ? noticeList.size() : 0;
    }

    public interface CallBack {
        public void onItemClicked(NoticeList item);
    }
    public void setData(List<NoticeList> noticeItems) {
        noticeList = noticeItems;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.eightnotice_tv1)
        TextView notice_tv_content;
        @BindView(R.id.eightnotice_img)
        ImageView notice_dengji;


        View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        NoticeList item;

        public void setItem(NoticeList item) {
            this.item = item;
        }

        @Nullable
        @OnClick(R.id.eightnotice_click)
        void onItemClicked() {
            if (mCallback != null && item != null) {
                if (mCallback != null && item != null) mCallback.onItemClicked(item);
            }

        }
    }

}
