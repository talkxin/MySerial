package com.serialportapi.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by young on 15-9-28.
 */
public class SerialService extends Service {

    /**
     * 串口不能获取root权限
     */
    public static final String SERIAL_NOT_ROOT = "com.serialportapi.sdk.serial_not_root";

    /**
     * 串口读取数据
     */
    public static final String SERIAL_READ = "com.serialportapi.sdk.serial_read";

    /**
     * 串口读取线程抛出的LOG
     */
    public static final String SERIAL_LOG = "com.serialportapi.sdk.serial_log";


    private final LocalBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public static IntentFilter getIntentFilter() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SERIAL_NOT_ROOT);
        intentFilter.addAction(SERIAL_LOG);
        intentFilter.addAction(SERIAL_READ);
        return intentFilter;
    }

    public class LocalBinder extends Binder {
        public SerialService getService() {
            return SerialService.this;
        }
    }
}
