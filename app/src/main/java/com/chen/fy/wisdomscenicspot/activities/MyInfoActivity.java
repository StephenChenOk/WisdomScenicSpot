package com.chen.fy.wisdomscenicspot.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.model.Visitor;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyInfoActivity extends TakePhotoActivity implements View.OnClickListener {

    private TextView name_my_info_tv;
    private TextView id_my_info_tv;
    private TextView phone_my_info_tv;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private CropOptions cropOptions;
    private Uri headIconUri;
    private CircleImageView headIcon;

    private Dialog dialog;


    //当前用户登入的账号
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info_layout);

        //初始化View,和设置各种点击事件
        initView();

        //获取当前登入的账号
        getUserID();

        //初始化TakePhoto,并进行图片的处理
        initTakePhoto();

        //初始化点击头像后生成的选择框
        initTakePhotoDialog();

    }

    /**
     * 初始化View
     */
    private void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_my_info);
        //找到控件
        LinearLayout head_icon_my_info_box = findViewById(R.id.head_icon_my_info_box);
        LinearLayout name_my_info_box = findViewById(R.id.name_my_info_box);
        LinearLayout id_my_info_box = findViewById(R.id.id_my_info_box);
        LinearLayout phone_my_info_box = findViewById(R.id.phone_my_info_box);
        Button out_btn = findViewById(R.id.out_my_info);
        name_my_info_tv = findViewById(R.id.name_my_info);
        id_my_info_tv = findViewById(R.id.id_my_info);
        phone_my_info_tv = findViewById(R.id.phone_my_info);
        headIcon = findViewById(R.id.head_icon_my_info);

        //点击事件
        head_icon_my_info_box.setOnClickListener(this);
        name_my_info_box.setOnClickListener(this);
        id_my_info_box.setOnClickListener(this);
        phone_my_info_box.setOnClickListener(this);
        out_btn.setOnClickListener(this);

        //标题栏返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
    }

    /**
     * 获取当前登入的账号
     */
    private void getUserID() {
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        }
    }

    /**
     * 初始化点击头像后生成的选择框
     */
    private void initTakePhotoDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //反射一个自定义的全新的对话框布局
        View view = inflater.inflate(R.layout.photo_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        //在当前布局中找到控件对象
        Button take_photo = view.findViewById(R.id.take_photo_dialog);
        Button chosen_photo = view.findViewById(R.id.chosen_photo_dialog);
        //监听事件
        take_photo.setOnClickListener(this);
        chosen_photo.setOnClickListener(this);
    }

    /**
     * 初始化TakePhoto,并进行图片的处理
     */
    private void initTakePhoto() {
        //获得对象
        takePhoto = getTakePhoto();
        //获取外部存储位置的uri
        File file = new File(getExternalFilesDir(null), userId + "headIcon.jpg");
        headIconUri = Uri.fromFile(file);

        //进行图片剪切
        int size = Math.min(getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels);
        cropOptions = new CropOptions.Builder().setOutputX(size).
                setOutputX(size).setWithOwnCrop(false).create();  //true表示使用TakePhoto自带的裁剪工具
    }

    @Override
    protected void onResume() {
        super.onResume();

        //头像加载
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(headIconUri));
            headIcon.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            headIcon.setImageResource(R.drawable.user_12);
        }

        //给控件赋值
        List<Visitor> visitors = LitePal.where("userId = ?", userId).find(Visitor.class);
        for (Visitor visitor : visitors) {
            name_my_info_tv.setText(visitor.getUserName());
            id_my_info_tv.setText(visitor.getUserId());
            phone_my_info_tv.setText(visitor.getPhone());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {   //点击头像
            case R.id.head_icon_my_info_box:
                dialog.show();
                break;
            case R.id.take_photo_dialog: {  //点击拍照
                //相机获取照片并剪裁
                takePhoto.onPickFromCaptureWithCrop(headIconUri, cropOptions);
                // takePhoto.onPickFromCapture(uri);
                dialog.dismiss();
                break;
            }
            case R.id.chosen_photo_dialog: {  //点击相册
                //相册获取照片并剪裁
                takePhoto.onPickFromGalleryWithCrop(headIconUri, cropOptions);
                // takePhoto.onPickFromGallery();
                dialog.dismiss();
                break;
            }
            case R.id.name_my_info_box:         //点击昵称,跳转修改昵称界面
                intent = new Intent(this, ModifyNameActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                break;
            case R.id.id_my_info_box:
                Toast.makeText(this, "账号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.phone_my_info_box:        //点击手机号码,跳转修改手机号码界面
                intent = new Intent(this, ModifyPhotoActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                break;
            case R.id.out_my_info:               //点击退出账号
                //登入状态清空
                SharedPreferences.Editor editor = getSharedPreferences("login_state",MODE_PRIVATE).edit();
                editor.putString("userId","");
                editor.apply();
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        //将拍摄的照片显示出来
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(headIconUri));
            //imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
