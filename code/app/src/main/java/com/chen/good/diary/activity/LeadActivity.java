package com.chen.good.diary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.good.diary.R;
import com.chen.good.diary.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_lead)
public class LeadActivity extends BaseActivity {

    @ViewInject(R.id.tv_test)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        textView.setText("init");
    }

    public void BtnClick(View view) {
        textView.setText("111");
    }


    @Event({R.id.tv_test})
    private void testClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                showToast("按钮");
                break;
            case R.id.tv_test:
                showToast("textview");
                break;

        }
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
