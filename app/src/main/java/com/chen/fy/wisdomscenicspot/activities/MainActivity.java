package com.chen.fy.wisdomscenicspot.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chen.fy.wisdomscenicspot.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.amap.api.maps.model.HeatmapTileProvider.DEFAULT_GRADIENT;

public class MainActivity extends AppCompatActivity {

    /**
     * 地图
     */
    MapView mMapView = null;
    /**
     * 地图控制器
     */
    private AMap aMap;
    /**
     * 当前地图显示位置的经纬度和位置
     */
    private double nowLatitude;
    private double nowLongitude;
    private String nowLocation = "";

    /**
     * 路线规划控件
     */
    private RelativeLayout road_sign_box;
    private EditText ed_myLocation;
    private EditText ed_targetLocation;
    private ImageView iv_road_sign_logo;
    /**
     * 路线规划控件是否可见,默认不可见
     */
    private boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //初始化view并设置接口
        initView(savedInstanceState);

        //设置路线规划空间是否显示
        setRoadSignVisible();

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

        road_sign_box = findViewById(R.id.road_sign_box);
        ed_myLocation = findViewById(R.id.road_sign_my_location);
        ed_targetLocation = findViewById(R.id.road_sign_target_location);
        iv_road_sign_logo = findViewById(R.id.road_sign_start_logo);

        /*
         * 顶部控件
         */
        ImageView iv_user = findViewById(R.id.user_main);
        ImageView iv_list = findViewById(R.id.list_main);
        TextView tv_search = findViewById(R.id.top_search);

        /**
         * 地图上各种功能按钮
         */
        ImageView iv_camera = findViewById(R.id.iv_camera);
        ImageView iv_road_sign = findViewById(R.id.iv_road_sign);
        ImageView iv_feedback = findViewById(R.id.iv_feedback);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        iv_list.setOnClickListener(myOnClickListener);
        iv_user.setOnClickListener(myOnClickListener);
        tv_search.setOnClickListener(myOnClickListener);

        iv_camera.setOnClickListener(myOnClickListener);
        iv_road_sign.setOnClickListener(myOnClickListener);
        iv_feedback.setOnClickListener(myOnClickListener);

    }

    /**
     * 设置路线规划的起点和终点
     */
    private void setLocation() {
        if(!nowLocation.isEmpty()) {
            ed_myLocation.setText(nowLocation);
        }
    }

    /**
     * 控件交互,手势交互
     */
    private void initUiSettings() {
        /*
      实现控件交互,手势交互等
     */
        UiSettings mUiSettings = aMap.getUiSettings();

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
       // aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        //一开始把地图镜头设置在象山景区
        navigateTo(25.267222,110.294322);
        //initPosition();
    }

    /**
     * 初始化定位
     */
    private void initPosition() {
        /*
         * 定位
         */
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        //navigateTo(aMapLocation.getLatitude(), aMapLocation.getLongitude(), 16);
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
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);

        mLocationClient.setLocationOption(mLocationOption);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
        mLocationClient.startLocation();

    }

    boolean flag = false;

    /**
     * 根据经纬度移动到某一个位置并显示
     */
    private void navigateTo(double latitude, double longitude) {
        nowLatitude = latitude;
        nowLongitude = longitude;
        //aMap = mMapView.getMap();//得到aMap对象
        LatLng latLng = new LatLng(latitude, longitude);//构造一个位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        if (flag) {
            drawFlowRate(latitude, longitude, 3);
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
    private void addressQuery(String address, String city) {
        /*
         * 地址转化为坐标
         */
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new MyGeocodeSearchListener());
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(address, city);

        geocoderSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 通过经纬度查询此处的信息
     */
    private void latitudeAndLongitudeQuery(double latitude, double longitude) {
        /*
         * 经纬度
         */
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new MyGeocodeSearchListener());
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 绘制线
     */
    private void drawLine(double latitude, double longitude) {
        //生成热力点坐标列表
        List<LatLng> latLngs = new ArrayList<LatLng>();
        for (int i = 0; i < 4; i++) {
            double x_ = 0;
            double y_ = 0;
            x_ = Math.random() * 0.005 - 0.0025;
            y_ = Math.random() * 0.005 - 0.0025;
            latLngs.add(new LatLng(latitude + x_, longitude + y_));
        }
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(5).color(Color.argb(255, 1, 1, 1)));
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
    }

    /**
     * 点击List后显示的popupMenu菜单
     */
    @SuppressLint("RestrictedApi")
    private void showListPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.list_menu, popupMenu.getMenu());

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
                        Intent intent1 = new Intent(MainActivity.this, ScenicIdentifyActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.road_sign:
                        isVisible = !isVisible;
                        setRoadSignVisible();
                        drawLine(nowLatitude, nowLongitude);
                        break;
                    case R.id.feedback:
                        Intent intent2 = new Intent(MainActivity.this, FeedbackActivity.class);
                        startActivity(intent2);
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
     * 设置路线规划控件是否可见
     * visible:可见    invisible:不可见，但保留所占有的空间   gone:不可见，不保留空间
     */
    private void setRoadSignVisible() {
        if (isVisible) {   //可见
            road_sign_box.setVisibility(View.GONE);
            ed_myLocation.setVisibility(View.GONE);
            ed_targetLocation.setVisibility(View.GONE);
            // iv_road_sign_logo.setVisibility(View.GONE);
        } else {
            road_sign_box.setVisibility(View.VISIBLE);
            ed_myLocation.setVisibility(View.VISIBLE);
            ed_targetLocation.setVisibility(View.VISIBLE);
            //iv_road_sign_logo.setVisibility(View.VISIBLE);
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
            if (1000 == i) {   //成功
                String address = regeocodeResult.getRegeocodeAddress().getDistrict();
            }
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
                    navigateTo(latitude, longititude);
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
                case R.id.user_main:      //点击头像
                    Intent intent_user = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent_user);
                    break;
                case R.id.list_main:      //点击右上角列表
                    showListPopupMenu(v);
                    break;
                case R.id.top_search:     //点击搜索框
                    Intent intent_search = new Intent(MainActivity.this, SearchActivity.class);
                    startActivityForResult(intent_search, 1);
                    break;
                case R.id.iv_camera:
                    Toast.makeText(MainActivity.this, "景物识别", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(MainActivity.this, ScenicIdentifyActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.iv_road_sign:
                    Toast.makeText(MainActivity.this, "路线规划", Toast.LENGTH_SHORT).show();
                    isVisible = !isVisible;
                    setLocation();
                    setRoadSignVisible();
                    //drawLine(nowLatitude, nowLongitude);
                    break;
                case R.id.iv_feedback:
                    Toast.makeText(MainActivity.this, "景区反馈", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(MainActivity.this, FeedbackActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.road_sign_start_logo:
                    Toast.makeText(MainActivity.this, "开始路线规划", Toast.LENGTH_SHORT).show();
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
                    nowLocation = data.getStringExtra("nowLocation");
                    navigateTo(latitudeData, longitudeData);
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
