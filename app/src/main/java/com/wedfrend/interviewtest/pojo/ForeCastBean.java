package com.wedfrend.interviewtest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
* author:wedfrend
* email:wedfrend@yeah.net
* create:2018/3/23 13:12
* desc: ForeCastBean
*/

public class ForeCastBean implements Parcelable{

    /**
     * date : 22日星期四
     * high : 高温 21℃
     * fengli : <![CDATA[4-5级]]>
     * low : 低温 11℃
     * fengxiang : 东风
     * type : 晴
     */

    private String date;
    private String high;
    private String fengli;
    private String low;
    private String fengxiang;
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

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
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
        dest.writeString(this.fengli);
        dest.writeString(this.low);
        dest.writeString(this.fengxiang);
        dest.writeString(this.type);
    }

    public ForeCastBean() {
    }

    protected ForeCastBean(Parcel in) {
        this.date = in.readString();
        this.high = in.readString();
        this.fengli = in.readString();
        this.low = in.readString();
        this.fengxiang = in.readString();
        this.type = in.readString();
    }

    public static final Creator<ForeCastBean> CREATOR = new Creator<ForeCastBean>() {
        @Override
        public ForeCastBean createFromParcel(Parcel source) {
            return new ForeCastBean(source);
        }

        @Override
        public ForeCastBean[] newArray(int size) {
            return new ForeCastBean[size];
        }
    };
}
