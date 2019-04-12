package com.chen.fy.wisdomscenicspot.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);

        initView();
    }

    private void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_feedback);
        setSupportActionBar(toolbar);

        EditText et_address_name_feedback = findViewById(R.id.et_address_name_feedback);
        EditText et_address_title_feedback = findViewById(R.id.et_address_title_feedback);
        ImageView iv_photo_feedback = findViewById(R.id.iv_photo_feedback);
        EditText et_matters_feedback = findViewById(R.id.et_matters_feedback);
        EditText et_phone_feedback = findViewById(R.id.et_phone_feedback);
        Button btn_submit = findViewById(R.id.btn_submit_feedback);

        et_address_name_feedback.setOnClickListener(new MyOnClickListener());
        et_address_title_feedback.setOnClickListener(new MyOnClickListener());
        iv_photo_feedback.setOnClickListener(new MyOnClickListener());
        et_matters_feedback.setOnClickListener(new MyOnClickListener());
        et_phone_feedback.setOnClickListener(new MyOnClickListener());
        btn_submit.setOnClickListener(new MyOnClickListener());

        //自动弹出输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 点击接口
     */
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.et_address_name_feedback:
                    break;
                case R.id.et_address_title_feedback:
                    break;
                case R.id.iv_photo_feedback:
                    break;
                case R.id.et_matters_feedback:
                    break;
                case R.id.et_phone_feedback:
                    break;
                case R.id.btn_submit_feedback:
                    Toast.makeText(FeedbackActivity.this, "提交", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
