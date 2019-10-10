package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.data.model.FingerEnterStudent;
import com.zhqz.wisc.ui.FingerprintEntry.FingerprintEntryActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FingerEnterAdapter extends RecyclerView.Adapter<FingerEnterAdapter.ViewHolder> {
    Context context;
    List<FingerEnterStudent> fingerEnterStudents;

    private CallBack mCallback;

    public FingerEnterAdapter(List<FingerEnterStudent> fingerEnterStudent, Context context, CallBack cb) {
        this.fingerEnterStudents = fingerEnterStudent;
        this.context = context;
        this.mCallback = cb;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enter_item, parent, false);
        return new FingerEnterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FingerEnterStudent student = fingerEnterStudents.get(position);
        holder.enter_item_image.setImageResource(R.mipmap.me);

        holder.enter_item_id.setText("学号 ： " + student.number);
        holder.enter_item_name.setText("姓名 ： " + student.name);
        if (student.status == 0) {
            holder.enter_item_status.setText("未录入");
            holder.enter_item_status.setTextColor(Color.parseColor("#ff0000"));
        } else {
            holder.enter_item_status.setText("已录入");
            holder.enter_item_status.setTextColor(Color.parseColor("#43b4ee"));
        }

        holder.setStudent(student);
    }

    public void setData(List<FingerEnterStudent> fingerEnterStudent) {
        fingerEnterStudents = fingerEnterStudent;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fingerEnterStudents != null ? fingerEnterStudents.size() : 0;
    }

    public interface CallBack {
        void ItemClicked(FingerEnterStudent student, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.enter_item_image)
        ImageView enter_item_image;
        @BindView(R.id.enter_item_name)
        TextView enter_item_name;
        @BindView(R.id.enter_item_id)
        TextView enter_item_id;
        @BindView(R.id.enter_item_status)
        TextView enter_item_status;

        View mView;
        FingerEnterStudent student;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void setStudent(FingerEnterStudent student) {
            this.student = student;
        }

        @OnClick(R.id.enter_item_image)
        void onItemClicked() {
            if (mCallback != null && student != null) {
                mCallback.ItemClicked(student, getPosition());
                mView.setBackgroundResource(R.color.kechengbg);  //选中项背景
            }
        }
    }
}
