package com.inred.library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by inred on 2015/10/16.
 */
public abstract class VelocityQuickAdapter<T extends Object> extends VelocityBaseAdapter {


    private int layoutId;

    private VelocityQuickAdapter(Context context) {
        super(context);
    }

    public VelocityQuickAdapter(Context context,int layoutId,List<T> datas){
        this(context);
        this.layoutId = layoutId;
        this.datas = datas;
    }



    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, int position) {

        ViewHolder holder = ViewHolder.get(context,convertView,parent,layoutId);
        data = getItem(position);
        convert(holder,data);

        return holder;
    }

    public abstract <T> void convert(ViewHolder holder,T item);

}
