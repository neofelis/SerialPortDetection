package com.chuangyingkeji.serialportdetection.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.chuangyingkeji.serialportdetection.activity.MainActivity;
import com.chuangyingkeji.serialportdetection.application.MyApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by Norton on 2017/6/6.
 */

public class SerialPortUtil {

    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static Thread receiveThread = null;
    public static int[] values;
    public static String serialData = "";
    public static boolean flag = false;

    /**
     * 打开串口的方法
     */
    public static void openSerialPort(Context context,String port,int baudRate, int dataBits, int stopBits, char parity) throws IOException {
        Log.i("test","打开串口");
        File file = new File("/dev/"+ port);
        serialPort = new SerialPort(file,baudRate,dataBits,stopBits,parity);
        //获取打开的串口0中的输入输出流，以便于串口数据的收发
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
        flag = true;
        receiveSerialPort(context);
    }


    /**
     *关闭串口的方法
     * 关闭串口中的输入输出流
     * 关闭串口
     */
    public static void closeSerialPort(){
        Log.i("test","关闭串口");
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
            flag = false;
            serialPort.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送十六进制串口数据的方法
     * @param data 要发送的数据
     */
    public static void sendHexSerialPort(int[] data){
        Log.i("test","发送串口数据");
        MyApplication.sendByte += data.length;
        try {
            //将16进制的int类型的数组转换为byte数组
            byte[] buf = AryChangeManager.hexToByte(data);
            //将数据写入串口
            outputStream.write(buf);
            outputStream.flush();
            Log.i("test","串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test","串口数据发送失败");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发送文本串口数据的方法
     * @param data 要发送的数据
     */
    public static void sendTextSerialPort(String data){
        Log.i("test","发送串口数据");
        try {
            //将16进制的int类型的数组转换为byte数组
//            byte[] buf = AryChangeManager.hexToByte(data);
            //将数据写入串口
            outputStream.write(data.getBytes());
            outputStream.flush();
            Log.i("test","串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test","串口数据发送失败");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 接收串口数据的方法
     */
    public static void receiveSerialPort(final Context context){
        Log.i("test","接收串口数据");
        /*定义一个handler对象要来接收子线程中接收到的数据
            并调用Activity中的刷新界面的方法更新UI界面
         */
        final Handler handler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    if(MainActivity.isHexReceive&&values!=null){
                        MyApplication.receiveByte += values.length;
                        MainActivity.refreshReceiveByte();
                        StringBuilder builder = new StringBuilder();
                        for(int i:values){
                            builder.append(AryChangeManager.dexToHex(i));
                            builder.append(" ");
                        }
//                    Toast.makeText(context,"串口接收到数据："+builder.toString(),Toast.LENGTH_LONG).show();
                        //对接收到的数据进行处理
                        MainActivity.refreshReceive(builder.toString());
                        values = null;
                    }else{
                        MainActivity.refreshReceive(serialData+" ");
                        serialData = "";
                    }
                }
            }
        };
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread(){
            @Override
            public void run() {
                while (flag){
                    try {
                        byte[] readData = new byte[1024];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size>0&&flag) {
                            if(MainActivity.isHexReceive){
                                //当前为16进制接收
                                values = new int[size];
                                for (int i = 0; i < values.length; i++) {
                                    values[i] |= (readData[i] & 0x000000ff);
                                }
                            }else{
                                //当前为文本模式接收
                                serialData = new String(readData,0,size);
                            }
                        /*将接收到的数据封装进Message中，然后发送给主线程
                            */
                            handler.sendEmptyMessage(1);
                            Thread.sleep(100);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();
    }


}
