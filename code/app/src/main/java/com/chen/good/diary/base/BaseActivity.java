package com.chen.good.diary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import org.xutils.x;

/**
 * @author ted.sun
 * @data 2019/1/23
 * @package com.chen.good.diary.base
 * @PS
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
