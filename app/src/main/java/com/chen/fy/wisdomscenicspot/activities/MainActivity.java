package com.chen.fy.wisdomscenicspot.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.amap.api.maps.model.HeatmapTileProvider.DEFAULT_GRADIENT;

public class MainActivity extends TakePhotoActivity {

    /**
     * 地图
     */
    MapView mMapView = null;
    /**
     * 地图控制器
     */
    private AMap aMap;
    /**
     * 定位
     */
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLocationListener;
    private AMapLocationClientOption mLocationOption;
    /**
     * 实现控件交互,手势交互等
     */
    private UiSettings mUiSettings;
    /**
     * 地址转化为坐标
     */
    private GeocodeSearch geocoderSearch;

    /**
     * 顶部控件
     */
    private ImageView iv_user;
    private ImageView iv_list;
    private TextView tv_search;

    /**
     * 拍照控件
     */
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    /**
     * 图片剪切以及图片地址
     */
    private CropOptions cropOptions;
    private Uri headIconUri;

    /**
     * 拍照,相册选择弹出框
     */
    private Dialog dialog;
    private Button take_photo;
    private Button chosen_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //初始化view并设置接口
        initView(savedInstanceState);
        //初始化点击景物识别时弹出的对话框
        initSelectBox();
        //初始化TakePhoto开源库,实现拍照以及从相册中选择图片
        initTakePhoto();

        //申请权限
        applyPermission();

    }

    /**
     * 初始化view
     */
    private void initView(Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        iv_user = findViewById(R.id.user_main);
        iv_list = findViewById(R.id.list_main);
        tv_search = findViewById(R.id.top_search);

        iv_list.setOnClickListener(new MyOnClickListener());
        iv_user.setOnClickListener(new MyOnClickListener());
        tv_search.setOnClickListener(new MyOnClickListener());

    }

    /**
     * 控件交互,手势交互
     */
    private void initUiSettings() {
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象

        //1 定位按钮
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置

        //2 手势设置
        mUiSettings.setRotateGesturesEnabled(false);   //旋转手势关闭
        mUiSettings.setTiltGesturesEnabled(false);     //倾斜手势关闭
    }

    /**
     * 开始进行定位等功能
     */
    private void requestLocation() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //aMap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式

        initPositionDot();
        initUiSettings();
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        initPosition();
    }

    /**
     * 初始化定位
     */
    private void initPosition() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //声明定位回调监听器
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        navigateTo(aMapLocation.getLatitude(), aMapLocation.getLongitude(), 16);
                        //drawFlowRate(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象,用来设置发起定位的模式和相关参数。
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    boolean flag = false;
    /**
     * 根据经纬度移动到某一个位置并显示
     */
    private void navigateTo(double latitude, double longitude, int size) {
        //aMap = mMapView.getMap();//得到aMap对象
        LatLng latLng = new LatLng(latitude, longitude);//构造一个位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, size));
        if(flag){
            drawFlowRate(latitude,longitude,3);
        }
        flag = true;
    }

    /**
     * 实现定位蓝点
     */
    private void initPositionDot() {
        MyLocationStyle myLocationStyle;
        //1  初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //   连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        //  （1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //2  设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(3000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        //3  设置定位圆圈
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        //4  设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        //5  设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
    }

    /**
     * 通过地址查询此处的信息
     */
    private void moveToPosition(String address, String city) {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new MyGeocodeSearchListener());
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(address, city);

        geocoderSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 绘制热力图,检测人流量变化
     */
    private void drawFlowRate(double x, double y, int number) {
        //生成热力点坐标列表
        LatLng[] latlngs = new LatLng[number];

        for (int i = 0; i < number; i++) {
            double x_ = 0;
            double y_ = 0;
            x_ = Math.random() * 0.0005 - 0.00025;
            y_ = Math.random() * 0.0005 - 0.00025;
            latlngs[i] = new LatLng(x + x_, y + y_);
        }

        // 构建热力图 HeatmapTileProvider
        HeatmapTileProvider.Builder builder = new HeatmapTileProvider.Builder();
        builder.data(Arrays.asList(latlngs)) // 设置热力图绘制的数据
                .gradient(DEFAULT_GRADIENT); // 设置热力图渐变，有默认值 DEFAULT_GRADIENT，可不设置该接口
        // Gradient 的设置可见参考手册
        // 构造热力图对象
        HeatmapTileProvider heatmapTileProvider = builder.build();

        // 初始化 TileOverlayOptions
        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
        tileOverlayOptions.tileProvider(heatmapTileProvider); // 设置瓦片图层的提供者
        // 向地图上添加 TileOverlayOptions 类对象
        aMap.addTileOverlay(tileOverlayOptions);
    }


    /***
     *  初始化点击景物识别后显示的对话框
     */
    private void initSelectBox() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //反射一个自定义的全新的对话框布局
        View view = inflater.inflate(R.layout.photo_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        //在当前布局中找到控件对象
        take_photo = view.findViewById(R.id.take_photo_dialog);
        chosen_photo = view.findViewById(R.id.chosen_photo_dialog);
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
        headIconUri = Uri.fromFile(file);

        //进行图片剪切
        int size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(true).create();  //true表示使用TakePhoto自带的裁剪工具
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
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {    //已经全部授权
            requestLocation();   //开始进行定位
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
                    //requestLocation();   //权限已经被全部授权
                } else {
                    Toast.makeText(MainActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
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
     * 点击List后显示的popupMenu
     */
    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper helper = (MenuPopupHelper) field.get(popupMenu);
            helper.setForceShowIcon(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.take_photo:
                        dialog.show();
                        break;
                    case R.id.road_sign:

                        break;
                    case R.id.feedback:

                        break;
                }
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });

        popupMenu.show();
    }

    /**
     * 拍照成功回调
     */
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

    /**
     * 地理编码接口
     */
    private class MyGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {
        /**
         * 通过经纬度获取地址
         */
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        }

        /**
         * 通过地址获取经纬度等
         */
        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            if (1000 == i) {   //查询成功
                if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
                        geocodeResult.getGeocodeAddressList().size() > 0) {

                    GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                    double latitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                    double longititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                    navigateTo(latitude,longititude,16);
                } else {
                    Toast.makeText(MainActivity.this, "地址名出错了!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 点击事件
     */
    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_photo_dialog:     //点击拍照
                    //相机获取照片并剪裁
                    takePhoto.onPickFromCaptureWithCrop(headIconUri, cropOptions);
                    dialog.dismiss();
                    break;
                case R.id.chosen_photo_dialog:   //点击相册
                    //相册获取照片并剪裁
                    takePhoto.onPickFromGalleryWithCrop(headIconUri, cropOptions);
                    // takePhoto.onPickFromGallery();
                    dialog.dismiss();
                    break;
                case R.id.user_main:      //点击头像
                    Intent intent_user = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent_user);
                    break;
                case R.id.list_main:      //点击右上角列表
                    showPopupMenu(v);
                    break;
                case R.id.top_search:     //点击搜索框
                    Intent intent_search = new Intent(MainActivity.this, SearchActivity.class);
                    startActivityForResult(intent_search, 1);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    double latitudeData = data.getDoubleExtra("latitude", 0);
                    double longitudeData = data.getDoubleExtra("longitude", 0);
                    navigateTo(latitudeData, longitudeData, 16);
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

}
