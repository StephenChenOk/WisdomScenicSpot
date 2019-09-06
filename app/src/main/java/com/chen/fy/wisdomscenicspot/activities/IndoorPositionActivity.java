package com.chen.fy.wisdomscenicspot.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.utils.DensityUtil;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 室内蓝牙定位
 */
public class IndoorPositionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_OPEN_BLUE_TOOTH = 2;

    private double[] RSSI = {0, 0, 0, 0};

    private ImageView img;

    private AnimatorSet set;
    private float startX;
    private float startY;
    private float endX = 500;
    private float endY = 500;
    private BleManager bleManager;

    //各个蓝牙节点的坐标
    MyPoint mPoint1 = new MyPoint(0.1, 0.1);
    MyPoint mPoint2 = new MyPoint(4.2, 0.3);
    MyPoint mPoint3 = new MyPoint(1.0, 9.5);
    MyPoint mPoint4 = new MyPoint(4.1, 10.0);

    //测试的总长度和宽度
    private int mWidth = 0;
    private int mHeight = 0;

    //Animation 移动单位
    private int mItem_Animation = 800;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indoor_position_layout);

        initView();
        getWH();
        checkPermissions();

        kalman1 = new Kalman(70, 100.0);//预测误差的方差,噪声误差的方差
        kalman2 = new Kalman(70, 100.0);
        kalman3 = new Kalman(70, 100.0);
        kalman4 = new Kalman(70, 100.0);

        set = new AnimatorSet();
    }

    /**
     * 获取手机屏幕的宽高
     */
    private void getWH() {
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mWidth = metrics.widthPixels - 80;  //以要素为单位
        mHeight = metrics.heightPixels - DensityUtil.dipToPx(this, 50) - 100;
        Log.d("getWH", mWidth + ":::" + mHeight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleManager.disconnectAllDevice(); //断开所有连接
        bleManager.destroy();
        set.cancel();
    }

    private void initView() {
        img = findViewById(R.id.test_iv);
        Toolbar toolbar = findViewById(R.id.toolbar_indoor);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.d("getHeight()", String.valueOf(toolbar.getMeasuredHeight()));

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
    }

    private void initBle() {
        bleManager = BleManager.getInstance();
        bleManager.init(getApplication());
        setScanRule();
        //开始扫描
        bleManager.scan(bleScanCallback);
    }

    /**
     * 开始执行动画
     */
    private void startAnimation(AnimatorSet set, float startX, float startY, float endX, float endY) {
        //set.end();
        //set.cancel();
        set.playTogether(
                ObjectAnimator.ofFloat(img, "translationX", startX, endX),
                ObjectAnimator.ofFloat(img, "translationY", startY, endY)
        );
        if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 200) {
            set.setDuration(mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 400) {
            set.setDuration(2 * mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 600) {
            set.setDuration(3 * mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 800) {
            set.setDuration(4 * mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1000) {
            set.setDuration(5 * mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1200) {
            set.setDuration(6 * mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1400) {
            set.setDuration(7 * mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1600) {
            set.setDuration(8 * mItem_Animation).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1800) {
            set.setDuration(9 * mItem_Animation).start();
        } else {
            set.setDuration(10 * mItem_Animation).start();
        }
    }

    /**
     * 开始扫描
     */
    int i = 0;
    private boolean flag1 = true;
    private boolean flag2 = true;
    private boolean flag3 = true;
    private boolean flag4 = true;
    BleScanCallback bleScanCallback = new BleScanCallback() {
        /**
         * 会回到主线程，参数表示本次扫描动作是否开启成功。
         * 由于蓝牙没有打开，上一次扫描没有结束等原因，会造成扫描开启失败。
         */
        @Override
        public void onScanStarted(boolean success) {
        }

        private int i1 = 0;
        private int i2 = 0;
        private int i3 = 0;
        private int i4 = 0;
        private double temp1 = 0;
        private double temp2 = 0;
        private double temp3 = 0;
        private double temp4 = 0;

        /**
         * 扫描过程中所有被扫描到的结果回调。由于扫描及过滤的过程是在工作线程中的，
         * 此方法也处于工作线程中。同一个设备会在不同的时间，携带自身不同的状态（比如信号强度等），
         * 出现在这个回调方法中，出现次数取决于周围的设备量及外围设备的广播间隔。
         */
        @Override
        public void onLeScan(BleDevice bleDevice) {
            super.onLeScan(bleDevice);
            //startAnimation(set, 0, 0, mWidth, mHeight);
            if (bleDevice.getName() != null) {
                switch (bleDevice.getName()) {
                    case "1836242":
//                        if (flag1) {
//                            flag1 = false;
//                            RSSI[0] = kalman1.KalmanFilter(bleDevice.getRssi());
//                            getLocation();
//                        }
//
                        if (flag1) {
                            temp1 += kalman1.KalmanFilter(bleDevice.getRssi());
                            i1++;
                            if (i1 % 2 == 0) {
                                flag1 = false;
                                RSSI[0] = temp1 / 2;
                                getLocation();
                                temp1 = 0;
                            }
                        }
                        break;
                    case "1836157":
//                        if (flag2) {
//                            flag2 = false;
//                            RSSI[1] = kalman2.KalmanFilter(bleDevice.getRssi());
//                            getLocation();
//                        }

                        if (flag2) {
                            temp2 += kalman2.KalmanFilter(bleDevice.getRssi());
                            i2++;
                            if (i2 % 2 == 0) {
                                flag2 = false;
                                RSSI[1] = temp2 / 2;
                                getLocation();
                                temp2 = 0;
                            }
                        }
                        break;
                    case "1836027":
//                        if (flag3) {
//                            flag3 = false;
//                            RSSI[2] = kalman3.KalmanFilter(bleDevice.getRssi());
//                            getLocation();
//                        }

                        if (flag3) {
                            temp3 += kalman3.KalmanFilter(bleDevice.getRssi());
                            i3++;
                            if (i3 % 2 == 0) {
                                flag3 = false;
                                RSSI[2] = temp3 / 2;
                                getLocation();
                                temp3 = 0;
                            }
                        }
                        break;
                    case "1836072":
//                        if (flag4) {
//                            flag4 = false;
//                            RSSI[3] = kalman4.KalmanFilter(bleDevice.getRssi());
//                            getLocation();
//                        }

                        if (flag4) {
                            temp4 += kalman4.KalmanFilter(bleDevice.getRssi());
                            i4++;
                            if (i4 % 2 == 0) {
                                flag4 = false;
                                RSSI[3] = temp4 / 2;
                                getLocation();
                                temp4 = 0;
                            }
                        }
                        break;
                }
            }
        }

        /**
         * 扫描过程中的所有过滤后的结果回调。与onLeScan区别之处在于：
         * 它会回到主线程；同一个设备只会出现一次；出现的设备是经过扫描过滤规则过滤后的设备。
         */
        @Override
        public void onScanning(final BleDevice bleDevice) {
            Log.d("onScanning:", "onScanning....");
        }

        /**
         * 本次扫描时段内所有被扫描且过滤后的设备集合。它会回到主线程，相当于onScanning设备之和。
         */
        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
        }
    };
    DecimalFormat decimalFormat = new DecimalFormat("00.00");

    private void getLocation() {
        i++;
        if (i % 4 == 0) {
            flag1 = true;
            flag2 = true;
            flag3 = true;
            flag4 = true;
            MyPoint p = getCenterPoint();
            if (p == null) {
                return;
            }
            Log.d("kalman:", decimalFormat.format(RSSI[0]) + "," + decimalFormat.format(RSSI[1])
                    + "," + decimalFormat.format(RSSI[2]) + "," + decimalFormat.format(RSSI[3]));
            Log.d("getCenterPoint:", decimalFormat.format(p.x) + "," + decimalFormat.format(p.y));
            startX = img.getX();
            startY = img.getY();
            endX = (float) (p.x * (mWidth / mPoint2.x));
            endY = (float) (mHeight - (p.y * (mHeight / mPoint3.y)));
            startAnimation(set, startX, startY, endX, endY);
        }
    }

    /**
     * 使用搜索设置功能时，自定义自己的扫描规则
     */
    private void setScanRule() {
        //获取蓝牙设备名称
        String[] names = {"1836242", "1836157", "1836027", "1836072"};

        //设置扫描规则
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setScanTimeOut(0)              // 扫描超时时间，可选，默认10秒
                .build();
        bleManager.initScanRule(scanRuleConfig);
    }

    class MyPoint {
        double x;
        double y;

        MyPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        MyPoint() {
            this.x = 0;
            this.y = 0;
        }
    }

    /**
     * 卡尔曼滤波
     */
    private Kalman kalman1;
    private Kalman kalman2;
    private Kalman kalman3;
    private Kalman kalman4;

    class Kalman {
        double startValue;  //k-1时刻的滤波值，即是k-1时刻的值
        double kalmanGain;   //   Kalamn增益
        double A;   // x(n)=A*x(n-1)+u(n),u(n)~N(0,Q)
        double H;   // z(n)=H*x(n)+w(n),w(n)~N(0,R)
        double Q;   //预测过程噪声偏差的方差
        double R;   //测量噪声偏差，(系统搭建好以后，通过测量统计实验获得)
        double P;   //估计误差协方差;

        Kalman(double Q, double R) {
            A = 1;
            H = 1;
            P = 10;//下一时刻的协方差，初始化随意
            this.Q = Q;
            this.R = R;
            startValue = 70;
        }

        double KalmanFilter(double value) {
            //预测下一时刻的值
            double predictValue = A * startValue;

            //求预测下一时刻的协方差
            P = A * A * P + Q;  //计算先验均方差 p(n|n-1)=A^2*p(n-1|n-1)+q
            //计算kalman增益
            kalmanGain = P * H / (P * H * H + R);  //Kg(k)= P(k|k-1) H’ / (H P(k|k-1) H’ + R)
            //修正结果，即计算滤波值
            startValue = predictValue + (value - predictValue) * kalmanGain;  //利用残余的信息改善对x(t)的估计，给出后验估计，这个值也就是输出  X(k|k)= X(k|k-1)+Kg(k) (Z(k)-H X(k|k-1))
            //更新后验估计
            P = (1 - kalmanGain * H) * P;//计算后验均方差  P[n|n]=(1-K[n]*H)*P[n|n-1]
            return startValue;
        }
    }

    public MyPoint getCenterPoint() {

        double r1 = Math.pow(10, (Math.abs(RSSI[0]) - 62) / (10 * 4.5));
        double r2 = Math.pow(10, (Math.abs(RSSI[1]) - 62) / (10 * 4));
        double r3 = Math.pow(10, (Math.abs(RSSI[2]) - 63) / (10 * 2.9));
        double r4 = Math.pow(10, (Math.abs(RSSI[3]) - 62) / (10 * 3.3));

        Log.d("ridus::", decimalFormat.format(r1) + "," + decimalFormat.format(r2)
                + "," + decimalFormat.format(r3) + "," + decimalFormat.format(r4));

        //三点定位
       // return ThreadPoint(r1, r2, r3);

        //改进的质心算法
        return focusPoint(r1, r2, r3, r4);
    }

    private double preX;
    private double preY;
    private double locationX = 0;
    private double locationY = 0;

    private MyPoint focusPoint(double r1, double r2, double r3, double r4) {
        //1 获取四边形的四个顶点
        MyPoint p1 = getPoint_1(mPoint1, mPoint2, r1, r2);
        MyPoint p2 = getPoint_1(mPoint1, mPoint3, r1, r3);
        MyPoint p3 = getPoint_1(mPoint3, mPoint4, r3, r4);
        MyPoint p4 = getPoint_1(mPoint2, mPoint4, r2, r4);

        if (p1 == null || p2 == null || p3 == null || p4 == null) {
            return null;
        }

        Log.d("getPoint11::", decimalFormat.format(p1.x) + "," + decimalFormat.format(p1.y));
        Log.d("getPoint22::", decimalFormat.format(p2.x) + "," + decimalFormat.format(p2.y));
        Log.d("getPoint33::", decimalFormat.format(p3.x) + "," + decimalFormat.format(p3.y));
        Log.d("getPoint44::", decimalFormat.format(p4.x) + "," + decimalFormat.format(p4.y));

        //2 求出四边形的对角线交点
        double x = ((p4.y - p1.y) * (p3.x - p2.x) * p1.x - (p3.y - p2.y) * (p4.x - p1.x) * p2.x + (p2.y - p1.y) * (p4.x - p1.x) * (p3.x - p2.x))
                / (p3.x * p4.y - p3.x * p1.y - p2.x * p4.y + p2.x * p1.y - p4.x * p3.y + p4.x * p2.y + p1.x * p3.y - p1.x * p2.y);
        double y = ((p4.y - p1.y) * (x - p1.x)) / (p4.x - p1.x) + p1.y;

        //3 获取四个三角形的质心坐标
        MyPoint focus1 = new MyPoint((p1.x + p3.x + x) / 3, (p1.y + p3.y + y) / 3);
        MyPoint focus2 = new MyPoint((p1.x + p2.x + x) / 3, (p1.y + p2.y + y) / 3);
        MyPoint focus3 = new MyPoint((p2.x + p4.x + x) / 3, (p2.y + p4.y + y) / 3);
        MyPoint focus4 = new MyPoint((p3.x + p4.x + x) / 3, (p3.y + p4.y + y) / 3);

        //4 获取不相邻三角形质心连线的中点坐标
        MyPoint midpoint1 = new MyPoint((focus1.x + focus3.x) / 2, (focus1.y + focus3.y) / 2);
        MyPoint midpoint2 = new MyPoint((focus2.x + focus4.x) / 2, (focus2.y + focus4.y) / 2);

        preX = locationX;
        preY = locationY;
        locationX = (midpoint1.x + midpoint2.x) / 2;
        locationY = (midpoint1.y + midpoint2.y) / 2;
        Log.d("locationXY::", decimalFormat.format(locationX) + "," + decimalFormat.format(locationY));
        if (locationX < mPoint1.x + 0.1) {
            locationX = mPoint1.x + 0.1;
        }
        if (locationX > mPoint2.x - 0.1) {
            locationX = mPoint2.x - 0.1;
        }
        if (locationY < mPoint1.y + 0.1) {
            locationY = mPoint1.y + 0.1;
        }
        if (locationY > mPoint3.y - 0.1) {
            locationY = mPoint3.y - 0.1;
        }

        if (preX - locationX > 0.2) {
            locationX = preX - 0.2;
        }
        if (preX - locationX < -0.2) {
            locationX = preX + 0.2;
        }
        if (preY - locationY > 0.4) {
            locationY = preY - 0.4;
        }
        if (preY - locationY < -0.4) {
            locationY = preY + 0.4;
        }

        //5 得出定位坐标
        Log.d("resultXY::", decimalFormat.format(locationX) + "," + decimalFormat.format(locationY));
        return new MyPoint(locationX, locationY);
    }

    //得到四边形的四个顶点
    private MyPoint getPoint(MyPoint p1, MyPoint p2, double r1, double r2) {
        double L = getDistance(p1.x, p1.y, p2.x, p2.y);
        double K1 = (p2.y - p1.y) / (p2.x - p1.x);
        double K2 = -1 / K1;
        double AE = (r1 * r1 - r2 * r2 + L * L) / (2 * L);
        //得到 (x0,y0)
        double x0 = p1.x + (AE * (p2.x - p1.x)) / L;
        double y0 = p1.y + (AE * (p2.y - p1.y)) / L;
        //得到 CE
        double CE_1 = r1 * r1 - (x0 - p1.x) * (x0 - p1.x) - (y0 - p1.y) * (y0 - p1.y);
        if (CE_1 < 0) {
            CE_1 = 0;
        }
        double CE = Math.sqrt(CE_1);
        //得到两个交点的坐标
        double xa = x0 - (CE / Math.sqrt(1 + K2 * K2));
        double ya = y0 + K2 * (xa - x0);
        double xb = x0 + (CE / Math.sqrt(1 + K2 * K2));
        double yb = y0 + K2 * (xb - x0);
        //判断哪个点距离中心最近
        if (getDistance(xa, ya, mPoint4.x / 2.0, mPoint4.y / 2.0) < getDistance(xb, yb, mPoint4.x / 2.0, mPoint4.y / 2.0)) {
            return new MyPoint(xa, ya);
        } else {
            return new MyPoint(xb, yb);
        }
    }

    //两点间距离公式
    private double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @NonNull
    private MyPoint ThreadPoint(double r1, double r2, double r3) {
        //x = (r12 - r22 + d2)/2d 和 y = (r12 - r32 -x2 +(x - i)2 + j2)/ 2j

        preX = locationX;
        preY = locationY;

        double x = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(mPoint2.x - mPoint1.x, 2)) / (2 * (mPoint2.x - mPoint1.x));
        double y = (Math.pow(r1, 2) - Math.pow(r3, 2) - Math.pow(x, 2) + Math.pow(x - mPoint3.x, 2) + Math.pow(mPoint3.y, 2)) / (2 * mPoint3.y);

        locationX = x;
        locationY = y;

        Log.d("x::y", x + ":::" + y);
        if (locationX < mPoint1.x + 0.1) {
            locationX = mPoint1.x + 0.1;
        }
        if (locationX > mPoint2.x - 0.1) {
            locationX = mPoint2.x - 0.1;
        }
        if (locationY < mPoint1.y + 0.1) {
            locationY = mPoint1.y + 0.1;
        }
        if (locationY > mPoint3.y - 0.1) {
            locationY = mPoint3.y - 0.1;
        }

        if (preX - locationX > 0.3) {
            locationX = preX - 0.3;
        }
        if (preX - locationX < -0.3) {
            locationX = preX + 0.3;
        }
        if (preY - locationY > 0.7) {
            locationY = preY - 0.7;
        }
        if (preY - locationY < -0.7) {
            locationY = preY + 0.7;
        }
        return new MyPoint(locationX, locationY);
    }

    private MyPoint getPoint_1(MyPoint p1, MyPoint p2, double r1, double r2) {
        // 在一元二次方程中 a*x^2+b*x+c=0
        double a, b, c;

        //x的两个根 x_1 , x_2
        //y的两个根 y_1 , y_2
        double x_1 = 0, x_2 = 0, y_1 = 0, y_2 = 0;

        //判别式的值
        double delta = -1;

        //如果 y1!=mY2
        if (p1.y != p2.y) {

            //为了方便代入
            double A = (p1.x * p1.x - p2.x * p2.x + p1.y * p1.y - p2.y * p2.y + r2 * r2 - r1 * r1) / (2 * (p1.y - p2.y));
            double B = (p1.x - p2.x) / (p1.y - p2.y);

            a = 1 + B * B;
            b = -2 * (p1.x + (A - p1.y) * B);
            c = p1.x * p1.x + (A - p1.y) * (A - p1.y) - r1 * r1;

            //下面使用判定式 判断是否有解
            delta = b * b - 4 * a * c;

            if (delta > 0) {

                x_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                x_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                y_1 = A - B * x_1;
                y_2 = A - B * x_2;
            } else if (delta == 0) {
                x_1 = x_2 = -b / (2 * a);
                y_1 = y_2 = A - B * x_1;
            } else {
                Log.d("两个圆不相交", "两个圆不相交");
                return null;
            }
        } else if (p1.x != p2.x) {

            //当mY1=mY2时，x的两个解相等
            x_1 = x_2 = (p1.x * p1.x - p2.x * p2.x + r2 * r2 - r1 * r1) / (2 * (p1.x - p2.x));

            a = 1;
            b = -2 * p1.y;
            c = p1.y * p1.y - r1 * r1 + (x_1 - p1.x) * (x_1 - p1.x);

            delta = b * b - 4 * a * c;

            if (delta > 0) {
                y_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                y_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
            } else if (delta == 0) {
                y_1 = y_2 = -b / (2 * a);
            } else {
                Log.d("两个圆不相交", "两个圆不相交");
                return null;
            }
        } else {
            Log.d("无解", "无解");
            return null;
        }
        //mPoint4.x / 2.0, mPoint4.y / 2.0
        double t1 = (mPoint4.x / 2.0 - x_1) * (mPoint4.x / 2.0 - x_1) + (mPoint4.y / 2.0 - y_1) * (mPoint4.y / 2.0 - y_1);
        double t2 = (mPoint4.x / 2.0 - x_2) * (mPoint4.x / 2.0 - x_2) + (mPoint4.y / 2.0 - y_2) * (mPoint4.y / 2.0 - y_2);
        if (t1 < t2) {  // (x_1,y_1)为交点
            return new MyPoint(x_1, y_1);
        } else {
            return new MyPoint(x_2, y_2);
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
}
