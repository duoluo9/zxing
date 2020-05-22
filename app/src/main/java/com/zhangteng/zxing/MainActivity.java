package com.zhangteng.zxing;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.zxing.camera.CameraConfigurationUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends CaptureActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        new AndroidPermission.Buidler()
                .with(this)
                .permission(Permission.Group.CAMERA)
                .callback(new Callback() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void failure() {

                    }

                    @Override
                    public void nonExecution() {

                    }
                })
                .build()
                .excute();
        ImageView ivFlash = findViewById(R.id.ivFlash);
        if (!hasTorch()) {
            ivFlash.setVisibility(View.GONE);
        }
    }

    /**
     * 开启或关闭闪光灯（手电筒）
     *
     * @param on {@code true}表示开启，{@code false}表示关闭
     */
    public void setTorch(boolean on) {
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        CameraConfigurationUtils.setTorch(parameters, on);
        camera.setParameters(parameters);
    }

    /**
     * 检测是否支持闪光灯（手电筒）
     *
     * @return
     */
    public boolean hasTorch() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    /**
     * 扫码结果回调
     *
     * @param result 扫码结果
     * @return
     */
    @Override
    public boolean onResultCallback(String result) {

        return true;
    }

    /**
     * 获取指定url中的某个参数
     *
     * @param url  目标url
     * @param name 目标字段名
     * @return
     */
    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "([?&])#?" + name + "=[a-zA-Z0-9]*(&)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }

    private void clickFlash(View v) {
        boolean isSelected = v.isSelected();
        setTorch(!isSelected);
        v.setSelected(!isSelected);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivFlash) {
            clickFlash(v);
        }
    }
}