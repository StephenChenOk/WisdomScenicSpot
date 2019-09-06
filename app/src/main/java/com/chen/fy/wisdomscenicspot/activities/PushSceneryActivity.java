package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.utils.ScenicDescribeUtils;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class PushSceneryActivity extends AppCompatActivity {

    private ImageView img;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_scenery_layout);

        initView();

        getScenery();
    }

    private void getScenery() {
        String scenery = getIntent().getStringExtra("scenery");
        switch (scenery){
            case "狮岭朝霞":
                Glide.with(this).load(R.drawable.slzx).into(img);
                textView.setText(ScenicDescribeUtils.getSLZX());
                break;
            case "水晶宫":
                Glide.with(this).load(R.drawable.sjg).into(img);
                textView.setText(ScenicDescribeUtils.getSJG());
                break;
            case "红罗宝帐":
                Glide.with(this).load(R.drawable.hlbz).into(img);
                textView.setText(ScenicDescribeUtils.getHLBZ());
                break;
        }
    }

    private void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_push_scenery);

        img = findViewById(R.id.im_push_scenery);
        textView = findViewById(R.id.tv_push_scenic);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
    }
}
