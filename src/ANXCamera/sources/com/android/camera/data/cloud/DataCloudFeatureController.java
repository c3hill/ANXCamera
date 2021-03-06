package com.android.camera.data.cloud;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.android.camera.CameraAppImpl;
import com.android.camera.data.cloud.DataCloud.CloudFeature;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.android.camera.data.data.config.SupportedConfigs;
import com.android.camera.log.Log;
import java.util.Collection;

public class DataCloudFeatureController implements CloudFeature {
    public static final String KEY = "cloud_feature";
    private static final String TAG = "DataCloudFeatureController";
    private SharedPreferences mPreferences;
    private boolean mReady;

    private SharedPreferences getSharedPreferences() {
        if (this.mPreferences == null) {
            initPreferences();
        }
        return this.mPreferences;
    }

    private void initPreferences() {
        this.mPreferences = CameraAppImpl.getAndroidContext().getSharedPreferences(provideKey(), 0);
    }

    public Editor editor() {
        return getSharedPreferences().edit();
    }

    public SupportedConfigs filterFeature(SupportedConfigs supportedConfigs) {
        if (!this.mReady) {
            return supportedConfigs;
        }
        SupportedConfigs supportedConfigs2 = new SupportedConfigs();
        SharedPreferences sharedPreferences = getSharedPreferences();
        for (int i = 0; i < supportedConfigs.getLength(); i++) {
            int config = supportedConfigs.getConfig(i);
            boolean z = true;
            try {
                z = sharedPreferences.getBoolean(SupportedConfigFactory.getConfigKey(config), true);
            } catch (RuntimeException unused) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unknown config ");
                sb.append(config);
                Log.w(str, sb.toString());
            }
            if (z) {
                supportedConfigs2.add(config);
            }
        }
        return supportedConfigs2;
    }

    public Collection<String> getAllDisabledFeatures() {
        return getSharedPreferences().getAll().keySet();
    }

    public String provideKey() {
        return KEY;
    }

    public void setReady(boolean z) {
        this.mReady = z;
    }
}
