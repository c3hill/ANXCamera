package com.android.camera.data.data.config;

import android.support.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.List;

public class ComponentConfigAi extends ComponentData {
    private boolean mIsClosed;

    public ComponentConfigAi(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    public void clearClosed() {
        this.mIsClosed = false;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return null;
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List<ComponentDataItem> getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_camera_ai_scene_mode_key";
    }

    public boolean isAiSceneOn(int i) {
        if (!isEmpty() && !isClosed()) {
            return this.mParentDataItem.getBoolean(getKey(i), DataRepository.dataItemFeature().za());
        }
        return false;
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public List<ComponentDataItem> reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        List<ComponentDataItem> list = this.mItems;
        if (list == null) {
            this.mItems = new ArrayList();
        } else {
            list.clear();
        }
        String str = "on";
        if (i == 163 || i == 165) {
            if (i2 == 0) {
                if (DataRepository.dataItemFeature().Ab()) {
                    this.mItems.add(new ComponentDataItem((int) R.drawable.ic_new_ai_scene_off, (int) R.drawable.ic_new_ai_scene_on, (int) R.string.accessibility_ai_scene_on, str));
                }
            } else if (DataRepository.dataItemFeature().Nc()) {
                this.mItems.add(new ComponentDataItem((int) R.drawable.ic_new_ai_scene_off, (int) R.drawable.ic_new_ai_scene_on, (int) R.string.accessibility_ai_scene_on, str));
            }
        } else if (i == 171) {
            if (i2 == 0) {
                if (DataRepository.dataItemFeature().Vc()) {
                    this.mItems.add(new ComponentDataItem((int) R.drawable.ic_new_ai_scene_off, (int) R.drawable.ic_new_ai_scene_on, (int) R.string.accessibility_ai_scene_on, str));
                }
            } else if (DataRepository.dataItemFeature().Nc()) {
                this.mItems.add(new ComponentDataItem((int) R.drawable.ic_new_ai_scene_off, (int) R.drawable.ic_new_ai_scene_on, (int) R.string.accessibility_ai_scene_on, str));
            }
        }
        return this.mItems;
    }

    public void setAiScene(int i, boolean z) {
        if (!isEmpty()) {
            setClosed(false);
            this.mParentDataItem.editor().putBoolean(getKey(i), z).apply();
        }
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }
}
