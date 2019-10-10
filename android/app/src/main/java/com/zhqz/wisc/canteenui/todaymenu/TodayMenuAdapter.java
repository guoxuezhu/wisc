package com.zhqz.wisc.canteenui.todaymenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingchengl on 2/20/16.
 */
public class TodayMenuAdapter extends RecyclerView
        .Adapter<TodayMenuAdapter.ViewHolder> {
    private static final int ITEM_TYPE_COURSE_DESC = 0;
    private static final int ITEM_TYPE_COURSE_TIME = 1;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<TodayMenu> todayMenus;


    public TodayMenuAdapter(Context context, List<TodayMenu> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        todayMenus = items;
    }

    public void setTodayMenu(List<TodayMenu> todayMenuList) {
        todayMenus = todayMenuList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rollcall_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodayMenu student = todayMenus.get(position);

        holder.studentName.setText(student.name);

        holder.setStudent(student);
        holder.radioGroup.check(student.attendanceResId());

        Glide.with(WiscApplication.context).load(student.url)
                .placeholder(R.mipmap.me)
                .error(R.mipmap.me)
                .dontAnimate()
                .into(holder.userAvatar);
    }

    @Override
    public int getItemCount() {
        return todayMenus != null ? todayMenus.size() : 0;
    }

    /*
    * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {

        @BindView(R.id.student_name)
        TextView studentName;

        @BindView(R.id.rollcall_grp)
        RadioGroup radioGroup;

        @BindView(R.id.user_head)
        ImageView userAvatar;

        TodayMenu todayMenu;

        public ViewHolder(View view, int type) {
            super(view);
            ButterKnife.bind(this, view);
            radioGroup.setOnCheckedChangeListener(this);

        }

        void setStudent(TodayMenu todayMenu1) {
            this.todayMenu = todayMenu1;
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radio_late) {
                todayMenu.status = "2";
            }  else {
                todayMenu.status = "1";
            }
        }


        @Override
        public String toString() {
            return super.toString() + " '" + studentName.getText();
        }
    }
}

