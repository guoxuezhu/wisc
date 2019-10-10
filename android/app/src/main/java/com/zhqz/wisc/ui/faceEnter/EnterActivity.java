package com.zhqz.wisc.ui.faceEnter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.ui.adapter.EnterAdapter;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.faceScann.FaceScannActivity;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.utils.FileUtils;

import java.util.List;

import javax.inject.Inject;

import android_serialport_api.DeviceMonitorService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhqz.wisc.WiscApplication.context;


/**
 * Created by jingjingtan on 11/3/17.
 */

public class EnterActivity extends BaseActivity implements EnterMvpView, EnterAdapter.CallBack {


    @Inject
    EnterPresenter enterPresenter;
    @BindView(R.id.enter_image)
    ImageView enter_image;
    @BindView(R.id.RecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.enter_textview)
    TextView enter_show;

    @BindView(R.id.line_layout_default)
    LinearLayout line_layout_default;
    @BindView(R.id.line_studentslist)
    LinearLayout line_studentslist;

    Bitmap bitmap;
    EnterAdapter enterAdapter;
    private List<EnterStudent> enterStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        enterPresenter.attachView(this);

        initdata();
        if (WiscApplication.prefs.getCardNumber() == null) {
            line_layout_default.setVisibility(View.VISIBLE);
            line_studentslist.setVisibility(View.GONE);
        } else {
            line_layout_default.setVisibility(View.GONE);
            line_studentslist.setVisibility(View.VISIBLE);
            bitmap = BitmapFactory.decodeFile(FileUtils.ReadBitmap());
            if (bitmap == null) {
                enter_image.setImageDrawable(getResources().getDrawable(R.mipmap.me));
                enter_show.setText("录入信息失败");
            } else {
                enter_image.setImageBitmap(bitmap);
                enter_show.setText("录入信息成功");
            }
            enterPresenter.getEnterStudent(WiscApplication.prefs.getCardNumber());

        }
        enterPresenter.getkahaornter();
        FileUtils.deleteDir();
    }

    public void enter(String cardNumber) {
        WiscApplication.prefs.setCardNumber(cardNumber);
        enterPresenter.getEnterStudent(WiscApplication.prefs.getCardNumber());
    }


    public void setMessage(String s) {
        line_layout_default.setVisibility(View.VISIBLE);
        line_studentslist.setVisibility(View.GONE);
        Toast.makeText(this, "你没有班主任权限", Toast.LENGTH_SHORT).show();
        enterAdapter.setData(null);
    }

    private void initdata() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        enterAdapter = new EnterAdapter(enterStudents, this, this);
        recyclerView.setAdapter(enterAdapter);
    }

    @OnClick(R.id.enter_back)
    void enter_back() {
        DeviceMonitorService.stop();
        enter_image.setImageBitmap(null);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap = null;
        }
        WiscClient.isEnter = false;
        Intent intent = new Intent(EnterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        enter_image.setImageBitmap(null);
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap = null;
        }
        enterPresenter.detachView();
        super.onDestroy();
    }


    @Override
    public void EnterStudents(List<EnterStudent> enterStudent) {
        if (enterStudent == null || enterStudent.size() <= 0) {
            line_layout_default.setVisibility(View.VISIBLE);
            line_studentslist.setVisibility(View.GONE);
            Toast.makeText(this, "你没有班主任权限", Toast.LENGTH_SHORT).show();
            enterAdapter.setData(null);
        } else {
            line_layout_default.setVisibility(View.GONE);
            line_studentslist.setVisibility(View.VISIBLE);
            enterStudents = enterStudent;
            enterAdapter.setData(enterStudent);
        }
    }

    @Override
    public void ItemClicked(EnterStudent student, int Position) {
//        Toast.makeText(this,"点击了"+WiscClient.enterStudents.get(Position),Toast.LENGTH_SHORT).show();
        WiscClient.isEnter = true;
        DeviceMonitorService.stop();
        Intent intent = new Intent(EnterActivity.this, FaceScannActivity.class);
        intent.putExtra("stId", student.stId);
        intent.putExtra("psType", student.psType);
        intent.putExtra("status", student.status);
        startActivity(intent);
        finish();
    }
}
