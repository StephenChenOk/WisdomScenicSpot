package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.adapter.ItemClickListener;
import com.chen.fy.wisdomscenicspot.adapter.ViewPointAdapter;
import com.chen.fy.wisdomscenicspot.model.AreaWeatherInfo;
import com.chen.fy.wisdomscenicspot.model.ViewPointInfo;
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
    private List<AreaWeatherInfo> weatherInfos;
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
                    getDates_CQ();

                    break;
                case "上海":
                    toolbar.setTitle("上海");
                    initSH();
                    getDates_SH();
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
        viewPointInfo1.setScore(4.6);
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
    }

    /**
     * 得到上海的景点信息
     */
    private void initSH() {
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

        ViewPointInfo viewPointInfo5 = new ViewPointInfo();
        viewPointInfo5.setName("朱家角古镇景区");
        viewPointInfo5.setAddress("上海市青浦区朱家角镇");
        viewPointInfo5.setScore(4.4);
        viewPointInfo5.setDistance(387.17);

        list.add(viewPointInfo1);
        list.add(viewPointInfo2);
        list.add(viewPointInfo3);
        list.add(viewPointInfo4);
        list.add(viewPointInfo5);


    }

    /**
     * 获取天气数据
     */
    private void getDates_CQ() {
        SharedPreferences preferences = getSharedPreferences("BigDates", MODE_PRIVATE);

        if(weatherInfos == null){
            weatherInfos = new ArrayList<>();
        }

        if(!weatherInfos.isEmpty()){
            weatherInfos.clear();
        }

        //重庆 今日
        AreaWeatherInfo areaWeatherInfo1 = new AreaWeatherInfo();
        areaWeatherInfo1.setName("重庆市区_今日");
        areaWeatherInfo1.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_sq", "")));
        areaWeatherInfo1.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_sq", "")));
        areaWeatherInfo1.setPressure(Double.parseDouble(preferences.getString("pressure_cq_sq", "")));
        areaWeatherInfo1.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_sq", "")));
        areaWeatherInfo1.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_sq", "")));

        AreaWeatherInfo areaWeatherInfo2 = new AreaWeatherInfo();
        areaWeatherInfo2.setName("大足区_今日");
        areaWeatherInfo2.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_dz", "")));
        areaWeatherInfo2.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_dz", "")));
        areaWeatherInfo2.setPressure(Double.parseDouble(preferences.getString("pressure_cq_dz", "")));
        areaWeatherInfo2.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_dz", "")));
        areaWeatherInfo2.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_dz", "")));

        AreaWeatherInfo areaWeatherInfo3 = new AreaWeatherInfo();
        areaWeatherInfo3.setName("武隆区_今日");
        areaWeatherInfo3.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_wl", "")));
        areaWeatherInfo3.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_wl", "")));
        areaWeatherInfo3.setPressure(Double.parseDouble(preferences.getString("pressure_cq_wl", "")));
        areaWeatherInfo3.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_wl", "")));
        areaWeatherInfo3.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_wl", "")));

        AreaWeatherInfo areaWeatherInfo4 = new AreaWeatherInfo();
        areaWeatherInfo4.setName("奉节区_今日");
        areaWeatherInfo4.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_fj", "")));
        areaWeatherInfo4.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_fj", "")));
        areaWeatherInfo4.setPressure(Double.parseDouble(preferences.getString("pressure_cq_fj", "")));
        areaWeatherInfo4.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_fj", "")));
        areaWeatherInfo4.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_fj", "")));

        //重庆 明日
        AreaWeatherInfo areaWeatherInfo5 = new AreaWeatherInfo();
        areaWeatherInfo5.setName("重庆市区_明日");
        areaWeatherInfo5.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_sq_t", "")));
        areaWeatherInfo5.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_sq_t", "")));
        areaWeatherInfo5.setPressure(Double.parseDouble(preferences.getString("pressure_cq_sq_t", "")));
        areaWeatherInfo5.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_sq_t", "")));
        areaWeatherInfo5.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_sq_t", "")));

        AreaWeatherInfo areaWeatherInfo6 = new AreaWeatherInfo();
        areaWeatherInfo6.setName("大足区_明日");
        areaWeatherInfo6.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_dz_t", "")));
        areaWeatherInfo6.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_dz_t", "")));
        areaWeatherInfo6.setPressure(Double.parseDouble(preferences.getString("pressure_cq_dz_t", "")));
        areaWeatherInfo6.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_dz_t", "")));
        areaWeatherInfo6.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_dz_t", "")));

        AreaWeatherInfo areaWeatherInfo7 = new AreaWeatherInfo();
        areaWeatherInfo7.setName("武隆区_明日");
        areaWeatherInfo7.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_wl_t", "")));
        areaWeatherInfo7.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_wl_t", "")));
        areaWeatherInfo7.setPressure(Double.parseDouble(preferences.getString("pressure_cq_wl_t", "")));
        areaWeatherInfo7.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_wl_t", "")));
        areaWeatherInfo7.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_wl_t", "")));

        AreaWeatherInfo areaWeatherInfo8 = new AreaWeatherInfo();
        areaWeatherInfo8.setName("奉节区_明日");
        areaWeatherInfo8.setTemperature(Double.parseDouble(preferences.getString("temperature_cq_fj_t", "")));
        areaWeatherInfo8.setHumidity(Double.parseDouble(preferences.getString("humidity_cq_fj_t", "")));
        areaWeatherInfo8.setPressure(Double.parseDouble(preferences.getString("pressure_cq_fj_t", "")));
        areaWeatherInfo8.setVisibility(Double.parseDouble(preferences.getString("visibility_cq_fj_t", "")));
        areaWeatherInfo8.setRainfull(Integer.parseInt(preferences.getString("rainfall_cq_fj_t", "")));

        weatherInfos.add(areaWeatherInfo1);
        weatherInfos.add(areaWeatherInfo2);
        weatherInfos.add(areaWeatherInfo3);
        weatherInfos.add(areaWeatherInfo4);
        weatherInfos.add(areaWeatherInfo5);
        weatherInfos.add(areaWeatherInfo6);
        weatherInfos.add(areaWeatherInfo7);
        weatherInfos.add(areaWeatherInfo8);

    }

    private void getDates_SH(){
        SharedPreferences preferences = getSharedPreferences("BigDates", MODE_PRIVATE);

        if(weatherInfos == null){
            weatherInfos = new ArrayList<>();
        }

        if(!weatherInfos.isEmpty()){
            weatherInfos.clear();
        }

        //上海 今日
        AreaWeatherInfo areaWeatherInfo9 = new AreaWeatherInfo();
        areaWeatherInfo9.setName("上海市区1_今日");
        areaWeatherInfo9.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_sq1", "")));
        areaWeatherInfo9.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_sq1", "")));
        areaWeatherInfo9.setPressure(Double.parseDouble(preferences.getString("pressure_sh_sq1", "")));
        areaWeatherInfo9.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_sq1", "")));
        areaWeatherInfo9.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_sq1", "")));

        AreaWeatherInfo areaWeatherInfo10 = new AreaWeatherInfo();
        areaWeatherInfo10.setName("浦东新区_今日");
        areaWeatherInfo10.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_pd", "")));
        areaWeatherInfo10.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_pd", "")));
        areaWeatherInfo10.setPressure(Double.parseDouble(preferences.getString("pressure_sh_pd", "")));
        areaWeatherInfo10.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_pd", "")));
        areaWeatherInfo10.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_pd", "")));

        AreaWeatherInfo areaWeatherInfo11 = new AreaWeatherInfo();
        areaWeatherInfo11.setName("上海市区2_今日");
        areaWeatherInfo11.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_sq2", "")));
        areaWeatherInfo11.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_sq2", "")));
        areaWeatherInfo11.setPressure(Double.parseDouble(preferences.getString("pressure_sh_sq2", "")));
        areaWeatherInfo11.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_sq2", "")));
        areaWeatherInfo11.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_sq2", "")));

        AreaWeatherInfo areaWeatherInfo12 = new AreaWeatherInfo();
        areaWeatherInfo12.setName("青浦区_今日");
        areaWeatherInfo12.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_qp", "")));
        areaWeatherInfo12.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_qp", "")));
        areaWeatherInfo12.setPressure(Double.parseDouble(preferences.getString("pressure_sh_qp", "")));
        areaWeatherInfo12.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_qp", "")));
        areaWeatherInfo12.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_qp", "")));

        //上海 明日
        AreaWeatherInfo areaWeatherInfo13 = new AreaWeatherInfo();
        areaWeatherInfo13.setName("上海市区1_明日");
        areaWeatherInfo13.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_sq1_t", "")));
        areaWeatherInfo13.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_sq1_t", "")));
        areaWeatherInfo13.setPressure(Double.parseDouble(preferences.getString("pressure_sh_sq1_t", "")));
        areaWeatherInfo13.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_sq1_t", "")));
        areaWeatherInfo13.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_sq1_t", "")));

        AreaWeatherInfo areaWeatherInfo14 = new AreaWeatherInfo();
        areaWeatherInfo14.setName("浦东新区_明日");
        areaWeatherInfo14.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_pd_t", "")));
        areaWeatherInfo14.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_pd_t", "")));
        areaWeatherInfo14.setPressure(Double.parseDouble(preferences.getString("pressure_sh_pd_t", "")));
        areaWeatherInfo14.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_pd_t", "")));
        areaWeatherInfo14.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_pd_t", "")));

        AreaWeatherInfo areaWeatherInfo15 = new AreaWeatherInfo();
        areaWeatherInfo15.setName("上海市区2_明日");
        areaWeatherInfo15.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_sq2_t", "")));
        areaWeatherInfo15.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_sq2_t", "")));
        areaWeatherInfo15.setPressure(Double.parseDouble(preferences.getString("pressure_sh_sq2_t", "")));
        areaWeatherInfo15.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_sq2_t", "")));
        areaWeatherInfo15.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_sq2_t", "")));

        AreaWeatherInfo areaWeatherInfo16 = new AreaWeatherInfo();
        areaWeatherInfo16.setName("青浦区_明日");
        areaWeatherInfo16.setTemperature(Double.parseDouble(preferences.getString("temperature_sh_qp_t", "")));
        areaWeatherInfo16.setHumidity(Double.parseDouble(preferences.getString("humidity_sh_qp_t", "")));
        areaWeatherInfo16.setPressure(Double.parseDouble(preferences.getString("pressure_sh_qp_t", "")));
        areaWeatherInfo16.setVisibility(Double.parseDouble(preferences.getString("visibility_sh_qp_t", "")));
        areaWeatherInfo16.setRainfull(Integer.parseInt(preferences.getString("rainfall_sh_qp_t", "")));

        weatherInfos.add(areaWeatherInfo9);
        weatherInfos.add(areaWeatherInfo10);
        weatherInfos.add(areaWeatherInfo11);
        weatherInfos.add(areaWeatherInfo12);
        weatherInfos.add(areaWeatherInfo13);
        weatherInfos.add(areaWeatherInfo14);
        weatherInfos.add(areaWeatherInfo15);
        weatherInfos.add(areaWeatherInfo16);
    }

    /**
     * 进行景点排序
     */
    private void startSorting(){

//        for(AreaWeatherInfo weatherInfo:weatherInfos){
//
//        }

        if(weatherComparator == null) {
            weatherComparator = new WeatherComparator();
        }
        Collections.sort(list, weatherComparator);
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
            case "朱家角古镇景区":
                toDestination(31.109319, 121.053951);
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
