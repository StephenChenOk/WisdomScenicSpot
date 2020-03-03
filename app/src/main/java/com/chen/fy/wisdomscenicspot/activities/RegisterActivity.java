package com.chen.fy.wisdomscenicspot.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.model.Visitor;
import com.chen.fy.wisdomscenicspot.utils.LoginRegisterUtils;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_userId;
    private EditText et_passwdOne;
    private EditText et_passwdTwo;
    private Button btn_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initView();
        //禁止输入空格
        LoginRegisterUtils.setEditTextInhibitInputSpace(et_userId);
        LoginRegisterUtils.setEditTextInhibitInputSpace(et_passwdOne);
        LoginRegisterUtils.setEditTextInhibitInputSpace(et_passwdTwo);
    }

    private void initView(){
        //设置toolbar
       // Toolbar toolbar = findViewById(R.id.toolbar_register);
        //setSupportActionBar(toolbar);

        et_userId = findViewById(R.id.userId_register);
        et_passwdOne = findViewById(R.id.password_one_register);
        et_passwdTwo = findViewById(R.id.password_two_register);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(this);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                String userId = et_userId.getText().toString();
                //用户名已填入且没有被注册过时
                if(LoginRegisterUtils.userAvailable(userId)){
                    String password1 = et_passwdOne.getText().toString();
                    String password2 = et_passwdTwo.getText().toString();
                    //判断两次密码是否相等
                    if(LoginRegisterUtils.passwordSame(password1,password2)){
                        //获取系统随机生成的值(盐值),用于加密
                        String pwSalt = UUID.randomUUID().toString().substring(0, 5);
                        //用输入的密码+盐值进行MD5加密
                        String pwHash = LoginRegisterUtils.getMD5(password1 + pwSalt);
                        //存入数据库
                        Visitor visitor = new Visitor();
                        visitor.setUserName("匿名网友");
                        visitor.setUserId(userId);
                        visitor.setPwSalt(pwSalt);
                        visitor.setPwHash(pwHash);
                        visitor.save();
                        //注册成功后跳到登入界面
                        Toast.makeText(RegisterActivity.this,"注册成功,可以进行登入操作",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;

        }
    }
}
