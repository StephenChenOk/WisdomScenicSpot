package com.chen.fy.wisdomscenicspot.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.model.SearchHistoryInfo;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private ArrayList<SearchHistoryInfo> lists;
    private ItemClickListener itemClickListener;

    public SearchHistoryAdapter(ArrayList<SearchHistoryInfo> lists) {
        this.lists = lists;
    }

    /**
     * 传入RecyclerView点击接口
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_history_adapter_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        SearchHistoryInfo historyInfo = lists.get(i);
        viewHolder.tv_title.setText(historyInfo.getTitle());
        //实现item的点击事件
        if (itemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, i);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickListener.onLongClick(v, i);
                    return true;   //消化事件
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.title_history_search_adapter_item);
        }
    }
}