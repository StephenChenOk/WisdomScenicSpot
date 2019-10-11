package com.chen.fy.wisdomscenicspot.utils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 网络数据传输工具类
 */
public class HttpUtils {

    /**
     * 使用OkHttp访问网络
     * 不能在此方法中直接开启子线程进行耗时操作
     * 因为此方法很快就进行结束,然后等到子线程中的耗时操作完成后无法在返回数据
     * 所以可以使用接口解决这个问题
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        //enqueue方法中已经自行开启了子线程
        client.newCall(request).enqueue(callback);
    }

    /**
     * 向服务器发送请求
     * application/x-www-form-urlencoded 数据是个普通表单  （默认）则不需要设置
     * multipart/form-data 数据里有文件
     * application/json 数据是个json
     */
    public static void postOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("test","123").build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //enqueue方法中已经自行开启了子线程
        client.newCall(request).enqueue(callback);
    }
    // HttpUtils.senOkHttpRequest("http://192.168.43.178/get_data.json", new okhttp3.Callback() {
//                        @Override
//                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//
//                            String responseData = response.body().string();
//                            parseJSONWithGSON(responseData);
//                            showResponse(responseData);
//
//                        }
//                    });
}
