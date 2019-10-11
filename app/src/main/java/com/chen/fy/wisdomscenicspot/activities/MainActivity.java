package com.chen.fy.wisdomscenicspot.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.fragment.FoundFragment;
import com.chen.fy.wisdomscenicspot.fragment.HomeFragment;
import com.chen.fy.wisdomscenicspot.fragment.MineFragment;
import com.chen.fy.wisdomscenicspot.services.BigDatesIntentServices;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private HomeFragment homeFragment;
    private FoundFragment foundFragment;
    private MineFragment mineFragment;

    //登入的用户名
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this, true);

        setContentView(R.layout.main_scenic);

        this.getFilesDir();

        applyPermission();

        //找到控件对象
        RadioGroup radioGroup = findViewById(R.id.radio_group_main);

        //初始化fragment
        homeFragment = new HomeFragment();
        foundFragment = new FoundFragment();
        mineFragment = new MineFragment();

        //第一次进入时显示home界面
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main, homeFragment).
                commitAllowingStateLoss();

        radioGroup.setOnCheckedChangeListener(this);

        Intent intent = new Intent(this, BigDatesIntentServices.class);
        startService(intent);

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
        switch (v.getId()) {
            case R.id.home_main:
                transaction.replace(R.id.fragment_main, homeFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.found_main:
                transaction.replace(R.id.fragment_main, foundFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.mine_main:
                transaction.replace(R.id.fragment_main, mineFragment);
                transaction.commitAllowingStateLoss();
                break;
        }
    }

    //跳转登录界面,方便返回值
    public void jumpLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * 动态申请危险权限
     */
    private void applyPermission() {
        //权限集合
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.SEND_SMS);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "必须同意所有权限才可以使用本程序!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}