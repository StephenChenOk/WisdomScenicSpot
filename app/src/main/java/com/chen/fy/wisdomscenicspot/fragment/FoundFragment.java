package com.chen.fy.wisdomscenicspot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.activities.ViewPointActivity;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

public class FoundFragment extends Fragment implements View.OnClickListener{

    private CardView chongqi;
    private CardView beijing;
    private CardView shanghai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.found_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chongqi = view.findViewById(R.id.chongqi_cd);
        shanghai = view.findViewById(R.id.shanghai_cd);
        beijing = view.findViewById(R.id.beijing_cd);

        chongqi.setOnClickListener(this);
        beijing.setOnClickListener(this);
        shanghai.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity()!=null) {
            UiUtils.changeStatusBarTextImgColor(getActivity(), true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chongqi_cd:
                Intent intent1 = new Intent(getActivity(), ViewPointActivity.class);
                intent1.putExtra("目的地","重庆");
                startActivity(intent1);
                break;
            case R.id.shanghai_cd:
                Intent intent2 = new Intent(getActivity(), ViewPointActivity.class);
                intent2.putExtra("目的地","上海");
                startActivity(intent2);
                break;
            case R.id.beijing_cd:
                break;
        }
    }
}