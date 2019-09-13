package com.chen.fy.wisdomscenicspot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.activities.ViewPointDetailActivity;
import com.chen.fy.wisdomscenicspot.beans.ViewPointInfo;
import com.chen.fy.wisdomscenicspot.utils.ImageUtil;

import java.util.List;

public class ViewPointAdapter extends RecyclerView.Adapter<ViewPointAdapter.ViewHolder> implements View.OnClickListener {

    private List<ViewPointInfo> list;
    private ItemClickListener itemClickLister;
    private Context myContext;

    //构造方法,并传入数据源
    public ViewPointAdapter(List<ViewPointInfo> list) {
        this.list = list;
    }

    public void setItemClickLister(ItemClickListener itemClickLister) {
        this.itemClickLister = itemClickLister;
    }

    @NonNull
    @Override
    public ViewPointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (myContext == null) {
            myContext = viewGroup.getContext();
        }
        //反射每行的子布局,并把view传入viewHolder中,以便获取控件对象
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.viewpoint_item_adapter, viewGroup, false);
        return new ViewPointAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        ViewPointInfo viewPointInfo = list.get(i);

        viewHolder.image_iv.setImageResource(ImageUtil.getImageId(viewPointInfo.getName()));
        viewHolder.name_tv.setText(viewPointInfo.getName());
        viewHolder.address_tv.setText(viewPointInfo.getAddress());
        viewHolder.number_tv.setText(viewPointInfo.getNumber() + "");
        viewHolder.distance_tv.setText(String.format("%skm", viewPointInfo.getDistance()));

        viewHolder.detail_iv.setOnClickListener(this);
        viewHolder.detail_tv.setOnClickListener(this);

        if (itemClickLister != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickLister.onItemClick(i);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickLister.onLongClick(i);
                    return true;  //消化事件
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_iv_viewpoint:
            case R.id.detail_tv_viewpoint:
                Intent intent = new Intent(myContext, ViewPointDetailActivity.class);
                myContext.startActivity(intent);
                break;
        }
    }

    /**
     * 内部类,获取各控件的对象
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_iv;
        private TextView name_tv;
        private TextView address_tv;
        private TextView number_tv;
        private TextView distance_tv;
        private ImageView detail_iv;
        private TextView detail_tv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_iv = itemView.findViewById(R.id.image_scenery);
            name_tv = itemView.findViewById(R.id.name_scenery);
            address_tv = itemView.findViewById(R.id.address_scenery);
            number_tv = itemView.findViewById(R.id.number_scenery);
            distance_tv = itemView.findViewById(R.id.distance_scenery);
            detail_iv = itemView.findViewById(R.id.detail_iv_viewpoint);
            detail_tv = itemView.findViewById(R.id.detail_tv_viewpoint);
        }
    }

}
