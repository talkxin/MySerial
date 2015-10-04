package com.serialport;

import android.app.Activity;
import android.content.Context;
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

import com.serialportapi.SerialCommunication;
import com.serialportapi.driver.USBSystm;
import com.serialportapi.mean.MessageHandler;
import com.serialportapi.serialport_api.SerialPort;
import com.serialportapi.thread.Event;

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
        final MessageHandler han = new MessageHandler() {
            @Override
            public void messageReceived(Context context, Object message) {

            }

            @Override
            public void messageEvent(Context context, Event event, String action) {

            }
        };
        new Thread() {
            @Override
            public void run() {
                SerialCommunication sp = new USBSystm(MainActivity.this, han);
                sp.initSerialPort(null, 115200);
            }
        }.start();
    }
}
