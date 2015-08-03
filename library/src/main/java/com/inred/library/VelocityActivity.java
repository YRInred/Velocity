package com.inred.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inred.library.application.VelocityApplication;
import com.inred.library.entity.RequestEntity;
import com.inred.library.irequest.RequestListener;

import java.util.List;
import java.util.Map;

/**
 * Created by inred on 2015/7/31.
 */
public abstract class VelocityActivity extends Activity {
    protected List<RequestEntity> RequestEntitys;
    private RequestListener requestListener;


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
        addRequest(RequestEntitys);
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

    /**
     * 设置网络请求监听
     * @param requestListener
     */
    protected void setRequestListener(RequestListener requestListener){
        this.requestListener = requestListener;
    }

    /**
     * 添加网络请求
     * @param RequestEntitys
     */
    private void addRequest(List<RequestEntity> RequestEntitys) {
        if (RequestEntitys == null || RequestEntitys.isEmpty())
            return;

        StringRequest stringRequest;
        for (final RequestEntity entitiy : RequestEntitys) {
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
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (entitiy.getParams() == null || entitiy.getParams().isEmpty())
                        return super.getParams();
                    return entitiy.getParams();
                }
            };
            VelocityApplication.getInstance().addToRequestQueue(stringRequest, entitiy.getTag());
        }
    }

}
