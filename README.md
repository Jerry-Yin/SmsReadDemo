# SmsReadDemo
service ＋ 广播， 接收短信，并存储到sd卡跟目下的实时新建.txt文件中

长期service实现广播注册监听，接受到时触发广播，在广播接收器中解析短信，并新建.txt文件进行信息存储；
实际上就是一个短信app，没有界面效果。感兴趣的可以自行修改完善。
  
添加了通知栏状态栏，保证服务不被系统kill。

初次运行会在sd卡根目录下面创建文件夹 AMessage,用于保存信息；
信息内容：
  from: （电话号码）；
  message: null （信息内容）；
  time : 时间；
  
