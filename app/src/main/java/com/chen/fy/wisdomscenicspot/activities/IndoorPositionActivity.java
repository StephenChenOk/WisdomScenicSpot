package com.chen.fy.wisdomscenicspot.activities;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 室内蓝牙定位
 */
public class IndoorPositionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_OPEN_BLUE_TOOTH = 2;

    private double[] RSSI = {0, 0, 0};

    private ImageView img;

    private AnimatorSet set;
    private float startX;
    private float startY;
    private float endX = 500;
    private float endY = 500;
    private BleManager bleManager;

    //各个蓝牙节点的坐标
    private double mX1 = 0;
    private double mY1 = 0;
    private double mX2 = 3.5;
    private double mY2 = 0;
    private double mX3 = 1.75;
    private double mY3 = 5;

    //测试的总长度和宽度
    private int mWidth;
    private int mHeight;

    //Animation 移动单位
    private int mItem_Animation = 800;

    int j = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startAnimation(set, startX, startY, endX, endY);
        }
    };
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indoor_position_layout);

        initView();
        getWH();
        checkPermissions();

        kalman1 = new Kalman(9.0, 100.0);//预测误差的方差,噪声误差的方差
        kalman2 = new Kalman(9.0, 100.0);
        kalman3 = new Kalman(9.0, 100.0);

        set = new AnimatorSet();
    }

    /**
     * 获取手机屏幕的宽高
     */
    private void getWH() {
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mWidth = metrics.widthPixels;  //以要素为单位
        mHeight = metrics.heightPixels;
        Log.d("getWH", mWidth + ":::" + mHeight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleManager.disconnectAllDevice(); //断开所有连接
        bleManager.destroy();
    }

    private void initView() {
        img = findViewById(R.id.test_iv);
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
        set.cancel();
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
    BleScanCallback bleScanCallback = new BleScanCallback() {
        /**
         * 会回到主线程，参数表示本次扫描动作是否开启成功。
         * 由于蓝牙没有打开，上一次扫描没有结束等原因，会造成扫描开启失败。
         */
        @Override
        public void onScanStarted(boolean success) {
            Log.d("onScanStarted", "onScanStarted");
        }

        private int i1 = 0;
        private int i2 = 0;
        private int i3 = 0;
        private double temp1 = 0;
        private double temp2 = 0;
        private double temp3 = 0;

        /**
         * 扫描过程中所有被扫描到的结果回调。由于扫描及过滤的过程是在工作线程中的，
         * 此方法也处于工作线程中。同一个设备会在不同的时间，携带自身不同的状态（比如信号强度等），
         * 出现在这个回调方法中，出现次数取决于周围的设备量及外围设备的广播间隔。
         */
        @Override
        public void onLeScan(BleDevice bleDevice) {
            super.onLeScan(bleDevice);
            Log.d("bleDevice.getRssi():", String.valueOf(bleDevice.getRssi()));
            if (bleDevice.getName() != null) {
                switch (bleDevice.getName()) {
                    case "1836242":
                        Log.d("onLeScan:1836242:", String.valueOf(bleDevice.getRssi()));
                        temp1 += bleDevice.getRssi();
                        //RSSI[0] += kalman1.KalmanFilter(bleDevice.getRssi());
                        i1++;
                        if (i1 % 5 == 0) {
                            RSSI[0] = kalman1.KalmanFilter(temp1 / 5);
                            getLocation();
                            temp1 = 0;
                        }
                        break;
                    case "1836157":
                        Log.d("onLeScan:1836157:", String.valueOf(bleDevice.getRssi()));
                        temp2 += bleDevice.getRssi();
                        //RSSI[1] += kalman2.KalmanFilter(bleDevice.getRssi());
                        i2++;
                        if (i2 % 5 == 0) {
                            RSSI[1] = kalman2.KalmanFilter(temp2 / 5);
                            getLocation();
                            temp2 = 0;
                        }
                        break;
                    case "1836027":
                        Log.d("onLeScan:1836027:", String.valueOf(bleDevice.getRssi()));
                        temp3 += bleDevice.getRssi();
                        //RSSI[2] += kalman3.KalmanFilter(bleDevice.getRssi());
                        i3++;
                        if (i3 % 5 == 0) {
                            RSSI[2] = kalman3.KalmanFilter(temp3 / 5);
                            getLocation();
                            temp3 = 0;
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
        if (i % 3 == 0) {
            MyPoint p = getCenterPoint();
            Log.d("kalman:", decimalFormat.format(RSSI[0]) + "," + decimalFormat.format(RSSI[1])
                    + "," + decimalFormat.format(RSSI[2]));
            Log.d("point:", decimalFormat.format(p.x) + "," + decimalFormat.format(p.y));
            startX = img.getX();
            startY = img.getY();
            endX = (float) ((float) p.x * (mWidth / mX2));
            endY = (float) ((float) p.y * (mHeight / mY3));
//            mHandler.removeCallbacksAndMessages(null);
//            mHandler.sendEmptyMessage(0);
            startAnimation(set, startX, startY, endX, endY);
        }
    }

    /**
     * 使用搜索设置功能时，自定义自己的扫描规则
     */
    private void setScanRule() {
        //获取蓝牙设备名称
        String[] names = {"1836242", "1836157", "1836027"};

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
            startValue = 50;
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

        double r1 = Math.pow(10, (Math.abs(RSSI[0]) - 62) / (10 * 2.5));
        double r2 = Math.pow(10, (Math.abs(RSSI[1]) - 62) / (10 * 2.5));
        double r3 = Math.pow(10, (Math.abs(RSSI[2]) - 63) / (10 * 2.5));

        Log.d("ridus::", decimalFormat.format(r1) + "," + decimalFormat.format(r2)
                + "," + decimalFormat.format(r3));

        //x = (r12 - r22 + d2)/2d 和 y = (r12 - r32 -x2 +(x - i)2 + j2)/ 2j
        double x = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(mX2, 2)) / (2 * mX2);
        double y = (Math.pow(r1, 2) - Math.pow(r3, 2) - Math.pow(x, 2) + Math.pow(x - mX3, 2) + Math.pow(mY3, 2)) / (2 * mY3);
        Log.d("x::y", x + ":::" + y);
        if (x < mX1 + 0.1) {
            x = mX1 + 0.1;
        }
        if (x > mX2 - 0.1) {
            x = mX2 - 0.1;
        }
        if (y < mY1 + 0.1) {
            y = mY1 + 0.1;
        }
        if (y > mY3 - 0.1) {
            y = mY3 - 0.1;
        }
        return new MyPoint(x, y);
//        // 在一元二次方程中 a*x^2+b*x+c=0
//        double a, b, c;
//
//        //x的两个根 x_1 , x_2
//        //y的两个根 y_1 , y_2
//        double x_1 = 0, x_2 = 0, y_1 = 0, y_2 = 0;
//
//        //判别式的值
//        double delta = -1;
//
//        //如果 y1!=mY2
//        if (mY1 != mY2) {
//
//            //为了方便代入
//            double A = (mX1 * mX1 - mX2 * mX2 + mY1 * mY1 - mY2 * mY2 + r2 * r2 - r1 * r1) / (2 * (mY1 - mY2));
//            double B = (mX1 - mX2) / (mY1 - mY2);
//
//            a = 1 + B * B;
//            b = -2 * (mX1 + (A - mY1) * B);
//            c = mX1 * mX1 + (A - mY1) * (A - mY1) - r1 * r1;
//
//            //下面使用判定式 判断是否有解
//            delta = b * b - 4 * a * c;
//
//            if (delta > 0) {
//
//                x_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
//                x_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
//                y_1 = A - B * x_1;
//                y_2 = A - B * x_2;
//            } else if (delta == 0) {
//                x_1 = x_2 = -b / (2 * a);
//                y_1 = y_2 = A - B * x_1;
//            } else {
//                return new MyPoint((mX1 + mX2 + mX3) / 3, (mY1 + mY2 + mY3) / 3);
//            }
//        } else if (mX1 != mX2) {
//
//            //当mY1=mY2时，x的两个解相等
//            x_1 = x_2 = (mX1 * mX1 - mX2 * mX2 + r2 * r2 - r1 * r1) / (2 * (mX1 - mX2));
//
//            a = 1;
//            b = -2 * mY1;
//            c = mY1 * mY1 - r1 * r1 + (x_1 - mX1) * (x_1 - mX1);
//
//            delta = b * b - 4 * a * c;
//
//            if (delta > 0) {
//                y_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
//                y_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
//            } else if (delta == 0) {
//                y_1 = y_2 = -b / (2 * a);
//            } else {
//                Log.d("两个圆不相交", "两个圆不相交");
//                return new MyPoint((mX1 + mX2 + mX3) / 3, (mY1 + mY2 + mY3) / 3);
//            }
//        } else {
//            Log.d("无解", "无解");
//            return new MyPoint((mX1 + mX2 + mX3) / 3, (mY1 + mY2 + mY3) / 3);
//        }
//        double t1 = (mX3 - x_1) * (mX3 - x_1) + (mY3 - y_1) * (mY3 - y_1);
//        double t2 = (mX3 - x_2) * (mX3 - x_2) + (mY3 - y_2) * (mY3 - y_2);
//        if (t1 < t2) {  // (x_1,y_1)为交点
//            if (x_1 <= 0) {
//                x_1 = 0.1;
//            }
//            if (x_1 > mX2) {
//                x_1 = mX2 - 0.1;
//            }
//            if (y_1 <= 0) {
//                y_1 = 0.1;
//            }
//            if (y_1 > mY2) {
//                y_1 = mY2 - 0.1;
//            }
//            return new MyPoint(x_1, y_1);
//        } else {
//            if (x_2 <= 0) {
//                x_2 = 0.1;
//            }
//            if (x_2 > mX2) {
//                x_2 = mX2 - 0.1;
//            }
//            if (y_2 <= 0) {
//                y_2 = 0.1;
//            }
//            if (y_2 > mY2) {
//                y_2 = mY2 - 0.1;
//            }
//            return new MyPoint(x_2, y_2);
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
