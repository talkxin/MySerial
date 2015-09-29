package com.serialportapi.usbserial_api;

import java.io.FileDescriptor;

/**
 * Created by young on 15-9-29.
 */
public class USBSerial {
    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    static {
        System.loadLibrary("usbserial_port");
    }
}
