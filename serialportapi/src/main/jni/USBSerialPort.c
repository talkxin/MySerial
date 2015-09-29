//
// Created by young on 15-9-29.
//
#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

#include "com_serialportapi_usbserial_api_USBSerial.h"
#include "android/log.h"
/*
 * Class:     android_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_android_1serialport_1api_SerialPort_open
        (JNIEnv *env, jclass thiz, jstring path, jint baudrate, jint flags) {
    return NULL;
}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_android_1serialport_1api_SerialPort_close(JNIEnv *env, jobject thiz) {

}