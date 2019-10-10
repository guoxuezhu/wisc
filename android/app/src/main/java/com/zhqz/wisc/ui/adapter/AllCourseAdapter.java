package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.CourseTwo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoxuezhu on 16-12-20.
 */
public class AllCourseAdapter extends RecyclerView.Adapter<AllCourseAdapter.ViewHolder> {

    Context context;
    List<CourseTwo> coursesList;

    public AllCourseAdapter(Context context, List<CourseTwo> coursesList) {
        this.context = context;
        this.coursesList = coursesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_kecheng_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseTwo mcourses= coursesList.get(position);
        holder.all_course_name.setText(mcourses.name);
        holder.all_start_time.setText(mcourses.startTime.substring(11));
        holder.all_teacher_name.setText(mcourses.teacherName);
        holder.all_end_time.setText(mcourses.endTime.substring(11));
        holder.all_teacher_number.setText(mcourses.lessonNum);
    }

    @Override
    public int getItemCount() {
        return coursesList != null ? coursesList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.all_course_name)
        TextView all_course_name;

        @BindView(R.id.all_teacher_name)
        TextView all_teacher_name;

        @BindView(R.id.all_start_time)
        TextView all_start_time;

        @BindView(R.id.all_end_time)
        TextView all_end_time;

        @BindView(R.id.all_teacher_number)
        TextView all_teacher_number;

        View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
