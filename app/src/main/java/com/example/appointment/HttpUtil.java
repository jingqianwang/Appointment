package com.example.appointment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by work on 2017/9/12.
 */

//网络请求封装类

public class HttpUtil {

    //发送网络请求
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    //判断网络是否链接
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                //当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    //当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
