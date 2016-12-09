package com.inred.simaple;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.inred.library.VelocityHttpActivity;
import com.inred.library.adapter.VelocityQuickAdapter;
import com.inred.library.adapter.VelocityQuickExpandableListAdapter;
import com.inred.simaple.center.MyAppCenter;
import com.inred.simaple.entity.Test;
import com.inred.simaple.net.HttpApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends VelocityHttpActivity { //VelocityHttpActivity


//    private TextView weatherText;
//    private Weather weather;

    private ListView listview;
    private List<String> lists;

    private ExpandableListView expandableListView;
    private List<Test> tests;

    private Animation animup, animdown;

    int width;
    int tipwidth;
    int status = 0;

    private Timer timer;
    private Interpolator interpolator;

    int num = 0;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    num++;
                    ViewGroup.LayoutParams layoutParamsdown = listview.getLayoutParams();
//                    layoutParamsdown.width = num == 25 ? width * 3 / 10 : width - num * tipwidth;
                    tipwidth = (int) (width*7/10 * interpolator.getInterpolation(((float)num)/(float)25));
                    layoutParamsdown.width = num == 25 ? width * 3 / 10 : width - tipwidth;
                    listview.setLayoutParams(layoutParamsdown);
                    if (num == 25) {
                        status = 0;
                    }
                    break;
                case 2:
                    num++;
                    ViewGroup.LayoutParams layoutParamsup = listview.getLayoutParams();
                    tipwidth = (int) (width*7/10 * interpolator.getInterpolation(((float)num)/(float)25));
//                    layoutParamsup.width = num == 25 ? ViewGroup.LayoutParams.MATCH_PARENT : width * 3 / 10 + num * tipwidth;
                    layoutParamsup.width = num == 25 ? ViewGroup.LayoutParams.MATCH_PARENT : width * 3 / 10 + tipwidth;
                    listview.setLayoutParams(layoutParamsup);
                    if (num == 25) {
                        status = 0;
                    }
                    break;
            }
        }
    };

    @Override
    protected void initData() {

        interpolator = new AnticipateOvershootInterpolator();

        WindowManager wm = this.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
        tipwidth = width * 7 / 10 / 25;
        super.initData();
        addRequestEntity(MyAppCenter.getInstance().api.GetWeatherApi());//add RequestItem in List
        startRequest();//beginRequest

        tests = new ArrayList<>();
        tests.add(new Test("123"));
        tests.add(new Test("321"));
        tests.add(new Test("222"));
        tests.add(new Test("111"));
        tests.add(new Test("333"));
        tests.add(new Test("666"));
        for (Test test : tests) {
            test.childs.addAll(tests);
        }


        lists = new ArrayList<>();
        lists.add("123");
        lists.add("123");
        lists.add("123");
        lists.add("123");
        lists.add("123");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(status);
            }
        }, 500, 20);

//        animup = AnimationUtils.loadAnimation(this,R.anim.upanim);
//
//
//        animdown = AnimationUtils.loadAnimation(this,R.anim.downanim);
//        animdown.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                ViewGroup.LayoutParams layoutParams = listview.getLayoutParams();
//                layoutParams.width = width*3/10;
//                listview.setLayoutParams(layoutParams);
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });

    }

    @Override
    protected int main_layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {


//        weatherText = (TextView) findViewById(R.id.weatherText);
        listview = (ListView) findViewById(R.id.first_list);
        expandableListView = (ExpandableListView) findViewById(R.id.two_three_exlist);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 0;
                status = 2;
//                ViewGroup.LayoutParams layoutParams = listview.getLayoutParams();
//                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                listview.setLayoutParams(layoutParams);
//                listview.startAnimation(animup);
            }
        });

        expandableListView.addHeaderView(imageView);

    }

    @Override
    protected void initVoid() {

        expandableListView.setAdapter(new VelocityQuickExpandableListAdapter<Test, Test>(this, R.layout.listview_item, R.layout.listview_item, tests) {

            @Override
            public <T> void convertGroup(ViewHolder holder, T item) {
                Test tItem = (Test) item;
                holder.setText(R.id.test, tItem.getName());
            }

            @Override
            public <V> void convertChild(ViewHolder holder, V item) {
                Test tItem = (Test) item;
                holder.setText(R.id.test, tItem.getName());
            }
        });

        for (int i = 0; i < tests.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        listview.setAdapter(new VelocityQuickAdapter<String>(this, R.layout.listview_item, lists) {
            @Override
            public <T> void convert(ViewHolder holder, T item, int position) {
                Log.e("convert", "position=" + position);
                holder.setText(R.id.test, item.toString());
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                num = 0;
                status = 1;

//                listview.startAnimation(animdown);

            }
        });
    }

    @Override
    protected void onErrorsResponse(int tagInt, String tag, VolleyError volleyError, Object... objects) {

    }

    @Override
    protected void onSuccessResponse(int tagInt, String tag, Object entity, Object... objects) {
        switch (tagInt) {
            case HttpApi.WEATHERTAGINT:
//                weather = (Weather) entity;
//                weatherText.setText(weather.toString());
                break;
        }
    }

}
