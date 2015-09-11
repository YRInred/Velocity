package com.inred.library.velocitycenter;

import android.content.Context;

/**
 * Created by inred on 2015/9/7.
 */
public class VelocityCenter {

    private static VelocityCenter center;

    public VelocityAppHelper helper;

    public static VelocityCenter getInstance(){
        if (center == null)
            center = new VelocityCenter();
        return center;
    }


    private VelocityCenter() {
    }

    public void init(Context context){
        helper = new VelocityAppHelper();

    }




}
