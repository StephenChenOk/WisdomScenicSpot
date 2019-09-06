package com.chen.fy.wisdomscenicspot.adapter;

import android.view.View;

/**
 * recyclerView点击事件
 */
public interface ItemClickListener {

    /**
     *  点击事件
     * @param view      当前所点击的view
     * @param position  所点击的item所在的位置
     */
    void onItemClick(View view, int position);

    /**
     *  长按事件
     * @param view      当前所点击的view
     * @param position  所点击的item所在的位置
     */
    void onLongClick(View view, int position);

    void onItemClick(int i);

    void onLongClick(int i);
}
