package com.inred.library.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by inred on 2015/8/25.
 */
public abstract class VelocityBaseAdapter<T> extends BaseAdapter {

    protected List<T> datas;
    protected Context context;
    protected T data;

    public VelocityBaseAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewHolder(convertView, parent, position).getConvertView();
    }

    protected ViewHolder viewHolder;

    public abstract ViewHolder getViewHolder(View convertView, ViewGroup parent, int position);

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

        public View getConvertView() {
            return convertView;
        }

        public void setConvertView(View convertView) {
            this.convertView = convertView;
        }
    }
}
