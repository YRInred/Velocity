package com.inred.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.inred.library.application.VelocityApplication;
import com.inred.library.velocitycenter.VelocityCenter;

/**
 * Created by inred on 2015/7/31.
 */
public abstract class VelocityActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VelocityApplication.getInstance().addActivity(this);
        initData();
        setContentView(main_layout());
        initView();
        initVoid();
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }



    /**
     * layoutID
     * @return
     */
    protected abstract int main_layout();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化方法
     */
    protected void initVoid() {
    }

    /**
     * showToast
     * @param s
     */
    protected void showToast(String s) {
        VelocityCenter.getInstance().helper.showToast(s, this);
    }

    /**
     * showToast
     * @param resId
     */
    protected void showToast(int resId) {
        VelocityCenter.getInstance().helper.showToast(resId, this);
    }

    /**
     * 基础跳转Activity
     * @param activity
     */
    protected <T> void toActivity(Class<T> activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VelocityApplication.getInstance().removeActivity(this);
    }
}
