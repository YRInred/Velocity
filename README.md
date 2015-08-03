# Velocity
A quick development framework with volley
Config
====
  First you should generate your own applicaition witch extended VelocityApplication
```JAVA
public class MyApplication extends VelocityApplication {
      ...
}
```
  Makesure your AndroidManifest's application name is your own application 
```XML
<application
        android:name=".application.MyApplication"
        ...>
</application>
```
How to use the framework?
====
  For Activity
```JAVA
public class MainActivity extends VelocityActivity //velocityActivity
        implements RequestListener {//requestListener
        
        ...
        
        @Override
    protected void initData() {
        requestEntitys = new ArrayList<>();
        requestEntitys.add(MyAppCenter.getInstance().api.GetWeatherApi());//add RequestItem in List

        setRequestListener(this);//addRequestListener
        super.initData();
    }
        
        ...
        
         @Override
    public void onResponse(int tagInt, String tag, Object entity) {//sucessResponse
        switch (tagInt) {
            case HttpApi.WEATHERTAGINT://tagInt
                weather = (Weather) entity;
                weatherText.setText(weather.toString());
                break;
        }
    }

    @Override
    public void onErrorResponse(int tagInt, String tag, VolleyError volleyError) {//failResponse

    }
}
```
  For Entity
```JAVA
public class Weather extends VelocityParser<Weather> {//Entity extend VelocityParser
  ...
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
    ...
  }
```
  For RequestItem
```JAVA
 public class HttpApi {
    public static final int WEATHERTAGINT = 0X1314;
    public RequestEntity GetWeatherApi(){
       return new RequestEntity()//Request item
                .setUrl("http://www.weather.com.cn/adat/sk/101010100.html")
                .setTagInt(WEATHERTAGINT)
                .setParser(new Weather());
    }
}
```
Which choose can config the Request Item?
====
```JAVA
new RequestEntity()
                .setUrl("")//url
                .setRequestMethod(Request.Method.POST)//default get
                .setParams(null)//params
                .setTagInt(0)//tagInt
                .setTag("");//tag
```


There have many function will be build in the framwork,just for android developer to development quickly~
Thank everyone;



