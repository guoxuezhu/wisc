package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.Leave;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jingjingtan on 4/6/17.
 */

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {
    Context context;
    List<Leave.LeaveList> leaveLists;

    public LeaveAdapter(Context context, List<Leave.LeaveList> lists) {
        this.context = context;
        this.leaveLists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leave_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Leave.LeaveList mcourses = leaveLists.get(position);
        holder.leave_name.setText(mcourses.leave_user_name);

        String beginLessonNum = mcourses.beginLessonNum == null ? " " : mcourses.beginLessonNum;
        String endLessonNum = mcourses.endLessonNum == null ? " " : mcourses.endLessonNum;

        holder.leave_time.setText(mcourses.startDate +"   " + beginLessonNum +"   起   " +"\n"+
                mcourses.endDate +"   " + endLessonNum +"   止   ");
        
        holder.leave_reason.setText(mcourses.leaveName);
        if (mcourses.progress == 0){//0.发起申请，1.同意申请，2.申请驳回
            holder.leave_status.setText("待审核");
            holder.leave_status.setTextColor(Color.parseColor("#FFFF0000"));
        } else if (mcourses.progress == 1){
            holder.leave_status.setText("已同意");
            holder.leave_status.setTextColor(Color.parseColor("#FF42B033"));
        } else if (mcourses.progress == 2){
            holder.leave_status.setText("拒绝");
            holder.leave_status.setTextColor(Color.parseColor("#ff008bea"));
        }
    }

    @Override
    public int getItemCount() {
        return leaveLists != null ? leaveLists.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leave_name)
        TextView leave_name;
        @BindView(R.id.leave_time)
        TextView leave_time;
        @BindView(R.id.leave_reason)
        TextView leave_reason;
        @BindView(R.id.leave_status)
        TextView leave_status;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
