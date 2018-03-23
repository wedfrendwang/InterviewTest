package com.wedfrend.interviewtest.pojo;

import android.os.Parcel;
import android.os.Parcelable;


/**
* author:wedfrend
* email:wedfrend@yeah.net
* create:2018/3/23 12:58
* desc: 封装weather类并且使用实现序列化方法
*/

public class weatherPojo implements Parcelable {

    /**
     * code : 200
     * msg : 成功!
     * data : {"yesterday":{"date":"21日星期三","high":"高温 22℃","fx":"东风","low":"低温 12℃","fl":"<![CDATA[3-4级]]>","type":"晴"},"city":"厦门","aqi":"50","forecast":[{"date":"22日星期四","high":"高温 21℃","fengli":"<![CDATA[4-5级]]>","low":"低温 11℃","fengxiang":"东风","type":"晴"},{"date":"23日星期五","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 12℃","fengxiang":"东风","type":"晴"},{"date":"24日星期六","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 14℃","fengxiang":"东风","type":"多云"},{"date":"25日星期天","high":"高温 24℃","fengli":"<![CDATA[3-4级]]>","low":"低温 16℃","fengxiang":"东风","type":"多云"},{"date":"26日星期一","high":"高温 24℃","fengli":"<![CDATA[<3级]]>","low":"低温 15℃","fengxiang":"东风","type":"多云"}],"ganmao":"昼夜温差大，风力较强，易发生感冒，请注意适当增减衣服，加强自我防护避免感冒。","wendu":"18"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeParcelable(this.data, flags);
    }

    public weatherPojo() {
    }

    protected weatherPojo(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Creator<weatherPojo> CREATOR = new Creator<weatherPojo>() {
        @Override
        public weatherPojo createFromParcel(Parcel source) {
            return new weatherPojo(source);
        }

        @Override
        public weatherPojo[] newArray(int size) {
            return new weatherPojo[size];
        }
    };
}
