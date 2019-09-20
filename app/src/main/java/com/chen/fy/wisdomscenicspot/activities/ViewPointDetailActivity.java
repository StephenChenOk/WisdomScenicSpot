package com.chen.fy.wisdomscenicspot.activities;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.utils.ImageUtil;
import com.chen.fy.wisdomscenicspot.utils.ScenicDescribeUtils;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class ViewPointDetailActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageView imageView;
    private Toolbar toolbar;

    private TextView tv_rainfall_detail;
    private TextView tv_temperature_detail;
    private TextView tv_humidity_detail;
    private TextView tv_visibility_detail;

    private TextView tv_viewpoint_detail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UiUtils.changeStatusBarTextImgColor(this, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpoint_detail_layout);
        initView();

        if (getIntent() != null) {
            setDates_Today(getIntent().getStringExtra("ViewPointName"));
        }
    }

    private void initView() {
        ImageView iv_return = findViewById(R.id.iv_return_viewpoint_detail);
        toolbar = findViewById(R.id.tb_viewpoint_detail);
        imageView = findViewById(R.id.iv_viewpoint_detail);

        tv_rainfall_detail = findViewById(R.id.tv_rainfall_detail);
        tv_temperature_detail = findViewById(R.id.tv_temperature_detail);
        tv_humidity_detail = findViewById(R.id.tv_humidity_detail);
        tv_visibility_detail = findViewById(R.id.tv_visibility_detail);

        tv_viewpoint_detail = findViewById(R.id.tv_viewpoint_detail);

        iv_return.setOnClickListener(this);

        //找到控件对象
        RadioGroup radioGroup = findViewById(R.id.viewpoint_detail_group_main);
        radioGroup.setOnCheckedChangeListener(this);


        if (getIntent() != null) {
            setViewPoint(getIntent().getStringExtra("ViewPointName"));
        }
    }

    private void setViewPoint(String name) {
        if (ImageUtil.getImageId(name) != 0) {
            Glide.with(this).load(ImageUtil.getImageId(name)).into(imageView);
        }
        switch (name) {
            //重庆
            case "磁器口古镇":
                initDetailInfo("磁器口古镇", ScenicDescribeUtils.getQCK(), 1);
                break;
            case "解放碑步行街":
                initDetailInfo("解放碑步行街", ScenicDescribeUtils.getJFB(), 1);
                break;
            case "武隆天生三桥":
                initDetailInfo("武隆天生三桥", ScenicDescribeUtils.getSQ(), 1);
                break;
            case "大足石刻":
                initDetailInfo("大足石刻", ScenicDescribeUtils.getDZSK(), 1);
                break;
            case "白公馆":
                initDetailInfo("白公馆", ScenicDescribeUtils.getBGG(), 1);
                break;
            case "长江索道":
                initDetailInfo("长江索道", ScenicDescribeUtils.getCJSD(), 1);
                break;
            case "南山风景区":
                initDetailInfo("南山风景区", ScenicDescribeUtils.getNS(), 1);
                break;
            case "白帝城景区":
                initDetailInfo("白帝城景区", ScenicDescribeUtils.getBDC(), 1);
                break;

            //上海
            case "外滩":
                initDetailInfo("外滩", ScenicDescribeUtils.getWT(), 2);
                break;
            case "上海迪士尼度假区":
                initDetailInfo("上海迪士尼度假区", ScenicDescribeUtils.getDSN(), 2);
                break;
            case "南京路步行街":
                initDetailInfo("南京路步行街", ScenicDescribeUtils.getNJL_BXJ(), 2);
                break;
            case "上海长风海洋世界":
                initDetailInfo("上海长风海洋世界", ScenicDescribeUtils.getCF_HYSJ(), 2);
                break;
            case "朱家角古镇景区":
                initDetailInfo("朱家角古镇景区", ScenicDescribeUtils.getZJJ(), 2);
                break;
        }
    }

    private void initDetailInfo(String name, String qck, int code) {
        toolbar.setTitle(name);
        tv_viewpoint_detail.setText(qck);
    }

    private void setDates_Today(String name) {
        SharedPreferences preferences = getSharedPreferences("BigDates", MODE_PRIVATE);

        switch (name) {
            //重庆
            case "磁器口古镇":   //重庆市区
                initDetailInfo("磁器口古镇", ScenicDescribeUtils.getQCK(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq", "")
                                ,preferences.getString("temperature_cq_sq", "")
                                ,preferences.getString("humidity_cq_sq", "")
                                ,preferences.getString("visibility_cq_sq", "")
                );
                break;
            case "解放碑步行街":  //重庆市区
                initDetailInfo("解放碑步行街", ScenicDescribeUtils.getJFB(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq", "")
                        ,preferences.getString("temperature_cq_sq", "")
                        ,preferences.getString("humidity_cq_sq", "")
                        ,preferences.getString("visibility_cq_sq", "")
                );
                break;
            case "武隆天生三桥":  //武隆区
                initDetailInfo("武隆天生三桥", ScenicDescribeUtils.getSQ(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_wl", "")
                        ,preferences.getString("temperature_cq_wl", "")
                        ,preferences.getString("humidity_cq_wl", "")
                        ,preferences.getString("visibility_cq_wl", "")
                );
                break;
            case "大足石刻":      //大足区
                initDetailInfo("大足石刻", ScenicDescribeUtils.getDZSK(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_dz", "")
                        ,preferences.getString("temperature_cq_dz", "")
                        ,preferences.getString("humidity_cq_dz", "")
                        ,preferences.getString("visibility_cq_dz", "")
                );
                break;
            case "白公馆":         //重庆市区
                initDetailInfo("白公馆", ScenicDescribeUtils.getBGG(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq", "")
                        ,preferences.getString("temperature_cq_sq", "")
                        ,preferences.getString("humidity_cq_sq", "")
                        ,preferences.getString("visibility_cq_sq", "")
                );
                break;
            case "长江索道":       //重庆市区
                initDetailInfo("长江索道", ScenicDescribeUtils.getCJSD(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq", "")
                        ,preferences.getString("temperature_cq_sq", "")
                        ,preferences.getString("humidity_cq_sq", "")
                        ,preferences.getString("visibility_cq_sq", "")
                );
                break;
            case "南山风景区":     //重庆市区
                initDetailInfo("南山风景区", ScenicDescribeUtils.getNS(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq", "")
                        ,preferences.getString("temperature_cq_sq", "")
                        ,preferences.getString("humidity_cq_sq", "")
                        ,preferences.getString("visibility_cq_sq", "")
                );
                break;
            case "白帝城景区":       //奉节区
                initDetailInfo("白帝城景区", ScenicDescribeUtils.getBDC(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_fj", "")
                        ,preferences.getString("temperature_cq_fj", "")
                        ,preferences.getString("humidity_cq_fj", "")
                        ,preferences.getString("visibility_cq_fj", "")
                );
                break;

            //上海
            case "外滩":               //上海市区
                initDetailInfo("外滩", ScenicDescribeUtils.getWT(), 2);
                setWeatherDates_youyu(preferences.getString("rainfall_sh_sq1", "")
                        ,preferences.getString("temperature_sh_sq1", "")
                        ,preferences.getString("humidity_sh_sq1", "")
                        ,preferences.getString("visibility_sh_sq1", "")
                );
                break;
            case "上海迪士尼度假区":    //浦东新区
                initDetailInfo("上海迪士尼度假区", ScenicDescribeUtils.getDSN(), 2);
                setWeatherDates_youyu(preferences.getString("rainfall_sh_pd", "")
                        ,preferences.getString("temperature_sh_pd", "")
                        ,preferences.getString("humidity_sh_pd", "")
                        ,preferences.getString("visibility_sh_pd", "")
                );
                break;
            case "南京路步行街":      //上海市区
                initDetailInfo("南京路步行街", ScenicDescribeUtils.getNJL_BXJ(), 2);
                setWeatherDates_youyu(preferences.getString("rainfall_sh_sq1", "")
                        ,preferences.getString("temperature_sh_sq1", "")
                        ,preferences.getString("humidity_sh_sq1", "")
                        ,preferences.getString("visibility_sh_sq1", "")
                );
                break;
            case "上海长风海洋世界":    //上海市区
                initDetailInfo("上海长风海洋世界", ScenicDescribeUtils.getCF_HYSJ(), 2);
                setWeatherDates_youyu(preferences.getString("rainfall_sh_sq2", "")
                        ,preferences.getString("temperature_sh_sq2", "")
                        ,preferences.getString("humidity_sh_sq2", "")
                        ,preferences.getString("visibility_sh_sq2", "")
                );
                break;
            case "朱家角古镇景区":     //青浦区
                initDetailInfo("朱家角古镇景区", ScenicDescribeUtils.getZJJ(), 2);
                setWeatherDates_youyu(preferences.getString("rainfall_sh_qp", "")
                        ,preferences.getString("temperature_sh_qp", "")
                        ,preferences.getString("humidity_sh_qp", "")
                        ,preferences.getString("visibility_sh_qp", "")
                );
                break;
        }
    }

    private void setDates_Tomorrow(String name) {
        SharedPreferences preferences = getSharedPreferences("BigDates", MODE_PRIVATE);

        switch (name) {
            //重庆
            case "磁器口古镇":   //重庆市区
                initDetailInfo("磁器口古镇", ScenicDescribeUtils.getQCK(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq_t", "")
                        ,preferences.getString("temperature_cq_sq_t", "")
                        ,preferences.getString("humidity_cq_sq_t", "")
                        ,preferences.getString("visibility_cq_sq_t", "")
                );
                break;
            case "解放碑步行街":  //重庆市区
                initDetailInfo("解放碑步行街", ScenicDescribeUtils.getJFB(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq_t", "")
                        ,preferences.getString("temperature_cq_sq_t", "")
                        ,preferences.getString("humidity_cq_sq_t", "")
                        ,preferences.getString("visibility_cq_sq_t", "")
                );
                break;
            case "武隆天生三桥":  //武隆区
                initDetailInfo("武隆天生三桥", ScenicDescribeUtils.getSQ(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_wl_t", "")
                        ,preferences.getString("temperature_cq_wl_t", "")
                        ,preferences.getString("humidity_cq_wl_t", "")
                        ,preferences.getString("visibility_cq_wl_t", "")
                );
                break;
            case "大足石刻":      //大足区
                initDetailInfo("大足石刻", ScenicDescribeUtils.getDZSK(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_dz_t", "")
                        ,preferences.getString("temperature_cq_dz_t", "")
                        ,preferences.getString("humidity_cq_dz_t", "")
                        ,preferences.getString("visibility_cq_dz_t", "")
                );
                break;
            case "白公馆":         //重庆市区
                initDetailInfo("白公馆", ScenicDescribeUtils.getBGG(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq_t", "")
                        ,preferences.getString("temperature_cq_sq_t", "")
                        ,preferences.getString("humidity_cq_sq_t", "")
                        ,preferences.getString("visibility_cq_sq_t", "")
                );
                break;
            case "长江索道":       //重庆市区
                initDetailInfo("长江索道", ScenicDescribeUtils.getCJSD(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq_t", "")
                        ,preferences.getString("temperature_cq_sq_t", "")
                        ,preferences.getString("humidity_cq_sq_t", "")
                        ,preferences.getString("visibility_cq_sq_t", "")
                );
                break;
            case "南山风景区":     //重庆市区
                initDetailInfo("南山风景区", ScenicDescribeUtils.getNS(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_sq_t", "")
                        ,preferences.getString("temperature_cq_sq_t", "")
                        ,preferences.getString("humidity_cq_sq_t", "")
                        ,preferences.getString("visibility_cq_sq_t", "")
                );
                break;
            case "白帝城景区":       //奉节区
                initDetailInfo("白帝城景区", ScenicDescribeUtils.getBDC(), 1);
                setWeatherDates(preferences.getString("rainfall_cq_fj_t", "")
                        ,preferences.getString("temperature_cq_fj_t", "")
                        ,preferences.getString("humidity_cq_fj_t", "")
                        ,preferences.getString("visibility_cq_fj_t", "")
                );
                break;

            //上海
            case "外滩":               //上海市区
                initDetailInfo("外滩", ScenicDescribeUtils.getWT(), 2);
                setWeatherDates(preferences.getString("rainfall_sh_sq1_t", "")
                        ,preferences.getString("temperature_sh_sq1_t", "")
                        ,preferences.getString("humidity_sh_sq1_t", "")
                        ,preferences.getString("visibility_sh_sq1_t", "")
                );
                break;
            case "上海迪士尼度假区":    //浦东新区
                initDetailInfo("上海迪士尼度假区", ScenicDescribeUtils.getDSN(), 2);
                setWeatherDates(preferences.getString("rainfall_sh_pd_t", "")
                        ,preferences.getString("temperature_sh_pd_t", "")
                        ,preferences.getString("humidity_sh_pd_t", "")
                        ,preferences.getString("visibility_sh_pd_t", "")
                );
                break;
            case "南京路步行街":      //上海市区
                initDetailInfo("南京路步行街", ScenicDescribeUtils.getNJL_BXJ(), 2);
                setWeatherDates(preferences.getString("rainfall_sh_sq1_t", "")
                        ,preferences.getString("temperature_sh_sq1_t", "")
                        ,preferences.getString("humidity_sh_sq1_t", "")
                        ,preferences.getString("visibility_sh_sq1_t", "")
                );
                break;
            case "上海长风海洋世界":    //上海市区
                initDetailInfo("上海长风海洋世界", ScenicDescribeUtils.getCF_HYSJ(), 2);
                setWeatherDates(preferences.getString("rainfall_sh_sq2_t", "")
                        ,preferences.getString("temperature_sh_sq2_t", "")
                        ,preferences.getString("humidity_sh_sq2_t", "")
                        ,preferences.getString("visibility_sh_sq2_t", "")
                );
                break;
            case "朱家角古镇景区":     //青浦区
                initDetailInfo("朱家角古镇景区", ScenicDescribeUtils.getZJJ(), 2);
                setWeatherDates(preferences.getString("rainfall_sh_qp_t", "")
                        ,preferences.getString("temperature_sh_qp_t", "")
                        ,preferences.getString("humidity_sh_qp_t", "")
                        ,preferences.getString("visibility_sh_qp_t", "")
                );
                break;
        }
    }


    private void setWeatherDates(String rainfull, String temperature, String humidity, String visibility) {
        switch (rainfull){
            case "0":
                tv_rainfall_detail.setText("无雨");
                //获取更换的图片
                Drawable drawable1=getResources().getDrawable(R.drawable.duo_yun_1);
                //setBounds(x,y,width,height)
                drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
                //mDownLoad是控件的名称,setCompoundDrawables(left,top,right,bottom)
                tv_rainfall_detail.setCompoundDrawables(null,drawable1,null,null);
                break;
            case "1":
                tv_rainfall_detail.setText("有雨");
                //获取更换的图片
                Drawable drawable2=getResources().getDrawable(R.drawable.rain_1);
                //setBounds(x,y,width,height)
                drawable2.setBounds(0,0,drawable2.getMinimumWidth(),drawable2.getMinimumHeight());
                //mDownLoad是控件的名称,setCompoundDrawables(left,top,right,bottom)
                tv_rainfall_detail.setCompoundDrawables(null,drawable2,null,null);
                break;
        }
        tv_temperature_detail.setText(String.format("%s ℃", temperature));
        tv_humidity_detail.setText(String.format("%s ％", humidity));
        tv_visibility_detail.setText(String.format("%s m", visibility));
    }

    private void setWeatherDates_youyu(String rainfull, String temperature, String humidity, String visibility) {
        tv_rainfall_detail.setText("无雨");
        //获取更换的图片
        Drawable drawable1=getResources().getDrawable(R.drawable.duo_yun_1);
        //setBounds(x,y,width,height)
        drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
        //mDownLoad是控件的名称,setCompoundDrawables(left,top,right,bottom)
        tv_rainfall_detail.setCompoundDrawables(null,drawable1,null,null);
//        switch (rainfull){
//            case "0":
//                tv_rainfall_detail.setText("无雨");
//                //获取更换的图片
//                Drawable drawable1=getResources().getDrawable(R.drawable.sun_1);
//                //setBounds(x,y,width,height)
//                drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
//                //mDownLoad是控件的名称,setCompoundDrawables(left,top,right,bottom)
//                tv_rainfall_detail.setCompoundDrawables(null,drawable1,null,null);
//                break;
//            case "1":
//                tv_rainfall_detail.setText("有雨");
//                //获取更换的图片
//                Drawable drawable2=getResources().getDrawable(R.drawable.rain_1);
//                //setBounds(x,y,width,height)
//                drawable2.setBounds(0,0,drawable2.getMinimumWidth(),drawable2.getMinimumHeight());
//                //mDownLoad是控件的名称,setCompoundDrawables(left,top,right,bottom)
//                tv_rainfall_detail.setCompoundDrawables(null,drawable2,null,null);
//                break;
//        }
        tv_temperature_detail.setText(String.format("%s ℃", temperature));
        tv_humidity_detail.setText(String.format("%s ％", humidity));
        tv_visibility_detail.setText(String.format("%s m", visibility));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_viewpoint_detail:
                finish();
                break;
            case R.id.btn_today_weather:
                Toast.makeText(this, "今日天气", Toast.LENGTH_LONG).show();
                //setWeatherDates("有雨", "24", "82", "11584");
                setDates_Today(getIntent().getStringExtra("ViewPointName"));
                break;
            case R.id.btn_tomorrow_weather:
                Toast.makeText(this, "明日天气", Toast.LENGTH_LONG).show();
                //setWeatherDates("有雨", "26", "79", "13105");
                setDates_Tomorrow(getIntent().getStringExtra("ViewPointName"));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //底部导航栏按钮选中事件
        RadioButton radioButton = group.findViewById(checkedId);
        radioButton.setOnClickListener(this);
    }
}
