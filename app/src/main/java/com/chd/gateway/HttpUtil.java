package com.chd.gateway;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/3/17.
 */

public class HttpUtil {
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String httpResult = ((String) msg.obj);
            iHttpUtil.onSuccess( msg.what, httpResult );
        }
    };
    protected IHttpUtil iHttpUtil;

    public interface IHttpUtil{
        void onSuccess( int type, String httpResult );
    }

    public void setHttpUtilInterface( IHttpUtil a ){ iHttpUtil = a; }

    //拼接Http请求URL
    private String getUrl( String ctrl ){
        String urlString = "http://192.168.1.143/api/gw/dogw/?did=" + SharedPreferencesUtil.getUserUid() +
                "&key=" + SharedPreferencesUtil.getUserKey();
        if ( ctrl != null && !ctrl.equals("") ){
            urlString += "&ctrl=" + ctrl + "&t=2";
        }
        return urlString;
    }

    public void getHttpData(final int type, final String ctrl ) {
        final Runnable runnable = new Runnable() {
            public void run() {
                startThread( type, ctrl );
                if ( type == Common.HTTP_GET_ENDDEVICE_DATA ){
                    Log.i("ycc", "ycc:Thread===run99999==" + type + "###" + ctrl);
                    handler.postDelayed(this, 3500);
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void startThread( final int type, final String ctrl ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = getUrl( ctrl );
                String result = requestData( urlString );
                Log.i("ycc", "ycc:Thread===run99999==change==" + urlString + "###" + result);
                Message msg = Message.obtain();
                msg.what = type;
                msg.obj = result;
                handler.sendMessageDelayed(msg, 0);
            }
        }).start();
    }

    //耗时操作，必须放在线程中执行
    private String requestData( String urlString ){
        String result = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(10000);// 设置超时的时间
            urlConnection.setConnectTimeout(10000);// 设置链接超时的时间

            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    os.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                os.close();
                // 返回字符串
                result = new String(os.toByteArray());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
