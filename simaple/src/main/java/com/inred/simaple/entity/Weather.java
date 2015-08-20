package com.inred.simaple.entity;

import com.inred.library.entity.parser.VelocityParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

/**
 * Created by inred on 2015/8/3.
 */
public class Weather extends VelocityParser<Weather> {//Entity extend VelocityParser

    private String city;
    private String temp;
    private String windDirection;
    private String windScale;
    private String time;


    @Override
    public Weather doParser(String s) {
        Weather weather = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject weatherinfo = jsonObject.optJSONObject("weatherinfo");
            weather.setCity(weatherinfo.optString("city"));
            weather.setWindDirection(weatherinfo.optString("WD"));
            weather.setTemp(weatherinfo.optString("temp"));
            weather.setWindScale(weatherinfo.optString("WS"));
            weather.setTime(weatherinfo.optString("time"));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        String message = MessageFormat.format("WEATHERINFO<br>" +
                "city:{0}<br>" +
                "temp:{1}<br>" +
                "windDirection:{2}<br>" +
                "windScale:{3}<br>" +
                "time:{4}",
                city,temp,windDirection,windScale,time);
        return message;
    }
}
