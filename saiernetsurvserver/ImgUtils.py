import threading
import cv2
import time
import numpy as np
import sys
import os
from pylibfreenect2 import Freenect2, SyncMultiFrameListener
from pylibfreenect2 import FrameType, Registration, Frame
from Pic_str import *
from FireDetect import *
from SendUtils import *

class ImgUtils:
    def __init__(self):
        self.fireDetect = FireDetect()
        self.sendUtils=SendUtils()
    def getDateStr(self):
        return (time.strftime('%Y-%m-%d',time.localtime(time.time())), time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))

    def getcameraimgbyusb(self, basedir):
        cap = cv2.VideoCapture(0)
        if (not cap.isOpened()):
            print("Cannot open the camera!")
            return
        cap.set(cv2.CAP_PROP_FPS, 30)
        cap.set(cv2.CAP_PROP_FRAME_WIDTH, 480)
        cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
        types = 0
        types |= FrameType.Color

        while True:
            ret, frame=cap.read()
            colorimg  =  cv2.resize(frame.asarray(), (200,150))
            (shortDate, longDate)=self.getDateStr()
            file_dir = basedir+"/"+shortDate
            if not os.path.exists(file_dir):
                os.makedirs(file_dir)
            new_filename = Pic_str().create_uuid() + '.jpg' 
            fpath = os.path.join(file_dir,  new_filename) 
            # print(fpath )
            cv2.imwrite(fpath, colorimg, [cv2.IMWRITE_JPEG_QUALITY,70])
            isfire = self.fireDetect.detect_image(fpath)
            self.sendUtils.sendReqtoServer("园区厂区仓库","网络摄像头",longDate,isfire,shortDate+"/"+new_filename)
            time.sleep(3)
        cap.release()
        return

    def  getcameraimgbykinect(self, basedir):
        try:
            from pylibfreenect2 import OpenGLPacketPipeline
            pipeline = OpenGLPacketPipeline()
        except:
            try:
                from pylibfreenect2 import OpenCLPacketPipeline
                pipeline = OpenCLPacketPipeline()
            except:
                from pylibfreenect2 import CpuPacketPipeline
                pipeline = CpuPacketPipeline()
        fn = Freenect2()
        num_devices = fn.enumerateDevices()
        if num_devices == 0:
            print("No device connected!")
            return
        serial = fn.getDeviceSerialNumber(0)
        device = fn.openDevice(serial, pipeline=pipeline)

        types = 0
        types |= FrameType.Color
            
        listener = SyncMultiFrameListener(types)

        # Register listeners
        device.setColorFrameListener(listener)
        device.setIrAndDepthFrameListener(listener)

        device.startStreams(rgb=True, depth=False)

        while True:
            frames = listener.waitForNewFrame()
            color = frames["color"]
            colorimg  =  cv2.resize(color.asarray(), (200,150))
            (shortDate, longDate)=self.getDateStr()
            file_dir = basedir+"/"+shortDate
            if not os.path.exists(file_dir):
                os.makedirs(file_dir)
            new_filename = Pic_str().create_uuid() + '.jpg' 
            fpath = os.path.join(file_dir,  new_filename) 
            # print(fpath )
            cv2.imwrite(fpath, colorimg, [cv2.IMWRITE_JPEG_QUALITY,70])
            listener.release(frames)
            isfire = self.fireDetect.detect_image(fpath)
            self.sendUtils.sendReqtoServer("园区厂区仓库","网络摄像头",longDate,isfire,shortDate+"/"+new_filename)
            time.sleep(3)
        device.stop()
        device.close()
        return
    
    def getcameraimgbyrtsp(self, basedir):
        rtspurl = "rtsp://admin:111111@192.168.1.211/Streaming/Channels/1"
        cap=cv2.VideoCapture(rtspurl)
         if (not cap.isOpened()):
            print("Cannot open the camera!")
            return
        cap.set(cv2.CAP_PROP_FPS, 30)
        cap.set(cv2.CAP_PROP_FRAME_WIDTH, 480)
        cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
        types = 0
        types |= FrameType.Color

        while True:
            ret, frame=cap.read()
            colorimg  =  cv2.resize(frame.asarray(), (200,150))
            (shortDate, longDate)=self.getDateStr()
            file_dir = basedir+"/"+shortDate
            if not os.path.exists(file_dir):
                os.makedirs(file_dir)
            new_filename = Pic_str().create_uuid() + '.jpg' 
            fpath = os.path.join(file_dir,  new_filename) 
            # print(fpath )
            cv2.imwrite(fpath, colorimg, [cv2.IMWRITE_JPEG_QUALITY,70])
            isfire = self.fireDetect.detect_image(fpath)
            self.sendUtils.sendReqtoServer("园区厂区仓库","网络摄像头",longDate,isfire,shortDate+"/"+new_filename)
            time.sleep(3)
        cap.release()
        return