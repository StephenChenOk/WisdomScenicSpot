package com.chen.fy.wisdomscenicspot.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.fragment.HomeFragment;

public class MyPagerAdapter extends PagerAdapter {
    private Context myContext;

    public MyPagerAdapter(Context context) {
        myContext = context;
    }

    /**
     * @return 图片总数
     */
    @Override
    public int getCount() {
        return 500;            //实现左右无限滑动   数值可以任意调动
    }

    /**
     * 比较两个页面是否为同一个
     *
     * @param view 当前页面
     * @param o    instantiateItem方法返回的页面
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * 初始化页面
     *
     * @param container viewPager自身
     * @param position  当前实例化页面位置
     * @return 返回初始化好的页面
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        ImageView imageView = HomeFragment.images.get(position % HomeFragment.images.size());

        //设置触摸事件,当用户点击了页面时,页面不再自己往后动,应该做停留
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   //手指按下
                        HomeFragment.handler.removeCallbacksAndMessages(null);  //移除所有的回调以及消息
                        break;
                    case MotionEvent.ACTION_MOVE:   //移动
                        break;
                    case MotionEvent.ACTION_UP:     //离开
                        HomeFragment.handler.removeCallbacksAndMessages(null);  //移除所有的回调以及消息
                        HomeFragment.handler.sendEmptyMessageDelayed(0, 3000);
                        break;
                }

                return false;    //返回false表示没有消费该事件,事件继续往下传递,onClick等事件仍然可以进行响应
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if (imageView.getParent() != null) {
            ((ViewPager) imageView.getParent()).removeView(imageView);
        }
        container.addView(imageView);   //把初始化好的页面加入到viewPager中
        return imageView;
    }

    /**
     * 释放资源
     *
     * @param container viewpager本身
     * @param position  要释放的位置
     * @param object    要释放的页面
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}

