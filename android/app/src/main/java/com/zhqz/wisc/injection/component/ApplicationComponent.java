package com.zhqz.wisc.injection.component;

import android.app.Application;
import android.content.Context;


import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.data.db.AppDbModule;
import com.zhqz.wisc.injection.ApplicationContext;
import com.zhqz.wisc.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, AppDbModule.class})
public interface ApplicationComponent {

    void inject(Application application);

    @ApplicationContext
    Context context();

    Application application();

    WiscClient wiscClient();

}
