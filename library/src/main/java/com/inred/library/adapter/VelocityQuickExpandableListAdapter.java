package com.inred.library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.inred.library.entity.VelocityBELEntity;

import java.util.List;

/**
 * Created by inred on 2015/10/16.
 */
public abstract class VelocityQuickExpandableListAdapter<T extends VelocityBELEntity<V>,V> extends VelocityBaseExpandableListAdapter{

    private int groupLayoutId;
    private int childLayoutId;


    private VelocityQuickExpandableListAdapter(Context context) {
        super(context);
    }

    /**
     * 快速构造器
     * @param context
     * @param groupLayoutId
     * @param childLayoutId
     * @param groups
     */
    public VelocityQuickExpandableListAdapter(Context context,int groupLayoutId,int childLayoutId,List<T> groups){
        this(context);
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
        this.groups = groups;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public ViewHolder getChildViewHolder(View convertView, ViewGroup parent, int groupPosition, int childPosition, boolean isLastChild) {
        ViewHolder holder = ViewHolder.get(context, convertView, parent, groupLayoutId);
        child = getChild(groupPosition, childPosition);
        convertChild(holder, child);
        return holder;
    }

    @Override
    public ViewHolder getGroupViewHolder(View convertView, ViewGroup parent, int groupPosition, boolean isExpanded) {
        ViewHolder holder = ViewHolder.get(context, convertView, parent, childLayoutId);
        group = getGroup(groupPosition);
        convertGroup(holder, group);
        return holder;
    }

    /**
     * 父布局事件构建
     * @param holder
     * @param item
     * @param <T>
     */
    public abstract <T> void convertGroup(ViewHolder holder,T item);

    /**
     * 子布局事件构建
     * @param holder
     * @param item
     * @param <V>
     */
    public abstract <V> void convertChild(ViewHolder holder,V item);


}
