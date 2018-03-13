package com.chuangyingkeji.serialportdetection.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.chuangyingkeji.serialportdetection.R;
import com.chuangyingkeji.serialportdetection.adapter.MyGridAdapter;
import com.chuangyingkeji.serialportdetection.application.MyApplication;
import com.chuangyingkeji.serialportdetection.constants.IConstant;
import com.chuangyingkeji.serialportdetection.entity.Option;
import com.chuangyingkeji.serialportdetection.utils.AryChangeManager;
import com.chuangyingkeji.serialportdetection.utils.SerialPortUtil;
import com.chuangyingkeji.serialportdetection.utils.ThreadPoolManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static boolean isSerialOpen = false;//串口状态
    private static Button btnControlSerial;//控制串口开关

    private GridView gridSetting;//串口数据设置区
    private MyGridAdapter adapter;
    private List<Option> options;

    private  static TextView textReceiveContent;//串口数据接收区
    private EditText editTransportContent;//串口数据发送区
    private RadioGroup groupReceive;//接收缓冲区
    private RadioGroup groupTransport;//发送缓冲区
    private Button btnClearReceive;//清空接收区按钮
    private Button btnClearTransport;//清空发送区按钮
    private Button btnSend;//发送串口指令

    private CheckBox boxAutoSend;//自动发送
    private EditText editAutoTime;//自动发送串口数据的时间间隔
    private boolean isAutoSend = false;//是否自动发送
    private static TextView textSendByte;//发送出去的字节数
    private static TextView textReceiveByte;//接收到的字节数
    private Button btnClear;//清空自动接收的字节数

    private static String port;//串口号
    private static int baud;//波特率
    private static char check;//校验位
    private static int data;//数据位
    private static int stop;//停止位
    private RadioGroup groupReceiveModel;//接收方法的单选按钮组
    private RadioGroup groupTransportModel;//发送方法的单选按钮组
    public static boolean isHexReceive = true;//当前为16进制接收
    private static boolean isHexTransport = true;//当前为16进制发送

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        /*清空接收区
         */
        btnClearReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textReceiveContent.setText("");
            }
        });

        /*清空发送区
         */
        btnClearTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTransportContent.setText("");
            }
        });

        /*打开/关闭串口
         */
        btnControlSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSerialOpen){
                    //关闭串口
                    closeSerial();
                }else{
                    //打开串口
                    openSerial();
                }
            }
        });

        /*发送串口指令
         */
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editTransportContent.getText().toString();
                Log.i("TAG",str+"=");
//                for(int i:AryChangeManager.stringToHex(str)){
//                    Log.i("TAG",""+i);
//                }
                if(isHexTransport){
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                }else{
                    SerialPortUtil.sendTextSerialPort(str);
                }
            }
        });

        /**
         *接收方式的选择改变监听
         */
        groupReceiveModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("TAG","接收方式改变");
                if(group.getCheckedRadioButtonId()==R.id.rb_receive_hex){
                    Log.i("TAG","16进制接收");
                    isHexReceive = true;
                }else{
                    Log.i("TAG","文本模式接收");
                    isHexReceive = false;
                }
            }
        });

        /**
         *发送方式的选择改变监听
         */
        groupTransportModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("TAG","发送方式改变");
                if(group.getCheckedRadioButtonId()==R.id.rb_transport_hex){
                    Log.i("TAG","16进制发送");
                    isHexTransport = true;
                }else{
                    Log.i("TAG","文本模式发送");
                    isHexTransport = false;
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.receiveByte = 0;
                MyApplication.sendByte = 0;
                refreshReceiveByte();
                refreshSendByte();
            }
        });

        /*自动发送
         */
        boxAutoSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                isAutoSend = isChecked;
                ThreadPoolManager.getManager().getCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            int time = Integer.valueOf(editAutoTime.getText().toString());
                            String str = editTransportContent.getText().toString();
                            Log.i("test","str="+str);
                            while (isAutoSend){
                                //自动发送
                                if(isHexTransport){
                                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                                }else{
                                    SerialPortUtil.sendTextSerialPort(str);
                                }
                                Thread.sleep(time);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        final Handler handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==100){
                    refreshSendByte();
                }
            }
        };
        ThreadPoolManager.getManager().getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    handler.sendEmptyMessage(100);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     *初始化操作
     */
    private void init() {
        initUI();
        initGridSetting();
    }

    private void initGridSetting() {
        options = new ArrayList<>();
        options.add(IConstant.SERIAL_PORT);
        options.add(IConstant.BAUD_RATE);
        options.add(IConstant.CHECK);
        options.add(IConstant.DATA);
        options.add(IConstant.STOP);
        adapter = new MyGridAdapter(MainActivity.this,options);
        gridSetting.setAdapter(adapter);
    }

    /**
     *初始化UI控件
     */
    private void initUI() {
        btnControlSerial = (Button) findViewById(R.id.btn_control_serial);

        gridSetting = (GridView) findViewById(R.id.gv_main_setting);

        textReceiveContent = (TextView) findViewById(R.id.tv_receive_content);
        editTransportContent = (EditText) findViewById(R.id.et_transport_content);
        groupReceive = (RadioGroup) findViewById(R.id.rg_receive_model);
        groupTransport = (RadioGroup) findViewById(R.id.rg_transport_model);
        btnClearReceive = (Button) findViewById(R.id.btn_receive_clear);
        btnClearTransport = (Button) findViewById(R.id.btn_transport_clear);
        btnSend = (Button) findViewById(R.id.btn_transport_send);
        groupReceiveModel = (RadioGroup) findViewById(R.id.rg_receive_model);
        groupTransportModel = (RadioGroup) findViewById(R.id.rg_transport_model);

        boxAutoSend = (CheckBox) findViewById(R.id.cb_auto_send);
        editAutoTime = (EditText) findViewById(R.id.et_auto_time);
        textSendByte = (TextView) findViewById(R.id.tv_send_byte);
        textReceiveByte = (TextView) findViewById(R.id.tv_receive_byte);
        btnClear = (Button)findViewById(R.id.btn_clear);
    }

    /**
     *设置更改，关闭串口
     */
    public static void closeSerial(){
        if(!isSerialOpen)
            return;
        btnControlSerial.setText("打开串口");
        isSerialOpen = false;
        SerialPortUtil.closeSerialPort();
    }
    /**
     *打开串口
     */
    public void openSerial(){
        try{
            getSerialPortSetting();
            Log.i("TAG","port="+port+" baud="+baud+" data="+data+" stop="+stop+" check="+check);
            SerialPortUtil.openSerialPort(this,port,baud,data,stop,check);
            btnControlSerial.setText("关闭串口");
            isSerialOpen = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *获取用户设置的串口数据
     */
    private void getSerialPortSetting() {
        for(int i=0;i<5;i++){
            LinearLayout view = (LinearLayout) gridSetting.getChildAt(i);
            Spinner spinner = (Spinner) view.findViewById(R.id.sp_setting_option);
            switch (i){
                case 0:port = (String) spinner.getSelectedItem();break;
                case 1:baud = Integer.valueOf((String) spinner.getSelectedItem());break;
                case 2:
                    String str = ((String) spinner.getSelectedItem());
                    check = str.equals("无校验")?'N':str.equals("奇校验")?'O':'E';break;
                case 3:data = Integer.valueOf((String) spinner.getSelectedItem());break;
                case 4:stop = Integer.valueOf((String) spinner.getSelectedItem());break;
            }
        }
    }

    public static void refreshReceive(String data){
        String str = textReceiveContent.getText().toString();
        if(str.length()>500){
            str = "";
        }
        textReceiveContent.setText(str+data);
    }

    public static void refreshSendByte(){
        textSendByte.setText("发送:"+ MyApplication.sendByte);
    }
    public static void refreshReceiveByte(){
        textReceiveByte.setText("接收:"+ MyApplication.receiveByte);
    }
}
