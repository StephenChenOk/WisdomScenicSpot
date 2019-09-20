package com.chen.fy.wisdomscenicspot.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.utils.ImageUtil;
import com.chen.fy.wisdomscenicspot.utils.ScenicDescribeUtils;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class ViewPointDetailActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageView imageView;
    private Toolbar toolbar;

    private TextView tv_rainfall_detail;
    private TextView tv_temperature_detail;
    private TextView tv_humidity_detail;
    private TextView tv_visibility_detail;

    private TextView tv_viewpoint_detail;

    private String rainfull;
    private String temperature;
    private String humidity;
    private String visibility;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UiUtils.changeStatusBarTextImgColor(this, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpoint_detail_layout);
        initView();
        initDate();
    }

    private void initView() {
        ImageView iv_return = findViewById(R.id.iv_return_viewpoint_detail);
        toolbar = findViewById(R.id.tb_viewpoint_detail);
        imageView = findViewById(R.id.iv_viewpoint_detail);

        tv_rainfall_detail = findViewById(R.id.tv_rainfall_detail);
        tv_temperature_detail = findViewById(R.id.tv_temperature_detail);
        tv_humidity_detail = findViewById(R.id.tv_humidity_detail);
        tv_visibility_detail = findViewById(R.id.tv_visibility_detail);

        tv_viewpoint_detail = findViewById(R.id.tv_viewpoint_detail);

        iv_return.setOnClickListener(this);

        //找到控件对象
        RadioGroup radioGroup = findViewById(R.id.viewpoint_detail_group_main);
        radioGroup.setOnCheckedChangeListener(this);


        if (getIntent() != null) {
            setViewPoint(getIntent().getStringExtra("ViewPointName"));
        }
    }

    private void setViewPoint(String name) {
        if (ImageUtil.getImageId(name) != 0) {
            Glide.with(this).load(ImageUtil.getImageId(name)).into(imageView);
        }
        switch (name) {
            //重庆
            case "磁器口古镇":
                initDetailInfo("磁器口古镇", ScenicDescribeUtils.getQCK());
                break;
            case "解放碑步行街":
                initDetailInfo("解放碑步行街", ScenicDescribeUtils.getJFB());
                break;
            case "武隆天生三桥":
                initDetailInfo("武隆天生三桥", ScenicDescribeUtils.getSQ());
                break;
            case "大足石刻":
                initDetailInfo("大足石刻", ScenicDescribeUtils.getDZSK());
                break;
            case "白公馆":
                initDetailInfo("白公馆", ScenicDescribeUtils.getBGG());
                break;
            case "长江索道":
                initDetailInfo("长江索道", ScenicDescribeUtils.getCJSD());
                break;
            case "南山风景区":
                initDetailInfo("南山风景区", ScenicDescribeUtils.getNS());
                break;
            case "白帝城景区":
                initDetailInfo("白帝城景区", ScenicDescribeUtils.getBDC());
                break;

            //上海
            case "外滩":
                initDetailInfo("外滩", ScenicDescribeUtils.getWT());
                break;
            case "上海迪士尼度假区":
                initDetailInfo("上海迪士尼度假区", ScenicDescribeUtils.getDSN());
                break;
            case "南京路步行街":
                initDetailInfo("南京路步行街", ScenicDescribeUtils.getNJL_BXJ());
                break;
            case "上海长风海洋世界":
                initDetailInfo("上海长风海洋世界", ScenicDescribeUtils.getCF_HYSJ());
                break;
        }
    }

    private void initDetailInfo(String name, String qck) {
        toolbar.setTitle(name);
        tv_viewpoint_detail.setText(qck);
    }

    private void initDate() {
        SharedPreferences preferences = getSharedPreferences("BigDates", MODE_PRIVATE);
        rainfull = preferences.getString("rainfall", "");
        temperature = preferences.getString("temperature", "");
        humidity = preferences.getString("humidity", "");
        visibility = preferences.getString("visibility", "");

        switch (rainfull) {
            case "0":   //无雨
                rainfull = "无雨";
                break;
            case "1":   //有雨
                rainfull = "有雨";
                break;
        }

        setWeatherDates(rainfull, temperature, humidity, visibility);
    }

    private void setWeatherDates(String rainfull, String temperature, String humidity, String visibility) {
        tv_rainfall_detail.setText(rainfull);
        tv_temperature_detail.setText(String.format("%s℃", temperature));
        tv_humidity_detail.setText(String.format("%s％", humidity));
        tv_visibility_detail.setText(String.format("%sm", visibility));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_viewpoint_detail:
                finish();
                break;
            case R.id.btn_today_weather:
                Toast.makeText(this,"今日天气",Toast.LENGTH_LONG).show();
                setWeatherDates(rainfull, temperature, humidity, visibility);
                break;
            case R.id.btn_tomorrow_weather:
                Toast.makeText(this,"明日天气",Toast.LENGTH_LONG).show();
                setWeatherDates("有雨","26" , "79", "13105");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //底部导航栏按钮选中事件
        RadioButton radioButton = group.findViewById(checkedId);
        radioButton.setOnClickListener(this);
    }
}
