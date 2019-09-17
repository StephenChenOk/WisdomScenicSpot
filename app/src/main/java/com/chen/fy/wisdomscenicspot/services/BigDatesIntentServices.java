package com.chen.fy.wisdomscenicspot.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chen.fy.wisdomscenicspot.activities.ViewPointActivity;
import com.chen.fy.wisdomscenicspot.consts.Consts;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 启动app后在后台直接向服务器请求大数据
 */
public class BigDatesIntentServices extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BigDatesIntentServices(String name) {
        super(name);
    }

    public BigDatesIntentServices(){
        super("BigDatesIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30000, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30000, TimeUnit.SECONDS)
                .build();
        //真机访问电脑本机地址
        Request request = new Request.Builder().url(Consts.BIG_DATA_SERVER_URL).build();
        //发送请求并获取服务器返回的数据
        try {

            //  温度/湿度/大气压/能见度/降雨情况（0为不降雨1为降雨）
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                String responseData = response.body().string();
                saveDates(responseData);
                Log.d("chenyisheng", "responseData:" + responseData);
            } else {
                Log.d("chenyisheng", "请求失败！");
            }
        } catch (Exception e) {
            Log.d("chenyisheng", "连接失败!!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("chenyisheng", "IntentService onDestroy!!");
    }

    /**
     * 保存从服务器返回的天气数据
     * 温度/湿度/大气压/能见度/降雨情况（0为不降雨1为降雨）
     * 24/82/914/11584/1
     */
    private void saveDates(String responseData) {

        String[] dates = responseData.split("/");
        SharedPreferences.Editor editor = getSharedPreferences("BigDates", MODE_PRIVATE).edit();
        editor.putString("temperature", dates[0]);
        editor.putString("humidity", dates[1]);
        editor.putString("pressure", dates[2]);
        editor.putString("visibility", dates[3]);
        editor.putString("rainfall", dates[4]);
        editor.apply();
    }
}
