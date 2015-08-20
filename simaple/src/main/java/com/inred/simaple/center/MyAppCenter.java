package com.inred.simaple.center;

import android.content.Context;

import com.inred.simaple.net.HttpApi;

/**
 * Created by inred on 2015/8/3.
 */
public class MyAppCenter {


    private static MyAppCenter center;

    public HttpApi api;

    public static MyAppCenter getInstance(){
        if (center == null)
            center = new MyAppCenter();
        return center;
    }


    private MyAppCenter() {
    }

    public void init(Context context){
        api = new HttpApi();
    }

}
