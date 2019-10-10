package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.model.Student;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoxuezhu on 16-12-5.
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    Context context;
    List<Student> students;

    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kaoqin_xiangqing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.student_name.setText(student.getStudentName());

        Glide.with(WiscApplication.context).load(student.picture)
                .placeholder(R.mipmap.me)
                .error(R.mipmap.me)
                .fitCenter()
                .dontAnimate()
                .into(holder.student_image);

        if (student.status == -1) {
            holder.shijian.setText("");
            holder.chuqin.setText(context.getResources().getString(R.string.meidaka));
        } else if (student.status == 1) {
            holder.shijian.setText(student.getAttendancedate().substring(11));
            holder.chuqin.setText(context.getResources().getString(R.string.dao));
        } else if (student.status == 2) {
            holder.shijian.setText(student.getAttendancedate().substring(11));
            holder.chuqin.setText(context.getResources().getString(R.string.chidao));
        } else if (student.status == 3) {
            holder.shijian.setText("");
            holder.chuqin.setText(context.getResources().getString(R.string.queqin));
        } else if (student.status == 4) {
            holder.shijian.setText("");
            holder.chuqin.setText(context.getResources().getString(R.string.qingjia));
        } else if (student.status == 5) {
            holder.shijian.setText("");
            holder.chuqin.setText(context.getResources().getString(R.string.qingjia));
        }
    }

    @Override
    public int getItemCount() {
        return students != null ? students.size() : 0;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.student_image)
        ImageView student_image;

        @BindView(R.id.student_name)
        TextView student_name;

        @BindView(R.id.shijian)
        TextView shijian;

        @BindView(R.id.chuqin)
        TextView chuqin;

        View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
