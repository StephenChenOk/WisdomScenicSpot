package com.chen.fy.wisdomscenicspot.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.beans.JobSchedulingInfo;
import com.chen.fy.wisdomscenicspot.consts.Consts;
import com.chen.fy.wisdomscenicspot.utils.DateUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static com.amap.api.maps.model.HeatmapTileProvider.DEFAULT_GRADIENT;

public class MainActivity extends AppCompatActivity {

    private final String Tag = "MainActivity";

    /**
     * 跳转查询地点活动的请求吗
     */
    private final int SEARCH_REQUEST_CODE = 1;
    /**
     * 跳转工作调度活动的请求吗
     */
    private final int JOB_SCHEDULING_CODE = 2;

    /**
     * 进行与服务端的实时数据交流
     */
    private WebSocket webSocket;

    /**
     * 点击接口
     */
    private MyOnClickListener myOnClickListener;

    /**
     * 部分景点的人数,普贤塔,桂林抗战遗址,象眼岩
     */
    private int[] numbers = new int[3];
    private String[] locations = new String[3];

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
    /**
     * 路线规划控件是否可见,默认不可见
     */
    private boolean isVisible = false;
    /**
     * 各个景点的Maker
     */
    private Marker marker1;   //普贤塔
    private Marker marker2;   //桂林抗战遗址
    private Marker marker3;   //象眼岩
    private Circle circle1;
    private Circle circle2;
    private Circle circle3;
    /**
     * 当前的登入状态，游客端/管理员端
     */
    private int loginType;
    /**
     * 管理员端工作调度控件
     */
    private LinearLayout job_scheduling_box_main;
    /**
     * 管理员端工作调度弹窗,以及控件
     */
    private AlertDialog jobSchedulingDialog;
    private TextView title_job_scheduling_dialog_tv;
    private Button receive_job_scheduling_dialog_btn;


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

        //绘制Marker
        drawMarker();

        //webSocket连接
        webSocketConnect();

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
        ImageView iv_road_sign_logo = findViewById(R.id.road_sign_start_logo);
        TextView tv_road_sign_go = findViewById(R.id.road_sign_go);
        job_scheduling_box_main = findViewById(R.id.job_scheduling_box_main);

        /*
         * 顶部控件
         */
        ImageView iv_user = findViewById(R.id.user_main);
        ImageView iv_list = findViewById(R.id.list_main);
        TextView tv_search = findViewById(R.id.top_search);

        /*
         * 地图上各种功能按钮
         */
        ImageView iv_camera = findViewById(R.id.iv_camera);
        ImageView iv_road_sign = findViewById(R.id.iv_road_sign);
        ImageView iv_feedback = findViewById(R.id.iv_feedback);

        if (myOnClickListener == null) {
            myOnClickListener = new MyOnClickListener();
        }
        iv_list.setOnClickListener(myOnClickListener);
        iv_user.setOnClickListener(myOnClickListener);
        tv_search.setOnClickListener(myOnClickListener);
        iv_road_sign_logo.setOnClickListener(myOnClickListener);
        tv_road_sign_go.setOnClickListener(myOnClickListener);

        iv_camera.setOnClickListener(myOnClickListener);
        iv_road_sign.setOnClickListener(myOnClickListener);
        iv_feedback.setOnClickListener(myOnClickListener);

        job_scheduling_box_main.setOnClickListener(myOnClickListener);

        //初始化工作调度弹窗
        initJobSchedulingDialog();
    }

    /**
     * 获取之前的登入状态
     */
    private void getLoginState() {
        SharedPreferences preferences = getSharedPreferences("login_state", MODE_PRIVATE);
        //userId = preferences.getString("userId", "");
        loginType = preferences.getInt("loginType", -1);
        Log.d(Tag, "------>" + String.valueOf(loginType));
    }

    /**
     * 设置路线规划的起点和终点
     */
    private void setLocation() {
        if (!nowLocation.isEmpty()) {
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
        //一开始把地图镜头设置在太平天国纪念馆
        navigateTo(25.266431, 110.295181);
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
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        if (flag) {
            //drawFlowRate(latitude, longitude, 3);
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
     * 绘制点标记,普贤塔，桂林抗战遗址，象眼岩
     */
    private void drawMarker() {
        //Marker点击接口
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            boolean flag = false;

            // marker 对象被点击时回调的接口
            // 返回true 表示接口已经相应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (flag) {
                    marker.showInfoWindow();
                } else {
                    marker.hideInfoWindow();
                }
                flag = !flag;
                return true;
            }
        };
        //点击Marker后弹出的信息接口
        AMap.OnInfoWindowClickListener infoWindowClickListener = new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        };

        // 1 普贤塔
        LatLng latLng1 = new LatLng(25.267242, 110.296046);
        marker1 = aMap.addMarker(new MarkerOptions().
                position(latLng1).title("普贤塔").snippet("人流量：10").visible(true));
        // 2 象眼岩
        LatLng latLng2 = new LatLng(25.267088, 110.296427);
        marker2 = aMap.addMarker(new MarkerOptions().
                position(latLng2).title("象眼岩").snippet("人流量：30").visible(true));
        // 2 桂林抗战遗址
        LatLng latLng3 = new LatLng(25.266798, 110.295988);
        marker3 = aMap.addMarker(new MarkerOptions().
                position(latLng3).title("桂林抗战遗址").snippet("人流量：20").visible(true));

        //设置接口
        aMap.setOnMarkerClickListener(markerClickListener);
        aMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }

    /**
     * 路线规划，从 云峰寺--->至向山景区
     */
    private void drawLine() {
        //得到最少的人数
        int temp = numbers[0];
        int minFlag = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] < temp) {
                temp = numbers[i];
                minFlag = i;
            }
        }
        //生成路线定点坐标列表
        List<LatLng> latLngs = new ArrayList<>();
        //road1(latLngs);

        switch (locations[minFlag]) {
            case "tower":
                road1(latLngs);

                break;
            case "ruins":
                road2(latLngs);

                break;
            case "rock":
                road3(latLngs);

                break;
        }

        aMap.addPolyline(new PolylineOptions()
                .addAll(latLngs)
                .width(10)
                .setUseTexture(true)       //使用纹理图
                .setCustomTexture
                        (BitmapDescriptorFactory.fromResource(R.drawable.road)));
        //        aMap.addPolyline(new PolylineOptions().
        //               addAll(latLngs).width(5).color(Color.argb(255, 1, 1, 1)));
        latLngs.clear();
        new_road1(latLngs);
        moveLocation(latLngs);
    }

    /**
     * 醉乡-->至象山景区路线1，途经普贤塔
     *
     * @param latLngs 经过的定点坐标集合
     */
    private void road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng1 = new LatLng(25.266453, 110.295620);      //地标1
        LatLng latLng2 = new LatLng(25.266413, 110.295356);      //地标2
        LatLng latLng3 = new LatLng(25.266510, 110.295420);      //地标3
        LatLng latLng4 = new LatLng(25.266820, 110.295275);      //地标3
        LatLng latLng5 = new LatLng(25.266950, 110.295231);      //地标4
        LatLng latLng6 = new LatLng(25.267153, 110.295631);      //地标5
        LatLng latLng7 = new LatLng(25.267168, 110.295691);      //地标6
        LatLng latLng8 = new LatLng(25.267109, 110.295895);      //地标7
        LatLng latLng9 = new LatLng(25.267157, 110.296042);       //地标8
        LatLng latLng10 = new LatLng(25.267242, 110.296046);      //地标10

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
    }
    /**
     * 醉乡-->至象山景区路线1，途经普贤塔, 导航用
     *
     * @param latLngs 经过的定点坐标集合
     */
    private void new_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng1 = new LatLng(25.266453, 110.295620);      //地标1
        LatLng latLng2 = new LatLng(25.266400, 110.295345);      //地标2
        LatLng latLng3 = new LatLng(25.266490, 110.295430);      //地标3
        LatLng latLng4 = new LatLng(25.266570, 110.295390);      //地标3
        LatLng latLng5 = new LatLng(25.266700, 110.295320);      //地标3
        LatLng latLng6 = new LatLng(25.266820, 110.295284);      //地标3
        LatLng latLng7 = new LatLng(25.266963, 110.295227);      //地标4
        LatLng latLng8 = new LatLng(25.267153, 110.295631);      //地标5
        LatLng latLng9 = new LatLng(25.267164, 110.295690);      //地标6
        LatLng latLng10 = new LatLng(25.267107, 110.295890);      //地标7
        LatLng latLng11 = new LatLng(25.267166, 110.296050);       //地标8
        LatLng latLng12 = new LatLng(25.267230, 110.296046);      //地标10

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
    }

    /**
     * 醉乡-->至象山景区路线2，途经桂林抗战遗址
     *
     * @param latLngs 经过的定点坐标集合
     */
    private void road2(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng1 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng2 = new LatLng(25.266453, 110.295620);      //地标2
        LatLng latLng3 = new LatLng(25.266440, 110.295681);      //地标3
        LatLng latLng4 = new LatLng(25.266463, 110.295721);      //地标4
        LatLng latLng5 = new LatLng(25.266735, 110.295805);      //地标5
        LatLng latLng6 = new LatLng(25.266745, 110.295845);      //地标6
        LatLng latLng7 = new LatLng(25.266745, 110.296030);      //地标6
        LatLng latLng8 = new LatLng(25.266930, 110.296095);     //地标7
        LatLng latLng9 = new LatLng(25.267160, 110.296300);     //地标8
        LatLng latLng10 = new LatLng(25.267157, 110.296042);       //地标9
        LatLng latLng11 = new LatLng(25.267242, 110.296046);      //普贤塔

        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
    }

    /**
     * 醉乡-->至象山景区路线1，途经象眼岩
     *
     * @param latLngs 经过的定点坐标集合
     */
    private void road3(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng1 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng2 = new LatLng(25.266370, 110.295597);      //地标4
        LatLng latLng3 = new LatLng(25.266387, 110.296083);      //地标5
        LatLng latLng4 = new LatLng(25.266425, 110.296220);      //地标6
        LatLng latLng5 = new LatLng(25.266415, 110.296490);      //地标7
        LatLng latLng6 = new LatLng(25.266460, 110.296535);      //地标8
        LatLng latLng7 = new LatLng(25.266793, 110.296583);      //地标9
        LatLng latLng8 = new LatLng(25.267015, 110.296490);     //地标10
        LatLng latLng9 = new LatLng(25.267215, 110.296620);     //地标11
        LatLng latLng10 = new LatLng(25.267370, 110.296630);     //地标12
        LatLng latLng11 = new LatLng(25.267300, 110.296520);     //地标13
        LatLng latLng12 = new LatLng(25.267240, 110.296480);     //地标13
        LatLng latLng13 = new LatLng(25.267160, 110.296300);     //地标13
        LatLng latLng14 = new LatLng(25.267157, 110.296042);       //地标9
        LatLng latLng15 = new LatLng(25.267242, 110.296046);      //普贤塔

        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
        latLngs.add(latLng14);
        latLngs.add(latLng15);
    }

    private void moveLocation(List<LatLng> points){

        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.locations_1));

        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());
        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList);
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(20);
        // 开始滑动
        smoothMarker.startSmoothMove();
    }

    /**
     * 根据人流量变化画出显示人流量多少的圆形
     */
    private void drawFlowRateCircle_Marker(String nowLocation, int number) {
        int radius = 15;   //人流量圆的半径
        int circleNumber;
        if (number <= 5) {
            circleNumber = 10;
        } else if (number <= 15) {
            circleNumber = 30;
        } else if (number <= 30) {
            radius = 17;
            circleNumber = 50;
        } else if (number <= 80) {
            radius = 18;
            circleNumber = 80;
        } else if (number <= 110) {
            radius = 19;
            circleNumber = 110;
        } else if (number <= 140) {
            radius = 20;
            circleNumber = 140;
        } else if (number <= 170) {
            radius = 21;
            circleNumber = 170;
        } else if (number <= 200) {
            radius = 22;
            circleNumber = 200;
        } else if (number <= 230) {
            radius = 23;
            circleNumber = 230;
        } else {
            radius = 24;
            circleNumber = 255;
        }
        switch (nowLocation) {
            case "tower":       //普贤塔
                marker1.setSnippet("人流量：" + number);
                if (circle1 != null) {
                    circle1.remove();
                }
                LatLng latLng1 = new LatLng(25.267242, 110.296046);
                circle1 = aMap.addCircle(new CircleOptions()
                        .center(latLng1)
                        .radius(radius)
                        .fillColor(Color.argb(circleNumber, 255, 0, 0))
                        .strokeColor(Color.argb(0, 0, 0, 0)));
                break;
            case "rock":            //象眼岩
                marker2.setSnippet("人流量：" + number);
                if (circle2 != null) {
                    circle2.remove();
                }
                LatLng latLng2 = new LatLng(25.267088, 110.296427);
                circle2 = aMap.addCircle(new CircleOptions()
                        .center(latLng2)
                        .radius(radius)
                        .fillColor(Color.argb(circleNumber, 255, 0, 0))
                        .strokeColor(Color.argb(0, 0, 0, 0)));

                break;
            case "ruins":       //桂林抗战遗址
                marker3.setSnippet("人流量：" + number);
                if (circle3 != null) {
                    circle3.remove();
                }
                LatLng latLng3 = new LatLng(25.266798, 110.295988);
                circle3 = aMap.addCircle(new CircleOptions()
                        .center(latLng3)
                        .radius(radius)
                        .fillColor(Color.argb(circleNumber, 255, 0, 0))
                        .strokeColor(Color.argb(0, 0, 0, 0)));
                break;
        }
    }

    /**
     * 绘制热力图,检测人流量变化
     */
    private void drawFlowRate(double x, double y, int number) {
        //生成热力点坐标列表
        int pointNumbers = number / 7;
        LatLng[] latlngs = new LatLng[pointNumbers];

        for (int i = 0; i < pointNumbers; i++) {
            double x_ = 0;
            double y_ = 0;
            x_ = Math.random() * 0.00024 - 0.00012;
            y_ = Math.random() * 0.00024 - 0.00012;
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
        aMap.removecache();

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
                        //drawLine(nowLatitude, nowLongitude);
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
            road_sign_box.setVisibility(View.VISIBLE);
        } else {
            road_sign_box.setVisibility(View.GONE);
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
     * 实现客户端与服务端的实时连接，
     * 可以不断的进行数据交流，服务端可以自主的向客户端发送消息，不用客户端每次都进行请求
     */
    private final class MyWebSocketListener extends WebSocketListener {

        /**
         * 当WebSocket和远端服务器建立连接成功后回调
         */
        @Override
        public void onOpen(WebSocket webSocket, Response response) {

        }

        /**
         * 当接受到服务端传来的信息时回调，消息内容为String类型
         */
        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            //根据服务器发送的信息对地图进行实时更新
            Log.d(Tag, text);
            new Thread() {
                @Override
                public void run() {
                    updateMap(text);
                }
            }.start();
        }

        /**
         * 当接受到服务端传来的信息时回调，消息内容为ByteString类型
         */
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }

        /**
         * 当服务端暗示没有数据交互时回调（即此时准备关闭，但连接还没有关闭）
         */
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000, null);
            Log.d(Tag, "正在关闭");

        }

        /**
         * 当连接已经释放的时候被回调
         */
        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.d(Tag, "关闭");
        }

        /**
         * 失败时被回调（包括连接失败，发送失败等）。
         */
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

            Log.d(Tag, "连接失败");
        }
    }

    /**
     * webSocket连接
     */
    private void webSocketConnect() {
        MyWebSocketListener webSocketListener = new MyWebSocketListener();
        Request request = new Request.Builder()
                .url(Consts.WEBSOCKET_URL)
                .build();
        OkHttpClient client = new OkHttpClient();
        webSocket = client.newWebSocket(request, webSocketListener);

        client.dispatcher().executorService().shutdown();
    }

    /**
     * 通过webSocket获取服务端传来的信息后在地图上进行实时更新
     *
     * @param text 人数信息
     */
    private boolean is_show_dialog = true;
    private void updateMap(final String text) {
        //Log.d("updateMap",text);
        int i, j = 0, k = 0;   //循环参数
        //格式：  地点/人数/地点/人数
        String[] s = text.split(",");
        for (i = 0; i < 6; i++) {
            if (i % 2 == 0) {    //地点获取
                locations[j] = s[i];
                j++;
            } else {
                numbers[k] = Integer.parseInt(s[i]);
                k++;
            }
        }

        //跳到UI线程进行UI操作
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //获取登入状态
                getLoginState();
                //各个景区人流量显示
                for (int t = 0; t < locations.length; t++) {
                    //画出人流量圆形
                    drawFlowRateCircle_Marker(locations[t], numbers[t]);
                    if (2 == loginType && numbers[t] >= 10 && is_show_dialog) {
                        is_show_dialog = false;
                        JobSchedulingInfo info = new JobSchedulingInfo();
                        long time = System.currentTimeMillis();
                        switch (locations[t]) {
                            case "tower":
                                title_job_scheduling_dialog_tv.setText("普贤塔:出现人流量过多情况，请工作人员前去引导疏通");
                                info.setDate(DateUtils.dateToString(time));
                                info.setAddress("普贤塔");
                                info.setTitle("出现人流量过多情况，请工作人员前去引导疏通");
                                info.setLatitude(25.267242);
                                info.setLongitude(110.296046);
                                break;
                            case "rock":
                                title_job_scheduling_dialog_tv.setText("象眼岩:出现人流量过多情况，请工作人员前去引导疏通");
                                info.setDate(DateUtils.dateToString(time));
                                info.setAddress("象眼岩");
                                info.setTitle("出现人流量过多情况，请工作人员前去引导疏通");
                                info.setLatitude(25.267088);
                                info.setLongitude(110.296427);
                                break;
                            case "ruins":
                                title_job_scheduling_dialog_tv.setText("桂林抗战景区:出现人流量过多情况，请工作人员前去引导疏通");
                                info.setDate(DateUtils.dateToString(time));
                                info.setTitle("桂林抗战景区");
                                info.setAddress("出现人流量过多情况，请工作人员前去引导疏通");
                                info.setLatitude(25.266798);
                                info.setLongitude(110.295988);
                                break;
                        }
                        jobSchedulingDialog.show();
                    }
                }
            }
        });

    }


    /**
     * 初始化工作调度弹窗
     */
    private void initJobSchedulingDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //反射一个自定义的全新的对话框布局
        View view = inflater.inflate(R.layout.job_scheduling_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        jobSchedulingDialog = builder.create();
        //设置不可以点击弹窗之后进行取消
        jobSchedulingDialog.setCanceledOnTouchOutside(false);
        //在当前布局中找到控件对象
        title_job_scheduling_dialog_tv = view.findViewById(R.id.title_job_scheduling_dialog);
        receive_job_scheduling_dialog_btn = view.findViewById(R.id.receive_job_scheduling_dialog_btn);

        //监听事件
        receive_job_scheduling_dialog_btn.setOnClickListener(myOnClickListener);
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
                    isVisible = false;   //返回界面时不显示布局
                    Intent intent_search = new Intent(MainActivity.this, SearchActivity.class);
                    startActivityForResult(intent_search, SEARCH_REQUEST_CODE);
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
                    break;
                case R.id.iv_feedback:
                    Toast.makeText(MainActivity.this, "景区反馈", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(MainActivity.this, FeedbackActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.road_sign_start_logo:    //开始进行路线规划
                case R.id.road_sign_go:
                    //获取终点的位置
                    String targetLocation = ed_targetLocation.getText().toString();
                    //开始进行路线规划
                    drawLine();
                    Toast.makeText(MainActivity.this, "开始路线规划", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.job_scheduling_box_main:   //工作调度
                    Intent intent_job_scheduling = new Intent(MainActivity.this, JobSchedulingActivity.class);
                    startActivityForResult(intent_job_scheduling, JOB_SCHEDULING_CODE);
                    break;
                case R.id.title_job_scheduling_dialog: //工作调度弹窗详细情况说明

                    break;
                case R.id.receive_job_scheduling_dialog_btn:    //工作调度弹窗收到按钮
                    addJobScheduling();
                    jobSchedulingDialog.dismiss();
                    break;
            }
        }
    }

    /**
     * 增加工作调度安排
     */
    private void addJobScheduling() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SEARCH_REQUEST_CODE:    //从查询活动中返回
                if (resultCode == RESULT_OK) {
                    double latitudeData = data.getDoubleExtra("latitude", 0);
                    double longitudeData = data.getDoubleExtra("longitude", 0);
                    Log.d(Tag, String.valueOf(latitudeData));
                    Log.d(Tag, String.valueOf(longitudeData));
                    nowLocation = data.getStringExtra("nowLocation");
                    navigateTo(latitudeData, longitudeData);
                }
                break;
            case JOB_SCHEDULING_CODE:   //从工作调度活动中返回
                if (resultCode == RESULT_OK) {
                    double latitudeData = data.getDoubleExtra("latitude", 0);
                    double longitudeData = data.getDoubleExtra("longitude", 0);
                    Log.d(Tag, String.valueOf(latitudeData));
                    Log.d(Tag, String.valueOf(longitudeData));
                    navigateTo(latitudeData, longitudeData);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        //关闭webSocket连接
        webSocket.close(1000, "再见");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次重新进入当前的界面时判断路线规划布局是否应该被显示
        setRoadSignVisible();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        Log.d(Tag, "onResume");
        //获取当前登入状态
        getLoginState();
        //对不同的登录状态进行不同操作
        switch (loginType) {
            case 2:    //显示管理者
                job_scheduling_box_main.setVisibility(View.VISIBLE);   //显示 呼叫云端 功能
                break;
            default:   //当loginType不为2时显示常规界面
                job_scheduling_box_main.setVisibility(View.GONE);
                break;
        }
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
