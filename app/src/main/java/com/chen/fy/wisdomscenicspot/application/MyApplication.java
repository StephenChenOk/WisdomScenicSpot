package com.chen.fy.wisdomscenicspot.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import org.litepal.LitePal;

/**
 * 全局获取当前app的Context的方法
 */
public class MyApplication extends MultiDexApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        //初始化LitePal,让其在内部自动获取到当前的Context
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}