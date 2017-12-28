package com.demo.test.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lx on 2017/12/22.
 */
/*简单封装okhttpUtils
* */
public class HttpUtil {
    public  static void sendOkhttpRequest(String address , Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
