package com.chuangyingkeji.serialportdetection.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chuangyingkeji.serialportdetection.R;
import com.chuangyingkeji.serialportdetection.entity.Option;

import java.util.List;

/**
 * Created by Norton on 2017/7/10.
 */

public class MyGridAdapter extends MyBaseAdapter<Option> {
    private ViewHolder holder;
    private Context context;

    public MyGridAdapter(Context context, List<Option> dataSource) {
        super(context, dataSource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.inflate_serial_port_setting,parent,false);
            holder = new ViewHolder();
            holder.textName = (TextView) convertView.findViewById(R.id.tv_setting_name);
            holder.spinnerOption = (Spinner) convertView.findViewById(R.id.sp_setting_option);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final Option option = ((Option) getItem(position));
        holder.textName.setText(option.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,option.getOptions());
        holder.spinnerOption.setAdapter(adapter);

        return convertView;
    }

    /**
     *类持有者
     */
    private class ViewHolder{
        private TextView textName;
        private Spinner spinnerOption;
    }
}
