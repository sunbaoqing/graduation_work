package com.chen.good.diary;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.chen.good.diary.util.Config;
import com.chen.good.diary.util.CrashHandler;
import com.chen.good.diary.util.Utils;

import org.xutils.x;

import java.io.File;
import java.util.Locale;

/**
 * @author ted.sun
 * @data 2019/1/23
 * @package com.chen.good.diary
 * @PS
 */

public class UApplication extends Application {

    private static UApplication instance;
    public static File CACHE_DIR;
    public static boolean isDebug = false;

    public static UApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        setLanguage();
        initX();
        initCrashLog();
        getStatusBarH();
    }

    private void init() {
        instance = this;
        CACHE_DIR = getCacheDir();
    }

    /**
     * 切换语言
     */
    private void setLanguage() {
        SharedPreferences preferences;
        preferences = this.getSharedPreferences("personnalaccount", Context.MODE_PRIVATE);
        String lag = preferences.getString("language", "cn");
        lag = Utils.getCountrylag(this);
        if (lag.equals("zh")) {
            switchLanguage(Locale.SIMPLIFIED_CHINESE);
        } else if (lag.equals("ja")) {
            switchLanguage(Locale.JAPANESE);
        } else if (lag.equals("en")) {
            switchLanguage(Locale.ENGLISH);
        }
    }

    public void switchLanguage(Locale locale) {
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素�??
        config.locale = locale; //
        resources.updateConfiguration(config, dm);
    }

    private void initX() {
        //XUtils初始化
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

    /**
     * 初始化异常崩溃日志目录
     */
    private void initCrashLog() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    //状态栏高度
    private void getStatusBarH() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            Config.STATUS_BAR_HEIGHT = this.getResources().getDimensionPixelSize(resourceId);
        }

    }
}