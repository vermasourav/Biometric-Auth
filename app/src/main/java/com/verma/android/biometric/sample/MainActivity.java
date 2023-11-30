/*
 * Created by: V3RMA SOURAV on 30/11/23, 11:37 pm
 * Copyright © 2023 All rights reserved
 * Class name : MainActivity
 * Last modified:  30/11/23, 11:33 pm
 * Location: Bangalore, India
 */

package com.verma.android.biometric.sample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.verma.android.biometric.BiometricLoginCallback;
import com.verma.android.biometric.BiometricLoginManager;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView logs;
    BiometricLoginManager mBiometricLoginManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn_authenticate);
        logs = findViewById(R.id.logs);

        button.setOnClickListener(view -> {
            logs.setText("");
            mBiometricLoginManager = new BiometricLoginManager.BiometricBuilder(MainActivity.this)
                    .setTitle(getString(R.string.biometric_title))
                    .setSubtitle(getString(R.string.biometric_subtitle))
                    .setDescription(getString(R.string.biometric_description))
                    .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                    .build();

            mBiometricLoginManager.authenticate(biometricLoginCallback);
        });
    }
    BiometricLoginCallback biometricLoginCallback = new BiometricLoginCallback() {
        @Override
        public void onSdkVersionNotSupported() {
            logs.setText(getString(R.string.biometric_error_sdk_not_supported));
        }

        @Override
        public void onBiometricAuthenticationNotSupported() {
            logs.setText(getString(R.string.biometric_error_hardware_not_supported));
        }

        @Override
        public void onBiometricAuthenticationNotAvailable() {
            logs.setText(getString(R.string.biometric_error_fingerprint_not_available));
        }

        @Override
        public void onBiometricAuthenticationPermissionNotGranted() {
            logs.setText(getString(R.string.biometric_error_permission_not_granted));
        }

        @Override
        public void onBiometricSetupError(String error) {
            logs.setText(error);
        }

        @Override
        public void onAuthenticationFailed() {
            logs.setText(getString(R.string.biometric_failure));
        }

        @Override
        public void onAuthenticationCancelled() {
            logs.setText(getString(R.string.biometric_cancelled));
            mBiometricLoginManager.cancelAuthentication();
        }

        @Override
        public void onAuthenticationSuccessful() {
            logs.setText(getString(R.string.biometric_success));
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            logs.setText(helpString);
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            logs.setText(errString);
            //Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
        }
    };

}
