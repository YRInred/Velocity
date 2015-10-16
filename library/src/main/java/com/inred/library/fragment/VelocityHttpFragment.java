package com.inred.library.fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inred.library.application.VelocityApplication;
import com.inred.library.entity.RequestEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by inred on 2015/9/7.
 */
public abstract class VelocityHttpFragment extends VelocityFragment{


    protected  int SOCKTIMEOUT = 5;//timeout time
    protected  int RETRYTIME = 1;//retry times

    protected List<RequestEntity> requestEntitys;

    @Override
    protected abstract int main_layout();

    @Override
    protected abstract void initView();

    /**
     * 添加请求
     *
     * @param requestEntity
     */
    public void addRequestEntity(RequestEntity requestEntity) {
        if (requestEntitys == null)
            requestEntitys = new ArrayList<>();
        requestEntitys.add(requestEntity);
    }

    /**
     * 启动网络加载
     */
    protected void startRequest() {
        addRequest(requestEntitys);
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
                            onSuccessResponse(entitiy.getTagInt(), entitiy.getTag(), entitiy.getParser().doParser(s),entitiy.getObjects());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            onErrorsResponse(entitiy.getTagInt(),
                                    entitiy.getTag(),
                                    volleyError,entitiy.getObjects());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (entitiy.getParams() == null || entitiy.getParams().isEmpty())
                        return super.getParams();
                    return entitiy.getParams();
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKTIMEOUT*1000,RETRYTIME,1.0f));
            VelocityApplication.getInstance().addToRequestQueue(stringRequest, entitiy.getTag());
        }
        requestEntitys.clear();
    }


    protected abstract void onSuccessResponse(int tagInt, String tag, Object entity,Object... objects);

    protected abstract void onErrorsResponse(int tagInt, String tag, VolleyError volleyError,Object... objects);

}
