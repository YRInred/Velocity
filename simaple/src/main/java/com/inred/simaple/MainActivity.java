package com.inred.simaple;

import android.widget.TextView;

import com.android.volley.VolleyError;
import com.inred.library.VelocityHttpActivity;
import com.inred.simaple.center.MyAppCenter;
import com.inred.simaple.entity.Weather;
import com.inred.simaple.net.HttpApi;

public class MainActivity extends VelocityHttpActivity { //VelocityHttpActivity


    private TextView weatherText;
    private Weather weather;

    @Override
    protected void initData() {
        super.initData();
        addRequestEntity(MyAppCenter.getInstance().api.GetWeatherApi());//add RequestItem in List
        startRequest();//beginRequest
    }

    @Override
    protected int main_layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        weatherText = (TextView) findViewById(R.id.weatherText);
    }

    @Override
    protected void onErrorsResponse(int tagInt, String tag, VolleyError volleyError, Object... objects) {

    }

    @Override
    protected void onSuccessResponse(int tagInt, String tag, Object entity, Object... objects) {
        switch (tagInt) {
            case HttpApi.WEATHERTAGINT:
                weather = (Weather) entity;
                weatherText.setText(weather.toString());
                break;
        }
    }

}
