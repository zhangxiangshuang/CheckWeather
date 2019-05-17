package com.jason.checkweather.view;

public class WeatherBean {

    public static final String Sunny = "晴";
    public static final String Cloudy ="多云";
    public static final String FewClouds = "少云";
    public static final String PartlyCloudy = "晴间多云";
    public static final String Overcast = "阴";
    public static final String Windy = "有风";
    public static final String Calm = "平静";
    public static final String LightBreeze = "微风";
    public static final String Moderate = "和风";
    public static final String FreshBreeze = "清风";
    public static final String StrongBreeze = "强风";
    public static final String HighWind = "疾风";
    public static final String Gale = "大风";
    public static final String StrongGale = "烈风";
    public static final String Storm = "风暴";
    public static final String ViolentStorm = "狂爆风";
    public static final String Hurricane = "飓风";
    public static final String Tornado = "龙卷风";
    public static final String TropicalStorm = "热带风暴";
    public static final String ShowerRain = "阵雨";
    public static final String HeavyShowerRain = "强阵雨";
    public static final String Thundershower = "雷阵雨";
    public static final String HeavyThunderstorm = "强雷阵雨";
    public static final String Thundershowerwithhail = "雷阵雨伴有冰雹";
    public static final String LightRain = "小雨";
    public static final String ModerateRain = "中雨";
    public static final String HeavyRain = "大雨";
    public static final String ExtremeRain = "极端降雨";
    public static final String DrizzleRain = "细雨";
    public static final String StormRain = "暴雨";
    public static final String HeavyStorm = "大暴雨";
    public static final String SevereStorm = "特大暴雨";
    public static final String FreezingRain = "冻雨";
    public static final String Lighttomoderaterain = "小到中雨";
    public static final String Moderatetoheavyrain = "中到大雨";
    public static final String Heavyraintostorm = "大到暴雨";
    public static final String Stormtoheavystorm = "暴雨到大暴雨";
    public static final String Heavytoseverestorm = "大暴雨到特大暴雨";
    public static final String Rain = "雨";
    public static final String LightSnow = "小雪";
    public static final String ModerateSnow = "中雪";
    public static final String HeavySnow = "大雪";
    public static final String Snowstorm = "暴雪";
    public static final String Sleet = "雨夹雪";
    public static final String RainAndSnow = "雨雪天气";
    public static final String ShowerSnow = "阵雨夹雪";
    public static final String SnowFlurry = "阵雪";
    public static final String Lighttomoderatesnow = "小到中雪";
    public static final String Moderatetoheavysnow = "中到大雪";
    public static final String Heavysnowtosnowstorm = "大到暴雪";
    public static final String Snow = "雪";
    public static final String Mist = "薄雾";
    public static final String Foggy = "雾";
    public static final String Haze = "霾";
    public static final String Sand = "扬沙";
    public static final String Dust = "浮尘";
    public static final String Duststorm = "沙尘暴";
    public static final String Sandstorm = "强沙尘暴";
    public static final String Densefog = "浓雾";
    public static final String Strongfog = "强浓雾";
    public static final String Moderatehaze = "中度霾";
    public static final String Heavyhaze = "重度霾";
    public static final String Severehaze = "严重霾";
    public static final String Heavyfog ="大雾";
    public static final String Extraheavyfog = "特强浓雾";
    public static final String Hot = "热";
    public static final String Cold = "冷";
    public static final String Unknown = "未知";





    public String weather;  //天气，取值为上面6种
    public int temperature; //温度值
    public String temperatureStr; //温度的描述值
    public String time; //时间值
    public String cond_code;

    public WeatherBean(String weather ,int temperature,String time) {
        this.weather = weather;
        this.cond_code=cond_code;
        this.temperature = temperature;
        this.time = time;
        this.temperatureStr = temperature + "°";
    }

    public WeatherBean(String weather, String cond_code ,int temperature, String temperatureStr, String time) {
        this.weather = weather;
        this.cond_code=cond_code;
        this.temperature = temperature;
        this.temperatureStr = temperatureStr;
        this.time = time;
    }

    public static String[] getAllWeathers() {
        String[] str = {
                Sunny, Cloudy, FewClouds, PartlyCloudy, Overcast, Windy,Calm ,LightBreeze,Moderate,
                FreshBreeze,StrongBreeze,HighWind,Gale,StrongGale,Storm,ViolentStorm,Hurricane,Tornado,
                TropicalStorm,ShowerRain,HeavyShowerRain,Thundershower,HeavyThunderstorm,Thundershowerwithhail,
                LightRain,ModerateRain,HeavyRain,ExtremeRain,DrizzleRain,StormRain,HeavyStorm,SevereStorm,
                FreezingRain,Lighttomoderaterain,Moderatetoheavyrain,Heavyraintostorm,Stormtoheavystorm,
                Heavytoseverestorm,Rain,LightSnow,ModerateSnow,HeavySnow,Snowstorm,Sleet,RainAndSnow,
                ShowerSnow,SnowFlurry,Lighttomoderatesnow,Moderatetoheavysnow,Heavysnowtosnowstorm,
                Snow,Mist,Foggy,Haze,Sand,Dust,Duststorm,Sandstorm,Densefog,Strongfog,Moderatehaze,
                Heavyhaze,Severehaze,Heavyfog,Extraheavyfog,Hot,Cold,Unknown,
        };
        return str;
    }
}
