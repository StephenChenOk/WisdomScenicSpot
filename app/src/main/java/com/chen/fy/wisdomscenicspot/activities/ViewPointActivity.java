package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.adapter.ItemClickListener;
import com.chen.fy.wisdomscenicspot.adapter.ViewPointAdapter;
import com.chen.fy.wisdomscenicspot.beans.ViewPointInfo;
import com.chen.fy.wisdomscenicspot.comparators.WeatherComparator;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 景点推荐活动
 */
public class ViewPointActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    private List<ViewPointInfo> list;
    private Toolbar toolbar;

    private WeatherComparator weatherComparator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this, true);
        setContentView(R.layout.view_point_layout);

        initView();
        initData();

        //2 RecyclerView设置
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);//1 表示列数
        recyclerView.setLayoutManager(layoutManager);
        ViewPointAdapter viewPointAdapter = new ViewPointAdapter(list);
        viewPointAdapter.setItemClickLister(this);
        recyclerView.setAdapter(viewPointAdapter);

        getDates();
    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_viewpoint);
        toolbar = findViewById(R.id.toolbar_view_point);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        if (getIntent() != null) {
            switch (getIntent().getStringExtra("目的地")) {
                case "重庆":
                    toolbar.setTitle("重庆");
                    initCQ();
                    break;
                case "上海":
                    toolbar.setTitle("上海");
                    initSH();
                    break;
                case "北京":
                    toolbar.setTitle("北京");
                    break;
            }
        }
    }

    /**
     * 得到重庆的景点信息
     */
    private void initCQ() {
        //2 初始化RecyclerView显示的数据
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!list.isEmpty()) {
            list.clear();
        }
        ViewPointInfo viewPointInfo1 = new ViewPointInfo();
        viewPointInfo1.setName("磁器口古镇");
        viewPointInfo1.setAddress("重庆市沙坪坝区磁器口古镇");
        viewPointInfo1.setScore(4.3);
        viewPointInfo1.setDistance(285.15);

        ViewPointInfo viewPointInfo2 = new ViewPointInfo();
        viewPointInfo2.setName("解放碑步行街");
        viewPointInfo2.setAddress("重庆市渝中区解放碑周边区域");
        viewPointInfo2.setScore(4.5);
        viewPointInfo2.setDistance(219.15);

        ViewPointInfo viewPointInfo3 = new ViewPointInfo();
        viewPointInfo3.setName("武隆天生三桥");
        viewPointInfo3.setAddress("重庆市武隆区仙女山镇游客接待中心");
        viewPointInfo3.setScore(4.6);
        viewPointInfo3.setDistance(200.13);

        ViewPointInfo viewPointInfo4 = new ViewPointInfo();
        viewPointInfo4.setName("大足石刻");
        viewPointInfo4.setAddress("重庆市大足区宝顶镇大足石刻风景区");
        viewPointInfo4.setScore(4.6);
        viewPointInfo4.setDistance(237.17);

        ViewPointInfo viewPointInfo5 = new ViewPointInfo();
        viewPointInfo5.setName("白公馆");
        viewPointInfo5.setAddress("沙坪坝区壮志路治法三村63号");
        viewPointInfo5.setScore(4.3);
        viewPointInfo5.setDistance(138.15);

        ViewPointInfo viewPointInfo6 = new ViewPointInfo();
        viewPointInfo6.setName("长江索道");
        viewPointInfo6.setAddress("重庆市渝中区新华路151号");
        viewPointInfo6.setScore(4.2);
        viewPointInfo6.setDistance(257.15);

        ViewPointInfo viewPointInfo7 = new ViewPointInfo();
        viewPointInfo7.setName("南山风景区");
        viewPointInfo7.setAddress("重庆市南岸区南山镇南山公园附近");
        viewPointInfo7.setScore(4.4);
        viewPointInfo7.setDistance(240.15);

        ViewPointInfo viewPointInfo8 = new ViewPointInfo();
        viewPointInfo8.setName("白帝城景区");
        viewPointInfo8.setAddress("奉节县夔门街道办事处瞿塘峡社区白帝城景区");
        viewPointInfo8.setScore(4.5);
        viewPointInfo8.setDistance(320.15);

        list.add(viewPointInfo1);
        list.add(viewPointInfo2);
        list.add(viewPointInfo3);
        list.add(viewPointInfo4);
        list.add(viewPointInfo5);
        list.add(viewPointInfo6);
        list.add(viewPointInfo7);
        list.add(viewPointInfo8);

        weatherComparator = new WeatherComparator();
        Collections.sort(list, weatherComparator);
    }

    /**
     * 得到上海的景点信息
     */
    private void initSH(){
        //2 初始化RecyclerView显示的数据
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!list.isEmpty()) {
            list.clear();
        }
        ViewPointInfo viewPointInfo1 = new ViewPointInfo();
        viewPointInfo1.setName("外滩");
        viewPointInfo1.setAddress("上海市黄浦区中山东一路（临黄浦江）");
        viewPointInfo1.setScore(4.7);
        viewPointInfo1.setDistance(385.15);

        ViewPointInfo viewPointInfo2 = new ViewPointInfo();
        viewPointInfo2.setName("上海迪士尼度假区");
        viewPointInfo2.setAddress("上海市浦东新区川沙新镇上海迪士尼度假区");
        viewPointInfo2.setScore(4.6);
        viewPointInfo2.setDistance(219.15);

        ViewPointInfo viewPointInfo3 = new ViewPointInfo();
        viewPointInfo3.setName("南京路步行街");
        viewPointInfo3.setAddress("上海市黄浦区河南中路");
        viewPointInfo3.setScore(4.6);
        viewPointInfo3.setDistance(320.13);

        ViewPointInfo viewPointInfo4 = new ViewPointInfo();
        viewPointInfo4.setName("上海长风海洋世界");
        viewPointInfo4.setAddress("普陀区大渡河路451号（长风公园4号门）");
        viewPointInfo4.setScore(4.4);
        viewPointInfo4.setDistance(337.17);


        list.add(viewPointInfo1);
        list.add(viewPointInfo2);
        list.add(viewPointInfo3);
        list.add(viewPointInfo4);

        weatherComparator = new WeatherComparator();
        Collections.sort(list, weatherComparator);

    }

    /**
     * 获取天气数据
     */
    private void getDates() {
        SharedPreferences preferences = getSharedPreferences("BigDates", MODE_PRIVATE);
        Log.d("BigDates",preferences.getString("temperature", ""));
        Log.d("BigDates",preferences.getString("humidity", ""));
        Log.d("BigDates",preferences.getString("pressure", ""));
        Log.d("BigDates",preferences.getString("visibility", ""));
        Log.d("BigDates",preferences.getString("rainfall", ""));
    }

    /**
     * 计算景点推荐权值
     *
     * @param score       景点评分
     * @param rainfull    是否降雨
     * @param temperature 温度
     * @param humidity    湿度
     * @param visibility  可见度
     */
    private double getViewPointWeight(double score, int rainfull, double temperature, double humidity, double visibility) {

        score = score * 100;
        rainfull = -rainfull * 200;
        temperature = -temperature * 50;
        humidity = -humidity * 100;
        visibility = visibility / 200;

        return score + rainfull + temperature + humidity + visibility;
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onLongClick(View view, int position) {
    }

    @Override
    public void onItemClick(int i) {
        switch (list.get(i).getName()) {
            //重庆
            case "磁器口古镇":
                toDestination(29.578936, 106.452215);
                break;
            case "解放碑步行街":
                toDestination(29.557564, 106.577233);
                break;
            case "武隆天生三桥":
                toDestination(29.429943, 107.803549);
                break;
            case "大足石刻":
                toDestination(29.74814, 105.795545);
                break;
            case "白公馆":
                toDestination(29.576473, 106.432065);
                break;
            case "长江索道":
                toDestination(29.556249, 106.586634);
                break;
            case "南山风景区":
                toDestination(29.556989, 106.623053);
                break;
            case "白帝城景区":
                toDestination(31.045283, 109.571948);
                break;

                //上海
            case "外滩":
                toDestination(31.233462, 121.492156);
                break;
            case "上海迪士尼度假区":
                toDestination(31.141269, 121.661699);
                break;
            case "南京路步行街":
                toDestination(31.235986, 121.479503);
                break;
            case "上海长风海洋世界":
                toDestination(31.225079, 121.397016);
                break;
        }
    }

    private void toDestination(double latitude, double longitude) {
        Intent intent;
        intent = new Intent(this, MapActivity.class);
        intent.putExtra("Latitude", latitude);
        intent.putExtra("Longitude", longitude);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int i) {
    }
}
