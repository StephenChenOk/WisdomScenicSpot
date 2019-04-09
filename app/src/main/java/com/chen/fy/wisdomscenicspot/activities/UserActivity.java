package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chen.fy.wisdomscenicspot.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity{

    private CircleImageView headIcon;
    private TextView userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine);

        initView();
    }

    private void initView(){
        headIcon = findViewById(R.id.head_icon_mine);
        userName = findViewById(R.id.user_name_mine);
        headIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
