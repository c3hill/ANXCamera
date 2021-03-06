package com.android.camera.statistic;

import android.content.Context;
import android.os.SystemProperties;
import com.android.camera.log.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Map.Entry;
import miui.os.Build;

public class CameraStat {
    private static final String APP_ID = "2882303761517373386";
    private static final String APP_KEY = "5641737344386";
    public static final String CATEGORY_CAMERA = "capture";
    public static final String CATEGORY_COUNTER = "counter";
    public static final String CATEGORY_SETTINGS = "settings";
    private static final String CHANNEL = SystemProperties.get("ro.product.mod_device", Build.DEVICE);
    public static final String KEY_AI_DETECT_CHANGED = "ai_detect_changed";
    public static final String KEY_AI_SCENE_CHANGED = "ai_scene_changed";
    public static final String KEY_AI_SCENE_SWITCHED = "ai_scene_switched";
    public static final String KEY_BACK_CAMERA_INFO = "back_camera_info";
    public static final String KEY_BEAUTY_3D_MAKEUP_BUTTON = "beauty_3d_makeup_button";
    public static final String KEY_BEAUTY_BODY_SLIM = "beauty_body_slim";
    public static final String KEY_BEAUTY_BUTTON = "beauty_button";
    public static final String KEY_BEAUTY_LEVEL_BUTTON = "beauty_level_button";
    public static final String KEY_BEAUTY_MAKEUP_BUTTON = "beauty_makeup_button";
    public static final String KEY_BEAUTY_SWITCH_CHANGED = "beauty_switch_changed";
    public static final String KEY_BOKEH_CHANGED = "bokeh_changed";
    public static final String KEY_BURST_SHOT_TIMES = "burst_shot_times";
    public static final String KEY_CALL_CAMERA_FROM_VOICE_CONTROL = "voice_assist_call_event";
    public static final String KEY_CAMERA_BROADCAST_KILL_SERVICE = "camera_broadcast_kill_service";
    public static final String KEY_CAMERA_ERROR_DIALOG_SHOW = "camera_error_dialog_show";
    public static final String KEY_CAMERA_HARDWARE_ERROR = "camera_hardware_error";
    public static final String KEY_DUAL_ZOOM_CHANGED = "dual_zoom_changed";
    public static final String KEY_EV_ADJUSTED = "ev_adjusted";
    public static final String KEY_FILTER_CHANGED = "filter_changed";
    public static final String KEY_FLASH_CHANGED = "flash_changed";
    public static final String KEY_FPS960_PROCESS_FAILED = "fps_960_process_failed";
    public static final String KEY_FPS960_TOO_SHORT = "fps_960_too_short";
    public static final String KEY_FRONT_CAMERA_INFO = "front_camera_info";
    public static final String KEY_FUN_VIDEO_TAKEN = "fun_video_taken";
    public static final String KEY_GENDER_AGE_CHANGED = "gender_age_changed";
    public static final String KEY_GOOGLE_LENS_OOBE_CONTINUE = "google_lens_oobe_continue";
    public static final String KEY_GOOGLE_LENS_PICKER = "google_lens_picker";
    public static final String KEY_GOOGLE_LENS_PICKER_VALUE = "google_lens_picker_value";
    public static final String KEY_GOOGLE_LENS_TOUCH_AND_HOLD = "google_lens_touch_and_hold";
    public static final String KEY_GOTO_GALLERY = "goto_gallery";
    public static final String KEY_GOTO_SETTINGS = "goto_settings";
    public static final String KEY_GRADIENT_CHANGED = "gradienter_changed";
    public static final String KEY_GROUP_SHOT_CHANGED = "group_shot_changed";
    public static final String KEY_HDR_CHANGED = "hdr_changed";
    public static final String KEY_HHT_CHANGED = "hht_changed";
    public static final String KEY_LIGHTING_BUTTON = "lighting_button";
    public static final String KEY_LIGHTING_CHANGED = "lighting_changed";
    public static final String KEY_LIVE_BEAUTY = "live_beauty";
    public static final String KEY_LIVE_BEAUTY_BACK = "live_beauty_back";
    public static final String KEY_LIVE_BEAUTY_FRON = "live_beauty_fron";
    public static final String KEY_LIVE_SPEED = "live_speed";
    public static final String KEY_LIVE_STICKER = "live_sticker";
    public static final String KEY_LIVE_STICKER_DOWNLOAD = "live_sticker_download";
    public static final String KEY_LIVE_STICKER_MORE = "live_sticker_more";
    public static final String KEY_LIVE_STICKER_OFF = "live_sticker_off";
    public static final String KEY_LIVE_VIDEO = "live_video";
    public static final String KEY_LYING_DIRECT_SHOW = "lying_direct_show";
    public static final String KEY_MAGIC_MIRROR_CHANGED = "magic_mirror_changed";
    public static final String KEY_MANUAL_AWB_CHANGED = "manual_awb_changed";
    public static final String KEY_MANUAL_EXPOSURE_TIME_CHANGED = "manual_exposure_time_changed";
    public static final String KEY_MANUAL_FOCUS_PEAK_CHANGED = "manual_focus_peak_changed";
    public static final String KEY_MANUAL_FOCUS_POSITION_CHANGED = "manual_focus_position_changed";
    public static final String KEY_MANUAL_ISO_CHANGED = "manual_iso_changed";
    public static final String KEY_MANUAL_LENS_CHANGED = "manual_lens_changed";
    public static final String KEY_MIMOJI_CAPTURE = "萌拍拍照";
    public static final String KEY_MIMOJI_RECORD = "萌拍录像";
    public static final String KEY_MODE_SWITCH = "camera_mode_switch";
    public static final String KEY_MOON_MODE = "moon_mode";
    public static final String KEY_NEW_BEAUTY = "new_beauty";
    public static final String KEY_NEW_SLOW_MOTION = "slow_motion_mode";
    public static final String KEY_NORMAL_WIDE_LDC = "normal_wide_ldc";
    public static final String KEY_PANORAMA_DIRECTION_CHANGED = "panorama_direction_changed";
    public static final String KEY_PICTURE_LOST = "lost_picture";
    public static final String KEY_PICTURE_MISS_TAKEN = "picture_miss_taken";
    public static final String KEY_PICTURE_SIZE = "camera_picturesize";
    public static final String KEY_PICTURE_TAKEN = "picture_taken";
    public static final String KEY_PICTURE_TAKEN_BEAUTY = "picture_taken_beauty";
    public static final String KEY_PICTURE_TAKEN_BEAUTY_LEGACY = "picture_taken_beauty_legacy";
    public static final String KEY_PICTURE_TAKEN_BOKEH_ADJUST = "picture_taken_bokeh_adjust";
    public static final String KEY_PICTURE_TAKEN_BURST = "picture_taken_burst";
    public static final String KEY_PICTURE_TAKEN_GAP = "picture_taken_gap";
    public static final String KEY_PICTURE_TAKEN_MANUAL = "picture_taken_manual";
    public static final String KEY_PICTURE_TAKEN_PANORAMA = "picture_taken_panorama";
    public static final String KEY_PICTURE_TAKEN_WIDESELFIE = "picture_taken_front_panorama";
    public static final String KEY_POCKET_MODE_ENTER = "pocket_mode_enter";
    public static final String KEY_POCKET_MODE_KEYGUARD_EXIT = "pocket_mode_keyguard_exit";
    public static final String KEY_POCKET_MODE_SENSOR_DELAY = "pocket_mode_sensor_delay";
    public static final String KEY_QRCODE_DETECTED = "qrcode_detected";
    public static final String KEY_SELECT_OBJECT = "select_object";
    public static final String KEY_SNAP_CAMERA = "snap_camera";
    public static final String KEY_START_APP_COST = "start_app_cost";
    public static final String KEY_SUPER_EIS_CHANGED = "super_eis_changed";
    public static final String KEY_SUPER_RESOLUTION_CHANGED = "super_resolution_changed";
    public static final String KEY_T2T_TIMES = "t2t_times";
    public static final String KEY_TAKE_PICTURE_COST = "take_picture_cost";
    public static final String KEY_TILTSHIFT_CHANGED = "tiltshift_changed";
    public static final String KEY_TIMER_CHANGED = "timer_changed";
    public static final String KEY_TRACKING_LOST = "tracking_lost";
    public static final String KEY_ULTRAPIXEL_PORTRAIT_CHANGED = "ultrapixel_portrait_changed";
    public static final String KEY_ULTRA_WIDE_LDC = "ultra_wide_ldc";
    public static final String KEY_USERDEFINE_WATERMARK = "userdefine_watermark";
    public static final String KEY_VIDEO_MODE_CHANGED = "video_mode_changed";
    public static final String KEY_VIDEO_PAUSE_RECORDING = "video_pause_recording";
    public static final String KEY_VIDEO_SNAPSHOT = "video_snapshot";
    public static final String KEY_VIDEO_TAKEN = "video_taken";
    public static final String KEY_VIDEO_TIME_LAPSE_INTERVAL = "video_time_lapse_interval";
    public static final String KEY_ZOOM_ADJUSTED = "zoom_adjusted";
    public static final String LOCATION_CLOSED = "关";
    public static final String LOCATION_WITH = "有";
    public static final String LOCATION_WITHOUT = "无";
    public static final String NEW_SLOW_MOTION_SWITCH_FPS = "帧率切换";
    public static final String PANORAMA_DIRECTION_L2R = "从左往右";
    public static final String PANORAMA_DIRECTION_R2L = "从右往左";
    public static final String PARAM_AI_SCENE_DETECTED = "智能场景发现";
    public static final String PARAM_AI_SCENE_SWITCHED = "智能场景开关";
    public static final String PARAM_AUTO_HDR = "自动HDR";
    public static final String PARAM_AUTO_HHT = "自动HHT";
    public static final String PARAM_AUTO_ZOOM = "运动跟拍";
    public static final String PARAM_AWB = "白平衡";
    public static final String PARAM_BACK_CAMERA = "后置";
    public static final String PARAM_BEAUTY_BLUSHER = "腮红";
    public static final String PARAM_BEAUTY_BODY_SLIM = "瘦身";
    public static final String PARAM_BEAUTY_CHIN = "翘下巴";
    public static final String PARAM_BEAUTY_ENLARGE_EYE = "大眼";
    public static final String PARAM_BEAUTY_EYEBROW_DYE = "染眉";
    public static final String PARAM_BEAUTY_HEAD_SLIM = "头部";
    public static final String PARAM_BEAUTY_JELLY_LIPS = "果冻唇";
    public static final String PARAM_BEAUTY_LEG_SLIM = "长腿";
    public static final String PARAM_BEAUTY_LEVEL = "等级";
    public static final String PARAM_BEAUTY_LIPS = "花瓣唇";
    public static final String PARAM_BEAUTY_NECK = "天鹅颈";
    public static final String PARAM_BEAUTY_NOSE = "芭比鼻";
    public static final String PARAM_BEAUTY_PORT = "部位";
    public static final String PARAM_BEAUTY_PUPIL_LINE = "美瞳线";
    public static final String PARAM_BEAUTY_RISORIUS = "苹果肌";
    public static final String PARAM_BEAUTY_SHOULDER_SLIM = "肩部";
    public static final String PARAM_BEAUTY_SKIN_COLOR = "美白";
    public static final String PARAM_BEAUTY_SKIN_SMOOTH = "嫩肤";
    public static final String PARAM_BEAUTY_SLIM_FACE = "瘦脸";
    public static final String PARAM_BEAUTY_SLIM_NOSE = "瘦鼻";
    public static final String PARAM_BEAUTY_SMILE = "微笑";
    public static final String PARAM_BEAUTY_WHOLE_BODY_SLIM = "全身";
    public static final String PARAM_BOKEN = "BOKEH";
    public static final String PARAM_CAMERA_ID = "前后摄";
    public static final String PARAM_CAMERA_MODE = "模式";
    public static final String PARAM_COMMON_MODE = "方式";
    public static final String PARAM_DEVICE_WATERMARK = "机型水印";
    public static final String PARAM_ET = "快门";
    public static final String PARAM_FILTER = "滤镜";
    public static final String PARAM_FLASH_MODE = "闪光灯";
    public static final String PARAM_FOCUS_PEAK = "峰值对焦";
    public static final String PARAM_FOCUS_POSITION = "对焦";
    public static final String PARAM_FRONT_CAMERA = "前置";
    public static final String PARAM_GENDER_AGE = "年龄检测";
    public static final String PARAM_GRADIENTER = "水平仪";
    public static final String PARAM_GROUP_SHOT = "合影优选";
    public static final String PARAM_HDR = "HDR";
    public static final String PARAM_HHT = "HHT";
    public static final String PARAM_ISO = "感光度";
    public static final String PARAM_LENS = "镜头";
    public static final String PARAM_LIGHTING = "光效";
    public static final String PARAM_LIVESHOT = "动态照片";
    public static final String PARAM_LIVE_BEAUTY_BACK = "live后置美颜";
    public static final String PARAM_LIVE_BEAUTY_FRON = "live前置美颜";
    public static final String PARAM_LIVE_BEAUTY_ON = "抖音美颜1";
    public static final String PARAM_LIVE_BEAUTY_SEGMENT_ON = "抖音美颜2";
    public static final String PARAM_LIVE_BEAUTY_TYPE = "美化方式";
    public static final String PARAM_LIVE_BOKEH_CAMERA = "模式";
    public static final String PARAM_LIVE_BOKEH_LEVEL = "虚化程度";
    public static final String PARAM_LIVE_CLICK_PAUSE = "live录制暂停";
    public static final String PARAM_LIVE_CLICK_PLAY_EXIT = "live预览退出";
    public static final String PARAM_LIVE_CLICK_PLAY_EXIT_CONFIRM = "live预览退出确认";
    public static final String PARAM_LIVE_CLICK_PLAY_SAVE = "live预览保存";
    public static final String PARAM_LIVE_CLICK_PLAY_SHARE = "live预览分享";
    public static final String PARAM_LIVE_CLICK_PLAY_SHARE_SHEET = "live预览分享_";
    public static final String PARAM_LIVE_CLICK_RESUME = "live录制继续";
    public static final String PARAM_LIVE_CLICK_REVERSE = "live录制回退";
    public static final String PARAM_LIVE_CLICK_REVERSE_CONFIRM = "live录制回退确认";
    public static final String PARAM_LIVE_CLICK_START = "live开始录制";
    public static final String PARAM_LIVE_CLICK_SWITCH = "live录制切换";
    public static final String PARAM_LIVE_ENLARGE_EYE_RATIO = "大眼等级";
    public static final String PARAM_LIVE_FILTER_NAME = "滤镜类型";
    public static final String PARAM_LIVE_FILTER_ON = "滤镜1";
    public static final String PARAM_LIVE_FILTER_SEGMENT_ON = "滤镜2";
    public static final String PARAM_LIVE_MUSIC_ICON_CLICK = "live音乐";
    public static final String PARAM_LIVE_MUSIC_NAME = "live音乐类型";
    public static final String PARAM_LIVE_MUSIC_ON = "live音乐2";
    public static final String PARAM_LIVE_RECORD_SEGMENTS = "录制次数";
    public static final String PARAM_LIVE_RECORD_TIME = "抖音视频录制时长";
    public static final String PARAM_LIVE_SHRINK_FACE_RATIO = "瘦脸等级";
    public static final String PARAM_LIVE_SMOOTH_RATIO = "美白等级";
    public static final String PARAM_LIVE_SPEED_LEVEL = "速度调节细节";
    public static final String PARAM_LIVE_STICKER_DOWNLOAD = "下载状态";
    public static final String PARAM_LIVE_STICKER_MORE = "点击跳转";
    public static final String PARAM_LIVE_STICKER_NAME = "魔法道具类型";
    public static final String PARAM_LIVE_STICKER_ON = "魔法道具1";
    public static final String PARAM_LIVE_STICKER_SEGMENT_ON = "魔法道具2";
    public static final String PARAM_LOCATION = "地理位置";
    public static final String PARAM_LYING_DIRECT = "照片方向";
    public static final String PARAM_MACRO_MODE = "微距模式";
    public static final String PARAM_MAGIC_MIRROR = "魔镜";
    public static final String PARAM_METER_ICON_CLICK = "测量模式";
    public static final String PARAM_MIMOJI_CATEGORY = "萌拍分类";
    public static final String PARAM_MIMOJI_CLICK_ADD = "萌拍添加";
    public static final String PARAM_MIMOJI_CLICK_CREATE_BACK = "萌拍取景页返回";
    public static final String PARAM_MIMOJI_CLICK_CREATE_CAPTURE = "萌拍取景页点击拍照";
    public static final String PARAM_MIMOJI_CLICK_CREATE_SOFT_BACK = "萌拍取景页物理键返回";
    public static final String PARAM_MIMOJI_CLICK_CREATE_SWITCH = "萌拍取景页切换前后置";
    public static final String PARAM_MIMOJI_CLICK_DELETE = "萌拍删除";
    public static final String PARAM_MIMOJI_CLICK_EDIT = "萌拍编辑";
    public static final String PARAM_MIMOJI_CLICK_EDIT_CANCEL = "萌拍编辑取消";
    public static final String PARAM_MIMOJI_CLICK_EDIT_RESET = "萌拍编辑返回";
    public static final String PARAM_MIMOJI_CLICK_EDIT_SAVE_NEW = "萌拍编辑首次保存";
    public static final String PARAM_MIMOJI_CLICK_EDIT_SAVE_OLD = "萌拍编辑再次保存";
    public static final String PARAM_MIMOJI_CLICK_EDIT_SOFT_BACK = "萌拍编辑物理键返回";
    public static final String PARAM_MIMOJI_CLICK_NULL = "萌拍空置";
    public static final String PARAM_MIMOJI_CLICK_PREVIEW_MID_BACK = "萌拍预览中间页返回";
    public static final String PARAM_MIMOJI_CLICK_PREVIEW_MID_EDIT = "萌拍预览中间页编辑";
    public static final String PARAM_MIMOJI_CLICK_PREVIEW_MID_RECAPTURE = "萌拍预览中间页重拍";
    public static final String PARAM_MIMOJI_CLICK_PREVIEW_MID_SAVE = "萌拍预览中间页保存";
    public static final String PARAM_MIMOJI_CLICK_PREVIEW_MID_SOFT_BACK = "萌拍预览中间页物理键返回";
    public static final String PARAM_MIMOJI_CONFIG_EAR = "萌拍耳朵";
    public static final String PARAM_MIMOJI_CONFIG_EARING = "萌拍耳环";
    public static final String PARAM_MIMOJI_CONFIG_EYEBROW_SHAPE = "萌拍眉型";
    public static final String PARAM_MIMOJI_CONFIG_EYEGLASS = "萌拍眼镜";
    public static final String PARAM_MIMOJI_CONFIG_EYELASH = "萌拍睫毛";
    public static final String PARAM_MIMOJI_CONFIG_EYE_SHAPE = "萌拍眼型";
    public static final String PARAM_MIMOJI_CONFIG_FEATURE_FACE = "萌拍脸型";
    public static final String PARAM_MIMOJI_CONFIG_FRECKLE = "萌拍雀斑";
    public static final String PARAM_MIMOJI_CONFIG_HARISTYLE = "萌拍发型";
    public static final String PARAM_MIMOJI_CONFIG_HEADWEAR = "萌拍头饰";
    public static final String PARAM_MIMOJI_CONFIG_MOUTH_SHAPE = "萌拍嘴型";
    public static final String PARAM_MIMOJI_CONFIG_MUSTACHE = "萌拍胡子";
    public static final String PARAM_MIMOJI_CONFIG_NOSE = "萌拍鼻子";
    public static final String PARAM_MIMOJI_COUNT = "萌拍数量";
    public static final String PARAM_MOON_MODE_SELECT = "模式选择";
    public static final String PARAM_OOBE_CONTINUE_CLICK = "Google OOBE continue button click";
    public static final String PARAM_PALM_SHUTTER = "手势拍照";
    public static final String PARAM_PANORAMA_DIRECTION = "方向";
    public static final String PARAM_PICK_WHICH = "长按预览弹窗选择";
    public static final String PARAM_PICTURE_RATIO = "画幅";
    public static final String PARAM_PICTURE_SIZE = "拍照画幅";
    public static final String PARAM_QUALITY = "画质";
    public static final String PARAM_SAT_ZOOM = "SAT";
    public static final String PARAM_SELECT_OBJECT = "选取主体物";
    public static final String PARAM_SQUARE = "方形";
    public static final String PARAM_STANDALONE_MACRO_MODE = "独立微距模式";
    public static final String PARAM_STOP_CAPTURE_MODE = "结束方式";
    public static final String PARAM_SUPER_EIS = "超级防抖";
    public static final String PARAM_SUPER_RESOLUTION = "超分辨率";
    public static final String PARAM_TARGET_MODE = "目标模式";
    public static final String PARAM_TILTSHIFT = "移轴";
    public static final String PARAM_TIMER = "倒计时";
    public static final String PARAM_TIME_WATERMARK = "时间水印";
    public static final String PARAM_TRACKING_LOST = "跟丢次数";
    public static final String PARAM_TRIGGER_MODE = "触发方式";
    public static final String PARAM_ULTRAPIXEL_PORTRAIT = "超清人像";
    public static final String PARAM_ULTRA_PIXEL_32MP = "3200万超清像素";
    public static final String PARAM_ULTRA_PIXEL_48MP = "4800万超清像素";
    public static final String PARAM_ULTRA_PIXEL_64MP = "6400万超清像素";
    public static final String PARAM_ULTRA_WIDE = "超广角";
    public static final String PARAM_ULTRA_WIDE_BOKEH = "全身模式";
    public static final String PARAM_VIDEO_FPS = "帧率";
    public static final String PARAM_VIDEO_MODE = "模式";
    public static final String PARAM_VIDEO_QUALITY = "视频画质";
    public static final String PARAM_VIDEO_TIME = "时长";
    public static final String PARAM_ZOOM = "变焦";
    public static final String POCKET_MODE_KEYGUARD_EXIT_DISMISS = "锁屏手动解除";
    public static final String POCKET_MODE_KEYGUARD_EXIT_TIMEOUT = "锁屏超时退出";
    public static final String POCKET_MODE_KEYGUARD_EXIT_UNLOCK = "锁屏自动解除";
    public static final String POCKET_MODE_NONUI_ENTER_SNAP = "NonUI街拍";
    public static final String POCKET_MODE_NONUI_ENTER_VOLUME = "NonUI音量键";
    public static final String POCKET_MODE_PSENSOR_ENTER_KEYGUARD = "Psensor锁屏进入";
    public static final String POCKET_MODE_PSENSOR_ENTER_SNAP = "Psensor街拍";
    public static final String POCKET_MODE_PSENSOR_ENTER_VOLUME = "Psensor音量键";
    public static final String STOP_CAPTURE_MODE_FILL = "图像充满预览框";
    public static final String STOP_CAPTURE_MODE_HORIZONTAL_OUT = "越过左右临界值";
    public static final String STOP_CAPTURE_MODE_ON_HOME_OR_BACK = "按下Home/Back键";
    public static final String STOP_CAPTURE_MODE_ON_SHUTTER_BUTTON = "再次单机拍照键";
    public static final String STOP_CAPTURE_MODE_ROTATE_OUT = "旋转角度超过临界值";
    public static final String STOP_CAPTURE_MODE_VERTICAL_OUT = "越过上下临界值";
    public static final String SWITCH_MODE_CLICK = "点击";
    public static final String SWITCH_MODE_SLIDE = "滑动";
    private static final String TAG = "CameraStat";
    public static final String VALUE_AUTOZOOM_DISABLE = "off";
    public static final String VALUE_AUTOZOOM_LOST_10_MORE = "10次及以上";
    public static final String VALUE_AUTOZOOM_NOT_ULTRA = "非超广角模式下运动跟拍";
    public static final String VALUE_AUTOZOOM_ULTRA = "超广角模式下运动跟拍";
    public static final String VALUE_BACK_CAMERA = "后摄";
    public static final String VALUE_FRONT_CAMERA = "前摄";
    public static final String VALUE_LIVE_STICKER_APP = "liveAPP";
    public static final String VALUE_LIVE_STICKER_FAILED = "失败";
    public static final String VALUE_LIVE_STICKER_MARKET = "应用商店";
    public static final String VALUE_LIVE_STICKER_SUCCESS = "成功";
    public static final String VALUE_OFF = "off";
    public static final String VALUE_ON = "on";
    public static final String VALUE_SELECT_OBJECT_BEFORE_RECORDING = "录像前";
    public static final String VALUE_SELECT_OBJECT_RECORDING = "录像中";
    public static final String ZOOM_MODE_GESTURE = "手势";
    public static final String ZOOM_MODE_SLIDER = "滑动条";
    public static final String ZOOM_MODE_VOLUME = "音量键";
    private static boolean sDumpStatEvent;
    private static boolean sIsAnonymous;
    private static boolean sIsCounterEventEnabled;
    private static boolean sIsEnabled;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
    }

    private static void dumpEvent(String str, String str2, String str3, String str4, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" category:");
        sb.append(str2);
        sb.append(" key:");
        sb.append(str3);
        if (str4 != null) {
            sb.append(" value:");
            sb.append(str4);
        }
        if (map != null) {
            sb.append("\n");
            sb.append("params:");
            sb.append("[");
            for (Entry entry : map.entrySet()) {
                sb.append((String) entry.getKey());
                sb.append(":");
                sb.append((String) entry.getValue());
                sb.append(" ");
            }
            int length = sb.length();
            sb.replace(length - 1, length, "]");
        }
        Log.d(TAG, sb.toString());
    }

    public static void initialize(Context context) {
    }

    public static boolean isCounterEventDisabled() {
        return !sIsCounterEventEnabled;
    }

    public static void recordCalculateEvent(String str, String str2, long j) {
    }

    public static void recordCalculateEvent(String str, String str2, long j, Map map) {
    }

    public static void recordCountEvent(String str, String str2) {
    }

    public static void recordCountEvent(String str, String str2, Map map) {
    }

    public static void recordPageEnd() {
    }

    public static void recordPageStart(Context context, String str) {
    }

    public static void recordStringPropertyEvent(String str, String str2, String str3) {
    }
}
