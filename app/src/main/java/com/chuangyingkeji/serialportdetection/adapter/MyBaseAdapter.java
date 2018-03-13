package com.chuangyingkeji.serialportdetection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Norton on 2017/4/27.
 *
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> dataSource;

    public MyBaseAdapter(Context context, List<T> dataSource){
        super();
        this.context = context;
        this.dataSource = dataSource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 向集合汇总添加一堆数据
     * @param list 要添加的数据集
     * @param isClear 是否要对原有数据进行清空
     */
    public void addAll(List<T> list,boolean isClear){
        if(isClear){
            dataSource.clear();
        }
        dataSource.addAll(list);
        notifyDataSetChanged();
    }
    /**
     * 向集合中添加一条新的数据
     * @param t
     */
    public void add(T t){
        dataSource.add(t);
        notifyDataSetChanged();
    }

    /**
     * 向集合汇总移除一条数据
     * @param t
     */
    public void remove(T t){
        dataSource.remove(t);
        notifyDataSetChanged();
    }

    /**
     * 清空数据集合
     */
    public void clear(){
        dataSource.clear();
        notifyDataSetChanged();
    }
}
