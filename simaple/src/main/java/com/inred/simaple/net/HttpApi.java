package com.inred.simaple.net;

import com.inred.library.entity.RequestEntity;
import com.inred.simaple.entity.Weather;

/**
 * Created by inred on 2015/8/3.
 */
public class HttpApi {

    public static final int WEATHERTAGINT = 0X1314;

    public RequestEntity GetWeatherApi(){
       return new RequestEntity()//Request item
                .setUrl("http://www.weather.com.cn/adat/sk/101010100.html")
                .setTagInt(WEATHERTAGINT)
                .setParser(new Weather());
    }

}
