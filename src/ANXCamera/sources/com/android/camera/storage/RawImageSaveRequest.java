package com.android.camera.storage;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureResult;
import com.xiaomi.camera.base.PerformanceTracker;

public class RawImageSaveRequest implements SaveRequest {
    private static final String TAG = "RawImageSaveRequest";
    private CaptureResult captureResult;
    private CameraCharacteristics characteristics;
    private Context context;
    private byte[] data;
    private int height;
    private int orientation;
    private SaverCallback saverCallback;
    private int size;
    public String title;
    private int width;

    public RawImageSaveRequest(byte[] bArr, CaptureResult captureResult2, CameraCharacteristics cameraCharacteristics, String str, int i, int i2, int i3) {
        this.data = bArr;
        this.captureResult = captureResult2;
        this.characteristics = cameraCharacteristics;
        this.title = str;
        this.width = i;
        this.height = i2;
        this.size = bArr == null ? 0 : bArr.length;
        this.orientation = i3;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isFinal() {
        return true;
    }

    public void onFinish() {
        this.data = null;
        this.saverCallback.onSaveFinish(getSize());
    }

    public void run() {
        save();
        PerformanceTracker.trackImageSaver(this.data, 1);
        onFinish();
    }

    public void save() {
        Storage.addRawImage(this.context, this.title, this.characteristics, this.captureResult, this.data, this.width, this.height, this.orientation);
        Storage.getAvailableSpace();
    }

    public void setContextAndCallback(Context context2, SaverCallback saverCallback2) {
        this.context = context2;
        this.saverCallback = saverCallback2;
    }
}
