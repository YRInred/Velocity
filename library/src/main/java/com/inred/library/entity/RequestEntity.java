package com.inred.library.entity;

import com.android.volley.Request;
import com.inred.library.entity.parser.VelocityParser;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * �������������
 * Created by inred on 2015/7/31.
 */
public class RequestEntity {

    private int tagInt;
    private String tag = "";//����ͷ
    private String url;//����url
    private int requestMethod = Request.Method.GET;//Ĭ��Ϊget����
    private HashMap<String,String> params;//����
    private VelocityParser parser;



    public int getTagInt() {
        return tagInt;
    }

    public RequestEntity setTagInt(int tagInt) {
        this.tagInt = tagInt;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public RequestEntity setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RequestEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public RequestEntity setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public RequestEntity setParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    public <T> VelocityParser<T> getParser() {
        return parser;
    }

    public <T> RequestEntity setParser(VelocityParser<T> parser) {
        this.parser = parser;
        return this;
    }

    @Override
    public String toString() {
        String s = MessageFormat.format("index = {0}, url = {1}, requestMethod = {2}", tagInt, url, requestMethod);
        return s;
    }


}