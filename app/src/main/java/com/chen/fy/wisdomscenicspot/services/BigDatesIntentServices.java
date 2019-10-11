package com.chen.fy.wisdomscenicspot.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

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

    public BigDatesIntentServices() {
        super("BigDatesIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        Request request_cq = new Request.Builder().url(Consts.BIG_DATA_SERVER_URL_CQ).build();
        Request request_sh = new Request.Builder().url(Consts.BIG_DATA_SERVER_URL_SH).build();
        getBigDate(client, request_cq, 1);
        getBigDate(client, request_sh, 2);
    }

    private void getBigDate(OkHttpClient client, Request request, int code) {
        //真机访问电脑本机地址
        //发送请求并获取服务器返回的数据
        try {

            //  温度/湿度/大气压/能见度/降雨情况（0为不降雨1为降雨）
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                String responseData = response.body().string();
                switch (code) {
                    case 1:
                        Log.d("重庆：", "responseData:" + responseData);
                        saveDates(responseData, 1);
                        break;
                    case 2:
                        Log.d("上海：", "responseData:" + responseData);
                        saveDates(responseData, 2);
                        break;
                }
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
     * 今天                                              明日
     * 24/82/914/11584/1，24/82/914/11584/1，24/82/914/11584/1；24/82/914/11584/1，24/82/914/11584/1，24/82/914/11584/1
     */
    private void saveDates(String responseData, int code) {

        SharedPreferences.Editor editor = getSharedPreferences("BigDates", MODE_PRIVATE).edit();

        switch (code) {
            case 1:
                putCQ(responseData, editor);
                break;
            case 2:
                putSH(responseData, editor);
                break;
        }
        editor.apply();
    }

    private void putCQ(String responseData, SharedPreferences.Editor editor) {
        String[] dates = responseData.split(";");
        //今日 重庆
        String[] dates_today = dates[0].split(",");
        String[] dates_sq = dates_today[0].split("/");      //重庆市区
        String[] dates_dz = dates_today[1].split("/");      //大足区
        String[] dates_wl = dates_today[2].split("/");      //武隆区
        String[] dates_fj = dates_today[3].split("/");      //奉节区

        editor.putString("temperature_cq_sq", dates_sq[0]);
        editor.putString("humidity_cq_sq", dates_sq[1]);
        editor.putString("pressure_cq_sq", dates_sq[2]);
        editor.putString("visibility_cq_sq", dates_sq[3]);
        editor.putString("rainfall_cq_sq", dates_sq[4]);

        editor.putString("temperature_cq_dz", dates_dz[0]);
        editor.putString("humidity_cq_dz", dates_dz[1]);
        editor.putString("pressure_cq_dz", dates_dz[2]);
        editor.putString("visibility_cq_dz", dates_dz[3]);
        editor.putString("rainfall_cq_dz", dates_dz[4]);

        editor.putString("temperature_cq_wl", dates_wl[0]);
        editor.putString("humidity_cq_wl", dates_wl[1]);
        editor.putString("pressure_cq_wl", dates_wl[2]);
        editor.putString("visibility_cq_wl", dates_wl[3]);
        editor.putString("rainfall_cq_wl", dates_wl[4]);

        editor.putString("temperature_cq_fj", dates_fj[0]);
        editor.putString("humidity_cq_fj", dates_fj[1]);
        editor.putString("pressure_cq_fj", dates_fj[2]);
        editor.putString("visibility_cq_fj", dates_fj[3]);
        editor.putString("rainfall_cq_fj", dates_fj[4]);

        //明日 重庆
        String[] dates_tomorrow = dates[1].split(",");
        String[] dates_sq_t = dates_tomorrow[0].split("/");      //重庆市区
        String[] dates_dz_t = dates_tomorrow[1].split("/");      //大足区
        String[] dates_wl_t = dates_tomorrow[2].split("/");      //武隆区
        String[] dates_fj_t = dates_tomorrow[3].split("/");      //奉节区

        editor.putString("temperature_cq_sq_t", dates_sq_t[0]);
        editor.putString("humidity_cq_sq_t", dates_sq_t[1]);
        editor.putString("pressure_cq_sq_t", dates_sq_t[2]);
        editor.putString("visibility_cq_sq_t", dates_sq_t[3]);
        editor.putString("rainfall_cq_sq_t", dates_sq_t[4]);

        editor.putString("temperature_cq_dz_t", dates_dz_t[0]);
        editor.putString("humidity_cq_dz_t", dates_dz_t[1]);
        editor.putString("pressure_cq_dz_t", dates_dz_t[2]);
        editor.putString("visibility_cq_dz_t", dates_dz_t[3]);
        editor.putString("rainfall_cq_dz_t", dates_dz_t[4]);

        editor.putString("temperature_cq_wl_t", dates_wl_t[0]);
        editor.putString("humidity_cq_wl_t", dates_wl_t[1]);
        editor.putString("pressure_cq_wl_t", dates_wl_t[2]);
        editor.putString("visibility_cq_wl_t", dates_wl_t[3]);
        editor.putString("rainfall_cq_wl_t", dates_wl_t[4]);

        editor.putString("temperature_cq_fj_t", dates_fj_t[0]);
        editor.putString("humidity_cq_fj_t", dates_fj_t[1]);
        editor.putString("pressure_cq_fj_t", dates_fj_t[2]);
        editor.putString("visibility_cq_fj_t", dates_fj_t[3]);
        editor.putString("rainfall_cq_fj_t", dates_fj_t[4]);
    }

    private void putSH(String responseData, SharedPreferences.Editor editor) {
        String[] dates = responseData.split(";");
        //今日 重庆
        String[] dates_today = dates[0].split(",");
        String[] dates_sq = dates_today[0].split("/");      //上海市区
        String[] dates_dz = dates_today[1].split("/");      //浦东新区
        String[] dates_wl = dates_today[2].split("/");      //上海区
        String[] dates_fj = dates_today[3].split("/");      //青浦区

        editor.putString("temperature_sh_sq1", dates_sq[0]);
        editor.putString("humidity_sh_sq1", dates_sq[1]);
        editor.putString("pressure_sh_sq1", dates_sq[2]);
        editor.putString("visibility_sh_sq1", dates_sq[3]);
        editor.putString("rainfall_sh_sq1", dates_sq[4]);

        editor.putString("temperature_sh_pd", dates_dz[0]);
        editor.putString("humidity_sh_pd", dates_dz[1]);
        editor.putString("pressure_sh_pd", dates_dz[2]);
        editor.putString("visibility_sh_pd", dates_dz[3]);
        editor.putString("rainfall_sh_pd", dates_dz[4]);

        editor.putString("temperature_sh_sq2", dates_wl[0]);
        editor.putString("humidity_sh_sq2", dates_wl[1]);
        editor.putString("pressure_sh_sq2", dates_wl[2]);
        editor.putString("visibility_sh_sq2", dates_wl[3]);
        editor.putString("rainfall_sh_sq2", dates_wl[4]);

        editor.putString("temperature_sh_qp", dates_fj[0]);
        editor.putString("humidity_sh_qp", dates_fj[1]);
        editor.putString("pressure_sh_qp", dates_fj[2]);
        editor.putString("visibility_sh_qp", dates_fj[3]);
        editor.putString("rainfall_sh_qp", dates_fj[4]);

        //明日 上海
        String[] dates_tomorrow = dates[1].split(",");
        String[] dates_sq_t = dates_tomorrow[0].split("/");      //上海市区
        String[] dates_dz_t = dates_tomorrow[1].split("/");      //浦东新区
        String[] dates_wl_t = dates_tomorrow[2].split("/");      //上海市区
        String[] dates_fj_t = dates_tomorrow[3].split("/");      //青浦区

        editor.putString("temperature_sh_sq1_t", dates_sq_t[0]);
        editor.putString("humidity_sh_sq1_t", dates_sq_t[1]);
        editor.putString("pressure_sh_sq1_t", dates_sq_t[2]);
        editor.putString("visibility_sh_sq1_t", dates_sq_t[3]);
        editor.putString("rainfall_sh_sq1_t", dates_sq_t[4]);

        editor.putString("temperature_sh_pd_t", dates_dz_t[0]);
        editor.putString("humidity_sh_pd_t", dates_dz_t[1]);
        editor.putString("pressure_sh_pd_t", dates_dz_t[2]);
        editor.putString("visibility_sh_pd_t", dates_dz_t[3]);
        editor.putString("rainfall_sh_pd_t", dates_dz_t[4]);

        editor.putString("temperature_sh_sq2_t", dates_wl_t[0]);
        editor.putString("humidity_sh_sq2_t", dates_wl_t[1]);
        editor.putString("pressure_sh_sq2_t", dates_wl_t[2]);
        editor.putString("visibility_sh_sq2_t", dates_wl_t[3]);
        editor.putString("rainfall_sh_sq2_t", dates_wl_t[4]);

        editor.putString("temperature_sh_qp_t", dates_fj_t[0]);
        editor.putString("humidity_sh_qp_t", dates_fj_t[1]);
        editor.putString("pressure_sh_qp_t", dates_fj_t[2]);
        editor.putString("visibility_sh_qp_t", dates_fj_t[3]);
        editor.putString("rainfall_sh_qp_t", dates_fj_t[4]);
    }
}