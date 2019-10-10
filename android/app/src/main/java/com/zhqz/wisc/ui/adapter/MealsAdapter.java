package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.Meals;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jingjingtan on 4/6/17.
 */

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {
    Context context;
    List<Meals.Mealsdetali> mealsList;

    public MealsAdapter(Context context, List<Meals.Mealsdetali> mealses) {
        this.context = context;
        this.mealsList = mealses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meals_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meals.Mealsdetali mcourses = mealsList.get(position);
        holder.meals_time.setText(mcourses.mealTime);
        holder.use_time.setText(mcourses.mealsName);
        holder.use_count.setText(mcourses.mealsCount+context.getResources().getString(R.string.count));
    }

    @Override
    public int getItemCount() {
        return mealsList != null ? mealsList.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.use_count)
        TextView use_count;
        @BindView(R.id.use_time)
        TextView use_time;
        @BindView(R.id.meals_time)
        TextView meals_time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
