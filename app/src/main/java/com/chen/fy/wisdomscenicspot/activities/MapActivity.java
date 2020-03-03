package com.chen.fy.wisdomscenicspot.activities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amap.api.maps.model.Polyline;
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
import com.chen.fy.wisdomscenicspot.consts.Consts;
import com.chen.fy.wisdomscenicspot.utils.AStarUtils;
import com.chen.fy.wisdomscenicspot.utils.RoadPlanningUtils;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;

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

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OPEN_BLUE_TOOTH = 1;
    private static final int REQUEST_CODE_OPEN_GPS = 2;
    private final String Tag = "MapActivity";

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

    /**
     * 路线规划控件
     */
    private RelativeLayout road_sign_box;
    private EditText ed_myLocation;
    private EditText ed_targetLocation;
    private String targetLocation;
    private String myLocation;

    //路径规划路线
    private Polyline polyline;
    private Marker endMarker;   //终点标志
    private Marker startMarker;   //起点标志

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
     * 室内定位
     */
    private LinearLayout indoor_position_box;
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化view并设置接口
        initView(savedInstanceState);

        //设置路线规划空间是否显示
        setRoadSignVisible();

        //申请权限
        requestLocation();

        //绘制Marker
        drawMarker();

        //webSocket连接
        webSocketConnect();

        //发消息
        //sendMessage("22");
    }

    /**
     * 初始化view
     */
    private void initView(Bundle savedInstanceState) {

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this, true);//将状态栏字体变为黑色

        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        road_sign_box = findViewById(R.id.road_sign_box);
        ed_targetLocation = findViewById(R.id.road_sign_target_location);
        ed_myLocation = findViewById(R.id.road_sign_my_location);
        ImageView iv_road_sign_logo = findViewById(R.id.road_sign_start_logo);
        TextView tv_road_sign_go = findViewById(R.id.road_sign_go);
        indoor_position_box = findViewById(R.id.indoor_position_box);

        /*
         * 地图上各种功能按钮
         */
        ImageView iv_camera = findViewById(R.id.iv_camera);
        ImageView iv_road_sign = findViewById(R.id.iv_road_sign);
        ImageView iv_feedback = findViewById(R.id.iv_feedback);

        if (myOnClickListener == null) {
            myOnClickListener = new MyOnClickListener();
        }
        iv_road_sign_logo.setOnClickListener(myOnClickListener);
        tv_road_sign_go.setOnClickListener(myOnClickListener);

        iv_camera.setOnClickListener(myOnClickListener);
        iv_road_sign.setOnClickListener(myOnClickListener);
        iv_feedback.setOnClickListener(myOnClickListener);

        ed_myLocation.setOnClickListener(myOnClickListener);
        ed_targetLocation.setOnClickListener(myOnClickListener);

        indoor_position_box.setOnClickListener(myOnClickListener);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_targetLocation.getWindowToken(), 0);
    }

    /**
     * 控件交互,手势交互
     */
    private void initUiSettings() {

        //实现控件交互,手势交互等
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

        initPositionDot();
        initUiSettings();
        //设置地图的放缩级别
        // aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        if (getIntent() != null) {
            double latitude = getIntent().getDoubleExtra("Latitude", 25.266431);
            double longitude = getIntent().getDoubleExtra("Longitude", 110.295181);
            navigateTo(latitude, longitude);
        }
        aMap.showIndoorMap(true);
    }

    /**
     * 根据经纬度移动到某一个位置并显示
     */
    private void navigateTo(double latitude, double longitude) {
        nowLatitude = latitude;
        nowLongitude = longitude;
        LatLng latLng = new LatLng(latitude, longitude);//构造一个位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
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
     * 路线规划
     */
    private void drawLine() {
        //生成路线定点坐标列表
        List<LatLng> latLngs = new ArrayList<>();

        //RoadPlanningUtils.zxToRock_road1(latLngs);

        switch (AStarUtils.A_star(numbers, myLocation, targetLocation)) {
            case RoadPlanningUtils.ROAD_1:
                RoadPlanningUtils.zxToTown_road1(latLngs);
                end = "普贤塔";
                break;
            case RoadPlanningUtils.ROAD_2:
                RoadPlanningUtils.zxToTown_road2(latLngs);
                end = "普贤塔";
                break;
            case RoadPlanningUtils.ROAD_3:
                RoadPlanningUtils.zxToTown_road3(latLngs);
                end = "普贤塔";
                break;
            case RoadPlanningUtils.ROAD_4:
                RoadPlanningUtils.zxToRock_road1(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_5:
                RoadPlanningUtils.zxToRock_road2(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_6:
                RoadPlanningUtils.zxToRock_road3(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_7:
                RoadPlanningUtils.zxToRuins_road1(latLngs);
                end = "桂林抗战遗址";
                break;
            case RoadPlanningUtils.ROAD_8:
                RoadPlanningUtils.yfsToTown_road1(latLngs);
                end = "普贤塔";
                break;
            case RoadPlanningUtils.ROAD_9:
                RoadPlanningUtils.yfsToTown_road3(latLngs);
                end = "普贤塔";
                break;
            case RoadPlanningUtils.ROAD_10:
                RoadPlanningUtils.yfsToRock_road1(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_11:
                RoadPlanningUtils.yfsToRock_road2(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_12:
                RoadPlanningUtils.yfsToRock_road3(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_13:
                RoadPlanningUtils.yfsToRuins_road1(latLngs);
                end = "桂林抗战遗址";
                break;
            case RoadPlanningUtils.ROAD_14:
                RoadPlanningUtils.zyToTown_road1(latLngs);
                end = "普贤塔";
                break;
            case RoadPlanningUtils.ROAD_15:
                RoadPlanningUtils.zyToRock_road1(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_16:
                RoadPlanningUtils.zyToRock_road2(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_17:
                RoadPlanningUtils.zyToRock_road3(latLngs);
                end = "象眼岩";
                break;
            case RoadPlanningUtils.ROAD_18:
                RoadPlanningUtils.zyToRuins_road1(latLngs);
                end = "桂林抗战遗址";
                break;
            case RoadPlanningUtils.ROAD_19:
                RoadPlanningUtils.zyToRuins_road3(latLngs);
                end = "桂林抗战遗址";
                break;
            default:
                Toast.makeText(this, "输入错误...", Toast.LENGTH_SHORT).show();
                return;
        }
        if (polyline != null) {
            polyline.remove();
        }
        polyline = aMap.addPolyline(new PolylineOptions()
                .addAll(latLngs)
                .width(10)
                .setUseTexture(true)       //使用纹理图
                .setCustomTexture
                        (BitmapDescriptorFactory.fromResource(R.drawable.road)));
        moveLocation(latLngs);
        // smoothMarker.startSmoothMove();
    }

    /**
     * A*算法实现路径规划
     * 1）f(n)是节点n的综合优先级（节点n到终点的代价）,当我们选择下一个要遍历的节点时，我们总会选取综合优先级最高（值最小）的节点。
     * 2）g(n)是节点n距离终点的实际代价。这里采用两点之间的距离（经纬度）
     * 3）h(n)是节点n距离终点的预计代价，这也就是A*算法的启发函数。
     */

    private String end = "";

    private void moveLocation(List<LatLng> points) {
        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        LatLng latLng_start = points.get(0);
        LatLng latLng_end = points.get(points.size() - 1);
        switch (end) {
            case "普贤塔":
                marker1.setVisible(false);
                break;
            case "象眼岩":
                marker3.setVisible(false);
                break;
            case "桂林抗战遗址":
                marker2.setVisible(false);
                break;
        }
        if (startMarker != null) {
            startMarker.remove();
        }
        startMarker = aMap.addMarker(new MarkerOptions()
                .position(latLng_start).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.start_logo))));
        if (endMarker != null) {
            endMarker.remove();
        }
        endMarker = aMap.addMarker(new MarkerOptions()
                .position(latLng_end).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.end_logo))));

//        smoothMarker = new SmoothMoveMarker(aMap);
//        // 设置滑动的图标
//        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.locations_1));
//
//        LatLng drivePoint = points.get(0);
//        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
//        points.set(pair.first, drivePoint);
//        List<LatLng> subList = points.subList(pair.first, points.size());
//        // 设置滑动的轨迹左边点
//        smoothMarker.setPoints(subList);
//        // 设置滑动的总时间
//        smoothMarker.setTotalDuration(20);
        // 开始滑动
        // smoothMarker.startSmoothMove();
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
     * 点击List后显示的popupMenu菜单
     */
    @SuppressLint("RestrictedApi")
    private void showListPopupMenu_MyLocation(View view) {
        PopupMenu popupMenu = new PopupMenu(MapActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.list_menu_my_location, popupMenu.getMenu());

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
                    case R.id.zx_my_location:
                        ed_myLocation.setText("醉乡");
                        break;
                    case R.id.yfs_my_location:
                        ed_myLocation.setText("云峰寺");
                        break;
                    case R.id.zy_my_location:
                        ed_myLocation.setText("象山钟韵");
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

    @SuppressLint("RestrictedApi")
    private void showListPopupMenu_TargetLocation(View view) {
        PopupMenu popupMenu = new PopupMenu(MapActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.list_menu_target_location, popupMenu.getMenu());

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
                    case R.id.town_target_location:
                        ed_targetLocation.setText("普贤塔");
                        break;
                    case R.id.rock_target_location:
                        ed_targetLocation.setText("象眼岩");
                        break;
                    case R.id.ruins_target_location:
                        ed_targetLocation.setText("桂林抗战遗址");
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
                    Toast.makeText(MapActivity.this, "地址名出错了!", Toast.LENGTH_SHORT).show();
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
                    //根据实时人流量更新地图
                    String[] s = text.split(",");
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
                .url(Consts.RATE_FLOW_URL)
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
                //各个景区人流量显示
                for (int t = 0; t < locations.length; t++) {
                    //画出人流量圆形
                    drawFlowRateCircle_Marker(locations[t], numbers[t]);
                }
            }
        });

    }

    /**
     * 点击事件
     */
    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_camera:
                    Intent intent1 = new Intent(MapActivity.this, ScenicIdentifyActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.iv_road_sign:
                    setRoadLogo();
                    break;
                case R.id.iv_feedback:
                    Intent intent2 = new Intent(MapActivity.this, FeedbackActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.road_sign_target_location:
                    showListPopupMenu_TargetLocation(v);
                    break;
                case R.id.road_sign_my_location:
                    showListPopupMenu_MyLocation(v);
                    break;
                case R.id.road_sign_start_logo:    //开始进行路线规划
                case R.id.road_sign_go:
                    //获取终点的位置
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed_targetLocation.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(ed_myLocation.getWindowToken(), 0);
                    targetLocation = ed_targetLocation.getText().toString();
                    myLocation = ed_myLocation.getText().toString();
                    drawLine();
                    break;
                case R.id.indoor_position_box:   //景物推送
//                    Intent indoorPositionIntent = new Intent(MapActivity.this, IndoorPositionActivity.class);
//                    startActivity(indoorPositionIntent);
                    Toast.makeText(MapActivity.this, "开启景物推送", Toast.LENGTH_SHORT).show();
                    checkPermissions();
                    break;
                case R.id.title_job_scheduling_dialog: //工作调度弹窗详细情况说明

                    break;
            }
        }
    }

    private void checkPermissions() {
        //判断是否已经打开蓝牙
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "请先打开蓝牙...", Toast.LENGTH_LONG).show();
            //1 隐式打开蓝牙
            //mBluetoothAdapter.enable();

            //2 弹出对话框供用户选择是否打开蓝牙
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_OPEN_BLUE_TOOTH);
        } else {
            if (checkGPSIsOpen()) {
                Toast.makeText(this, "开始扫描", Toast.LENGTH_LONG).show();
                initBle();
            } else {
                Toast.makeText(this, "请先打开GPS", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
            }
        }
    }

    /**
     * 检查GPS定位是否已经打开
     */
    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_OPEN_BLUE_TOOTH:   //打开蓝牙
                if (mBluetoothAdapter.isEnabled()) {
                    if (checkGPSIsOpen()) {
                        Toast.makeText(this, "开始扫描", Toast.LENGTH_LONG).show();
                        initBle();
                    } else {
                        Toast.makeText(this, "请先打开GPS", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                    }
                } else {
                    Toast.makeText(this, "请先打开蓝牙", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_CODE_OPEN_GPS:         //打开GPS
                if (checkGPSIsOpen()) {
                    Toast.makeText(this, "开始扫描", Toast.LENGTH_LONG).show();
                    initBle();
                } else {
                    Toast.makeText(this, "请先打开GPS", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private BleManager bleManager;

    private void initBle() {
        bleManager = BleManager.getInstance();
        bleManager.init(getApplication());
        setScanRule();
        //开始扫描
        bleManager.scan(bleScanCallback);
    }

    private void setScanRule() {
        //获取蓝牙设备名称
        //String[] names = {"1836242", "1836157", "1836027"};
        String[] names = {"1836032", "1836072", "1836027"};

        //设置扫描规则
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setScanTimeOut(0)              // 扫描超时时间，可选，默认10秒
                .build();
        bleManager.initScanRule(scanRuleConfig);
    }

    BleScanCallback bleScanCallback = new BleScanCallback() {
        @Override
        public void onScanStarted(boolean success) {
        }

        @Override
        public void onLeScan(BleDevice bleDevice) {
            super.onLeScan(bleDevice);
            if (bleDevice.getName() != null) {
                switch (bleDevice.getName()) {
                    //case "1836242":
                    case "1836072":       //1836242", "1836157", "1836027
                        startPushScenic(bleDevice.getRssi(), "狮岭朝霞");
                        break;
                    case "1836157":
                        startPushScenic(bleDevice.getRssi(), "水晶宫");
                        break;
                    case "1836032":
                        startPushScenic(bleDevice.getRssi(), "红罗宝帐");
                        break;
                }
            }
        }

        @Override
        public void onScanning(final BleDevice bleDevice) {
        }

        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
        }
    };

    /**
     * 开始进行景物推送
     */
    boolean pushFlag = true;

    private long preTime;

    private void startPushScenic(int RSSI, String scenery) {
        long currentTime = System.currentTimeMillis();
        Log.d("startPushScenic:", String.valueOf(RSSI));
        if (RSSI > -65 && pushFlag && (currentTime - preTime) > 3 * 1000) {
            initXPopup(scenery);
            pushFlag = false;
            preTime = System.currentTimeMillis();
        }
    }

    private XPopup.Builder builder;

    private void initXPopup(final String scenery) {
        builder = new XPopup.Builder(this);
        builder
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(true)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onDismiss() {
                        pushFlag = true;
                    }

                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    @Override
                    public boolean onBackPressed() {
                        return false;
                    }
                }).asConfirm("景物推送", "附近可能存在景物{ *" + scenery + "* },请选择是否查看",
                "取消", "查看",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        Intent intent = new Intent(MapActivity.this, PushSceneryActivity.class);
                        intent.putExtra("scenery", scenery);
                        startActivity(intent);
                    }
                }, null, false)
                .show();
    }


    /**
     * 设置路径规划的各种图标和布局显示情况
     */
    private void setRoadLogo() {
        isVisible = !isVisible;
        setRoadSignVisible();
        if (polyline != null) {
            polyline.remove();
            polyline = null;
            navigateTo(25.266416, 110.295621);  //醉乡
        }
        switch (end) {
            case "普贤塔":
                marker1.setVisible(true);
                break;
            case "象眼岩":
                marker3.setVisible(true);
                break;
            case "桂林抗战遗址":
                marker2.setVisible(true);
                break;
        }
        if (startMarker != null) {
            startMarker.remove();
            startMarker = null;
        }
        if (endMarker != null) {
            endMarker.remove();
            endMarker = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        //关闭webSocket连接
        webSocket.close(1000, "WebSocket已断开连接。。。");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次重新进入当前的界面时判断路线规划布局是否应该被显示
        setRoadSignVisible();
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
