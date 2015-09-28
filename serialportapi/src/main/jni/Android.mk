LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \
	-lz \
	-lm \

LOCAL_SRC_FILES := \
	/home/young/AndroidStudioProjects/MySerial/serialportapi/src/main/jni/SerialPort.c \

LOCAL_C_INCLUDES += /home/young/AndroidStudioProjects/MySerial/serialportapi/src/main/jni
LOCAL_C_INCLUDES += /home/young/AndroidStudioProjects/MySerial/serialportapi/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
