package com.serialportapi.serialport_api.usb;

import java.io.FileDescriptor;

/**
 * Created by young on 15-9-28.
 * 建立USB JNI类库
 */
public class USBSerialProt {

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    static {
        System.loadLibrary("usbserial_port");
    }
}
