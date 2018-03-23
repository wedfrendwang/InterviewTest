// weatherInterface.aidl
package com.wedfrend.interviewtest;

import com.wedfrend.interviewtest.CallBackInterface;
/**
* Service IBind 实现接口，实现Activity调用
*/
interface weatherInterface {

    //get 请求网址
    void GetServiceDate(String url);
    //注册接口，服务端进行回掉
    void registerListener(in CallBackInterface listener);
    //注销接口
    void unRegisterListener(in CallBackInterface listener);

}
