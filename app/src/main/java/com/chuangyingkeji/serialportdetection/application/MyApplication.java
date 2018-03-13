package com.chuangyingkeji.serialportdetection.application;

import android.app.Application;

import com.chuangyingkeji.serialportdetection.utils.CrashHandler;

/**
 * Created by Norton on 2017/12/2.
 *
 */

public class MyApplication extends Application {
    public static int sendByte = 0;//发送出去的字节数
    public static int receiveByte = 0;//接收到的字节数

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
