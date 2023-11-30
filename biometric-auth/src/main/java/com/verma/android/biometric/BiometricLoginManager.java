/*
 * Created by: V3RMA SOURAV on 30/11/23, 11:37 pm
 * Copyright © 2023 All rights reserved
 * Class name : BiometricLoginManager
 * Last modified:  18/11/23, 3:25 pm
 * Location: Bangalore, India
 */

package com.verma.android.biometric;

import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;

public class BiometricLoginManager extends BiometricManagerV23 {


    protected CancellationSignal mCancellationSignal = new CancellationSignal();

    protected BiometricLoginManager(final BiometricBuilder biometricBuilder) {
        this.context = biometricBuilder.context;
        this.title = biometricBuilder.title;
        this.subtitle = biometricBuilder.subtitle;
        this.description = biometricBuilder.description;
        this.negativeButtonText = biometricBuilder.negativeButtonText;
    }


    public void authenticate(@NonNull final BiometricLoginCallback biometricLoginCallback) {
        if(null == title) {
            biometricLoginCallback.onBiometricSetupError(context.getString(R.string.error_dialog_title));
            return;
        }

        if(null == subtitle) {
            biometricLoginCallback.onBiometricSetupError(context.getString(R.string.error_dialog_sub_title));
            return;
        }

        if(null == description) {
            biometricLoginCallback.onBiometricSetupError(context.getString(R.string.error_dialog_description));
            return;
        }

        if(null == negativeButtonText) {
            biometricLoginCallback.onBiometricSetupError(context.getString(R.string.error_dialog_negative_button ));
            return;
        }


        if(!BiometricUtils.isSdkVersionSupported()) {
            biometricLoginCallback.onSdkVersionNotSupported();
            return;
        }

        if(!BiometricUtils.isPermissionGranted(context)) {
            biometricLoginCallback.onBiometricAuthenticationPermissionNotGranted();
            return;
        }

        if(!BiometricUtils.isHardwareSupported(context)) {
            biometricLoginCallback.onBiometricAuthenticationNotSupported();
            return;
        }

        if(!BiometricUtils.isFingerprintAvailable(context)) {
            biometricLoginCallback.onBiometricAuthenticationNotAvailable();
            return;
        }

        displayBiometricDialog(biometricLoginCallback);
    }

    public void cancelAuthentication(){
        if(BiometricUtils.isBiometricPromptEnabled()) {
            if (!mCancellationSignal.isCanceled())
                mCancellationSignal.cancel();
        }else{
            if (!mCancellationSignalV23.isCanceled())
                mCancellationSignalV23.cancel();
        }
    }



   public void displayBiometricDialog(BiometricLoginCallback biometricLoginCallback) {
        if(BiometricUtils.isBiometricPromptEnabled()) {
            displayBiometricPrompt(biometricLoginCallback);
        } else {
            displayBiometricPromptV23(biometricLoginCallback);
        }
    }

    private void displayBiometricPrompt(final BiometricLoginCallback biometricLoginCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BiometricPrompt biometricPrompt =
            new BiometricPrompt.Builder(context)
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setNegativeButton(negativeButtonText, context.getMainExecutor(), (dialogInterface, position) -> {
                        biometricLoginCallback.onAuthenticationCancelled();
                    })
                    .build();

            biometricPrompt.authenticate(mCancellationSignal, context.getMainExecutor(), new BiometricCallbackV28(biometricLoginCallback));

        }else{
            biometricLoginCallback.onBiometricAuthenticationNotSupported();
        }
    }


    public static class BiometricBuilder {

        private String title;
        private String subtitle;
        private String description;
        private String negativeButtonText;

        private Context context;
        public BiometricBuilder(Context context) {
            this.context = context;
        }

        public BiometricBuilder setTitle(@NonNull final String title) {
            this.title = title;
            return this;
        }

        public BiometricBuilder setSubtitle(@NonNull final String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BiometricBuilder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }


        public BiometricBuilder setNegativeButtonText(@NonNull final String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public BiometricLoginManager build() {
            return new BiometricLoginManager(this);
        }
    }
}
