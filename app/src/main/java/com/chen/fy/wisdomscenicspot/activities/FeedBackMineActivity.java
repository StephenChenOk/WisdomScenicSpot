package com.chen.fy.wisdomscenicspot.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class FeedBackMineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_mine_layout);

        UiUtils.changeStatusBarTextImgColor(this,true);

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.tb_feedback_mine);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
