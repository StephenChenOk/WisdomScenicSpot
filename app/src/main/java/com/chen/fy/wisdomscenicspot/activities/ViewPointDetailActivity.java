package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
            case "磁器口古镇":
                toolbar.setTitle("磁器口古镇");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getQCK());
                break;
            case "解放碑步行街":
                toolbar.setTitle("解放碑步行街");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getJFB());
                break;
            case "武隆天生三桥":
                toolbar.setTitle("武隆天生三桥");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getSQ());
                break;
            case "大足石刻":
                toolbar.setTitle("大足石刻");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getDZSK());
                break;
            case "白公馆":
                toolbar.setTitle("白公馆");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getBGG());
                break;
            case "长江索道":
                toolbar.setTitle("长江索道");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getCJSD());
                break;
            case "南山风景区":
                toolbar.setTitle("南山风景区");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getNS());
                break;
            case "白帝城景区":
                toolbar.setTitle("白帝城景区");
                tv_viewpoint_detail.setText(ScenicDescribeUtils.getBDC());
                break;
        }
    }

    private void initDate(){
        SharedPreferences preferences = getSharedPreferences("BigDates", MODE_PRIVATE);
        String rainfull = preferences.getString("rainfall", "");
        String temperature = preferences.getString("temperature", "");
        String humidity = preferences.getString("humidity", "");
        String visibility = preferences.getString("visibility", "");

        switch (rainfull){
            case "0":   //无雨
                rainfull = "无雨";
                break;
            case "1":   //有雨
                rainfull = "有雨";
                break;
        }

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
                Toast.makeText(this,"今日天气...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_tomorrow_weather:
                Toast.makeText(this,"明日天气...",Toast.LENGTH_SHORT).show();
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
