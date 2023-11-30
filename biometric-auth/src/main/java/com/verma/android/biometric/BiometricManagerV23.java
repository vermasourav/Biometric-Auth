/*
 * Created by: V3RMA SOURAV on 30/11/23, 11:37 pm
 * Copyright © 2023 All rights reserved
 * Class name : BiometricManagerV23
 * Last modified:  18/11/23, 3:37 pm
 * Location: Bangalore, India
 */

package com.verma.android.biometric;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


@RequiresApi(api = Build.VERSION_CODES.M)
@TargetApi(Build.VERSION_CODES.M)
public class BiometricManagerV23 {

    private static final String KEY_NAME = UUID.randomUUID().toString();

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManagerCompat.CryptoObject cryptoObject;


    protected Context context;

    protected String title;
    protected String subtitle;
    protected String description;
    protected String negativeButtonText;
    private BiometricDialogV23 biometricDialogV23;
    protected CancellationSignal mCancellationSignalV23 = new CancellationSignal();


    public void displayBiometricPromptV23(final BiometricLoginCallback biometricLoginCallback) {
        generateKey();
        if(initCipher()) {

            cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);

            fingerprintManagerCompat.authenticate(cryptoObject, 0, mCancellationSignalV23,
                    new FingerprintManagerCompat.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errMsgId, CharSequence errString) {
                            super.onAuthenticationError(errMsgId, errString);
                            updateStatus(String.valueOf(errString));
                            biometricLoginCallback.onAuthenticationError(errMsgId, errString);
                        }

                        @Override
                        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                            super.onAuthenticationHelp(helpMsgId, helpString);
                            updateStatus(String.valueOf(helpString));
                            biometricLoginCallback.onAuthenticationHelp(helpMsgId, helpString);
                        }

                        @Override
                        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            dismissDialog();
                            biometricLoginCallback.onAuthenticationSuccessful();
                        }
                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            updateStatus(context.getString(R.string.biometric_failed));
                            biometricLoginCallback.onAuthenticationFailed();
                        }
                    }, null);

            displayBiometricDialog(biometricLoginCallback);
        }
    }



    private void displayBiometricDialog(final BiometricLoginCallback biometricLoginCallback) {
        biometricDialogV23 = new BiometricDialogV23(context, biometricLoginCallback);
        biometricDialogV23.setTitle(title);
        biometricDialogV23.setSubtitle(subtitle);
        biometricDialogV23.setDescription(description);
        biometricDialogV23.setButtonText(negativeButtonText);
        biometricDialogV23.show();
    }



    private void dismissDialog() {
        if(biometricDialogV23 != null) {
            biometricDialogV23.dismiss();
        }
    }

    private void updateStatus(String status) {
        if(biometricDialogV23 != null) {
            biometricDialogV23.updateStatus(status);
        }
    }

    private void generateKey() {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT
                    | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
        }
    }



    private boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;


        } catch (KeyPermanentlyInvalidatedException e) {
            return false;

        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {

            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
