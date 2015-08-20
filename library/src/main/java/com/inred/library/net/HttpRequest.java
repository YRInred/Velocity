package com.inred.library.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inred.library.application.VelocityApplication;
import com.inred.library.entity.RequestEntity;
import com.inred.library.irequest.RequestListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by inred on 2015/8/4.
 */
public class HttpRequest {


    private List<RequestEntity> requestEntitys;
    private RequestListener requestListener;


    /**
     * 新建Http请求
     * @param requestListener
     */
    public HttpRequest(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    /**
     * 添加请求
     * @param requestEntity
     */
    public void addRequestEntity(RequestEntity requestEntity) {
        if (requestEntity == null)
            requestEntitys = new ArrayList<>();
        requestEntitys.add(requestEntity);
    }

    /**
     * 开始请求
     */
    public void startRequest(){
        addRequest(requestEntitys);
    }

    /**
     * 设置网络请求监听
     *
     * @param requestListener
     */
    protected void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }


    /**
     * 添加网络请求
     *
     * @param requestEntitys
     */
    private void addRequest(List<RequestEntity> requestEntitys) {
        if (requestEntitys == null || requestEntitys.isEmpty())
            return;

        StringRequest stringRequest;
        for (final RequestEntity entitiy : requestEntitys) {
            stringRequest = new StringRequest(
                    entitiy.getRequestMethod(),
                    entitiy.getUrl(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (requestListener != null)
                                requestListener.onResponse(entitiy.getTagInt(),
                                        entitiy.getTag(),
                                        entitiy.getParser().doParser(s));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (requestListener != null)
                                requestListener.onErrorResponse(entitiy.getTagInt(),
                                        entitiy.getTag(),
                                        volleyError);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (entitiy.getParams() == null || entitiy.getParams().isEmpty())
                        return super.getParams();
                    return entitiy.getParams();
                }
            };
            VelocityApplication.getInstance().addToRequestQueue(stringRequest, entitiy.getTag());
        }
        requestEntitys.clear();
    }



}
