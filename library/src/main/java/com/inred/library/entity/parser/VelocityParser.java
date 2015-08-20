package com.inred.library.entity.parser;

/**
 * 实体类解析器
 * Created by inred on 2015/7/31.
 */
public abstract class VelocityParser<T> {

    public abstract T doParser(String s);

}
