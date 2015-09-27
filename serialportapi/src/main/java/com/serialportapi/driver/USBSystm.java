package com.serialportapi.driver;

import android.content.Context;

import com.serialportapi.SerialCommunication;
import com.serialportapi.mean.MessageDecoder;

import java.util.List;

/**
 * Created by young on 15-9-23.
 * 基于android系统提供的usb串口进行通讯，采用了开源的usb支持
 * 不需要root
 */
public class USBSystm extends SerialCommunication {
    /**
     * 构造方法，接收处理的handler
     *
     * @param context
     */
    protected USBSystm(Context context) {
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
