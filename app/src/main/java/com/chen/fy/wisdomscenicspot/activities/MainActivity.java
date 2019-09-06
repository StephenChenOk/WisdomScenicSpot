package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.fragment.FoundFragment;
import com.chen.fy.wisdomscenicspot.fragment.HomeFragment;
import com.chen.fy.wisdomscenicspot.fragment.MineFragment;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{

    private HomeFragment homeFragment;
    private FoundFragment foundFragment;
    private MineFragment mineFragment;

    //登入的用户名
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scenic);

        this.getFilesDir();

        //找到控件对象
        RadioGroup radioGroup = findViewById(R.id.radio_group_main);

        //初始化fragment
        homeFragment = new HomeFragment();
        foundFragment = new FoundFragment();
        mineFragment = new MineFragment();

        //第一次进入时显示home界面
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main,homeFragment).
                commitAllowingStateLoss();

        radioGroup.setOnCheckedChangeListener(this);

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //底部导航栏按钮选中事件
        RadioButton radioButton = group.findViewById(checkedId);
        radioButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.home_main:
                transaction.replace(R.id.fragment_main,homeFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.found_main:
                transaction.replace(R.id.fragment_main,foundFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.mine_main:
                transaction.replace(R.id.fragment_main,mineFragment);
                transaction.commitAllowingStateLoss();
                break;
        }
    }

    //跳转登录界面,方便返回值
    public void jumpLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

}