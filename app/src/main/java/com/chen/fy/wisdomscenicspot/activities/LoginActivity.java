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

public class LoginActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    /**
     * 游客登入
     */
    private final int VISITOR_LOGIN = 1;
    /**
     * 管理员登入
     */
    private final int MANAGER_LOGIN = 2;
    /**
     * 当前登入类型
     */
    private int loginType;

    private EditText et_userId_login;
    private EditText et_password_login;
    private Button btn_login;

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
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        //当前登入状态默认为游客端
        loginType = 1;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                loginType = 1;
                break;
            case R.id.rb_manager_login:
                loginType = 2;
                break;
            case R.id.btn_login:
                String userId;
                String password;
                if (loginType == VISITOR_LOGIN) {    //登入游客端
                    userId = et_userId_login.getText().toString();
                    password = et_password_login.getText().toString();
                    //用户名存在且密码与用户名对应
                    if (LoginRegisterUtils.userExisted(userId, loginType) && LoginRegisterUtils.passwordCorrected(userId, password, loginType)) {
                        Toast.makeText(LoginActivity.this, "游客登入成功!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("userId", userId);
                        intent.putExtra("loginType", loginType);
                        setResult(RESULT_OK, intent);
                        //记录登入状态
                        saveLoginState(userId, loginType);
                        finish();
                    }
                } else if (loginType == MANAGER_LOGIN) {  //登入管理者端
                    userId = et_userId_login.getText().toString();
                    password = et_password_login.getText().toString();
                    if (LoginRegisterUtils.userExisted(userId, loginType) && LoginRegisterUtils.passwordCorrected(userId, password, loginType)) {
                        Toast.makeText(LoginActivity.this, "管理员登入成功!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("userId", userId);
                        intent.putExtra("loginType", loginType);
                        //记录登入状态
                        saveLoginState(userId, loginType);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    Toast.makeText(MyApplication.getContext(), "登入出错!", Toast.LENGTH_SHORT).show();
                }
                break;
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
}
