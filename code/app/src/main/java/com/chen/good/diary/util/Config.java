package com.chen.good.diary.util;

import android.app.Activity;
import android.os.Environment;
import android.util.DisplayMetrics;

/**
 * Created by baoqi on 2018/5/18.
 */

public class Config {

    public static int SCREEN_WIDTH;// px

    public static int SCREEN_HEIGHT;// px

    public static int STATUS_BAR_HEIGHT;// px

    //日志logo目录
    public static String DIR_ADDRESS = Environment.getExternalStorageDirectory().getPath() + "/U_Diary";

    //log日志前缀
    public static String logName_ = "crash-";

    public static void init_screen(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
    }
}
