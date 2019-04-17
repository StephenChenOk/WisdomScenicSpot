package com.chen.fy.wisdomscenicspot.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.beans.Visitor;

import org.litepal.LitePal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity{

    private CircleImageView headIcon;
    private TextView userName;
    private TextView infoText;
    /**
     * 当前用户登入的用户名
     */
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine);

        initView();
    }

    private void initView(){
        headIcon = findViewById(R.id.head_icon_mine);
        userName = findViewById(R.id.user_name_mine);
        infoText = findViewById(R.id.info_text_mine);
        headIcon.setOnClickListener(new MyOnClickListener());
        userName.setOnClickListener(new MyOnClickListener());
        infoText.setOnClickListener(new MyOnClickListener());
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.head_icon_mine:
                case R.id.user_name_mine:
                case R.id.info_text_mine:
                    Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                    startActivityForResult(intent,1);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    userId = data.getStringExtra("userId");

                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //赋值
        if (userId != null && !userId.isEmpty()) {
            List<Visitor> visitors = LitePal.where("userId = ?", userId).find(Visitor.class);
            for (Visitor visitor : visitors) {
                userName.setText(visitor.getUserName());
                infoText.setText("个人信息>");
            }
        } else {
            userName.setText("登入/注册");
            infoText.setText("");
        }
    }
}
