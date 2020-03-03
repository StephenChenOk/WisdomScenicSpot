package com.chen.fy.wisdomscenicspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.adapter.ItemClickListener;
import com.chen.fy.wisdomscenicspot.adapter.JobSchedulingAdapter;
import com.chen.fy.wisdomscenicspot.model.JobSchedulingInfo;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

import org.litepal.LitePal;

import java.util.ArrayList;

public class JobSchedulingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<JobSchedulingInfo> lists;

    private ItemClickListener mItemClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_scheduling_layout);

        initView();

    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_job_scheduling);
        Toolbar toolbar = findViewById(R.id.toolbar_job_scheduling);

        //用来指定RecycleView的布局方式,这里是卡片式布局的意思
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);//1 表示列数
        recyclerView.setLayoutManager(layoutManager);

        //获取数据
        initData();
        JobSchedulingAdapter jobSchedulingAdapter = new JobSchedulingAdapter(lists);
        if (mItemClickListener == null) {
            mItemClickListener = new MyItemClickListener();
        }
        jobSchedulingAdapter.setItemClickListener(mItemClickListener);
        recyclerView.setAdapter(jobSchedulingAdapter);


        //返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //将状态栏字体变为黑色
        UiUtils.changeStatusBarTextImgColor(this,true);
    }

    private void initData() {

        if (lists == null) {
            lists = new ArrayList<>();
        }
        //清除数据,以免重复
        lists.clear();
        //从数据库中获取数据
        lists = (ArrayList<JobSchedulingInfo>) LitePal.findAll(JobSchedulingInfo.class);
    }

    /**
     * recyclerView的item点击事件
     */
    class MyItemClickListener implements ItemClickListener {


        @Override
        public void onItemClick(View view, int position) {
            switch (view.getId()){
                case R.id.job_scheduling_item:
                    JobSchedulingInfo jobSchedulingInfo = lists.get(position);
                    double latitude = jobSchedulingInfo.getLatitude();
                    double longitude = jobSchedulingInfo.getLongitude();
                    //回传经纬度给谷歌地图进行显示
                    Intent intent = new Intent();
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }

        @Override
        public void onLongClick(View view, int position) {

        }

        @Override
        public void onItemClick(int i) {

        }

        @Override
        public void onLongClick(int i) {

        }
    }
}
