package com.zhqz.wisc.injection.component;

import com.zhqz.wisc.canteenui.CanteenMainActivity;
import com.zhqz.wisc.canteenui.bind.CanBindActivity;
import com.zhqz.wisc.canteenui.todaymenu.TodayMenuActivity;
import com.zhqz.wisc.injection.PerActivity;
import com.zhqz.wisc.injection.module.ActivityModule;
import com.zhqz.wisc.libraryui.main.LibraryMainActivity;
import com.zhqz.wisc.libraryui.selectLibrary.SelectLibraryActivity;
import com.zhqz.wisc.ui.FingerprintEntry.FingerprintEntryActivity;
import com.zhqz.wisc.ui.bind.BindActivity;
import com.zhqz.wisc.ui.faceEnter.EnterActivity;
import com.zhqz.wisc.ui.faceScann.FaceScannActivity;
import com.zhqz.wisc.ui.fingerprint.FingerprintActivity;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.ui.scene.SceneActivity;
import com.zhqz.wisc.ui.selectClass.SelectClassActivity;
import com.zhqz.wisc.ui.splash.SplashActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(BindActivity bindActivity);

    void inject(SelectClassActivity selectClassActivity);

    void inject(MainActivity mainActivity);

    void inject(EnterActivity enterActivity);

    void inject(FaceScannActivity faceScannActivity);

    void inject(FingerprintActivity fingerprintActivity);

    void inject(FingerprintEntryActivity fingerprintEntryActivity);

    void inject(CanteenMainActivity canteenMainActivity);

    void inject(CanBindActivity canBindActivity);

    void inject(SceneActivity sceneActivity);

    void inject(TodayMenuActivity todayMenuActivity);

    void inject(LibraryMainActivity libraryMainActivity);

    void inject(SelectLibraryActivity selectLibraryActivity);
}
