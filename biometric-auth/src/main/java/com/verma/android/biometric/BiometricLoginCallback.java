/*
 * Created by: V3RMA SOURAV on 30/11/23, 11:37 pm
 * Copyright © 2023 All rights reserved
 * Class name : BiometricLoginCallback
 * Last modified:  18/11/23, 3:25 pm
 * Location: Bangalore, India
 */

package com.verma.android.biometric;

public interface BiometricLoginCallback {

    void onSdkVersionNotSupported();

    void onBiometricAuthenticationNotSupported();

    void onBiometricAuthenticationNotAvailable();

    void onBiometricAuthenticationPermissionNotGranted();

    void onBiometricSetupError(String error);

    void onAuthenticationFailed();

    void onAuthenticationCancelled();

    void onAuthenticationSuccessful();

    void onAuthenticationHelp(int helpCode, CharSequence helpString);

    void onAuthenticationError(int errorCode, CharSequence errString);
}
