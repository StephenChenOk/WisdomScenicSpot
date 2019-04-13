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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity{

    private CircleImageView headIcon;
    private TextView userName;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine);

        initView();
        initSelectBox();
    }

    private void initView(){
        headIcon = findViewById(R.id.head_icon_mine);
        userName = findViewById(R.id.user_name_mine);
        headIcon.setOnClickListener(new MyOnClickListener());
        userName.setOnClickListener(new MyOnClickListener());
    }

    /***
     *  初始化点击头像后的弹出框
     */
    private void initSelectBox() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //反射一个自定义的全新的对话框布局
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.photo_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        //在当前布局中找到控件对象
        Button take_photo = view.findViewById(R.id.btn_visitor_login_dialog);
        Button chosen_photo = view.findViewById(R.id.btn_manager_login_dialog);
        //监听事件
        take_photo.setOnClickListener(new MyOnClickListener());
        chosen_photo.setOnClickListener(new MyOnClickListener());
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.head_icon_mine:
                    dialog.show();
                    break;
                case R.id.user_name_mine:
                    dialog.show();
                    break;
                case R.id.btn_visitor_login_dialog:   //游客登入
                    Intent intent_visitor = new Intent(UserActivity.this,LoginActivity.class);
                    intent_visitor.putExtra("loginType",1);
                    dialog.dismiss();
                    startActivity(intent_visitor);
                    break;
                case R.id.btn_manager_login_dialog:   //管理员登入
                    Intent intent_manager = new Intent(UserActivity.this,LoginActivity.class);
                    intent_manager.putExtra("loginType",2);
                    dialog.dismiss();
                    startActivity(intent_manager);
                    break;
            }
        }
    }
}
