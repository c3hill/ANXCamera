package com.android.camera.module.impl.component;

import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.Image;
import android.opengl.GLES20;
import android.os.Environment;
import android.os.Handler;
import android.provider.MiuiSettings.ScreenEffect;
import android.view.Surface;
import com.android.camera.ActivityBase;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSize;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.VMProcessing;
import com.android.camera.fragment.vv.VVItem;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.LiveModuleSubVV;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.LiveConfigVV;
import com.android.camera.protocol.ModeProtocol.LiveVVProcess;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera2.Camera2Proxy;
import com.ss.android.vesdk.VEEditor.MVConsts;
import com.xiaomi.mediaprocess.EffectCameraNotifier;
import com.xiaomi.mediaprocess.EffectMediaPlayer;
import com.xiaomi.mediaprocess.EffectMediaPlayer.SurfaceGravity;
import com.xiaomi.mediaprocess.EffectNotifier;
import com.xiaomi.mediaprocess.MediaComposeFile;
import com.xiaomi.mediaprocess.MediaEffectCamera;
import com.xiaomi.mediaprocess.MediaEffectGraph;
import com.xiaomi.mediaprocess.OpenGlRender;
import com.xiaomi.utils.SystemUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LiveSubVVImpl implements LiveConfigVV, ExternalFrameProcessor, EffectCameraNotifier {
    public static final String COMPOSE_PATH;
    private static final Executor PREVIEW_SAVER_EXECUTOR;
    public static final String SEGMENTS_PATH;
    /* access modifiers changed from: private */
    public static final String TAG = "LiveSubVVImpl";
    public static final String TEMPLATE_PATH;
    public static final String VV_DIR;
    private static final String WATERMARK_PATH;
    public static final String WORKSPACE_PATH;
    private static final BlockingQueue<Runnable> mPreviewRequestQueue = new LinkedBlockingQueue(32);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable runnable) {
            StringBuilder sb = new StringBuilder();
            sb.append("camera-vv-");
            sb.append(this.mCount.getAndIncrement());
            Thread thread = new Thread(runnable, sb.toString());
            thread.setPriority(10);
            return thread;
        }
    };
    /* access modifiers changed from: private */
    public LiveVVProcess liveVVProcess;
    private ActivityBase mActivity;
    private V6CameraGLSurfaceView mCameraView;
    private MediaComposeFile mComposeFile;
    private Context mContext;
    private int mCurrentIndex;
    private int mCurrentOrientation = -1;
    private VVItem mCurrentVVItem;
    private EffectMediaPlayer mEffectMediaPlayer;
    private Handler mHandler;
    private long mLastFrameTime;
    private MediaEffectCamera mMediaCamera;
    private MediaEffectGraph mMediaEffectGraph;
    private boolean mMediaRecorderRecording;
    private boolean mMediaRecorderRecordingPaused;
    private MiGLSurfaceViewRender mMiGLSurfaceViewRender;
    private boolean mNeedRequestRender;
    /* access modifiers changed from: private */
    public boolean mNeedStop;
    private OpenGlRender mOpenGlRender;
    /* access modifiers changed from: private */
    public boolean mPlayFinished;
    /* access modifiers changed from: private */
    public Disposable mRecordingTimerDisposable;
    private List<String> mTempVideoList;
    /* access modifiers changed from: private */
    public VMProcessing mVmProcessing;

    private static final class PreviewImageRunnable implements Runnable {
        private MediaEffectCamera mMediaCamera;
        public Image mPreviewImage;

        public PreviewImageRunnable(Image image, MediaEffectCamera mediaEffectCamera) {
            this.mPreviewImage = image;
            this.mMediaCamera = mediaEffectCamera;
        }

        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            this.mMediaCamera.PushExtraYAndUVFrame(this.mPreviewImage);
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            sb.append("");
            Log.e("push time：", sb.toString());
            this.mPreviewImage.close();
            this.mPreviewImage = null;
            this.mMediaCamera = null;
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/MIUI/Camera/vv/");
        VV_DIR = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(VV_DIR);
        sb2.append("template/");
        TEMPLATE_PATH = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(VV_DIR);
        sb3.append("workspace/");
        WORKSPACE_PATH = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(WORKSPACE_PATH);
        sb4.append("segments");
        SEGMENTS_PATH = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(WORKSPACE_PATH);
        sb5.append("compose/");
        COMPOSE_PATH = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(TEMPLATE_PATH);
        sb6.append("watermark.mp4");
        WATERMARK_PATH = sb6.toString();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, mPreviewRequestQueue, sThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        PREVIEW_SAVER_EXECUTOR = threadPoolExecutor;
    }

    private LiveSubVVImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
        this.mCameraView = activityBase.getGLView();
        this.mHandler = new Handler(this.mActivity.getMainLooper());
    }

    public static LiveSubVVImpl create(ActivityBase activityBase) {
        return new LiveSubVVImpl(activityBase);
    }

    /* access modifiers changed from: private */
    public void notifyModuleRecordingFinish() {
        resetFlag();
        BaseModule baseModule = (BaseModule) this.mActivity.getCurrentModule();
        if (baseModule.getModuleIndex() == 179) {
            ((LiveModuleSubVV) baseModule).stopVideoRecording(true, false);
        }
    }

    private void prepareEffectGraph() {
        VVItem vVItem = this.mCurrentVVItem;
        String str = vVItem.composeJsonPath;
        String str2 = vVItem.musicPath;
        ArrayList arrayList = new ArrayList(this.mTempVideoList);
        arrayList.add(WATERMARK_PATH);
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
        this.mMediaEffectGraph = new MediaEffectGraph();
        this.mMediaEffectGraph.ConstructMediaEffectGraph();
        this.mMediaEffectGraph.SetAudioMute(true);
        this.mMediaEffectGraph.AddSourceAndEffectByTemplate(strArr, str);
        this.mMediaEffectGraph.AddAudioTrack(str2, false);
    }

    private void resetFlag() {
        this.mMediaRecorderRecording = false;
        this.mNeedRequestRender = false;
        this.mNeedStop = false;
    }

    private void restoreWorkSpace() {
    }

    private void saveWorkSpace() {
    }

    private void startCountDown(long j) {
        long j2 = j / 100;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startCountDown: ");
        sb.append(j);
        Log.d(str, sb.toString());
        final long j3 = (j2 * 100) - 100;
        Observable.intervalRange(0, j2, 0, 100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer<? super T>) new Observer<Long>() {
            public void onComplete() {
                Log.e(LiveSubVVImpl.TAG, "onFinish");
            }

            public void onError(Throwable th) {
            }

            public void onNext(Long l) {
                LiveSubVVImpl.this.updateRecordingTime(j3 - (l.longValue() * 100));
            }

            public void onSubscribe(Disposable disposable) {
                LiveSubVVImpl.this.mRecordingTimerDisposable = disposable;
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateRecordingTime(long j) {
        float f2 = ((float) j) / 1000.0f;
        this.liveVVProcess.updateRecordingTime(String.format(Locale.ENGLISH, "%.1fS", new Object[]{Float.valueOf(Math.abs(f2))}));
    }

    public void OnNeedStopRecording() {
        Log.d(TAG, "OnNeedStopRecording");
        this.mHandler.post(new Runnable() {
            public void run() {
                LiveSubVVImpl.this.mNeedStop = true;
                LiveSubVVImpl.this.stopRecording();
            }
        });
    }

    public void OnNotifyRender() {
        Log.d(TAG, "OnNotifyRender");
        this.mNeedRequestRender = true;
        this.mCameraView.requestRender();
    }

    public void OnRecordFailed() {
        Log.d(TAG, "OnRecordFailed");
    }

    public void OnRecordFinish(String str) {
        if (!isRecording()) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            return;
        }
        this.mTempVideoList.add(str);
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("OnRecordFinish | s: ");
        sb.append(str);
        sb.append(" | ");
        sb.append(Thread.currentThread().getName());
        Log.d(str2, sb.toString());
        this.mHandler.post(new Runnable() {
            public void run() {
                LiveSubVVImpl.this.notifyModuleRecordingFinish();
            }
        });
    }

    public boolean canFinishRecording() {
        return this.mTempVideoList.size() == this.mCurrentVVItem.getEssentialFragmentSize();
    }

    public void combineVideoAudio(String str) {
        EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.StopPreView();
        }
        prepareEffectGraph();
        this.mComposeFile = new MediaComposeFile(this.mMediaEffectGraph);
        this.mComposeFile.ConstructMediaComposeFile(1920, ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_END_DEAULT, 31457280, 30);
        this.mComposeFile.SetComposeNotify(new EffectNotifier() {
            public void OnReceiveFailed() {
                android.util.Log.d(LiveSubVVImpl.TAG, "ComposeCameraRecord OnReceiveFinish");
                LiveSubVVImpl.this.mVmProcessing.updateState(9);
            }

            public void OnReceiveFinish() {
                android.util.Log.d(LiveSubVVImpl.TAG, "ComposeCameraRecord OnReceiveFinish");
                LiveSubVVImpl.this.mVmProcessing.updateState(8);
            }
        });
        this.mComposeFile.SetComposeFileName(str);
        this.mComposeFile.BeginComposeFile();
    }

    public void deleteLastFragment() {
        if (!this.mTempVideoList.isEmpty()) {
            int size = this.mTempVideoList.size() - 1;
            FileUtils.deleteFile((String) this.mTempVideoList.get(size));
            List<String> list = this.mTempVideoList;
            list.remove(list.size() - 1);
            this.mCurrentIndex = this.mTempVideoList.size();
            this.liveVVProcess.onRecordingFragmentUpdate(size, -this.mCurrentVVItem.getDuration(size));
        }
    }

    public CameraSize getAlgorithmPreviewSize(List<CameraSize> list) {
        return Util.getOptimalVideoSnapshotPictureSize(list, (double) getPreviewRatio(), 3840, 2160);
    }

    public int getAuthResult() {
        return 0;
    }

    public int getNextRecordStep() {
        return isRecording() ? 1 : 2;
    }

    public float getPreviewRatio() {
        return 1.7777777f;
    }

    public String getSegmentPath(int i) {
        return (String) this.mTempVideoList.get(i);
    }

    public void initResource() {
        FileUtils.makeDir(VV_DIR);
        FileUtils.makeSureNoMedia(VV_DIR);
        FileUtils.makeDir(TEMPLATE_PATH);
        FileUtils.makeDir(WORKSPACE_PATH);
        FileUtils.makeDir(SEGMENTS_PATH);
        FileUtils.makeDir(COMPOSE_PATH);
    }

    public boolean isProcessorReady() {
        return false;
    }

    public boolean isRecording() {
        return this.mMediaRecorderRecording;
    }

    public boolean isRecordingPaused() {
        return this.mMediaRecorderRecordingPaused;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        if (this.mOpenGlRender == null) {
            this.mOpenGlRender = new OpenGlRender();
            this.mMiGLSurfaceViewRender = new MiGLSurfaceViewRender(this.mOpenGlRender);
            this.mMiGLSurfaceViewRender.init();
        }
        if (isRecording() && this.mNeedRequestRender) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            this.mMiGLSurfaceViewRender.bind(rect, i, i2);
            this.mOpenGlRender.RenderFrame();
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        if (!(this.mCurrentOrientation == i2 || this.mMediaCamera == null || isRecording())) {
            this.mCurrentOrientation = i2;
            this.mMediaCamera.SetOrientation(180);
        }
    }

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        if (!isRecording() || this.mNeedStop) {
            this.mLastFrameTime = -1;
            return true;
        }
        if (this.mLastFrameTime == -1) {
            this.mLastFrameTime = System.currentTimeMillis();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis() - this.mLastFrameTime);
            sb.append("");
            Log.e("frame time：", sb.toString());
            this.mLastFrameTime = System.currentTimeMillis();
        }
        if (mPreviewRequestQueue.size() > 4) {
            Log.d(TAG, "queue full");
            return true;
        }
        PREVIEW_SAVER_EXECUTOR.execute(new PreviewImageRunnable(image, this.mMediaCamera));
        return false;
    }

    public void onRecordingNewFragmentFinished() {
        if (isRecording()) {
            resetFlag();
            stopRecording();
            Disposable disposable = this.mRecordingTimerDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                this.mRecordingTimerDisposable.dispose();
            }
            LiveVVProcess liveVVProcess2 = this.liveVVProcess;
            int i = this.mCurrentIndex;
            liveVVProcess2.onRecordingFragmentUpdate(i, -this.mCurrentVVItem.getDuration(i));
            return;
        }
        LiveVVProcess liveVVProcess3 = this.liveVVProcess;
        int i2 = this.mCurrentIndex;
        liveVVProcess3.onRecordingFragmentUpdate(i2, this.mCurrentVVItem.getDuration(i2));
    }

    public void pausePlay() {
        this.mEffectMediaPlayer.PausePreView();
    }

    public void prepare() {
        System.loadLibrary("vvc++_shared");
        System.loadLibrary("vvmediaeditor");
        SystemUtil.Init(this.mContext, 123);
        try {
            Util.verifyAssetZip(this.mContext, "vv/watermark.zip", TEMPLATE_PATH, 8192);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        prepare(DataRepository.dataItemLive().getCurrentVVItem());
        this.mMediaCamera = new MediaEffectCamera();
        this.mMediaCamera.ConstructMediaEffectCamera(1920, ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_END_DEAULT, 20971520, 30, this);
    }

    public void prepare(VVItem vVItem) {
        this.mCurrentVVItem = vVItem;
        if (this.mVmProcessing == null) {
            this.mVmProcessing = (VMProcessing) DataRepository.dataItemObservable().get(VMProcessing.class);
        }
        this.mTempVideoList = this.mVmProcessing.getTempVideoList();
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(228, this);
    }

    public void release() {
        if (isRecording()) {
            resetFlag();
            stopRecording();
        }
        EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.StopPreView();
        }
        saveWorkSpace();
        this.mHandler.removeCallbacksAndMessages(null);
        Disposable disposable = this.mRecordingTimerDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        SystemUtil.UnInit();
    }

    public void releaseRender() {
    }

    public void resumePlay() {
        this.mEffectMediaPlayer.ResumePreView();
    }

    public void startPlay(Surface surface) {
        prepareEffectGraph();
        this.mPlayFinished = false;
        this.mEffectMediaPlayer = new EffectMediaPlayer(this.mMediaEffectGraph);
        this.mEffectMediaPlayer.ConstructMediaPlayer();
        this.mEffectMediaPlayer.SetPlayerNotify(new EffectNotifier() {
            public void OnReceiveFailed() {
                Log.d("OnReceiveFailed:", "yes");
                LiveSubVVImpl.this.mPlayFinished = true;
                LiveSubVVImpl.this.liveVVProcess.onResultPreviewFinished(false);
            }

            public void OnReceiveFinish() {
                Log.d("OnReceiveFinish:", "yes");
                LiveSubVVImpl.this.mPlayFinished = true;
                LiveSubVVImpl.this.liveVVProcess.onResultPreviewFinished(true);
            }
        });
        this.mEffectMediaPlayer.SetViewSurface(surface);
        this.mEffectMediaPlayer.setGravity(SurfaceGravity.SurfaceGravityResizeAspectFit, 1920, ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_END_DEAULT);
        this.mEffectMediaPlayer.SetPlayLoop(true);
        this.mEffectMediaPlayer.SetGraphLoop(true);
        this.mEffectMediaPlayer.StartPreView();
    }

    public void startPreview(int i, int i2, int i3, CameraScreenNail cameraScreenNail) {
        cameraScreenNail.setExternalFrameProcessor(this);
    }

    public void startRecordingNewFragment() {
        startRecordingNextFragment();
    }

    public void startRecordingNextFragment() {
        this.mMediaRecorderRecordingPaused = false;
        ((AudioManager) this.mActivity.getSystemService(MVConsts.TYPE_AUDIO)).requestAudioFocus(new OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int i) {
            }
        }, 3, 1);
        if (this.liveVVProcess == null) {
            this.liveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
        }
        this.mCurrentIndex = this.mTempVideoList.size();
        if (this.mCurrentIndex == 0) {
            FileUtils.deleteSubFiles(SEGMENTS_PATH);
        }
        long duration = this.mCurrentVVItem.getDuration(this.mCurrentIndex);
        VVItem vVItem = this.mCurrentVVItem;
        String str = vVItem.musicPath;
        String str2 = vVItem.configJsonPath;
        String str3 = vVItem.filterPath;
        this.liveVVProcess.onRecordingNewFragmentStart(this.mCurrentIndex, duration);
        long j = 0;
        for (int i = 0; i < this.mCurrentIndex; i++) {
            j += this.mCurrentVVItem.getDuration(i);
        }
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("start : !!");
        sb.append(duration);
        String str5 = " | ";
        sb.append(str5);
        sb.append(str);
        sb.append(str5);
        sb.append(str2);
        sb.append(str5);
        sb.append(str3);
        sb.append(str5);
        sb.append(j);
        Log.e(str4, sb.toString());
        this.mMediaCamera.StartRecording(this.mCurrentIndex, SEGMENTS_PATH, str2, str3, str, j);
        this.mMediaRecorderRecording = true;
        startCountDown(duration);
    }

    public void stopRecording() {
        this.mMediaCamera.StopRecording();
    }

    public void trackVideoParams() {
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(228, this);
        release();
    }
}
