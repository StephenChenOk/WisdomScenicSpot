package com.chen.fy.wisdomscenicspot.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.consts.Consts;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeedbackActivity extends TakePhotoActivity {


    private static final String TAG = "FeedbackActivity";

    /**
     * 图片地址
     */
    private String imagePath;
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
    private EditText et_address_feedback;
    private EditText et_location_feedback;
    private EditText et_title_feedback;
    private EditText et_phone_feedback;
    /**
     * 我的点击事件
     */
    private MyOnClickListener myOnClickListener;
    /**
     * 反馈需要提交的信息
     */
    private String address;
    private String location;
    private String title;
    private String phone;

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

        et_address_feedback = findViewById(R.id.et_address_feedback);
        et_location_feedback = findViewById(R.id.et_location_feedback);
        iv_photo_feedback = findViewById(R.id.iv_photo_feedback);
        et_title_feedback = findViewById(R.id.et_title_feedback);
        et_phone_feedback = findViewById(R.id.et_phone_feedback);
        Button btn_submit = findViewById(R.id.btn_submit_feedback);

        myOnClickListener = new MyOnClickListener();

        iv_photo_feedback.setOnClickListener(myOnClickListener);
        btn_submit.setOnClickListener(myOnClickListener);

        //自动弹出输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
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
        take_photo.setOnClickListener(myOnClickListener);
        chosen_photo.setOnClickListener(myOnClickListener);
    }

    /**
     * 初始化TakePhoto开源库,实现拍照以及从相册中选择图片
     */
    private void initTakePhoto() {
        //获得对象
        takePhoto = getTakePhoto();

        //获取外部存储位置的uri
        File file = new File(getExternalFilesDir(null), "feedback.jpg");
        uri = Uri.fromFile(file);
        imagePath = uri.getPath();
//        String filePath = uri.getEncodedPath();
        // imagePath = Uri.decode(filePath);

        //进行图片剪切
        int size = Math.min(getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels);
        cropOptions = new CropOptions.Builder().setOutputX(size).
                setOutputX(size).setWithOwnCrop(false).create();  //true表示使用TakePhoto自带的裁剪工具

//        //进行图片压缩
//        CompressConfig compressConfig = new CompressConfig.Builder().
//                setMaxSize(50 * 1024).setMaxPixel(800).create();
//        /*
//         * 启用图片压缩
//         * @param config 压缩图片配置
//         * @param showCompressDialog 压缩时是否显示进度对话框
//         * @return
//         */
//        takePhoto.onEnableCompress(compressConfig, true);
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
                case R.id.iv_photo_feedback:      //添加图片
                    dialog.show();
                    break;
                case R.id.btn_submit_feedback:    //提交反馈
                    submitFeedback();
                    break;
                case R.id.take_photo_dialog:
                    //拍照照片并剪裁
                    takePhoto.onPickFromCaptureWithCrop(uri, cropOptions);
                    dialog.dismiss();
                    break;
                case R.id.chosen_photo_dialog:
                    //相机获取照片并剪裁
                    takePhoto.onPickFromGalleryWithCrop(uri, cropOptions);
                    dialog.dismiss();
                    break;
            }
        }
    }

    /**
     * 提交反馈
     */
    private void submitFeedback() {
        address = et_address_feedback.getText().toString();
        location = et_location_feedback.getText().toString();
        title = et_title_feedback.getText().toString();
        phone = et_phone_feedback.getText().toString();
        //判断反馈的内容是否已经全部填写完成
        if (address.isEmpty() || location.isEmpty() || title.isEmpty() || phone.isEmpty()) {
            Toast.makeText(FeedbackActivity.this, "请填写完成再提交", Toast.LENGTH_SHORT).show();
        } else {  //填写成功
            Toast.makeText(FeedbackActivity.this, "提交完成", Toast.LENGTH_SHORT).show();
            NetworkTask networkTask = new NetworkTask();
            networkTask.execute(imagePath);
            finish();
        }
    }

    /**
     * 进行图片上传
     */
    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        //setType(MultipartBody.FORM)
        String result = "error";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)                               //以文件形式上传
                .addFormDataPart("requestType","景区反馈")    //上传的类型
                .addFormDataPart("place", address)                  //地点名称
                .addFormDataPart("detailed_place", location)       //所在位置
                .addFormDataPart("content", title)                  //问题描述
                .addFormDataPart("contact_info", phone)             //电话号码
                .addFormDataPart("image", imagePath,                         //图片
                        RequestBody.create(MediaType.parse("image/jpg"), new File(imagePath)))
                .build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Consts.FEEDBACK_SERVER_URL)
                .post(requestBody)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d(TAG, "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d(TAG, "响应体 " + resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<String, Integer, String> {

        /**
         * 后台任务开始之前调用，通常用来初始化界面操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 执行后台耗时操作，已在子线程中执行
         * @return   对结果进行返回
         */
        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0]);
        }

        /**
         * 当后台任务执行完毕时调用
         * @param result  后台执行任务的返回值
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "服务器响应" + result);
        }
    }
}
