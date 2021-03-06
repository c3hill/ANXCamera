package com.android.camera.data.data.config;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.List;

public class ComponentConfigSlowMotion extends ComponentData {
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_120 = "slow_motion_120";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_240 = "slow_motion_240";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_960 = "slow_motion_960";
    private static final String[] SLOW_MOTION_MODE;

    static {
        boolean pc = DataRepository.dataItemFeature().pc();
        String str = DATA_CONFIG_NEW_SLOW_MOTION_240;
        String str2 = DATA_CONFIG_NEW_SLOW_MOTION_120;
        if (pc) {
            SLOW_MOTION_MODE = new String[]{DATA_CONFIG_NEW_SLOW_MOTION_960, str2, str};
        } else if (DataRepository.dataItemFeature().oc()) {
            SLOW_MOTION_MODE = new String[]{str2, str};
        } else if (DataRepository.dataItemFeature().sc()) {
            SLOW_MOTION_MODE = new String[]{str2};
        } else {
            SLOW_MOTION_MODE = null;
        }
    }

    public ComponentConfigSlowMotion(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    public int getContentDesc() {
        String componentValue = getComponentValue(172);
        return DATA_CONFIG_NEW_SLOW_MOTION_120.equals(componentValue) ? R.string.accessibility_camera_video_960fps_120 : DATA_CONFIG_NEW_SLOW_MOTION_240.equals(componentValue) ? R.string.accessibility_camera_video_960fps_240 : R.string.accessibility_camera_video_960fps_960;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return SLOW_MOTION_MODE[0];
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public int getImageResource() {
        String componentValue = getComponentValue(172);
        return DATA_CONFIG_NEW_SLOW_MOTION_120.equals(componentValue) ? R.drawable.ic_new_video_960fps_120 : DATA_CONFIG_NEW_SLOW_MOTION_240.equals(componentValue) ? R.drawable.ic_new_video_960fps_240 : R.drawable.ic_new_video_960fps_960;
    }

    public List<ComponentDataItem> getItems() {
        return new ArrayList();
    }

    public String getKey(int i) {
        return DataItemConfig.DATA_CONFIG_NEW_SLOW_MOTION_KEY;
    }

    public String getNextValue(int i) {
        String componentValue = getComponentValue(i);
        int length = SLOW_MOTION_MODE.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (TextUtils.equals(SLOW_MOTION_MODE[i2], componentValue)) {
                return SLOW_MOTION_MODE[(i2 + 1) % length];
            }
        }
        return getDefaultValue(i);
    }

    public String[] getSupportAllFPS() {
        return SLOW_MOTION_MODE;
    }

    public boolean isSlowMotionFps120() {
        return DATA_CONFIG_NEW_SLOW_MOTION_120.equals(getComponentValue(172));
    }

    public boolean isSlowMotionFps960() {
        return DATA_CONFIG_NEW_SLOW_MOTION_960.equals(getComponentValue(172));
    }

    public void setVideoNewSlowMotionFPS(String str) {
        setComponentValue(172, str);
    }

    public boolean supportSlowMotionSwitch() {
        return SLOW_MOTION_MODE.length > 1;
    }
}
