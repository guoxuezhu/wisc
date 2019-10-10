package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.ThisClassStudents;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jingjingtan on 4/6/17.
 */

public class ThisClassStudentsAdapter extends RecyclerView.Adapter<ThisClassStudentsAdapter.ViewHolder> {
    Context context;
    List<ThisClassStudents> studentDetailses;
    public ThisClassStudentsAdapter(Context context, List<ThisClassStudents> studentDetailsList) {
        this.context = context;
        this.studentDetailses = studentDetailsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.this_class_students_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ThisClassStudents mcourses = studentDetailses.get(position);
        holder.this_teacher.setText(mcourses.courseTeacherName);
        holder.this_rooms.setText(mcourses.roomsName);
        holder.this_clouse.setText(mcourses.courseName);
        holder.this_status.setText(mcourses.attendanceStatus);
        holder.this_name.setText(mcourses.studentName);
    }

    @Override
    public int getItemCount() {
        return studentDetailses != null ? studentDetailses.size() : 0;
    }

   /* @Override
    public int getCount() {
        return studentDetailses.size();
    }

    @Override
    public Object getItem(int position) {
        return studentDetailses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ThisClassStudentsAdapter.ViewHolder viewHolder = null;
        convertView = View.inflate(context, R.layout.this_class_students_item, null);
        if (viewHolder == null) {
            viewHolder = new ThisClassStudentsAdapter.ViewHolder();
            viewHolder.this_name= (TextView) convertView.findViewById(R.id.this_name);
            viewHolder.this_status= (TextView) convertView.findViewById(R.id.this_status);
            viewHolder.this_clouse= (TextView) convertView.findViewById(R.id.this_clouse);
            viewHolder.this_rooms= (TextView) convertView.findViewById(R.id.this_rooms);
            viewHolder.this_teacher= (TextView) convertView.findViewById(R.id.this_teacher);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ThisClassStudentsAdapter.ViewHolder) convertView.getTag();
        }
        ThisClassStudents mcourses = studentDetailses.get(position);
        viewHolder.this_teacher.setText(mcourses.courseTeacherName);
        viewHolder.this_rooms.setText(mcourses.roomsName);
        viewHolder.this_clouse.setText(mcourses.courseName);
        viewHolder.this_status.setText(mcourses.attendanceStatus);
        viewHolder.this_name.setText(mcourses.studentName);
        return convertView;
    }*/


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.this_name)
        TextView this_name;
        @BindView(R.id.this_status)
        TextView this_status;
        @BindView(R.id.this_clouse)
        TextView this_clouse;
        @BindView(R.id.this_rooms)
        TextView this_rooms;
        @BindView(R.id.this_teacher)
        TextView this_teacher;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
