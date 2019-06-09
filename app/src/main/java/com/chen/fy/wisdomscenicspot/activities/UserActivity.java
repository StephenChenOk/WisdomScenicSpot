package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.beans.Manager;
import com.chen.fy.wisdomscenicspot.beans.Visitor;
import com.chen.fy.wisdomscenicspot.utils.LoginRegisterUtils;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private CircleImageView headIcon;
    private TextView userName;
    private TextView infoText;
    private TextView tv_user_manager;
    /**
     * 当前用户登入的用户名
     */
    private String userId;
    /**
     * 当前登入的方式
     */
    private int loginType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示一些简单的用户信息
        //获取之前的登入状态
        getLoginState();
        switch (loginType) {
            case 2:    //显示管理者
                initManagerView();
                showManagerInfo();  //显示管理者信息
                break;
            default:   //当loginType不为2时显示常规界面
                initMineView();
                showVisitorInfo();
                //进行头像加载
                loadHeadIcon();
        }
    }

    private void initMineView() {
        setContentView(R.layout.mine);
        //游客端view
        headIcon = findViewById(R.id.head_icon_mine);
        userName = findViewById(R.id.user_name_mine);
        infoText = findViewById(R.id.info_text_mine);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        headIcon.setOnClickListener(myOnClickListener);
        userName.setOnClickListener(myOnClickListener);
        infoText.setOnClickListener(myOnClickListener);

    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_icon_mine:
                case R.id.user_name_mine:
                case R.id.info_text_mine:
                    if (userId == null || userId.isEmpty()) {   //当还没有登入账号,则进入登入界面
                        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    } else {    //已经登入账号,则进入显示个人信息界面
                        Intent intent = new Intent(UserActivity.this, MyInfoActivity.class);
                        intent.putExtra("userId", userId);
                        startActivityForResult(intent, 2);
                    }
                    break;
                case R.id.out_login_manager:   //退出管理员账号
                    loginType = -1;
                    userId = "";
                    initMineView();
                    showVisitorInfo();//显示管理者信息
                    saveLoginState(userId, loginType);
                    break;
            }
        }
    }

    /**
     * 保存登入状态
     */
    private void saveLoginState(String userId, int loginType) {
        SharedPreferences.Editor editor = getSharedPreferences("login_state", MODE_PRIVATE).edit();
        editor.putString("userId", userId);
        editor.putInt("loginType", loginType);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:   //登入成功返回账号
                if (resultCode == RESULT_OK) {
                    loginType = data.getIntExtra("loginType", -1);
                    userId = data.getStringExtra("userId");
                } else {
                    Toast.makeText(this, "未知错误!", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:   //退出账号
                if (resultCode == RESULT_OK) {
                    userId = "";
                }
                break;
        }

    }

    private void initManagerView() {
        setContentView(R.layout.manager_layout);
        //管理员端view
        tv_user_manager = findViewById(R.id.user_name_manager);
        Button btn_out_login = findViewById(R.id.out_login_manager);
        btn_out_login.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 获取之前的登入状态
     */
    private void getLoginState() {
        SharedPreferences preferences = getSharedPreferences("login_state", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        loginType = preferences.getInt("loginType", -1);
    }

    /**
     * 显示游客一些简单的信息
     */
    private void showVisitorInfo() {
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
     * 显示管理者一些简单的信息
     */
    private void showManagerInfo() {
        if (userId != null && !userId.isEmpty()) {
            List<Manager> managers = LitePal.where("userId = ?", userId).find(Manager.class);
            for (Manager manager : managers) {
                tv_user_manager.setText(String.format("管理员%s", manager.getUserId()));
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
            File file = new File(this.getExternalFilesDir(null), userId + "headIcon.jpg");
            Uri headIconUri = Uri.fromFile(file);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(headIconUri));
                headIcon.setImageBitmap(bitmap);                    //如果上面产生文件存在异常，则不执行
            } catch (FileNotFoundException e) {
                headIcon.setImageResource(R.drawable.user_12);   //捕获异常后，设置头像为默认头像，程序继续执行
            }
        } else {
            headIcon.setImageResource(R.drawable.user_12);
        }
    }
}
