package com.chen.fy.wisdomscenicspot.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;
import com.chen.fy.wisdomscenicspot.activities.LoginActivity;
import com.chen.fy.wisdomscenicspot.activities.MyInfoActivity;
import com.chen.fy.wisdomscenicspot.beans.Manager;
import com.chen.fy.wisdomscenicspot.beans.Visitor;
import com.chen.fy.wisdomscenicspot.utils.UiUtils;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment implements View.OnClickListener {

    private CircleImageView headIcon;
    private TextView userName;
    private TextView infoText;
    private TextView tv_user_manager;

    private View mView;

    /**
     * 当前用户登入的用户名
     */
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.mine, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity()!=null) {
            UiUtils.changeStatusBarTextImgColor(getActivity(), true);
        }

        //显示一些简单的用户信息
        //获取之前的登入状态
        getLoginState();

        initMineView();
        showVisitorInfo();
        //进行头像加载
        loadHeadIcon();
    }

    private void initMineView() {
        //游客端view
        headIcon = mView.findViewById(R.id.head_icon_mine);
        userName = mView.findViewById(R.id.user_name_mine);
        infoText = mView.findViewById(R.id.info_text_mine);

        headIcon.setOnClickListener(this);
        userName.setOnClickListener(this);
        infoText.setOnClickListener(this);
    }


    /**
     * 显示游客一些简单的信息
     */
    private void showVisitorInfo() {
        if (userId != null && !userId.isEmpty()) {
            List<Visitor> visitors = LitePal.where("userId = ?", userId).find(Visitor.class);
            for (Visitor visitor : visitors) {
                userName.setText(visitor.getUserName());
                infoText.setText("个人信息>");
            }
        } else {
            userName.setText("登入/注册");
            infoText.setText("");
        }
    }

    /**
     * 获取之前的登入状态
     */
    private void getLoginState() {
        if (getContext() != null) {
            SharedPreferences preferences = getContext().getSharedPreferences("login_state", MODE_PRIVATE);
            userId = preferences.getString("userId", "");
        }
    }

    /**
     * 进行头像加载
     */
    private void loadHeadIcon() {
        if (userId != null && !userId.isEmpty()) {
            if (getActivity() != null) {
                //头像加载
                File file = new File(getActivity().getExternalFilesDir(null), userId + "headIcon.jpg");
                Uri headIconUri = Uri.fromFile(file);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(headIconUri));
                    headIcon.setImageBitmap(bitmap);                    //如果上面产生文件存在异常，则不执行
                } catch (FileNotFoundException e) {
                    headIcon.setImageResource(R.drawable.user_12);   //捕获异常后，设置头像为默认头像，程序继续执行
                }
            } else {
                headIcon.setImageResource(R.drawable.user_12);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_icon_mine:
            case R.id.user_name_mine:
            case R.id.info_text_mine:
                if (userId == null || userId.isEmpty()) {   //当还没有登入账号,则进入登入界面
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else {    //已经登入账号,则进入显示个人信息界面
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                        intent.putExtra("userId", userId);
                        startActivityForResult(intent, 2);
                    }
                }
                break;
        }
    }
}

