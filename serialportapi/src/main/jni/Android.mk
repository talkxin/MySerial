#编译第一个动态库
#该动态库是提供对232串口的操作
#路径必须为绝对路径
MYJNI_PATH:=/home/young/AndroidStudioProjects/MySerial/serialportapi/src/main/jni/
MYJNIDEBUG_PATH:=/home/young/AndroidStudioProjects/MySerial/serialportapi/src/debug/jni/

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \
	-lz \
	-lm \

LOCAL_SRC_FILES := \
	$(MYJNI_PATH)/SerialPort.c \

LOCAL_C_INCLUDES += $(MYJNI_PATH)
LOCAL_C_INCLUDES += $(MYJNIDEBUG_PATH)

include $(BUILD_SHARED_LIBRARY)


#编译提供USB串口操作的动态库
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := usbserial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \
	-lz \
	-lm \

LOCAL_SRC_FILES := \
	$(MYJNI_PATH)/USBSerialPort.c \

LOCAL_C_INCLUDES += $(MYJNI_PATH)
LOCAL_C_INCLUDES += $(MYJNIDEBUG_PATH)

include $(BUILD_SHARED_LIBRARY)