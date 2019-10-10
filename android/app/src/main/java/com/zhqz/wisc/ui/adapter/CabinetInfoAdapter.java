package com.zhqz.wisc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhqz.wisc.R;
import com.zhqz.wisc.data.model.CabinetInfo;
import com.zhqz.wisc.utils.CabintDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jingjingtan on 12/7/17.
 */

public class CabinetInfoAdapter extends RecyclerView.Adapter<CabinetInfoAdapter.ViewHolder>{


    List<CabinetInfo> cabinetInfoList;
    private final Context ctx;
    private CallBack mCallback;

    public CabinetInfoAdapter(List<CabinetInfo> cabinetInfoList, Context mContext, CabintDialog.DialogCallback mCallback) {
        this.ctx = mContext;
        this.mCallback = mCallback;
        this.cabinetInfoList = cabinetInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cabintinfo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CabinetInfo cabinetInfo = cabinetInfoList.get(position);

        holder.cabinet_name.setText("姓名：" + cabinetInfo.name);
        holder.cabinet_number.setText("柜子编号："+cabinetInfo.lockerNoNumber);
        holder.cabinet_position.setText("位置："+cabinetInfo.position);
        if (cabinetInfo.distribution == 0){
            holder.cabinet_status.setText("状态：未分配");
        } else {
            if (cabinetInfo.status == 0){
                holder.cabinet_status.setText("状态：禁用");
            } else if (cabinetInfo.status == 1){
                holder.cabinet_status.setText("状态：使用");
            } else {
                holder.cabinet_status.setText("状态：损坏");
            }
        }
        holder.setItem(cabinetInfo);
    }

    @Override
    public int getItemCount() {
        return cabinetInfoList != null ? cabinetInfoList.size() : 0;
    }

    public void setdata(List<CabinetInfo> cabinetInfoLists) {
        this.cabinetInfoList = cabinetInfoLists;
        notifyDataSetChanged();
    }


    public interface CallBack {
        void OnClickItemCabinet_urgent(CabinetInfo item);
        void OnClickItemCabinet_locking(CabinetInfo item);
        void OnClickItemCabinet_open(CabinetInfo item);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cabinet_name)
        TextView cabinet_name;
        @BindView(R.id.cabinet_number)
        TextView cabinet_number;
        @BindView(R.id.cabinet_status)
        TextView cabinet_status;
        @BindView(R.id.cabinet_position)
        TextView cabinet_position;

        View mView;
        CabinetInfo item;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
        public void setItem(CabinetInfo item) {
            this.item = item;
        }

        @OnClick(R.id.cabinet_urgent)
        void cabinet_urgent(){
            if (mCallback != null) {
                mCallback.OnClickItemCabinet_urgent(item);
            }
        }

        @OnClick(R.id.cabinet_locking)
        void cabinet_locking(){
            if (mCallback != null) {
                mCallback.OnClickItemCabinet_locking(item);
            }

        }

        @OnClick(R.id.cabinet_open)
        void cabinet_open(){
            if (mCallback != null) {
                mCallback.OnClickItemCabinet_open(item);
            }
        }
    }
}
