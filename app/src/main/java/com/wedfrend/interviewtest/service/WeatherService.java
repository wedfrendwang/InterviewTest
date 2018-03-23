package com.wedfrend.interviewtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.wedfrend.interviewtest.CallBackInterface;
import com.wedfrend.interviewtest.pojo.DataBean;
import com.wedfrend.interviewtest.pojo.ForeCastBean;
import com.wedfrend.interviewtest.pojo.YesterdayBean;
import com.wedfrend.interviewtest.pojo.weatherPojo;
import com.wedfrend.interviewtest.util.HttpUtil;
import com.wedfrend.interviewtest.weatherInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
* author:wedfrend
* email:wedfrend@yeah.net
* create:2018/3/23 11:23
* desc: 服务进行网络操作
*/
public class WeatherService extends Service {
    private static final String TAG = "WeatherService";

    CallBackInterface callBackInterface;

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: WeatherService have been bound" );
        return new WeatherBinder();
    }


    public class WeatherBinder extends weatherInterface.Stub{

        @Override
        public void GetServiceDate(String url) throws RemoteException {
            //异步请求网络数据：获取厦门市天气API
            getWeatherJson(url);
        }

        /**
         * 注册客户端回掉接口
         * @param listener
         * @throws RemoteException
         */
        @Override
        public void registerListener(CallBackInterface listener) throws RemoteException {
            if(callBackInterface == null){
                callBackInterface = (listener);
            }
        }

        /**
         * 注销客户端回掉接口
         * @param listener
         * @throws RemoteException
         */
        @Override
        public void unRegisterListener(CallBackInterface listener) throws RemoteException {
            if(callBackInterface!=null){
                callBackInterface = null;
            }
        }
    }

    /*
    开启子线程请求网络数据，获取天气
     */
    private void getWeatherJson(final String getUrl){

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = HttpUtil.GetRequest(getUrl);
                if(bytes == null){
                    //告诉前端界面，请求数据失败
                    dateException(Net_Exception);
                }else{
                    //解析后台返回json数据
                    String jsonDate = null;
                    try {
                        jsonDate = new String(bytes,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        dateException(Byte_Encoding_Exception);
                    }
                    //解析封装数据
                    try {
                        jxJsonDate(jsonDate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dateException(Json_Encoding_Exception);
                    }
                }
            }
        }).start();

    }

    private final int Net_Exception=1;
    private final int Byte_Encoding_Exception=2;
    private final int Json_Encoding_Exception=3;
    private void dateException(int ExceptionReason){
        switch (ExceptionReason){
            case Net_Exception:
                try {
                    callBackInterface.dateException(TAG,"网络访问异常");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case Byte_Encoding_Exception:
                try {
                    callBackInterface.dateException(TAG,"byte数据转化异常");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case Json_Encoding_Exception:
                try {
                    callBackInterface.dateException(TAG,"json数据解析异常");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    /**
     * @param date
     * @throws JSONException
     */
    private weatherPojo weatherpojo;
    private void jxJsonDate(String date) throws JSONException {
        Log.i(TAG, "jxJsonDate: "+date);
        JSONObject jsonObject = new JSONObject(date);
        if(weatherpojo == null){
            weatherpojo = new weatherPojo();
        }
        //解析数据,不经常使用这种方法，一般会使用GSON来进行数据解析
        //但是因为涉及Parcelable，且是面试项目，所以使用最基础的方法
        weatherpojo.setCode(jsonObject.getInt("code"));
        weatherpojo.setMsg(jsonObject.getString("msg"));
        YesterdayBean yesterday = new YesterdayBean();
        yesterday.setFl(jsonObject.getJSONObject("data").getJSONObject("yesterday").getString("fl"));
        yesterday.setDate(jsonObject.getJSONObject("data").getJSONObject("yesterday").getString("date"));
        yesterday.setFx(jsonObject.getJSONObject("data").getJSONObject("yesterday").getString("fx"));
        yesterday.setHigh(jsonObject.getJSONObject("data").getJSONObject("yesterday").getString("high"));
        yesterday.setLow(jsonObject.getJSONObject("data").getJSONObject("yesterday").getString("low"));
        yesterday.setType(jsonObject.getJSONObject("data").getJSONObject("yesterday").getString("type"));
        ArrayList<ForeCastBean> listForeCast = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("forecast");
        for (int i=0;i<jsonArray.length();i++){
            ForeCastBean foreCastBean = new ForeCastBean();
            foreCastBean.setType(jsonArray.getJSONObject(i).getString("type"));
            foreCastBean.setDate(jsonArray.getJSONObject(i).getString("date"));
            foreCastBean.setLow(jsonArray.getJSONObject(i).getString("low"));
            foreCastBean.setFengli(jsonArray.getJSONObject(i).getString("fengli"));
            foreCastBean.setHigh(jsonArray.getJSONObject(i).getString("high"));
            foreCastBean.setFengxiang(jsonArray.getJSONObject(i).getString("fengxiang"));
            listForeCast.add(foreCastBean);
        }
        DataBean databean = new DataBean();
        databean.setAqi(jsonObject.getJSONObject("data").getString("aqi"));
        databean.setCity(jsonObject.getJSONObject("data").getString("city"));
        databean.setGanmao(jsonObject.getJSONObject("data").getString("ganmao"));
        databean.setWendu(jsonObject.getJSONObject("data").getString("wendu"));
        databean.setForecast(listForeCast);
        databean.setYesterday(yesterday);
        weatherpojo.setData(databean);
        try {
            callBackInterface.dateSuccess(weatherpojo);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG,"callBackInterface.dateSuccess(weatherpojo) CallBack Exception");
        }
    }


}
