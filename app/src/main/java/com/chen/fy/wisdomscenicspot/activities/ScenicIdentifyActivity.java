package com.chen.fy.wisdomscenicspot.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;

import java.io.File;
import java.io.FileNotFoundException;

public class ScenicIdentifyActivity extends TakePhotoActivity {

    /**
     * 拍照,相册选择弹出框
     */
    private AlertDialog dialog;

    /**
     * 拍照控件
     */
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    /**
     * 图片剪切以及图片地址
     */
    private CropOptions cropOptions;
    private Uri uri;

    /**
     * 景物识别图片
     */
    private ImageView im_scenic_identify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_identify_layout);

        initView();
        //初始化点击景物识别时弹出的对话框
        initSelectBox();
        //初始化TakePhoto开源库,实现拍照以及从相册中选择图片
        initTakePhoto();

    }

    /**
     * 初始化View
     */
    private void initView(){
        im_scenic_identify = findViewById(R.id.im_scenic_identify);
        im_scenic_identify.setOnClickListener(new MyOnClickListener());
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_scenic_identify);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /***
     *  初始化点击景物识别后显示的对话框
     */
    private void initSelectBox() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //反射一个自定义的全新的对话框布局
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.photo_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        //在当前布局中找到控件对象
        Button take_photo = view.findViewById(R.id.take_photo_dialog);
        Button chosen_photo = view.findViewById(R.id.chosen_photo_dialog);
        //监听事件
        take_photo.setOnClickListener(new MyOnClickListener());
        chosen_photo.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 初始化TakePhoto开源库,实现拍照以及从相册中选择图片
     */
    private void initTakePhoto() {
        //获得对象
        takePhoto = getTakePhoto();

        //获取外部存储位置的uri
        File file = new File(getExternalFilesDir(null), ".jpg");
        uri = Uri.fromFile(file);

        //进行图片剪切
        int size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(true).create();  //true表示使用TakePhoto自带的裁剪工具
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //以下代码为处理Android6.0、7.0动态权限所需(TakePhoto所需)
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

    /**
     * 拍照成功回调
     */
    @Override
    public void takeSuccess(TResult result) {
        //将拍摄的照片显示出来
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            im_scenic_identify.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     */
    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.im_scenic_identify:           //点击添加图片
                    dialog.show();
                    break;
                case R.id.take_photo_dialog:            //点击拍照
                    //相机获取照片并剪裁
                    takePhoto.onPickFromCaptureWithCrop(uri, cropOptions);
                    dialog.dismiss();
                    break;
                case R.id.chosen_photo_dialog:          //点击相册
                    //相册获取照片并剪裁
                    takePhoto.onPickFromGalleryWithCrop(uri, cropOptions);
                    // takePhoto.onPickFromGallery();
                    dialog.dismiss();
                    break;
            }
        }
    }
}
