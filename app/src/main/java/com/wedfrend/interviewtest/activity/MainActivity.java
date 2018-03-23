package com.wedfrend.interviewtest.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wedfrend.interviewtest.CallBackInterface;
import com.wedfrend.interviewtest.R;
import com.wedfrend.interviewtest.pojo.weatherPojo;
import com.wedfrend.interviewtest.service.WeatherService;
import com.wedfrend.interviewtest.weatherInterface;

import java.text.SimpleDateFormat;

/**
* author:wedfrend（应试：王晓波）
* email:wedfrend@yeah.net
* create:2018/3/23 14:30
* desc: 完成测试题目：1：天气的获取并显示数据
 *             要求：1：程序中两个进程   activity process  & service process
 *                  2: AIDL 进行接口的调用，回掉
 *                  3：service中开启线程进行网路耗时操作
 *                  4：如有异常，显示至客户端UI界面，方便测试
 */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    //请求网络权限判断值
    private static final int PHONEPERMISSION = 0;

    /*回到接口*/
    private weatherInterface weatherInter = null;

    private  ServiceConnection connect = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(weatherInter == null) {
                weatherInter = weatherInterface.Stub.asInterface(service);
                try {
                    weatherInter.registerListener(callBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(weatherInter != null){
                try {
                    weatherInter.unRegisterListener(callBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                //在销毁接口之后
                weatherInter = null;
            }
        }
    };

    /*请求网址*/
    private String RequestUrl = "https://www.apiopen.top/weatherApi?city=%E5%8E%A6%E9%97%A8";

    private TextView tvGetWeather,tvHint,tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewId();
        BindService();
        setOnclick();
    }



    /*实例控件*/
    private void findViewId(){
        tvGetWeather = ((TextView) findViewById(R.id.tvGetWeather));
        tvHint = ((TextView) findViewById(R.id.tvHint));
        tvInfo = ((TextView) findViewById(R.id.tvInfo));
    }
    /*绑定服务*/
    private void BindService(){
        Intent intent = new Intent(this,WeatherService.class);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            intent.setComponent(new ComponentName(getPackageName(),WeatherService.class.getName()));
        }
        //绑定的时候服务端自动创建
        bindService(intent,connect, BIND_AUTO_CREATE);
    }

    /*声明点击监听*/
    private void setOnclick() {
        tvGetWeather.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvGetWeather){
            //1：请求网络权限
            requestNeedPermission();
            //2：请求数据,权限判断完成之后进行 requestDate()
        }
    }


    /*调用service接口，请求数据*/
    private void requestDate(){

        try {
            weatherInter.GetServiceDate(RequestUrl);
        } catch (RemoteException e) {
            e.printStackTrace();
            tvHint.setText(TAG+"\n equestDate()出错");
        }

    }

    private void  requestNeedPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            //获取运行权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                //该判断：在6.0之后的手机中，会设置一项为不在进行提示的选择，所以当用户选择这个功能，但是应用必须使用该功能才能进行操作
                showSystemDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PHONEPERMISSION);
            }
        } else {
            requestDate();
        }
    }

    /**
     * 创建系统提示，使用AlterDialog
     */
    private void showSystemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.AlterDialogHint)
                .setMessage(R.string.AlterMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.openPermission, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.refusePermission, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PHONEPERMISSION:
                for (int grant:grantResults
                        ) {
                    if(grant == PackageManager.PERMISSION_GRANTED){
                        //表示同意权限
                        requestDate();
                    }else{
                        //提示原因
                        Snackbar.make(tvInfo,R.string.permissionHint,Snackbar.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
    
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tvInfo.setVisibility(View.VISIBLE);
                    tvHint.setVisibility(View.GONE);
                    tvInfo.setText(TAG+"\n"+(String)msg.obj);
                    break;
                case 2:
                    tvInfo.setVisibility(View.VISIBLE);
                    tvHint.setVisibility(View.GONE);
                    tvInfo.setText(TAG+"\n"+(String)msg.obj);
                    break;
            }

        }
    };
    



    private ConnectionCallBack callBack = new ConnectionCallBack();

    public class ConnectionCallBack extends CallBackInterface.Stub{
        @Override
        public void dateException(String Tag, String throwReason) throws RemoteException {
            if(tvInfo.getVisibility() == View.GONE){
                tvInfo.setVisibility(View.VISIBLE);
                tvHint.setVisibility(View.GONE);
            }
            tvInfo.setText(Tag+"\n"+throwReason);
        }

        @Override
        public void dateSuccess(weatherPojo pojo) throws RemoteException {

            //回掉方法显示相应天气数据
            if(pojo.getCode() != 200){//失败
                Message message = new Message();
                message.what = 1;
                message.obj = pojo.getMsg();
                handler.sendMessage(message);
                return;
            }
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("CITY:").append( pojo.getData().getCity()).append("\n");
            strBuffer.append("CityId:").append( pojo.getData().getAqi()).append("\n");
            strBuffer.append("Temp:").append( pojo.getData().getYesterday().getHigh()).append("\n");
            strBuffer.append("WD").append( pojo.getData().getYesterday().getFx()).append("\n");
            strBuffer.append("WS").append( pojo.getData().getYesterday().getFl()).append("\n");
            strBuffer.append("SD").append( pojo.getData().getYesterday().getHigh()).append("\n");
            strBuffer.append("WES").append( pojo.getData().getYesterday().getDate()).append("\n");
            strBuffer.append("Time").append(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis())).append("\n");
            Message message = new Message();
            message.what = 2;
            message.obj = strBuffer.toString();
            handler.sendMessage(message);


        }
    }

}
