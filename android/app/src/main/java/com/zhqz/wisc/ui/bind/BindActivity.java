package com.zhqz.wisc.ui.bind;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.canteenui.bind.CanBindActivity;
import com.zhqz.wisc.ui.base.BaseActivity;
import com.zhqz.wisc.ui.scene.SceneActivity;
import com.zhqz.wisc.ui.selectClass.SelectClassActivity;
import com.zhqz.wisc.utils.ELog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindActivity extends BaseActivity implements BindMvpView {

    @Inject
    BindPresenter mBindPresenter;

    @BindView(R.id.button3)
    Button button;
    @Nullable
    @BindView(R.id.spinner1)
    Spinner spinner;
    List<Person> ListPersons;
    SeclectSchoolAdapter MyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        mBindPresenter.attachView(this);
        mBindPresenter.SchoolId();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ELog.i("=========value==========" + ListPersons.get(position).value);
                ELog.i("=========id==========" + ListPersons.get(position).id);
                WiscApplication.prefs.setSchoolId(ListPersons.get(position).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.button3)
    public void onBindClick(View view) {
        if(ListPersons == null || WiscApplication.prefs.getSchoolId() == -1){
            Toast.makeText(this, "请选择学校", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, SceneActivity.class));
            BindActivity.this.finish();
        }
    }

    private static final long MIN_CLICK_INTERVAL = 5000;
    private long mLastClickTime;
    int mSecretNumber = 0;

    @OnClick(R.id.image_home)
    public void backhome(){
        try {
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;
            if (elapsedTime < MIN_CLICK_INTERVAL) {
                ++mSecretNumber;
//            int resetNum=5-mSecretNumber;
//            Toast.makeText(this, "还差"+resetNum+"可调出设置界面", Toast.LENGTH_SHORT).show();
                if (5 == mSecretNumber) {
                    Intent intent= new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                }
            } else {
                mSecretNumber = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mBindPresenter.detachView();//？
        super.onDestroy();
    }

    @Override
    public void showSchoolList(List<Person> persons) {
        ListPersons = persons;
        //  建立Adapter绑定数据源
        MyAdapter=new SeclectSchoolAdapter(this, persons);
        //绑定Adapter
        spinner.setAdapter(MyAdapter);
    }

    @Override
    public void showMessage(String msg) {

    }
}
