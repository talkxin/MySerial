package com.serialport;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends Activity {
    private Handler mHandler;
    private Button btnTest;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest = (Button) this.findViewById(R.id.btn_01);
        textView = (TextView) this.findViewById(R.id.view_01);
        btnTest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //可以看出这里启动了一个线程来操作消息的封装和发送的工作
                //这样原来主线程的发送就变成了其他线程的发送，简单吧？呵呵
                new MyThread().start();
            }
        });
    }

    class MyHandler extends Handler {

        public MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("liuxin", "我是主线程的Handler，收到了消息");
//            textView.setText("我是主线程的Handler，收到了消息：" + (String) msg.obj);
        }
    }

    //加了一个线程类
    class MyThread extends Thread {

        public void run() {
            Looper looper = Looper.getMainLooper(); //主线程的Looper对象
            //这里以主线程的Looper对象创建了handler，
            //所以，这个handler发送的Message会被传递给主线程的MessageQueue。
            mHandler = new MyHandler(looper);

            //构建Message对象
            //第一个参数：是自己指定的message代号，方便在handler选择性地接收
            //第二三个参数没有什么意义
            //第四个参数需要封装的对象
            Message msg = mHandler.obtainMessage(1, 1, 1, "其他线程发消息了");

            while (true) {
                mHandler.sendMessage(msg); //发送消息
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
