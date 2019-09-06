package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.application.MyApplication;
import com.chen.fy.wisdomscenicspot.utils.LoginRegisterUtils;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class LoginActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private EditText et_userId_login;
    private EditText et_password_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();
    }

    private void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        et_userId_login = findViewById(R.id.et_userId_login);
        et_password_login = findViewById(R.id.et_password_login);
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
    }

    //反射右上角菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //点击菜单项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.register_login_menu:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = group.findViewById(checkedId);
        radioButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_visitor_login:
                break;
            case R.id.rb_manager_login:
                break;
            case R.id.btn_login:
                String userId;
                String password;
                userId = et_userId_login.getText().toString();
                password = et_password_login.getText().toString();
                //用户名存在且密码与用户名对应
                if (LoginRegisterUtils.userExisted(userId, 1) && LoginRegisterUtils.passwordCorrected(userId, password, 1)) {
                    Toast.makeText(LoginActivity.this, "游客登入成功!", Toast.LENGTH_SHORT).show();
                    //记录登入状态
                    saveLoginState(userId);
                    finish();
                } else {
                    Toast.makeText(MyApplication.getContext(), "登入出错!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 保存登入状态
     */
    private void saveLoginState(String userId) {
        SharedPreferences.Editor editor = getSharedPreferences("login_state", MODE_PRIVATE).edit();
        editor.putString("userId", userId);
        editor.apply();
    }
}
