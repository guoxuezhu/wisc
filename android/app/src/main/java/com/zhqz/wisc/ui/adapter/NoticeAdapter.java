package com.zhqz.wisc.ui.adapter;

import android.content.Context;
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

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    Context context;
    List<NoticeList> noticeList;
    private CallBack mCallback;

    public NoticeAdapter(Context context, List<NoticeList> noticeList, CallBack mCallback) {
        this.context = context;
        this.noticeList = noticeList;
        this.mCallback = mCallback;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_tv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoticeList notice = noticeList.get(position);
        holder.notice_tv_content.setText(notice.title);
        if(notice.level==2){
            holder.notice_img.setImageResource(R.mipmap.red);
        }else {
            holder.notice_img.setImageResource(R.mipmap.gray);
        }
        holder.setItem(notice);
    }

    @Override
    public int getItemCount() {
        return noticeList != null ? noticeList.size() : 0;
    }
    public void setData(List<NoticeList> noticeItems) {
        noticeList = noticeItems;
        notifyDataSetChanged();
    }

    public interface CallBack {
        public void onNoticeItemClicked(NoticeList item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notice_tv_content)
        TextView notice_tv_content;
        @BindView(R.id.notice_dengji)
        ImageView notice_img;


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
        @OnClick(R.id.notice_linearlayout)
        void onItemClicked() {
            if (mCallback != null && item != null) {
                if (mCallback != null && item != null) mCallback.onNoticeItemClicked(item);
            }

        }
    }

   /* public NoticeAdapter(Context context, List<NoticeList> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList == null ? 0 : noticeList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        convertView = View.inflate(context, R.layout.notice_tv_item, null);
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.notice_tv_content = (TextView) convertView.findViewById(R.id.notice_tv_content);
            viewHolder.notice_dengji = (ImageView) convertView.findViewById(R.id.notice_dengji);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NoticeList notice = noticeList.get(position);
        viewHolder.notice_tv_content.setText(notice.title);
        if(notice.level==2){
            viewHolder.notice_dengji.setImageResource(R.mipmap.red);
        }else {
            viewHolder.notice_dengji.setImageResource(R.mipmap.gray);
        }
        return convertView;
    }


    class ViewHolder {
        TextView notice_tv_content;
        ImageView notice_dengji;

    }*/
}
