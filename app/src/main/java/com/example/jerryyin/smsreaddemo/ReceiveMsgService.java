package com.example.jerryyin.smsreaddemo;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

/**
 * Created by JerryYin on 11/20/15.
 * 服务中注册广播
 */
public class ReceiveMsgService extends Service {


    private static final String TAG = "ReceiveMsgService";
    private MessageReceiver messageReceiver;
    private IntentFilter mIntentFilter;
    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "服务已经启动 ！");
        mIntent = intent;
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, mIntentFilter);
        Log.d(TAG, "服务已经启动，广播注册完毕！");

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 保证服务长期运行
     */
    @Override
    public void onDestroy() {
        System.out.println("service onDestroy");
        unregisterReceiver(messageReceiver);

        //保留了开启service的intent，在这里再启动一次自己，以达到长期运行的服务，不被系统杀死
        if (mIntent != null) {
            System.out.println("serviceIntent not null");
            startService(mIntent);
        }
    }
}
