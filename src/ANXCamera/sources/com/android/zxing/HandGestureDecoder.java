package com.android.zxing;

import android.graphics.Rect;
import android.graphics.YuvImage;
import android.provider.MiuiSettings.ScreenEffect;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.handgesture.HandGesture;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.storage.Storage;
import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import miui.os.Environment;

public class HandGestureDecoder extends Decoder {
    private static boolean DEBUG = false;
    private static int DETECTION_FRAMES_PER_SECOND = 16;
    public static final String TAG = "HandGestureDecoder";
    private int mCameraId;
    private AtomicInteger mContinuousInterval = new AtomicInteger(0);
    private HandGesture mHandGesture = new HandGesture();
    private boolean mTargetDetected = false;
    private int mTipShowInterval = DETECTION_FRAMES_PER_SECOND;
    private boolean mTipVisible = true;
    private boolean mTriggeringPhoto = false;

    HandGestureDecoder() {
        this.mSubjects.toFlowable(BackpressureStrategy.LATEST).observeOn(Schedulers.computation()).map(new a(this)).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<? super T>) new b<Object>(this));
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x007e A[SYNTHETIC, Splitter:B:17:0x007e] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0089 A[SYNTHETIC, Splitter:B:22:0x0089] */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    private void dumpPreviewImage(PreviewImage previewImage) {
        String str = "Close stream failed!";
        String str2 = TAG;
        FileOutputStream fileOutputStream = null;
        try {
            long timestamp = previewImage.getTimestamp();
            int width = previewImage.getWidth();
            int height = previewImage.getHeight();
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory());
            sb.append("/DCIM/Camera/hand_");
            sb.append(String.valueOf(timestamp));
            sb.append(Storage.JPEG_SUFFIX);
            FileOutputStream fileOutputStream2 = new FileOutputStream(sb.toString());
            try {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("PreviewImage timestamp: [");
                sb2.append(timestamp);
                sb2.append("]");
                Log.d(str2, sb2.toString());
                YuvImage yuvImage = new YuvImage(previewImage.getData(), 17, width, height, null);
                yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, fileOutputStream2);
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.e(str2, "Dump preview Image failed!", e);
                    if (fileOutputStream == null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            Log.e(str2, str, e3);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                }
                throw th;
            }
            try {
                fileOutputStream2.close();
            } catch (IOException e4) {
                Log.e(str2, str, e4);
            }
        } catch (IOException e5) {
            e = e5;
            Log.e(str2, "Dump preview Image failed!", e);
            if (fileOutputStream == null) {
                fileOutputStream.close();
            }
        }
    }

    public /* synthetic */ Integer a(PreviewImage previewImage) throws Exception {
        Log.d(TAG, "HandGestureDecoder: decode E");
        int previewStatus = previewImage.getPreviewStatus();
        if (previewStatus == 1) {
            this.mHandGesture.init(previewImage.getCameraId());
        } else if (previewStatus != 2) {
            if (previewStatus == 3) {
                try {
                    this.mHandGesture.unInit();
                } catch (Exception unused) {
                }
            }
        } else if (!(previewImage.getData() == null || previewImage.getData().length == 0)) {
            if (DEBUG) {
                dumpPreviewImage(previewImage);
            }
            return Integer.valueOf(decode(previewImage));
        }
        return Integer.valueOf(-1);
    }

    public /* synthetic */ void b(Integer num) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Detected rect left = ");
        sb.append(num);
        sb.append(" ");
        sb.append(this.mTipShowInterval);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (num.intValue() >= 0) {
            this.mTargetDetected = true;
        } else {
            this.mTargetDetected = false;
            this.mContinuousInterval.set(0);
        }
        if (!this.mTriggeringPhoto) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Continuous interval: ");
            sb3.append(this.mContinuousInterval.get());
            Log.d(str, sb3.toString());
            if (this.mContinuousInterval.get() > 0) {
                this.mContinuousInterval.getAndDecrement();
            } else if (this.mTargetDetected) {
                Log.d(str, "Triggering countdown...");
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                if (cameraAction != null && !cameraAction.isDoingAction()) {
                    cameraAction.onShutterButtonClick(100);
                    this.mTriggeringPhoto = true;
                    this.mContinuousInterval.set(DETECTION_FRAMES_PER_SECOND * 3);
                    DataRepository.dataItemRunning().setHandGestureRunning(!this.mTriggeringPhoto);
                    this.mTipVisible = false;
                    this.mTipShowInterval = DETECTION_FRAMES_PER_SECOND;
                }
            }
            if (!this.mTipVisible && this.mTipShowInterval <= 0) {
                DataRepository.dataItemRunning().setHandGestureRunning(true);
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null && !topAlert.isExtraMenuShowing()) {
                    topAlert.reInitAlert(true);
                }
                this.mTipVisible = true;
            }
            int i = this.mTipShowInterval;
            if (i > 0) {
                this.mTipShowInterval = i - 1;
            }
        }
    }

    /* access modifiers changed from: protected */
    public int decode(PreviewImage previewImage) {
        int orientation = previewImage.getOrientation();
        if (orientation == -1) {
            orientation = 0;
        }
        return this.mHandGesture.detectGesture(previewImage.getData(), previewImage.getWidth(), previewImage.getHeight(), this.mCameraId == 1 ? 270 - orientation : (orientation + 90) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT);
    }

    public void init(int i) {
        reset();
        this.mCameraId = i;
        this.mSubjects.onNext(new PreviewImage(1, i));
        DataRepository.dataItemRunning().setHandGestureRunning(true);
    }

    public boolean needPreviewFrame() {
        return CameraSettings.isHandGestureOpen() && super.needPreviewFrame();
    }

    public void onPreviewFrame(PreviewImage previewImage) {
        PublishSubject<PreviewImage> publishSubject = this.mSubjects;
        if (publishSubject != null) {
            publishSubject.onNext(previewImage);
        }
    }

    public void quit() {
        super.quit();
        this.mSubjects.onNext(new PreviewImage(3, this.mCameraId));
        this.mSubjects.onComplete();
    }

    public void reset() {
        Log.d(TAG, "Reset");
        this.mDecodingCount.set(0);
        this.mTriggeringPhoto = false;
    }

    public void startDecode() {
        this.mDecoding = true;
        this.mDecodingCount.set(0);
    }
}
