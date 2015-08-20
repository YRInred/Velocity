package com.inred.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by inred on 2015/7/31.
 */
public abstract class VelocityActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    /**
     * 基础跳转Activity
     * @param activity
     */
    protected <T> void toActivity(Class<T> activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }



}
