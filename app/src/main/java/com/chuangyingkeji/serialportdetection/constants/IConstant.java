package com.chuangyingkeji.serialportdetection.constants;


import com.chuangyingkeji.serialportdetection.entity.Option;

/**
 * Created by Norton on 2017/7/10.
 *
 */

public interface IConstant {
    Option SERIAL_PORT = new Option("串口号",new String[]{"ttyS0","ttyS1","ttyS2","ttyS3","ttyS4","ttyS5","ttyS6","ttyS7","ttyS8","ttyS9","ttyXRM0","ttyXRM1","ttymxc1"});
    Option BAUD_RATE = new Option("波特率",new String[]{"9600","4800","19200","38400","43000","56000","57600"});
    Option DATA = new Option("数据位",new String[]{"8","6","7","9"});
    Option CHECK = new Option("校验位",new String[]{"无校验","奇校验","偶校验"});
    Option STOP = new Option("停止位",new String[]{"1","2"});

    int commodity1 = 283;
}
