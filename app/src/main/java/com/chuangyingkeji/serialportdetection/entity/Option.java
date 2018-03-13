package com.chuangyingkeji.serialportdetection.entity;

/**
 * Created by Norton on 2017/7/10.
 */

public class Option {
    private String name;//设置名称
    private String[] options;//对应的选项

    public Option(String name, String[] options) {
        this.name = name;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
