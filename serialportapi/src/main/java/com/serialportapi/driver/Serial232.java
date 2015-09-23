package com.serialportapi.driver;

import android.content.Context;

import com.serialportapi.SerialCommunication;
import com.serialportapi.mean.MessageDecoder;
import com.serialportapi.mean.MessageHandler;

import java.util.List;

/**
 * Created by young on 15-9-23.
 * 232串口子类，用于开启串口，并且与串口进行数据交互
 * 使用前提必须root
 */
public class Serial232 extends SerialCommunication {
    /**
     * 构造方法，接收处理的handler
     *
     * @param context
     * @param handler
     */
    Serial232(Context context, MessageHandler handler) {
        super(context, handler);
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
    public boolean sendCommend(int commend, byte[] data) {
        return false;
    }

    @Override
    public boolean closeDevice() {
        return false;
    }

    @Override
    public void setSleep(int sleep) {

    }
}