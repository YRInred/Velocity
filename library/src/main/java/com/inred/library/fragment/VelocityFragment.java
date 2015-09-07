package com.inred.library.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inred.library.velocitycenter.VelocityCenter;

/**
 * Created by inred on 2015/9/7.
 */
public abstract class VelocityFragment extends Fragment {

    protected View layoutView;//主layout视图

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化数据data
        initData();
        //初始化主layout
        layoutView = inflater.inflate(main_layout(), null);
        //初始化视图view
        initView();
        //初始化逻辑编辑
        initVoid();
        return layoutView;
    }

    /**
     * 四、初始化逻辑编辑
     */
    private void initVoid() {
    }

    /**
     * 三、初始化视图view
     */
    protected abstract void initView();

    /**
     * 一、初始化数据data
     */
    protected void initData() {
    }

    /**
     * 二、初始化主layout
     */
    protected abstract int main_layout();


    /**
     * showToast
     *
     * @param s
     */
    protected void showToast(String s) {
        VelocityCenter.getInstance().helper.showToast(s, getActivity());
    }

    /**
     * showToast
     *
     * @param resId
     */
    protected void showToast(int resId) {
        VelocityCenter.getInstance().helper.showToast(resId,getActivity());
    }

    /**
     * 基础跳转Activity
     *
     * @param activity
     */
    protected <T> void toActivity(Class<T> activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
    }


}
