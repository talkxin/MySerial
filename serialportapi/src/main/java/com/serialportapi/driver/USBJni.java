package com.serialportapi.driver;

import android.content.Context;

import com.serialportapi.SerialCommunication;
import com.serialportapi.mean.MessageDecoder;

import java.util.List;

/**
 * Created by young on 15-9-23.
 * 不基于android系统提供的api与usb进行通讯
 * 讲通讯部分封装至jni层
 * 使用前需要root，效率会比系统高
 */
public class USBJni extends SerialCommunication {
    /**
     * 构造方法，接收处理的handler
     *
     * @param context
     */
    protected USBJni(Context context) {
        super(context);
    }

    @Override
    public void setDecoder(MessageDecoder decoder) {

    }

    @Override
    public List<String> getDriverList(int driver) {
        return null;
    }

    @Override
    public boolean initSerialPort(String devName, int baudrate) {
        return false;
    }

    @Override
    public boolean initSerialPort(String devName, int baudrate, int sleep) {
        return false;
    }

    @Override
    public boolean sendCommend(int commend, byte[] data) {
        return false;
    }

    @Override
    public boolean closeDevice() {
        return false;
    }

}
