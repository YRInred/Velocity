package com.inred.simaple.entity;

import com.inred.library.entity.VelocityBELEntity;

/**
 * Created by inred on 2015/8/25.
 */
public class Test extends VelocityBELEntity<Test>{

    public Test(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
