package com.serialportapi.driver;

import android.content.Context;

import com.serialportapi.SerialCommunication;
import com.serialportapi.mean.MessageDecoder;

import java.util.List;

/**
 * Created by young on 15-9-23.
 * 232串口子类，用于开启串口，并且与串口进行数据交互
 * 使用前提必须root
 */
public class Serial232 extends SerialCommunication {
    /**
     * 设备目录
     */
    private final String devurl = "/dev/";

    /**
     * 构造方法，接收处理的handler
     *
     * @param context
     */
    Serial232(Context context) {
        super(context);
    }

    @Override
    public void setDecoder(MessageDecoder decoder) {
        this.decoder = decoder;
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
