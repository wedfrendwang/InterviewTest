package com.wedfrend.interviewtest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
* author:wedfrend
* email:wedfrend@yeah.net
* create:2018/3/23 13:12
* desc: YesterdayBean
*/
public class YesterdayBean implements Parcelable{

    /**
     * date : 21日星期三
     * high : 高温 22℃
     * fx : 东风
     * low : 低温 12℃
     * fl : <![CDATA[3-4级]]>
     * type : 晴
     */

    private String date;
    private String high;
    private String fx;
    private String low;
    private String fl;
    private String type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.high);
        dest.writeString(this.fx);
        dest.writeString(this.low);
        dest.writeString(this.fl);
        dest.writeString(this.type);
    }



    public YesterdayBean() {
    }

    protected YesterdayBean(Parcel in) {
        this.date = in.readString();
        this.high = in.readString();
        this.fx = in.readString();
        this.low = in.readString();
        this.fl = in.readString();
        this.type = in.readString();
    }

    public static final Creator<YesterdayBean> CREATOR = new Creator<YesterdayBean>() {
        @Override
        public YesterdayBean createFromParcel(Parcel source) {
            return new YesterdayBean(source);
        }

        @Override
        public YesterdayBean[] newArray(int size) {
            return new YesterdayBean[size];
        }
    };
}
