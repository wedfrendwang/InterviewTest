package com.wedfrend.interviewtest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by welive on 2018/3/23.
 */

public class DataBean implements Parcelable{

    /**
     * yesterday : {"date":"21日星期三","high":"高温 22℃","fx":"东风","low":"低温 12℃","fl":"<![CDATA[3-4级]]>","type":"晴"}
     * city : 厦门
     * aqi : 50
     * forecast : [{"date":"22日星期四","high":"高温 21℃","fengli":"<![CDATA[4-5级]]>","low":"低温 11℃","fengxiang":"东风","type":"晴"},{"date":"23日星期五","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 12℃","fengxiang":"东风","type":"晴"},{"date":"24日星期六","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 14℃","fengxiang":"东风","type":"多云"},{"date":"25日星期天","high":"高温 24℃","fengli":"<![CDATA[3-4级]]>","low":"低温 16℃","fengxiang":"东风","type":"多云"},{"date":"26日星期一","high":"高温 24℃","fengli":"<![CDATA[<3级]]>","low":"低温 15℃","fengxiang":"东风","type":"多云"}]
     * ganmao : 昼夜温差大，风力较强，易发生感冒，请注意适当增减衣服，加强自我防护避免感冒。
     * wendu : 18
     */

    private YesterdayBean yesterday;
    private String city;
    private String aqi;
    private String ganmao;
    private String wendu;
    private List<ForeCastBean> forecast;

    public YesterdayBean getYesterday() {
        return yesterday;
    }

    public void setYesterday(YesterdayBean yesterday) {
        this.yesterday = yesterday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public List<ForeCastBean> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForeCastBean> forecast) {
        this.forecast = forecast;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.yesterday, flags);
        dest.writeString(this.city);
        dest.writeString(this.aqi);
        dest.writeString(this.ganmao);
        dest.writeString(this.wendu);
        dest.writeTypedList(this.forecast);
    }

    public DataBean() {
    }

    protected DataBean(Parcel in) {
        this.yesterday = in.readParcelable(YesterdayBean.class.getClassLoader());
        this.city = in.readString();
        this.aqi = in.readString();
        this.ganmao = in.readString();
        this.wendu = in.readString();
        this.forecast = in.createTypedArrayList(ForeCastBean.CREATOR);
    }

    public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel source) {
            return new DataBean(source);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };
}
