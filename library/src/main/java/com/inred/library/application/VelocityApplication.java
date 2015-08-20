package com.inred.library.application;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.inred.library.exception.CrashHandler;

import java.util.List;

/**
 * Created by inred on 2015/7/31.
 */
public abstract class VelocityApplication extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    private static VelocityApplication app;

    private List<Activity> activityList;

    public static VelocityApplication getInstance() {
        return app;
    }

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        app = this;

    }

    /**
     * ɾ������activity
     *
     * @param b
     */
    public void finishAll(boolean b) {
        int size;
        if (b)
            size = activityList.size();
        else
            size = activityList.size() - 1;
        for (int i = 0; i < size; i++) {
            Activity activity = activityList.get(i);
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * ɾ��activity
     *
     * @param activity
     */
    public void delActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }

    }

    /**
     * ���activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }


    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            // 1
            // 2
            synchronized (VelocityApplication.class) {
                if (mRequestQueue == null) {
                    mRequestQueue = Volley
                            .newRequestQueue(getApplicationContext());
                }
            }
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified then
     * it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * 异常处理-上传或保存
     * @param sb
     */
    public abstract void crashTodo(String sb);


}
