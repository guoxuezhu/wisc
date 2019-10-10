package com.zhqz.wisc.ui.FingerprintEntry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.model.FingerEnterStudent;
import com.zhqz.wisc.ui.adapter.FingerEnterAdapter;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.faceEnter.EnterActivity;
import com.zhqz.wisc.ui.faceScann.FaceScannActivity;
import com.zhqz.wisc.ui.fingerprint.FingerprintActivity;
import com.zhqz.wisc.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import android_serialport_api.DeviceMonitorService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhqz.wisc.WiscApplication.context;

public class FingerprintEntryActivity extends BaseActivity implements FingerprintEntryMvpView, FingerEnterAdapter.CallBack {

    @Inject
    FingerprintEntryPresenter fingerprintEntryPresenter;


    @BindView(R.id.stu_RecyclerView)
    RecyclerView stu_RecyclerView;
    @BindView(R.id.this_stu_message)
    TextView this_stu_message;
    private List<FingerEnterStudent> fingerEnterStudents;
    private FingerEnterAdapter fingerEnterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_entry);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        fingerprintEntryPresenter.attachView(this);

        initdata();

        if (WiscApplication.prefs.getCardNumber() == null) {
            this_stu_message.setVisibility(View.VISIBLE);
            stu_RecyclerView.setVisibility(View.GONE);
        } else {
            this_stu_message.setVisibility(View.GONE);
            stu_RecyclerView.setVisibility(View.VISIBLE);
            fingerprintEntryPresenter.getFingerEnterStudent(WiscApplication.prefs.getCardNumber());
        }
        fingerprintEntryPresenter.getkahaornter();

    }


    private void initdata() {
        stu_RecyclerView.setLayoutManager(new LinearLayoutManager(context));
        stu_RecyclerView.setHasFixedSize(true);
        fingerEnterAdapter = new FingerEnterAdapter(fingerEnterStudents, this, this);
        stu_RecyclerView.setAdapter(fingerEnterAdapter);
    }

    @Override
    public void fingerEnter(String cardNumber) {
        WiscApplication.prefs.setCardNumber(cardNumber);
        fingerprintEntryPresenter.getFingerEnterStudent(WiscApplication.prefs.getCardNumber());
    }

    @Override
    public void fingerEnterStudents(List<FingerEnterStudent> fingerEnterStudent) {
        this_stu_message.setVisibility(View.GONE);
        stu_RecyclerView.setVisibility(View.VISIBLE);
        fingerEnterStudents = fingerEnterStudent;
        fingerEnterAdapter.setData(fingerEnterStudents);

    }

    @Override
    public void setFingerMessage(String s) {
        this_stu_message.setVisibility(View.VISIBLE);
        stu_RecyclerView.setVisibility(View.GONE);
        Toast.makeText(this, "你没有班主任权限", Toast.LENGTH_SHORT).show();
        fingerEnterAdapter.setData(null);
    }


    @Override
    public void ItemClicked(FingerEnterStudent student, int position) {
        WiscClient.isEnter = true;
        DeviceMonitorService.stop();
        WiscClient.isWriteFinger = true;
        WiscClient.isFinger= true;
        Intent intent = new Intent(FingerprintEntryActivity.this, FingerprintActivity.class);
        intent.putExtra("stId", student.stid);
        intent.putExtra("psType", student.psType);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.fingerprint_luru_back_LL)
    void fingerprint_luru_back_LL() {
        DeviceMonitorService.stop();
        WiscClient.isEnter = false;
        WiscClient.isFinger = false;
        startActivity(new Intent(FingerprintEntryActivity.this, MainActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        fingerprintEntryPresenter.detachView();
        super.onDestroy();
    }
}
