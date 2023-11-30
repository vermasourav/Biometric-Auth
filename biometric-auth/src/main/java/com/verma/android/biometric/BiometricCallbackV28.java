/*
 * Created by: V3RMA SOURAV on 30/11/23, 11:37 pm
 * Copyright © 2023 All rights reserved
 * Class name : BiometricCallbackV28
 * Last modified:  18/11/23, 3:25 pm
 * Location: Bangalore, India
 */

package com.verma.android.biometric;

import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.P)
public class BiometricCallbackV28 extends BiometricPrompt.AuthenticationCallback {

    private BiometricLoginCallback biometricLoginCallback;
    public BiometricCallbackV28(BiometricLoginCallback biometricLoginCallback) {
        this.biometricLoginCallback = biometricLoginCallback;
    }


    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        biometricLoginCallback.onAuthenticationSuccessful();
    }


    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        biometricLoginCallback.onAuthenticationHelp(helpCode, helpString);
    }


    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        biometricLoginCallback.onAuthenticationError(errorCode, errString);
    }


    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        biometricLoginCallback.onAuthenticationFailed();
    }
}
