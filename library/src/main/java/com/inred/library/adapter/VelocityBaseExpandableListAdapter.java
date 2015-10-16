package com.inred.library.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.inred.library.entity.VelocityBELEntity;

import java.util.List;

/**
 * Created by inred on 2015/8/25.
 */
public abstract class VelocityBaseExpandableListAdapter<T extends VelocityBELEntity<V>, V> extends BaseExpandableListAdapter {

    protected List<T> groups;
    protected T group;
    protected List<V> childs;
    protected V child;
    protected Context context;

    public VelocityBaseExpandableListAdapter(Context context) {
        this.context = context;
    }

    public void setGroups(List<T> groups) {
        this.groups = groups;
    }

    @Override
    public int getGroupCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).childs == null ? 0 : groups.get(groupPosition).childs.size();
    }

    @Override
    public T getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public V getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).childs.get(childPosition);
    }

    @Override
    public  long getGroupId(int groupPosition) {
        return groupPosition;
    };

    @Override
    public  long getChildId(int groupPosition, int childPosition){
        return childPosition;
    };
    @Override
    public abstract boolean hasStableIds();

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getGroupViewHolder(convertView,parent,groupPosition,isExpanded).getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getChildViewHolder(convertView,parent,groupPosition,childPosition,isLastChild).getConvertView();
    }

    @Override
    public abstract boolean isChildSelectable(int groupPosition, int childPosition);

    protected ViewHolder groupViewHolder;
    protected ViewHolder childViewHolder;

    public abstract ViewHolder getChildViewHolder(View convertView, ViewGroup parent,int groupPosition, int childPosition,boolean isLastChild);
    public abstract ViewHolder getGroupViewHolder(View convertView, ViewGroup parent, int groupPosition,boolean isExpanded);

    protected static class ViewHolder {
        private final SparseArray<View> views;
        private View convertView;

        private ViewHolder(Context mContext, View convertView) {
            this.views = new SparseArray<View>();
            this.convertView = convertView;
            convertView.setTag(this);
        }

        public static ViewHolder get(Context mContext, View convertView, ViewGroup parent, int resId) {
            if (convertView == null) {
                LayoutInflater factory = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = factory.inflate(resId, parent, false);//
                return new ViewHolder(mContext, convertView);
            }
            return (ViewHolder) convertView.getTag();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T findViewById(int resourceId) {
            View view = views.get(resourceId);
            if (view == null) {
                view = convertView.findViewById(resourceId);
                views.put(resourceId, view);
            }
            return (T) view;
        }


        public void setText(int viewId,int stringId){
            TextView textView = findViewById(viewId);
            textView.setText(stringId);
        }


        public void setText(int viewId,String stringId){
            TextView textView = findViewById(viewId);
            textView.setText(stringId);
        }


        public View getConvertView() {
            return convertView;
        }

        public void setConvertView(View convertView) {
            this.convertView = convertView;
        }
    }


}
