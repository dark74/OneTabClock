package com.dk.onetabclock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLockNow;
    private ComponentName mAdminReciver;
    private static final int ADMIN_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        lockNow();
    }

    private void initView() {
        mBtnLockNow = (Button) findViewById(R.id.btn_lock_now);
        mBtnLockNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lock_now:
                lockNow();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADMIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void lockNow() {
        mAdminReciver = new ComponentName(this, MyAdminDeviceReceiver.class);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        if (!devicePolicyManager.isAdminActive(mAdminReciver)) {//未授权
            goToPermission();
        }

        if (devicePolicyManager.isAdminActive(mAdminReciver)) {
            devicePolicyManager.lockNow();
        }
        finish();
    }

    private void goToPermission() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminReciver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.app_name) + "-授权设备管理权");
        startActivityForResult(intent, ADMIN_REQUEST_CODE);
    }
}
