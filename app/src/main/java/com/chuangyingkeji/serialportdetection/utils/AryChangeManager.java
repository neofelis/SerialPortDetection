package com.chuangyingkeji.serialportdetection.utils;

import android.util.Log;

/**
 * Created by Norton on 2017/6/16.
 */

public class AryChangeManager {

    /**
     *十六进制数组转byte数组
     * @param data 要进行转换的十六进制数
     * @return 转换得到的byte数组
     */
    public static byte[] hexToByte(int[] data){
        byte[] buf = new byte[data.length];
        for(int i=0;i<data.length;i++) {
            buf[i] = (byte) (data[i] & 0x000000ff);
        }
        return buf;
    }

    /**
     *将用户输入的字符串转成16进制数
     * @param data 要进行转化的字符串
     * @return 转换完成后的16进制数
     */
    public static int[] stringToHex(String data){
        data = data.toLowerCase();
        data = data.replace(" ","");
        Log.i("TAG","data="+data);
        Log.i("TAG","data.length="+data.length());
        int[] buf;
        if(data.length()%2==0){
            buf = new int[data.length()/2];
        }else{
            buf = new int[data.length()/2+1];
        }
        for(int i=0,j=0;i<data.length();i+=2,j++){
            char c1 = data.charAt(i);
            char c2;
            if(i+1==data.length()){
                c2 = 0;
            }else{
                c2 = data.charAt(i+1);
            }
            int n1 = charToHex(c1);
            int n2 = charToHex(c2);

            buf[j] = 16*n1+n2;
            Log.i("TAG","c1="+c1+" c2="+c2);
        }
        for(int k:buf){
            Log.i("TAG","k="+k);
        }
        return buf;
    }

    /**
     *将char类型的数转换成16进制的数
     * @param c char数
     * @return 对应的16进制数
     */
    public static int charToHex(char c){
        int n = 0;
        switch (c){
            case '0':n = 0X0;break;
            case '1':n = 0X1;break;
            case '2':n = 0X2;break;
            case '3':n = 0X3;break;
            case '4':n = 0X4;break;
            case '5':n = 0X5;break;
            case '6':n = 0X6;break;
            case '7':n = 0X7;break;
            case '8':n = 0X8;break;
            case '9':n = 0X9;break;
            case 'a':n = 0XA;break;
            case 'b':n = 0XB;break;
            case 'c':n = 0XC;break;
            case 'd':n = 0XD;break;
            case 'e':n = 0XE;break;
            case 'f':n = 0XF;break;
        }
        return n;
    }

    /**
     *将10进制数转对应的16进制的字符串显示
     * @param i 要进行转换显示的十进制数
     * @return 对应的十六进制数
     */
    public static String dexToHex(int i){
        StringBuilder builder = new StringBuilder();
        if(i==0){
            builder.append("00");
        }else{
            while(i>0){
                switch (i%16) {
                    case 10:
                        builder.insert(0, 'A');
                        break;
                    case 11:
                        builder.insert(0, 'B');
                        break;
                    case 12:
                        builder.insert(0, 'C');
                        break;
                    case 13:
                        builder.insert(0, 'D');
                        break;
                    case 14:
                        builder.insert(0, 'E');
                        break;
                    case 15:
                        builder.insert(0, 'F');
                        break;
                    default:
                        builder.insert(0,i%16);
                        break;
                }
                i /= 16;
            }
        }
        if(builder.length()==1){
            builder.insert(0,"0");
        }
        return builder.toString();
    }

}
