package com.chen.good.diary.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.LocaleList;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by baoqi on 2017/6/22.
 */

public class Utils {
    /**
     * 获取数字随机数
     */
    public static String getRandomNum(int length) {

        String hexDigits = "0123456789";
        String ranStr = "";
        for (int i = 0; i < length; i++) {
            int x = (int) (Math.random() * hexDigits.length());
            ranStr += hexDigits.substring(x, x + 1);
        }
        return ranStr;

    }

    /**
     * ACSII转化为字符串
     */
    public static String changeText(String text) {
        StringBuffer sbu = new StringBuffer();
        for (int i = 0; i < text.length() / 2; i++) {
            sbu.append((char) Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16));
        }
        return sbu.toString();
    }

    public static PackageInfo getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 加密
     *
     * @param encryptString
     * @param encryptKey    密钥
     * @return
     * @throws Exception
     */
    public static String encryptAES(String encryptString, String encryptKey) {
        try {
            // byte[] iv = { 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8 };
            String iv = "0102030405060708";
            IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes("utf-8"));
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("utf-8"), "AES");
            // 算法/模式/补码方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

            return Base64Util.encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密
     *
     * @param decryptString
     * @param decryptKey    密钥
     * @return
     * @throws Exception
     */
    public static String decryptAES(String decryptString, String decryptKey) {
        // byte[] iv = { 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8 };

        try {
            String iv = "0102030405060708";
            byte[] byteMi = Base64Util.decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes("utf-8"));
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = new byte[0];
            decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // (int) (Math.random() * 8999)

    /**
     * 获取随机数
     */
    public static String getRandom(int length) {

        String hexDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String ranStr = "";
        for (int i = 0; i < length; i++) {
            int x = (int) (Math.random() * hexDigits.length());
            ranStr += hexDigits.substring(x, x + 1);
        }
        return ranStr;
    }

    /**
     * 获取随机数
     */
    public static String getRandom(int length, String random) {
        String hexDigits = random;
        String ranStr = "";
        for (int i = 0; i < length; i++) {
            int x = (int) (Math.random() * hexDigits.length());
            ranStr += hexDigits.substring(x, x + 1);
        }
        return ranStr;
    }

    public static String getCountrylag(Context context) {
        String lag = "";
        Locale locale;
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            LocaleList locales = context.getResources().getConfiguration().getLocales();
            locale = locales.get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        if (locale != null) {
            List<String> countryList = Utils.getCountryList();
            for (int i = 0; i < countryList.size(); i++) {
                if (locale.getLanguage().contains(countryList.get(i))) {
                    lag = countryList.get(i);
                }
            }
        }
        return lag;
    }


    public static List<String> getCountryList() {
        List<String> countrtList = new ArrayList<>();
        countrtList.add("zh");
        countrtList.add("ja");
        countrtList.add("en");
        return countrtList;
    }

    /***/
    public static String getOrderNum() {
        String time = System.currentTimeMillis() + "";
        time = time.substring(0, time.length() - 1);
        return time + getRandomNum(4);
    }

    /***/
    public static String getRefundNum() {
        String time = System.currentTimeMillis() + "";
        time = time.substring(0, time.length() - 1);
        return "r" + time + getRandomNum(4);
    }

    /**
     * 获取SN号 Java反射
     * 此处用于标识设备
     * <p>
     * String[] propertys = {"ro.boot.serialno", "ro.serialno"};
     *
     * @return SN号
     */
    public static String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            Method systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null)
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }

    /**
     * 此处返回值和上面一样
     * <p>
     * adb 获取：adb devices
     * <p>
     * 其他可以使用的值:
     * <p>
     * String ANDROID_ID = Settings.System.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);//不怎么靠谱
     * String FINGERPRINT = android.os.Build.FINGERPRINT;//这是啥
     *
     * @return SN号
     */
    public static String getSN() {
        return android.os.Build.SERIAL;
    }

    /**
     * 生成二维码
     */
    public static Bitmap encodeAsBitmap(String str, int l) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, l, l);
            // 如果不使用 ZXing Android Embedded 的话，要写的代码
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }
        return bitmap;
    }


    /**
     * 生成带logo的二维码，logo默认为二维码的1/5
     *
     * @param text    需要生成二维码的文字、网址等
     * @param size    需要生成二维码的大小（）
     * @param mBitmap logo文件
     * @return bitmap
     */
    public static Bitmap createQRCodeWithLogo(String text, int size, Bitmap mBitmap) {
        try {
            int IMAGE_HALFWIDTH = size / 6;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            /*
             * 设置容错级别，默认为ErrorCorrectionLevel.L
             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
             */
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 1); //default is 4
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, size, size, hints);

            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;

            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH
                    / mBitmap.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                    mBitmap.getWidth(), mBitmap.getHeight(), m, false);

            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {
                        //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixels[y * width + x] = mBitmap.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * size + x] = 0xff000000;
                        } else {
                            pixels[y * size + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
