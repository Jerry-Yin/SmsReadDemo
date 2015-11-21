package com.example.jerryyin.smsreaddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JerryYin on 11/21/15.
 */
public class MessageReceiver extends BroadcastReceiver {


    private static final String TAG = "MessageReceiver";
    private String messageBody;
    private String address;

    private String fileName = null;
    private String time = null;

    @Override
    public void onReceive(Context context, Intent intent) {
//            Toast.makeText(MainActivity.this, "收到一条广播！", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "收到短信广播");
        //提取短信消息
        Bundle bundle = intent.getExtras();
        //使用 pdu 密钥来􏰀取 一个 SMS pdus 数组,其中每一个 pdu 都表示一条短信消息
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }

        address = messages[0].getOriginatingAddress();   //发送方号码
        MainActivity.mAddress = address;
        time = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        time = formatter.format(curDate);
        fileName = address + " " +time+".txt";    //文件名
//        fileName = address +".txt";    //文件名

        //凭借短信内容
        for (SmsMessage message : messages) {
            messageBody += message.getMessageBody();
        }

        MainActivity.mMessageBody = messageBody;

        setAndSaveMsg(context);

//            abortBroadcast();

    }

    private void setAndSaveMsg(Context context) {
//        if (TextUtils.isEmpty(mAddress) || TextUtils.isEmpty(mMessageBody)){
        Toast.makeText(context, "正在存储数据！", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "正在存储数据");
//        mTxtAddress.setText(mAddress);
//        mTxtMsgBody.setText(mMessageBody);

        String data ="from：" + address + "\n" + "message: " + messageBody + "\n" + "time: " + time;

        /**
         * 文件存储在外置sd卡根目录的 /MyMessage/ 文件夹下
         */
        String dir = String.valueOf(Environment.getExternalStorageDirectory());
        File file = null;
//        if (MainActivity.mFile != null) {
//            file = MainActivity.mFile;
//        } else {
////            dir = Environment.getExternalStorageDirectory() + "/MyMessage";
//            dir = String.valueOf(Environment.getExternalStorageDirectory());
//            dataName = "AMessage.txt";
//            file = new File(dir, dataName);
//        }
        file = new File(dir, fileName);

        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        //数据写入文件
        try {
//            outputStream = openFileOutput("MyMessage.txt", MODE_PRIVATE);
            outputStream = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(data);

            Toast.makeText(context, "数据存储成功，路径: " + dir + fileName, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "数据:" + fileName + " " + data);
            Log.d(TAG, "数据存储成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "文件" + fileName + "未找到", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "文件流异常！！", Toast.LENGTH_SHORT).show();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        }
    }

}

