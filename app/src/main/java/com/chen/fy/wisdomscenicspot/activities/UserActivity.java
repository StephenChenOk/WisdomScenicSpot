package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.beans.Visitor;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
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
                    if(userId == null || userId.isEmpty()) {   //当还没有登入账号,则进入登入界面
                        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    }else{    //已经登入账号,则进入显示个人信息界面
                        Intent intent = new Intent(UserActivity.this,MyInfoActivity.class);
                        intent.putExtra("userId",userId);
                        startActivityForResult(intent,2);
                        break;
                    }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:   //登入成功返回账号
                if (resultCode == RESULT_OK) {
                    userId = data.getStringExtra("userId");
                }
                break;
            case 2:   //退出账号
                if(resultCode == RESULT_OK){
                    userId = "";
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示一些简单的用户信息
        showInfo();
        //进行头像加载
        loadHeadIcon();
    }

    private void showInfo() {
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

    /**
     * 进行头像加载
     */
    private void loadHeadIcon() {
        if (userId != null && !userId.isEmpty()) {
            //头像加载
            File file = new File(this.getExternalFilesDir(null), userId + ".jpg");
            Uri headIconUri = Uri.fromFile(file);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(headIconUri));
                headIcon.setImageBitmap(bitmap);                    //如果上面产生文件存在异常，则不执行
            } catch (FileNotFoundException e) {
                headIcon.setImageResource(R.drawable.user_12);   //捕获异常后，设置头像为默认头像，程序继续执行
            }
        }else{
            headIcon.setImageResource(R.drawable.user_12);
        }
    }
}
