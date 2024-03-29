package com.chen.good.diary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.chen.good.diary.UApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SPUtil {
    private static String PreferenceName = "Constant";
  /*  public static String newconsumerId="57543112-4798-4a55-8b9e-d2e0ee2f9e19";
    public static String newcustomerId="00007f1d-3c0a-427a-bf8d-e2c5a7afd73f";*/

    /**
     * 缓存对象
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean saveObjectToShare(String key, T t) {
        return saveObjectToShare(getContext(), PreferenceName, key, t);
    }

    /**
     * 缓存对象
     *
     * @param context
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean saveObjectToShare(Context context, String key, T t) {
        return saveObjectToShare(context, PreferenceName, key, t);
    }

    /**
     * 缓存对象
     *
     * @param context
     * @param name
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean saveObjectToShare(Context context, String name, String key, T t) {
        try {
            SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            Editor editor = sp.edit();
            if (t == null) {
                editor.putString(key, "");
                editor.commit();
                return true;
            }
            ByteArrayOutputStream toByte = new ByteArrayOutputStream();
            ObjectOutputStream oos;

            oos = new ObjectOutputStream(toByte);
            oos.writeObject(t);
            String payCityMapBase64 = new String(Base64.encode(toByte.toByteArray(), Base64.DEFAULT));

            editor.putString(key, payCityMapBase64);
            editor.commit();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从缓存中获得存储的对象
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromShare(String key) {
        return getObjectFromShare(getContext(), PreferenceName, key);
    }

    /**
     * 从缓存中获取存储的对象
     *
     * @param context
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromShare(Context context, String key) {
        return getObjectFromShare(context, PreferenceName, key);
    }

    /**
     * 从缓存中获取存储的对象
     *
     * @param context
     * @param name
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromShare(Context context, String name, String key) {
        try {
            SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            String payCityMapBase64 = sp.getString(key, "");
            if (payCityMapBase64.length() == 0) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(payCityMapBase64, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缓存字符串
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 得到缓存的字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(key, "");
    }

    /**
     * 得到缓存的字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key, String defult) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getString(key, defult);
    }

    /**
     * 缓存boolen类型数据
     *
     * @param key
     * @param value
     */
    public static void saveboolean(String key, boolean value) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 得到boolen类型数据
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }

    public static boolean getBoolean(String key, boolean def) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }

    /**
     * 保存int类型数据
     *
     * @param key
     * @param value
     */
    public static void saveInt(String key, int value) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 得到int类型数据
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 保存float类型数据
     *
     * @param key
     * @param value
     */
    public static void saveFloat(String key, float value) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 得到float类型数据
     *
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, 0);
    }

    /**
     * 保存long类型数据
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, long value) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 得到long类型数据
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }


    /**
     * 清空操作
     */

    public static void Clear() {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * 清空操作
     */
    public static void Del(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }

    private static UApplication getContext() {
        return UApplication.getInstance();
    }

}
