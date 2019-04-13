package com.chen.fy.wisdomscenicspot.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class FeedbackActivity extends TakePhotoActivity {


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
    private ImageView iv_photo_feedback;
    private EditText et_address_name_feedback;
    private EditText et_address_title_feedback;
    private EditText et_matters_feedback;
    private EditText et_phone_feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);

        initView();
        initSelectBox();
        initTakePhoto();

    }

    private void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_feedback);

        et_address_name_feedback = findViewById(R.id.et_address_name_feedback);
        et_address_title_feedback = findViewById(R.id.et_address_title_feedback);
        iv_photo_feedback = findViewById(R.id.iv_photo_feedback);
        et_matters_feedback = findViewById(R.id.et_matters_feedback);
        et_phone_feedback = findViewById(R.id.et_phone_feedback);
        Button btn_submit = findViewById(R.id.btn_submit_feedback);

        et_address_name_feedback.setOnClickListener(new MyOnClickListener());
        et_address_title_feedback.setOnClickListener(new MyOnClickListener());
        iv_photo_feedback.setOnClickListener(new MyOnClickListener());
        et_matters_feedback.setOnClickListener(new MyOnClickListener());
        et_phone_feedback.setOnClickListener(new MyOnClickListener());
        btn_submit.setOnClickListener(new MyOnClickListener());

        //自动弹出输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /***
     *  初始化点击上传图片后显示的对话框
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
            iv_photo_feedback.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击接口
     */
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.et_address_name_feedback:
                    break;
                case R.id.et_address_title_feedback:
                    break;
                case R.id.iv_photo_feedback:
                    dialog.show();
                    break;
                case R.id.et_matters_feedback:
                    break;
                case R.id.et_phone_feedback:
                    break;
                case R.id.btn_submit_feedback:
                    Toast.makeText(FeedbackActivity.this, "提交", Toast.LENGTH_SHORT).show();
                case R.id.take_photo_dialog:
                    //相机获取照片并剪裁
                    takePhoto.onPickFromCaptureWithCrop(uri, cropOptions);
                    dialog.dismiss();
                    break;
                case R.id.chosen_photo_dialog:
                    //相机获取照片并剪裁
                    takePhoto.onPickFromCaptureWithCrop(uri, cropOptions);
                    dialog.dismiss();
                    break;
            }
        }
    }
}
