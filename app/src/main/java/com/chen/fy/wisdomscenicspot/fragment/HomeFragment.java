package com.chen.fy.wisdomscenicspot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.activities.MapActivity;
import com.chen.fy.wisdomscenicspot.activities.SearchActivity;
import com.chen.fy.wisdomscenicspot.adapter.ItemClickListener;
import com.chen.fy.wisdomscenicspot.adapter.SceneryAdapter;
import com.chen.fy.wisdomscenicspot.model.SceneryInfo;
import com.chen.fy.wisdomscenicspot.adapter.MyPagerAdapter;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener,View.OnClickListener, ItemClickListener {

    public static ViewPager viewPager;

    private TextView title;
    private LinearLayout box;
    private TextView search_tv;
    private TextView weather_tv;
    private TextView translation_home_tv;
    private TextView wc_tv;

    private RecyclerView recyclerView;
    private SceneryAdapter sceneryAdapter;
    private List<SceneryInfo> list;

    //图片id
    private int[] imagesId = {
            R.drawable.elephant_hill,
            R.drawable.sjg,
            R.drawable.slzx,
            R.drawable.bsh,
            R.drawable.px_town
    };
    //图片标题
    public static String[] imagesTitle = {
            "象鼻山",
            "水晶宫",
            "狮岭朝霞",
            "八路军桂林办事处纪念馆",
            "普贤塔"
    };

    //图片集合
    public static ArrayList<ImageView> images;
    //上一个被选中图片的位置
    private int prePosition = 0;
    //判断当前页面是否是滑动状态,解决当页面手动滑动后不能继续进行自动滑动
    private boolean isScroll = false;

    /**
     * 让图片自己动起来,采用异步handel,因为在Thread中不可以进行UI操作,所有可以用handel实行异步UI操作
     */
    public static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = viewPager.getCurrentItem()+1;
            viewPager.setCurrentItem(item);

            //延迟发消息
            handler.sendEmptyMessageDelayed(0,3000);
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        //设置手机应用内部状态栏字体图标为白色
        changeStatusBarTextImgColor(false);
    }



    private void initView(@NonNull View view) {
        //寻找控件
        viewPager = view.findViewById(R.id.viewpager_home);
        title = view.findViewById(R.id.title_viewpager_home);
        box = view.findViewById(R.id.box_home);
        search_tv = view.findViewById(R.id.search_home);
        weather_tv = view.findViewById(R.id.weather_home_tv);
        translation_home_tv = view.findViewById(R.id.translation_home_tv);
        wc_tv = view.findViewById(R.id.wc_home_tv);
        recyclerView = view.findViewById(R.id.rv_home);

        //设置点击事件
        search_tv.setOnClickListener(this);
        weather_tv.setOnClickListener(this);
        translation_home_tv.setOnClickListener(this);
        wc_tv.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        //1 设置顶部的自动轮转图
        viewPager.setAdapter(new MyPagerAdapter(getContext()));
        viewPager.addOnPageChangeListener(this);
        //设置当前页面在中间位置,保证可以实现左右滑动的效果
        viewPager.setCurrentItem(250);           //要保证是实际图片数量的整数倍,也就是保证每次进入都是先显示的第一张图片
        title.setText(imagesTitle[prePosition]);
        //第一次进入时延迟发消息
        handler.sendEmptyMessageDelayed(0,3000);

        //2 RecyclerView设置
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
        recyclerView.setLayoutManager(layoutManager);
        sceneryAdapter = new SceneryAdapter(list);
        sceneryAdapter.setItemClickLister(this);
        recyclerView.setAdapter(sceneryAdapter);
    }

    //准备数据
    private void initData() {
        //1 获取轮播图的图片和点
        images = new ArrayList<>();
        for (int i = 0; i < imagesId.length; i++) {
            //添加图片
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imagesId[i]);
            images.add(imageView);
            //添加点
            ImageView point = new ImageView(getContext());
            point.setBackgroundResource(R.drawable.point_selctor);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20); //自定义一个布局
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 30;    //距离左边的间距
            }
            point.setLayoutParams(params);   //设置格式,间距等
            box.addView(point);
        }

        //2 初始化RecyclerView显示的数据
        if(list == null){
            list = new ArrayList<>();
        }
        if(!list.isEmpty()){
            list.clear();
        }
        SceneryInfo sceneryInfo1 = new SceneryInfo();
        sceneryInfo1.setName("象鼻山");
        sceneryInfo1.setAddress("广西省桂林市象山景区");
        sceneryInfo1.setNumber(550);
        sceneryInfo1.setDistance(30.15);

        SceneryInfo sceneryInfo2 = new SceneryInfo();
        sceneryInfo2.setName("普贤塔");
        sceneryInfo2.setAddress("广西省桂林市象山景区");
        sceneryInfo2.setNumber(325);
        sceneryInfo2.setDistance(29.11);

        SceneryInfo sceneryInfo3 = new SceneryInfo();
        sceneryInfo3.setName("太平天国纪念馆");
        sceneryInfo3.setAddress("广西省桂林市象山景区");
        sceneryInfo3.setNumber(124);
        sceneryInfo3.setDistance(28.31);

        SceneryInfo sceneryInfo4 = new SceneryInfo();
        sceneryInfo4.setName("云峰寺");
        sceneryInfo4.setAddress("广西省桂林市象山景区");
        sceneryInfo4.setNumber(91);
        sceneryInfo4.setDistance(27.55);

        list.add(sceneryInfo1);
        list.add(sceneryInfo2);
        list.add(sceneryInfo3);
        list.add(sceneryInfo4);
    }


    /**
     * 界面设置状态栏字体颜色
     */
    public void changeStatusBarTextImgColor(boolean isBlack) {
        if(getActivity()!=null) {
            if (isBlack) {
                //设置状态栏黑色字体
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                //恢复状态栏白色字体
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 当页面滚动时回调这个方法
     *
     * @param i  当前页面的位置
     * @param v  滑动页面的百分比
     * @param i1 在屏幕上滑动的像素
     */
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    /**
     * 当前页面被选中时回调
     * @param i 被选中页面的位置
     */
    @Override
    public void onPageSelected(int i) {
        //设置位置
        title.setText(imagesTitle[i % images.size()]);
        //把上一个高亮位置设置为灰色
        box.getChildAt(prePosition).setEnabled(false);
        //当前位置设置为高亮
        box.getChildAt(i % images.size()).setEnabled(true);
        prePosition = i % images.size();
    }

    /**
     * 当页面状态改变时回调
     * 静止-滚动
     * 滚动-静止
     * 静止-拖拽
     *
     * @param i 当前状态
     */
    @Override
    public void onPageScrollStateChanged(int i) {
        if( i == ViewPager.SCROLL_STATE_DRAGGING){   //拖拽状态
            isScroll = true;
        }else if(i == ViewPager.SCROLL_STATE_SETTLING){  //滑动状态

        }else if(i== ViewPager.SCROLL_STATE_IDLE){   //静止状态
            isScroll = false;
            handler.removeCallbacksAndMessages(null);   //清除消息
            handler.sendEmptyMessageDelayed(0,3000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity()!=null) {
            UiUtils.changeStatusBarTextImgColor(getActivity(), false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weather_home_tv:
                break;
            case R.id.wc_home_tv:
                break;
            case R.id.translation_home_tv:
                break;
            case R.id.search_home:
                if(getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onItemClick(int position) {
        switch (list.get(position).getName()){
            case "象鼻山":
                if(getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("Latitude",25.267088);
                    intent.putExtra("Longitude",110.296427);
                    startActivity(intent);
                }
                break;
            case "普贤塔":
                if(getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("Latitude",25.267242);
                    intent.putExtra("Longitude",110.296046);
                    startActivity(intent);
                }
                break;
            case "太平天国纪念馆":
                if(getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("Latitude",25.266431);
                    intent.putExtra("Longitude",110.295181);
                    startActivity(intent);
                }
                break;
            case "云峰寺":
                if(getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("Latitude",25.266559);
                    intent.putExtra("Longitude",110.295011);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onLongClick(int position) { }
}