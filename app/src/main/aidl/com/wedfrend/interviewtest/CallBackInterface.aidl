// CallBackInterface.aidl
package com.wedfrend.interviewtest;

import com.wedfrend.interviewtest.pojo.weatherPojo;

//不同进程，service 返回数据供 Activity 进行UI改变或者其他操作
interface CallBackInterface {

    /*数据出现异常*/
    void dateException(String Tag,String throwReason);

    /*成功返回数据至客户端进行UI数据显示*/
    void dateSuccess(in weatherPojo pojo);


}
