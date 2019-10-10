package com.zhqz.wisc.libraryui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.ZuoPin;
import com.zhqz.wisc.libraryui.main.LibraryMainActivity;
import com.zhqz.wisc.ui.adapter.ZuopinImageAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LibraryListAdapter extends RecyclerView.Adapter<LibraryListAdapter.ViewHolder>{


    private Context context;
    private String[] strings;
    List<Librarymodel> librarymodelList;
    private CallBack mCallback;
    public static int mPosition;

    public LibraryListAdapter(Context context, List<Librarymodel> strings,CallBack mCallbacks) {
        this.context = context;
        this.librarymodelList = strings;
        this.mCallback = mCallbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mPosition = position;
        Librarymodel m = librarymodelList.get(position);
        holder.tv.setText(m.name);
//
        if (position == LibraryMainActivity.mPosition) {
            holder.linelibaray.setBackgroundResource(R.color.kechengbg);
        } else {
            holder.linelibaray.setBackgroundResource(R.color.colorgray);
        }

        holder.setItem(m);
    }

    @Override
    public int getItemCount() {
        return librarymodelList != null ? librarymodelList.size() : 0;
    }

    public interface CallBack {
        void onLibraryListItemClicked(int position,Librarymodel item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.linelibaray)
        LinearLayout linelibaray;


        View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        Librarymodel item;

        public void setItem(Librarymodel item) {
            this.item = item;
        }

        @OnClick(R.id.linelibaray)
        void onItemClicked() {
            if (mCallback != null && item != null) {
                mCallback.onLibraryListItemClicked(getPosition(),item);
                mView.setBackgroundResource(R.color.kechengbg);  //选中项背景
            }

        }
    }
}
