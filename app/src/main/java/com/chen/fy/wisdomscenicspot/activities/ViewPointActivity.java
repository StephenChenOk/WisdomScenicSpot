package com.chen.fy.wisdomscenicspot.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.adapter.ItemClickListener;
import com.chen.fy.wisdomscenicspot.adapter.ViewPointAdapter;
import com.chen.fy.wisdomscenicspot.beans.ViewPointInfo;

import java.util.List;

/**
 * 景点推荐活动
 */
public class ViewPointActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    private List<ViewPointInfo> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_point_layout);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_view_point);

        //2 RecyclerView设置
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);//1 表示列数
        recyclerView.setLayoutManager(layoutManager);
        ViewPointAdapter viewPointAdapter = new ViewPointAdapter(list);
        viewPointAdapter.setItemClickLister(this);
        recyclerView.setAdapter(viewPointAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

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
