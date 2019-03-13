package com.chen.good.diary.activity;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chen.good.diary.R;
import com.chen.good.diary.base.BaseActivity;
import com.chen.good.diary.fragment.FingerprintAuthenticationDialogFragment;
import com.chen.good.diary.util.SPUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 绑定layout
 */
@ContentView(R.layout.activity_lead)
public class LeadActivity extends BaseActivity {

    /**
     * 初始化控件
     */
    @ViewInject(R.id.btn_showdialog)
    private Button btn;

    private KeyStore mKeyStore;//key
    private KeyGenerator mKeyGenerator;

    static final String DIALOG_FRAGMENT_TAG = "myFragment";
    public static final String DEFAULT_KEY_NAME = "default_key";
    private static final String SECRET_MESSAGE = "Very secret message";

    static final String SP_KEY = "use_fingerprint_to_authenticate_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBar();
        init();
    }

    private void initBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void init() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
        try {
            mKeyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }

        KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
        FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);

        //判断是否支持指纹，版本太低
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!keyguardManager.isKeyguardSecure()) {
                showToast("屏幕未设置锁屏 请先设置锁屏并添加一个指纹");
                findViewById(R.id.btn_showdialog).setVisibility(View.GONE);
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                findViewById(R.id.btn_showdialog).setVisibility(View.GONE);
                showToast("至少在系统中添加一个指");
            }
        } else {
            findViewById(R.id.btn_showdialog).setVisibility(View.GONE);
        }

        createKey(DEFAULT_KEY_NAME, true);
    }

    /**
     * @param invalidatedByBiometricEnrollment 如果传递@code false，即使注册了新指纹，创建的密钥也不会失效。
     *                                         默认值为@code true，因此传递@code true不会更改行为（如果注册了新指纹，密钥将失效）。
     *                                         请注意，此参数仅在应用程序在Android N开发者预览版上工作时有效。
     */
    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        try {
            mKeyStore.load(null);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Event({R.id.btn_showdialog})
    private void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_showdialog:
                try {
                    showFingerDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    private void showFingerDialog() throws Exception {
        Cipher defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        if (initCipher(defaultCipher, DEFAULT_KEY_NAME)) {
            FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();
            fragment.setCryptoObject(new FingerprintManager.CryptoObject(defaultCipher));

            boolean useFingerprintPreference = SPUtil.getBoolean(SP_KEY, true);
            if (useFingerprintPreference) {
                fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
            } else {
                fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
            }
            fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();
            fragment.setCryptoObject(new FingerprintManager.CryptoObject(defaultCipher));
            fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
            fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        }
    }

    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    /**
     * Proceed the purchase operation
     *
     * @param withFingerprint {@code true} if the purchase was made by using a fingerprint
     * @param cryptoObject    the Crypto object
     */
    public void onPurchased(boolean withFingerprint,
                            @Nullable FingerprintManager.CryptoObject cryptoObject) {
        if (withFingerprint) {
            // If the user has authenticated with fingerprint, verify that using cryptography and
            // then show the confirmation message.
            assert cryptoObject != null;
            tryEncrypt(cryptoObject.getCipher());
        } else {
            // Authentication happened with backup password. Just show the confirmation message.
            showConfirmation(null);
        }
    }

    /**
     * Tries to encrypt some data with the generated key in {@link #createKey} which is
     * only works if the user has just authenticated via fingerprint.
     */
    private void tryEncrypt(Cipher cipher) {
        try {
            byte[] encrypted = cipher.doFinal(SECRET_MESSAGE.getBytes());
            showConfirmation(encrypted);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            Toast.makeText(this, "Failed to encrypt the data with the generated key. "
                    + "Retry the purchase", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to encrypt the data with the generated key." + e.getMessage());
        }
    }

    //显示key
    private void showConfirmation(byte[] encrypted) {
//        findViewById(R.id.confirmation_message).setVisibility(View.VISIBLE);
//        if (encrypted != null) {
//            TextView v = findViewById(R.id.encrypted_message);
//            v.setVisibility(View.VISIBLE);
//            v.setText(Base64.encodeToString(encrypted, 0 /* flags */));
//        }
    }
}
