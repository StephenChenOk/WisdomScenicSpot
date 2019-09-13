package com.chen.fy.wisdomscenicspot.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class ViewPointDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpoint_detail_layout);
        UiUtils.changeStatusBarTextImgColor(this,true);
        initView();
    }

    private void initView() {
    }
}
