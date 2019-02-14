package com.chen.good.diary;

import android.app.Application;

import org.xutils.x;

/**
 * @author ted.sun
 * @data 2019/1/23
 * @package com.chen.good.diary
 * @PS
 */

public class UApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //XUtils初始化
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

}
