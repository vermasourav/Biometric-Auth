/*
 * Created by: V3RMA SOURAV on 30/11/23, 11:37 pm
 * Copyright © 2023 All rights reserved
 * Class name : BiometricUtils
 * Last modified:  21/10/23, 1:42 pm
 * Location: Bangalore, India
 */

package com.verma.android.biometric;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;


@SuppressLint({"MissingPermission"})
public class BiometricUtils {


    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    public static boolean isBiometricPromptEnabled() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    public static boolean isSdkVersionSupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static boolean isHardwareSupported(Context context) {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        return fingerprintManager.isHardwareDetected();
    }

    public static boolean isFingerprintAvailable(Context context) {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        return fingerprintManager.hasEnrolledFingerprints();
    }

    public static boolean isPermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED;
    }
}
