package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;

public class LoginActivity extends AppCompatActivity {


    private Toolbar toolbar;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();
    }

    private void initView() {
        //设置toolbar
        toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        //获取登入的类型
        loginType = getIntent().getIntExtra("loginType", -1);

        switch (loginType) {
            case VISITOR_LOGIN:
                toolbar.setTitle("游客登入");
                break;
            case MANAGER_LOGIN:
                toolbar.setTitle("管理员登入");
                break;
            default:
                Toast.makeText(this, "出错了!", Toast.LENGTH_SHORT).show();
        }

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
}
