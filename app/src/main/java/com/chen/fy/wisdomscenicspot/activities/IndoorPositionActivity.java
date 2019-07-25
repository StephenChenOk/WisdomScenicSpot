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
import android.util.Log;
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

public class IndoorPositionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    // 扫描结束
    private static final int SCAN_FINISH = 0;

    private double[] RSSI = {-100, -100, -100};

    private ImageView img;

    private AnimatorSet set;
    private float startX;
    private float startY;
    private float endX = 500;
    private float endY = 500;
    private BleManager bleManager;

    //各个蓝牙节点的坐标
    private double mX1 = 0;
    private double mY1 = 9;
    private double mX2 = 6;
    private double mY2 = 9;
    private double mX3 = 3;
    private double mY3 = 0;

    //测试的总长度和宽度
    private int mWidth = 1000;
    private int mHeight = 1000;

    int j = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startAnimation(set, startX, startY, endX, endY);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indoor_position_layout);

        initView();
        initBle();

        kalman1 = new Kalman(9.0, 100.0);//预测误差的方差,噪声误差的方差
        kalman2 = new Kalman(9.0, 100.0);
        kalman3 = new Kalman(9.0, 100.0);

        set = new AnimatorSet();
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
        checkPermissions();
    }

    /**
     * 开始执行动画
     */
    private void startAnimation(AnimatorSet set, float startX, float startY, float endX, float endY) {
        set.cancel();
        set.playTogether(
                ObjectAnimator.ofFloat(img, "translationX", startX, endX),
                ObjectAnimator.ofFloat(img, "translationY", startY, endY)
        );
        if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 200) {
            set.setDuration(1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 400) {
            set.setDuration(2 * 1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 600) {
            set.setDuration(3 * 1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 800) {
            set.setDuration(4 * 1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1000) {
            set.setDuration(5 * 1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1200) {
            set.setDuration(6 * 1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1400) {
            set.setDuration(7 * 1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1600) {
            set.setDuration(8 * 1000).start();
        } else if ((Math.abs((startX - endX)) + Math.abs((startY - endY))) < 1800) {
            set.setDuration(9 * 1000).start();
        } else {
            set.setDuration(10 * 1000).start();
        }
    }

    /**
     * 开始扫描
     */
    int i = 0;
    BleScanCallback bleScanCallback = new BleScanCallback() {
        @Override
        public void onScanStarted(boolean success) {
            Log.d("onScanStarted", "onScanStarted");
        }

        @Override
        public void onScanning(final BleDevice bleDevice) {
        }

        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            Log.d("onScanFinished", String.valueOf(scanResultList.size()));
            for (BleDevice bleDevice : scanResultList) {
                if (bleDevice.getName() != null) {
                    switch (bleDevice.getName()) {
                        case "1836242":
                            Log.d("bleDevice.getRssi()...", String.valueOf(bleDevice.getRssi()));
                            RSSI[0] = kalman1.KalmanFilter(bleDevice.getRssi());
                            i++;
                            getLocation();
                            break;
                        case "1836157":
                            RSSI[1] = kalman2.KalmanFilter(bleDevice.getRssi());
                            i++;
                            getLocation();
                            break;
                        case "1836027":
                            RSSI[2] = kalman3.KalmanFilter(bleDevice.getRssi());
                            i++;
                            getLocation();
                            break;
                    }
                }
            }
            bleManager.scan(bleScanCallback);
        }
    };
    DecimalFormat decimalFormat = new DecimalFormat("00.00");

    private void getLocation() {
        if (i % 3 == 0) {
            MyPoint p = getCenterPoint();
            Log.d("kalman:", decimalFormat.format(RSSI[0]) + "," + decimalFormat.format(RSSI[1])
                    + "," + decimalFormat.format(RSSI[2]));
            Log.d("point:", decimalFormat.format(p.x) + "," + decimalFormat.format(p.y));
            startX = img.getX();
            startY = img.getY();
            endX = (float) ((float) p.x * (mWidth / mX2));
            endY = (float) ((float) p.y * (mHeight / mY2));
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessage(0);
            bleManager.cancelScan();
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
                .setScanTimeOut(3000)              // 扫描超时时间，可选，默认10秒
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

        double r1 = Math.pow(10, (Math.abs(RSSI[0]) - 62) / (10 * 3.5));
        double r2 = Math.pow(10, (Math.abs(RSSI[1]) - 62) / (10 * 3.5));
        double r3 = Math.pow(10, (Math.abs(RSSI[2]) - 62) / (10 * 3.5));

        Log.d("ridus::", decimalFormat.format(r1) + "," + decimalFormat.format(r2)
                + "," + decimalFormat.format(r3));

        // 在一元二次方程中 a*x^2+b*x+c=0
        double a, b, c;

        //x的两个根 x_1 , x_2
        //y的两个根 y_1 , y_2
        double x_1 = 0, x_2 = 0, y_1 = 0, y_2 = 0;

        //判别式的值
        double delta = -1;

        //如果 y1!=mY2
        if (mY1 != mY2) {

            //为了方便代入
            double A = (mX1 * mX1 - mX2 * mX2 + mY1 * mY1 - mY2 * mY2 + r2 * r2 - r1 * r1) / (2 * (mY1 - mY2));
            double B = (mX1 - mX2) / (mY1 - mY2);

            a = 1 + B * B;
            b = -2 * (mX1 + (A - mY1) * B);
            c = mX1 * mX1 + (A - mY1) * (A - mY1) - r1 * r1;

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
                return new MyPoint((mX1 + mX2 + mX3) / 3, (mY1 + mY2 + mY3) / 3);
            }
        } else if (mX1 != mX2) {

            //当mY1=mY2时，x的两个解相等
            x_1 = x_2 = (mX1 * mX1 - mX2 * mX2 + r2 * r2 - r1 * r1) / (2 * (mX1 - mX2));

            a = 1;
            b = -2 * mY1;
            c = mY1 * mY1 - r1 * r1 + (x_1 - mX1) * (x_1 - mX1);

            delta = b * b - 4 * a * c;

            if (delta > 0) {
                y_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                y_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
            } else if (delta == 0) {
                y_1 = y_2 = -b / (2 * a);
            } else {
                Log.d("两个圆不相交", "两个圆不相交");
                return new MyPoint((mX1 + mX2 + mX3) / 3, (mY1 + mY2 + mY3) / 3);
            }
        } else {
            Log.d("无解", "无解");
            return new MyPoint((mX1 + mX2 + mX3) / 3, (mY1 + mY2 + mY3) / 3);
        }
        double t1 = (mX3 - x_1) * (mX3 - x_1) + (mY3 - y_1) * (mY3 - y_1);
        double t2 = (mX3 - x_2) * (mX3 - x_2) + (mY3 - y_2) * (mY3 - y_2);
        if (t1 < t2) {  // (x_1,y_1)为交点
            if (x_1 <= 0) {
                x_1 = 0.1;
            }
            if (x_1 > 6) {
                x_1 = 5.9;
            }
            if (y_1 <= 0) {
                y_1 = 0.1;
            }
            if (y_1 > 9) {
                y_1 = 8.9;
            }
            return new MyPoint(x_1, y_1);
        } else {
            if (x_2 <= 0) {
                x_2 = 0.1;
            }
            if (x_2 > 6) {
                x_2 = 5.9;
            }
            if (y_2 <= 0) {
                y_2 = 0.1;
            }
            if (y_2 > 9) {
                y_2 = 8.9;
            }
            return new MyPoint(x_2, y_2);
        }
    }

    private void checkPermissions() {
        //判断是否已经打开蓝牙
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "请先打开蓝牙...", Toast.LENGTH_LONG).show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("当前手机扫描蓝牙需要打开定位功能...")
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton("前往设置",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    bleManager.scan(bleScanCallback);
                }
                break;
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
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                //setScanRule();
                //startScan();
            }
        }
    }
}
