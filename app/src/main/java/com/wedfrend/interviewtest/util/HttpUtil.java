package com.wedfrend.interviewtest.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
* author:wedfrend
* email:wedfrend@yeah.net
* create:2018/3/23 10:47
* desc: android 原生系统下网络请求方式
*/

public class HttpUtil {

    /**
     * 进行Get请求
     *
     * 如果返回json数据，请进行String(byte[],charset)操作
     *
     * @param GetUrl
     * @return
     */
    public static byte[] GetRequest(String GetUrl){
        //建立链接器
        HttpURLConnection httpURLConnection = null;
        // 获取后台传出数据
        ByteArrayOutputStream out = null;
        // 网络返回的数据
        InputStream inputStream = null;
        //单位数据
        byte[] buffer = new byte[1024];
        int length = 0;

        try {
            URL url = new URL(GetUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            //获取相应码
            if(httpURLConnection.getResponseCode()==200){
                out = new ByteArrayOutputStream();
                inputStream = httpURLConnection.getInputStream();
                //也可使用BufferInPutStream
                while ((length=inputStream.read(buffer))!=-1){
                    out.write(buffer,0,length);
                }
                return out.toByteArray();
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            //关闭链接,这里一定不要进行组合关闭
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭流
            if(out!= null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * POST 请求
     * @param hostUrl   http://xxxxxx.xxx.xxx
     * @param postParams   key1=value1&key2=value2
     * @return
     */
    public static byte[] PostRequest(String hostUrl,String postParams){
        //建立链接器
        HttpURLConnection httpURLConnection = null;
        // 获取后台传出数据
        ByteArrayOutputStream out = null;
        // 网络返回的数据
        InputStream inputStream = null;
        //单位数据
        byte[] buffer = new byte[1024];
        int length = 0;

        try {
            URL url = new URL(hostUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);//不使用缓存
            httpURLConnection.setDoInput(true);//输入
            httpURLConnection.setDoOutput(true);//输出
            //将数据传入
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(URLEncoder.encode(postParams,"utf-8").getBytes());
            outputStream.flush();
            //获取相应码
            if(httpURLConnection.getResponseCode()==200){
                inputStream = httpURLConnection.getInputStream();
                //也可使用BufferInPutStream
                while ((length=inputStream.read(buffer))!=-1){
                    out.write(buffer,0,length);
                }
                return out.toByteArray();
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            //关闭链接,这里一定不要进行组合关闭
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭流
            if(out!= null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
