package com.inred.simaple.application;

import com.inred.library.application.VelocityApplication;
import com.inred.simaple.center.MyAppCenter;

/**
 * Created by inred on 2015/8/3.
 */
public class MyApplication extends VelocityApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        MyAppCenter center = MyAppCenter.getInstance();
        center.init(this);

    }

    @Override
    public void crashTodo(String sb) {

    }
}
