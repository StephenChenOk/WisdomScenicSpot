package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.utils.ImageUtil;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class ViewPointDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UiUtils.changeStatusBarTextImgColor(this, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpoint_detail_layout);
        initView();
    }

    private void initView() {
        ImageView iv_return = findViewById(R.id.iv_return_viewpoint_detail);
        toolbar = findViewById(R.id.tb_viewpoint_detail);
        imageView = findViewById(R.id.iv_viewpoint_detail);

        iv_return.setOnClickListener(this);

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
                break;
            case "解放碑步行街":
                toolbar.setTitle("解放碑步行街");
                break;
            case "武隆天生三桥":
                toolbar.setTitle("武隆天生三桥");
                break;
            case "大足石刻":
                toolbar.setTitle("大足石刻");
                break;
            case "白公馆":
                toolbar.setTitle("白公馆");
                break;
            case "长江索道":
                toolbar.setTitle("长江索道");
                break;
            case "南山风景区":
                toolbar.setTitle("南山风景区");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_viewpoint_detail:
                finish();
                break;
        }
    }
}
