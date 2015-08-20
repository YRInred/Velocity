package com.inred.library.irequest;

import com.android.volley.VolleyError;

/**
 * 请求回调
 * Created by inred on 2015/7/31.
 */
public interface RequestListener {

    void onResponse(int tagInt, String tag, Object entity);
    void onErrorResponse(int tagInt, String tag, VolleyError volleyError);

}
