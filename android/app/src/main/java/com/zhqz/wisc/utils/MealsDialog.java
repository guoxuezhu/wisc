package com.zhqz.wisc.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.Meals;
import com.zhqz.wisc.ui.adapter.MealsAdapter;

/**
 * Created by jingjingtan on 4/6/17.
 */

public class MealsDialog extends Dialog {

    private Context mContext;
    private RecyclerView meals_listview;
    private TextView closed_time;
    private ImageView close_image;

    public TextView meals_counts;//总次数

    public TextView breakfast_count;

    public TextView breakfast_use_count;

    public TextView breakfast_Surplus_count;

    public TextView lunch_count;

    public TextView lunch_use_count;

    public TextView lunch_Surplus_count;

    public TextView dinner_count;

    public TextView dinner_use_count;

    public TextView dinner_Surplus_count;

    public LinearLayout nodata_detail;
    private LinearLayout meals_LL;

    public MealsDialog(Context context) {
        super(context, R.style.FullHeightDialog);
        mContext = context;
    }

    Handler mmhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WiscClient.CLOSED_DIALOG_HANDLER:
                    int miao = (int) msg.obj;
                    closed_time.setText(miao + "");
                    if (miao == 0) {
                        dismiss();
                    }
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_dialog);
        getView();
    }

    private void getView() {
        nodata_detail = (LinearLayout) findViewById(R.id.nodata_de);
        meals_LL = (LinearLayout) findViewById(R.id.meals_LL);
        meals_counts = (TextView) findViewById(R.id.meals_counts);
        breakfast_count = (TextView) findViewById(R.id.breakfast_count);
        breakfast_use_count = (TextView) findViewById(R.id.breakfast_use_count);
        breakfast_Surplus_count = (TextView) findViewById(R.id.breakfast_Surplus_count);

        lunch_count = (TextView) findViewById(R.id.lunch_count);
        lunch_use_count = (TextView) findViewById(R.id.lunch_use_count);
        lunch_Surplus_count = (TextView) findViewById(R.id.lunch_Surplus_count);
        dinner_count = (TextView) findViewById(R.id.dinner_count);
        dinner_use_count = (TextView) findViewById(R.id.dinner_use_count);
        dinner_Surplus_count = (TextView) findViewById(R.id.dinner_Surplus_count);

        meals_listview = (RecyclerView) findViewById(R.id.meals_listview);
        closed_time = (TextView) findViewById(R.id.closed_time);
        close_image = (ImageView) findViewById(R.id.close_image);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                TimeThread.setClosed();
            }
        });
    }


    public void setData(Meals mealses) {
        meals_counts.setText(mContext.getResources().getString(R.string.meals_counts) + mealses.totalNum + mContext.getResources().getString(R.string.count));
        breakfast_count.setText(mContext.getResources().getString(R.string.breakfast_count) + mealses.breakfastCount + mContext.getResources().getString(R.string.count));
        breakfast_use_count.setText(mContext.getResources().getString(R.string.use_count) + mealses.breakfastUseCount + mContext.getResources().getString(R.string.count));
        breakfast_Surplus_count.setText(mContext.getResources().getString(R.string.Surplus) + mealses.breakfastSurplusCount + mContext.getResources().getString(R.string.count));

        lunch_count.setText(mContext.getResources().getString(R.string.lunch_count) + mealses.lunchCount + mContext.getResources().getString(R.string.count));
        lunch_use_count.setText(mContext.getResources().getString(R.string.use_count) + mealses.lunchUseCount + mContext.getResources().getString(R.string.count));
        lunch_Surplus_count.setText(mContext.getResources().getString(R.string.Surplus) + mealses.lunchSurplusCount + mContext.getResources().getString(R.string.count));

        dinner_count.setText(mContext.getResources().getString(R.string.dinner_count) + mealses.dinnerCount + mContext.getResources().getString(R.string.count));
        dinner_use_count.setText(mContext.getResources().getString(R.string.use_count) + mealses.dinnerUseCount + mContext.getResources().getString(R.string.count));
        dinner_Surplus_count.setText(mContext.getResources().getString(R.string.Surplus) + mealses.dinnerSurplusCount + mContext.getResources().getString(R.string.count));

        if (mealses.details != null && mealses.details.size() > 0) {
            nodata_detail.setVisibility(View.GONE);
            meals_LL.setVisibility(View.VISIBLE);
            meals_listview.setLayoutManager(new LinearLayoutManager(mContext));
            MealsAdapter mealsAdapter = new MealsAdapter(mContext, mealses.details);
            meals_listview.setAdapter(mealsAdapter);
        } else {
            nodata_detail.setVisibility(View.VISIBLE);
            meals_LL.setVisibility(View.GONE);
        }

        TimeThread.setOpenedTime(mmhandler);
    }
}