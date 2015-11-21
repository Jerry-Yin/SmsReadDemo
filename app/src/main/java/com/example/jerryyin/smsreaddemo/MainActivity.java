package com.example.jerryyin.smsreaddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static final String TAG2 = "SQLiteException in getSmsInPhone";
    private StringBuilder smsBuilder;

    final String SMS_URI_ALL = "content://sms/";
    final String SMS_URI_INBOX = "content://sms/inbox";
    final String SMS_URI_SEND = "content://sms/sent";
    final String SMS_URI_DRAFT = "content://sms/draft";
    final String SMS_URI_OUTBOX = "content://sms/outbox";
    final String SMS_URI_FAILED = "content://sms/failed";
    final String SMS_URI_QUEUED = "content://sms/queued";


    private IntentFilter mIntentFilter;
    private MessageReceiver messageReceiver;

    private TextView mTxtAddress;
    private TextView mTxtMsgBody;

    public static String mMessageBody;
    public static String mAddress;

    public static File mFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtAddress = (TextView) findViewById(R.id.txt_address);
        mTxtMsgBody = (TextView) findViewById(R.id.txt_message_body);

//        createFile();

        Intent intent = new Intent(this, ReceiveMsgService.class);
        startService(intent);

//        Log.d(TAG, "smsMsg = " + ReadSmsMsg());
//
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
////        mIntentFilter.setPriority(100);
//        messageReceiver = new MessageReceiver();
//        registerReceiver(messageReceiver, mIntentFilter);
    }

    private void createFile() {
        if (mFile == null){
            String dir = String.valueOf(Environment.getExternalStorageDirectory());
            String dataName = "AMessage.txt";
            mFile = new File(dir, dataName);
            Log.d(TAG, "文件已经创建完毕 ！");
            Toast.makeText(MainActivity.this, "文件已经创建完毕!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 读取手机短信数据库的操作
     *
     * @return
     */
    private String ReadSmsMsg() {
        smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] destination = new String[]{"_id", "address", "person", "body", "date"};
            Cursor cur = getContentResolver().query(uri, destination, null, null, "date desc");

            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
//                int index_Type = cur.getColumnIndex("TYPE");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strBody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
//                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
//                    if (intType == 1) {
//                        strType = "接收";
//                    } else if (intType == 2) {
//                        strType = "发送";
//                    } else {
//                        strType = "null";
//                    }

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strBody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());
                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if
            smsBuilder.append("getSmsInPhone has executed!");
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }

        return smsBuilder.toString();
    }


//    public class MessageReceiver extends BroadcastReceiver {
//
//        private String messageBody;
//        private String address;
//
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            Toast.makeText(MainActivity.this, "收到一条广播！", Toast.LENGTH_SHORT).show();
//            //提取短信消息
//            Bundle bundle = intent.getExtras();
//            //使用 pdu 密钥来􏰀取 一个 SMS pdus 数组,其中每一个 pdu 都表示一条短信消息
//            Object[] pdus = (Object[]) bundle.get("pdus");
//            SmsMessage[] messages = new SmsMessage[pdus.length];
//            for (int i = 0; i < messages.length; i++) {
//                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//            }
//
//            address = messages[0].getOriginatingAddress();   //发送方号码
//            MainActivity.mAddress = address;
//
//            //凭借短信内容
//            for (SmsMessage message : messages) {
//                messageBody += message.getMessageBody();
//            }
//
//            MainActivity.mMessageBody = messageBody;
//
//            setAndSaveMsg();
//
////            abortBroadcast();
//
//        }
//    }

//    public static void setAndSaveMsg() {
////        if (TextUtils.isEmpty(mAddress) || TextUtils.isEmpty(mMessageBody)){
//        Toast.makeText(MainActivity.this, "正在存储数据！", Toast.LENGTH_SHORT).show();
//        mTxtAddress.setText(mAddress);
//        mTxtMsgBody.setText(mMessageBody);
//
//        String data = "from：" + mAddress + "\n" + "message: " + mMessageBody;
//
//        /**
//         * 文件存储在外置sd卡根目录的 /MyMessage/ 文件夹下
//         */
////        String dir = Environment.getExternalStorageDirectory() + "/MyMessage";
////        File file = new File(dir, "MyMessage.txt");
//
//        FileOutputStream outputStream = null;
//        BufferedWriter writer = null;
//        //数据写入文件
//        try {
//            outputStream = openFileOutput("MyMessage.txt", MODE_PRIVATE);
////            outputStream = new FileOutputStream(file);
//            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//            writer.write(data);
//
//            Toast.makeText(MainActivity.this, "数据存储成功，路径: "  + " MyMessage.txt", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, "文件MyMessage.txt 未找到", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, "文件流异常！！", Toast.LENGTH_SHORT).show();
//        } finally {
//            if (writer != null) {
//                try {
//                    writer.close();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
////        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(messageReceiver);
    }
}
