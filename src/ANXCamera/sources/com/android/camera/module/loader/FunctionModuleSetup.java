package com.android.camera.module.loader;

import com.android.camera.Camera;
import com.android.camera.constant.GlobalConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.EffectController;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.Camera2OpenManager;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

public class FunctionModuleSetup extends Func1Base<BaseModule, BaseModule> {
    private static final String TAG = "FunctionModuleSetup";

    public FunctionModuleSetup(int i) {
        super(i);
    }

    public NullHolder<BaseModule> apply(@NonNull NullHolder<BaseModule> nullHolder) throws Exception {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("apply: module isPresent = ");
        sb.append(nullHolder.isPresent());
        Log.d(str, sb.toString());
        if (!nullHolder.isPresent()) {
            return nullHolder;
        }
        BaseModule baseModule = (BaseModule) nullHolder.get();
        if (baseModule.isDeparted()) {
            return NullHolder.ofNullable(baseModule, 225);
        }
        EffectController.getInstance().reset();
        Camera activity = baseModule.getActivity();
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        int i = this.mTargetMode;
        String str2 = "pref_video_speed_fast_key";
        if (i != 162) {
            String str3 = "pref_camera_square_mode_key";
            if (i == 163) {
                dataItemRunning.switchOn(str3);
            } else if (i == 165) {
                dataItemRunning.switchOn(str3);
            } else if (i == 167) {
                dataItemRunning.switchOn("pref_camera_manual_mode_key");
            } else if (i == 169) {
                dataItemRunning.switchOn(str2);
            } else if (i == 171) {
                String str4 = "pref_camera_portrait_mode_key";
                if (dataItemGlobal.getCurrentCameraId() == 0) {
                    dataItemRunning.switchOn(str4);
                } else {
                    dataItemRunning.switchOff(str4);
                }
            } else if (i != 174) {
                if (i == 175) {
                    int currentCameraId = dataItemGlobal.getCurrentCameraId();
                    dataItemRunning.getComponentUltraPixel().switchOnCurrentSupported(175, currentCameraId, Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(currentCameraId, 175));
                }
            } else if (activity.startFromKeyguard()) {
                DataRepository.dataItemLive().setRecordSegmentTimeInfo(null);
            }
        } else {
            dataItemRunning.switchOff(str2);
        }
        baseModule.getActivity().removeNewBie();
        if (baseModule.isDeparted()) {
            return NullHolder.ofNullable(baseModule, 225);
        }
        try {
            baseModule.setCameraDevice(Camera2OpenManager.getInstance().getCurrentCamera2Device());
            baseModule.onCreate(this.mTargetMode, dataItemGlobal.getCurrentCameraId());
            if (baseModule.isCreated()) {
                baseModule.onResume();
                baseModule.registerProtocol();
            }
            return nullHolder;
        } catch (IllegalArgumentException e2) {
            Log.e(TAG, "Module init error: ", e2);
            baseModule.setDeparted();
            return NullHolder.ofNullable(null, 237);
        }
    }

    public Scheduler getWorkThread() {
        return GlobalConstant.sCameraSetupScheduler;
    }
}
