package com.zhqz.wisc.libraryui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.event.GridEvent;
import com.zhqz.wisc.ui.scene.SecenList;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于GridView装载数据的适配器
 *
 * @author xxs
 */
public class LibraryAppAdapter extends BaseAdapter {
    private List<ResolveInfo> mList1;// 定义一个list对象
    private Context mContext;// 上下文
    public static final int APP_PAGE_SIZE = 6;// 每一页装载数据的大小
    private PackageManager pm;// 定义一个PackageManager对象

    private int page;

//	public AppAdapter(SelectClassActivity context, List<String> classroomList, int i) {
//	}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private List<LibraryReshuModel> mList;// 定义一个list对象

    public LibraryAppAdapter(Context context, List<LibraryReshuModel> list, int page) {
        mContext = context;
        pm = context.getPackageManager();
        this.page = page;
        mList = new ArrayList<LibraryReshuModel>();
        // 根据当前页计算装载的应用，每页只装载16个
        int i = page * APP_PAGE_SIZE;// 当前页的其实位置
        int iEnd = i + APP_PAGE_SIZE;// 所有数据的结束位置
        while ((i < list.size()) && (i < iEnd)) {
            mList.add(list.get(i));
            i++;
        }
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.app_library_item, parent, false);
        }
        LibraryReshuModel classroom = mList.get(position);

        TextView appname = (TextView) convertView.findViewById(R.id.library_tvAppName);
        TextView library_author = (TextView) convertView.findViewById(R.id.library_author);

        ImageView scene_image = (ImageView) convertView.findViewById(R.id.library_image);
//        if (classroom.status == 1) {
////            appname.setBackgroundResource(R.drawable.btn_classroom_tv_no);
//        } else {
////            appname.setBackgroundResource(R.drawable.btn_classroom_tv);
//        }
        appname.setText(classroom.name);
        library_author.setText(classroom.author + " 著");
        Glide.with(WiscApplication.context).load(classroom.coverUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .centerCrop()
                .dontAnimate()
                .into(scene_image);

        return convertView;
    }

//    @SuppressLint("NewApi")
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        // TODO Auto-generated method stub
////		int index = this.getPage() * APP_PAGE_SIZE + arg2;//1*8+1
////        Log.d("=============", arg2 + "====" + mList.get(arg2).id + "===" + mList.get(arg2).name + "===" + mList.size());
//        if (mList.get(arg2).status == 1) {
//            Toast.makeText(mContext, mContext.getResources().getString(R.string.bind_repeat), Toast.LENGTH_SHORT).show();
//        } else {
//            arg0.getChildAt(arg2).setBackgroundResource(R.drawable.btn_bind);
//            EventBus.getDefault().post(new GridEvent(mList.get(arg2).id, arg0.getChildAt(arg2),mList.get(arg2).jumpPath,mList.get(arg2).name));
//        }
//    }

}
