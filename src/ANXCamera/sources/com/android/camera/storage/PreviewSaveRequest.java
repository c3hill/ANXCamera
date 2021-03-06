package com.android.camera.storage;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.text.TextUtils;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.db.DbRepository;
import com.android.camera.db.element.SaveTask;
import com.android.camera.log.Log;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.parallelservice.util.ParallelUtil.ParallelProvider;

public final class PreviewSaveRequest extends AbstractSaveRequest {
    private static final String TAG = "PreviewSaveRequest";
    private String algorithmName;
    private Context context;
    private byte[] data;
    private long date;
    private boolean finalImage;
    private int height;
    private PictureInfo info;
    private boolean isParallelProcess;
    private Location loc;
    private String mSavePath;
    private boolean needThumbnail;
    private int orientation;
    private SaverCallback saverCallback;
    private int size;
    private int width;

    private PreviewSaveRequest() {
    }

    PreviewSaveRequest(ParallelTaskData parallelTaskData, SaverCallback saverCallback2) {
        this.mParallelTaskData = parallelTaskData;
        setSaverCallback(saverCallback2);
        this.size = caculateMemoryUsed(this.mParallelTaskData);
    }

    PreviewSaveRequest(byte[] bArr, boolean z, String str, long j, Location location, int i, int i2, int i3, boolean z2, boolean z3, String str2, PictureInfo pictureInfo) {
        reFillSaveRequest(bArr, z, str, j, location, i, i2, i3, z2, z3, str2, pictureInfo);
    }

    public int getSize() {
        return this.size;
    }

    public boolean isFinal() {
        return this.finalImage;
    }

    public void onFinish() {
        this.data = null;
        this.saverCallback.onSaveFinish(getSize());
    }

    /* access modifiers changed from: protected */
    public void reFillSaveRequest(byte[] bArr, boolean z, String str, long j, Location location, int i, int i2, int i3, boolean z2, boolean z3, String str2, PictureInfo pictureInfo) {
        this.data = bArr;
        this.needThumbnail = z;
        this.date = j;
        this.mSavePath = str;
        this.loc = location == null ? null : new Location(location);
        this.width = i;
        this.height = i2;
        this.orientation = i3;
        this.finalImage = z2;
        this.isParallelProcess = z3;
        this.algorithmName = str2;
        this.info = pictureInfo;
    }

    public void run() {
        save();
        onFinish();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0054, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0142, code lost:
        return;
     */
    public void save() {
        parserParallelTaskData();
        if (this.data != null && !TextUtils.isEmpty(this.mSavePath)) {
            synchronized (this.mSavePath.intern()) {
                SaveTask itemByPath = DbRepository.dbItemSaveTask().getItemByPath(this.mSavePath);
                if (itemByPath != null) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("save preview: task is exist! isValid = ");
                    sb.append(itemByPath.isValid());
                    Log.w(str, sb.toString());
                    if (itemByPath.isValid()) {
                        ParallelProvider.deleteInProvider(this.context, itemByPath.getMediaStoreId().longValue());
                    }
                } else {
                    SaveTask saveTask = (SaveTask) DbRepository.dbItemSaveTask().generateItem(this.date);
                    saveTask.setPath(this.mSavePath);
                    DbRepository.dbItemSaveTask().endItemAndInsert(saveTask, 0);
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("insert preview picture:");
                    sb2.append(this.mSavePath);
                    Log.d(str2, sb2.toString());
                    String fileTitleFromPath = Util.getFileTitleFromPath(this.mSavePath);
                    Uri addImage = Storage.addImage(this.context, fileTitleFromPath, this.date, this.loc, this.orientation, this.data, this.width, this.height, false, false, false, this.algorithmName != null && this.algorithmName.equals(Util.ALGORITHM_NAME_MIMOJI_CAPTURE), this.isParallelProcess, this.algorithmName, this.info);
                    Storage.getAvailableSpace();
                    boolean z = this.needThumbnail && this.saverCallback.needThumbnail(isFinal());
                    if (addImage != null) {
                        if (z) {
                            int highestOneBit = Integer.highestOneBit((int) Math.ceil(Math.max((double) this.width, (double) this.height) / 512.0d));
                            Log.d(TAG, "image save try to create thumbnail");
                            Thumbnail createThumbnail = Thumbnail.createThumbnail(this.data, this.orientation, highestOneBit, addImage, false);
                            if (createThumbnail != null) {
                                this.saverCallback.postUpdateThumbnail(createThumbnail, true);
                            } else {
                                this.saverCallback.postHideThumbnailProgressing();
                            }
                        } else {
                            this.saverCallback.updatePreviewThumbnailUri(-1, addImage);
                        }
                        this.saverCallback.notifyNewMediaData(addImage, fileTitleFromPath, 2);
                        Log.d(TAG, "image save finished");
                    } else {
                        Log.e(TAG, "image save failed");
                        if (z) {
                            this.saverCallback.postHideThumbnailProgressing();
                        }
                    }
                }
            }
        }
    }

    public void setContextAndCallback(Context context2, SaverCallback saverCallback2) {
        this.context = context2;
        this.saverCallback = saverCallback2;
    }
}
