package com.android.camera;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.MeteringRectangle;
import android.location.Country;
import android.location.CountryDetector;
import android.location.Location;
import android.media.Image;
import android.media.Image.Plane;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IPowerManager.Stub;
import android.os.ParcelFileDescriptor;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.MiuiSettings.ScreenEffect;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.support.annotation.NonNull;
import android.support.v4.app.FrameMetricsAggregator;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.Xml;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigRatio;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.MiYuvImage;
import com.android.camera.effect.renders.CustomTextWaterMark;
import com.android.camera.effect.renders.NewStyleTextWaterMark;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.lib.compatibility.util.CompatibilityUtils.PackageInstallerListener;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.network.download.Verifier;
import com.android.camera.permission.PermissionManager;
import com.android.camera.statistic.CameraStatUtil;
import com.android.camera.storage.Storage;
import com.android.camera2.ArcsoftDepthMap;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CaptureResultParser;
import com.android.camera2.vendortag.struct.AECFrameControl;
import com.android.camera2.vendortag.struct.AFFrameControl;
import com.android.gallery3d.exif.ExifInterface;
import com.android.gallery3d.exif.ExifInterface.GpsSpeedRef;
import com.android.gallery3d.exif.Rational;
import com.android.gallery3d.ui.StringTexture;
import com.mi.config.b;
import com.mi.config.d;
import com.ss.android.ttve.common.TEDefine;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.liveshot.LivePhotoResult;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import miui.hardware.display.DisplayFeatureManager;
import miui.os.Build;
import miui.reflect.Field;
import miui.reflect.Method;
import miui.reflect.NoSuchClassException;
import miui.reflect.NoSuchFieldException;
import miui.reflect.NoSuchMethodException;
import miui.util.IOUtils;
import miui.view.animation.SineEaseInOutInterpolator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public final class Util {
    public static final String ACTION_BIND_GALLERY_SERVICE = "com.miui.gallery.action.BIND_SERVICE";
    public static final String ACTION_DISMISS_KEY_GUARD = "xiaomi.intent.action.SHOW_SECURE_KEYGUARD";
    public static final String ACTION_KILL_CAMERA_SERVICE = "com.android.camera.action.KILL_CAMERA_SERVICE";
    public static final String ACTION_RESET_CAMERA_PREF = "miui.intent.action.RESET_CAMERA_PREF";
    public static final String ALGORITHM_NAME_MIMOJI_CAPTURE = "mimoji";
    public static final String ALGORITHM_NAME_PORTRAIT = "portrait";
    public static final String ALGORITHM_NAME_SOFT_PORTRAIT = "soft-portrait";
    public static final String ALGORITHM_NAME_SOFT_PORTRAIT_ENCRYPTED = "soft-portrait-enc";
    public static final String ANDROID_ONE_EXTRA_IS_SECURE_MODE = "com.google.android.apps.photos.api.secure_mode";
    public static final String ANDROID_ONE_EXTRA_SECURE_MODE_MEDIA_STORE_IDS = "com.google.android.apps.photos.api.secure_mode_ids";
    public static final String ANDROID_ONE_REVIEW_ACTIVITY_PACKAGE = "com.google.android.apps.photos";
    private static HashSet<String> ANTIBANDING_60_COUNTRY = new HashSet<>(Arrays.asList(new String[]{"TW", "KR", "SA", "US", "CA", "BR", "CO", "MX", "PH"}));
    private static final Long APERTURE_VALUE_PRECISION = Long.valueOf(100);
    public static final double ASPECT_TOLERANCE = 0.02d;
    public static final int BLUR_DURATION = 100;
    private static final List<Integer> COLOR_TEMPERATURE_LIST = new ArrayList();
    private static final List<Integer> COLOR_TEMPERATURE_MAP = new ArrayList();
    public static final boolean DEBUG = (!Build.IS_STABLE_VERSION);
    public static final String EXTRAS_SKIP_LOCK = "skip_interception";
    private static final String EXTRAS_START_WITH_EFFECT_RENDER = "android.intent.extras.START_WITH_EFFECT_RENDER";
    public static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = Integer.MIN_VALUE;
    private static final Long FOCAL_LENGTH_PRECISION = Long.valueOf(100);
    private static final String FORCE_CAMERA_0_FILE = "force_camera_0";
    private static final String FORCE_NAME_SUFFIX_FILE = "force_name_suffix";
    private static final Long F_NUMBER_PRECISION = Long.valueOf(100);
    public static final int GOING_TO_CROP = 5;
    public static final int GOING_TO_DETAIL = 3;
    public static final int GOING_TO_GALLERY = 1;
    public static final int GOING_TO_PLAYBACK = 4;
    public static final int GOING_TO_SETTING = 2;
    public static final float GYROSCOPE_MAX_X = 0.7f;
    public static final float GYROSCOPE_MAX_Y = 5.0f;
    public static final float GYROSCOPE_MAX_Z = 0.7f;
    private static final File INTERNAL_STORAGE_DIRECTORY = new File("/data/sdcard");
    public static final int KEYCODE_SLIDE_OFF = 701;
    public static final int KEYCODE_SLIDE_ON = 700;
    public static final String KEY_CAMERA_BRIGHTNESS = "camera-brightness";
    public static final String KEY_CAMERA_BRIGHTNESS_AUTO = "camera-brightness-auto";
    public static final String KEY_CAMERA_BRIGHTNESS_MANUAL = "camera-brightness-manual";
    public static final String KEY_KILLED_MODULE_INDEX = "killed-moduleIndex";
    public static final String KEY_REVIEW_FROM_MIUICAMERA = "from_MiuiCamera";
    public static final String KEY_SECURE_ITEMS = "SecureUri";
    private static final String LAB_OPTIONS_VISIBLE_FILE = "lab_options_visible";
    public static final String LAST_FRAME_GAUSSIAN_FILE_NAME = "blur.jpg";
    public static final int LIMIT_SURFACE_WIDTH = 720;
    private static final double LOG_2 = Math.log(2.0d);
    public static final int MAX_SECURE_SIZE = 100;
    private static final Long MS_TO_S = Long.valueOf(1000000);
    private static final String NONUI_MODE_PROPERTY = "sys.power.nonui";
    private static final Long NS_TO_S = Long.valueOf(1000000000);
    public static final int ORIENTATION_HYSTERESIS = 5;
    public static final String QRCODE_RECEIVER_ACTION = "com.xiaomi.scanner.receiver.senderbarcodescanner";
    public static final float RATIO_16_9 = 1.7777777f;
    public static final float RATIO_18_7_5_9 = 2.0833333f;
    public static final float RATIO_18_9 = 2.0f;
    public static final float RATIO_19P5_9 = 2.1666667f;
    public static final float RATIO_19_9 = 2.1111112f;
    public static final float RATIO_1_1 = 1.0f;
    public static final float RATIO_4_3 = 1.3333333f;
    public static final String REVIEW_ACTION = "com.android.camera.action.REVIEW";
    public static final String REVIEW_ACTIVITY_PACKAGE = "com.miui.gallery";
    public static final String REVIEW_SCAN_RESULT_PACKAGE = "com.xiaomi.scanner";
    public static final int SCREEN_EFFECT_CAMERA_STATE = 14;
    public static final Uri SCREEN_SLIDE_STATUS_SETTING_URI = System.getUriFor(MiuiSettings.System.MIUI_SLIDER_COVER_STATUS);
    private static final String SCREEN_VENDOR = SystemProperties.get("sys.panel.display");
    private static final Long SHUTTER_SPEED_VALUE_PRECISION = Long.valueOf(100);
    private static final String TAG = "CameraUtil";
    private static final String TEMP_SUFFIX = ".tmp";
    public static final String WATERMARK_FILE_NAME;
    public static final String WATERMARK_FRONT_FILE_NAME;
    public static final String WATERMARK_STORAGE_DIRECTORY = "/mnt/vendor/persist/camera/";
    public static final String WATERMARK_ULTRA_PIXEL_FILE_NAME;
    private static final String ZOOM_ANIMATION_PROPERTY = "camera_zoom_animation";
    public static boolean isLongRatioScreen;
    public static boolean isNotchDevice;
    private static String mCountryIso = null;
    private static int mLockedOrientation = -1;
    public static String sAAID;
    private static boolean sClearMemoryLimit;
    public static int sFullScreenExtraMargin;
    private static boolean sHasNavigationBar;
    private static ImageFileNamer sImageFileNamer;
    private static boolean sIsAccessibilityEnable;
    private static Boolean sIsDumpImageEnabled;
    public static boolean sIsDumpLog;
    public static boolean sIsDumpOrigJpg;
    private static Boolean sIsForceNameSuffix;
    public static boolean sIsFullScreenNavBarHidden;
    public static boolean sIsKillCameraService;
    private static Boolean sIsLabOptionsVisible;
    public static boolean sIsnotchScreenHidden;
    public static int sNavigationBarHeight;
    public static float sPixelDensity = 1.0f;
    public static String sRegion;
    public static int sStatusBarHeight;
    private static HashMap<String, Typeface> sTypefaces = new HashMap<>();
    public static int sWindowHeight = ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_END_DEAULT;
    private static IWindowManager sWindowManager;
    public static int sWindowWidth = LIMIT_SURFACE_WIDTH;

    private static class ImageFileNamer {
        private SimpleDateFormat mFormat;
        private long mLastDate;
        private int mSameSecondCount;

        public ImageFileNamer(String str) {
            this.mFormat = new SimpleDateFormat(str);
        }

        public String generateName(long j) {
            String format = this.mFormat.format(new Date(j));
            if (j / 1000 == this.mLastDate / 1000) {
                this.mSameSecondCount++;
                StringBuilder sb = new StringBuilder();
                sb.append(format);
                sb.append("_");
                sb.append(this.mSameSecondCount);
                return sb.toString();
            }
            this.mLastDate = j;
            this.mSameSecondCount = 0;
            return format;
        }
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(android.os.Build.DEVICE);
        sb.append("_custom_watermark.png");
        WATERMARK_FILE_NAME = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(android.os.Build.DEVICE);
        sb2.append("_front_watermark.png");
        WATERMARK_FRONT_FILE_NAME = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(android.os.Build.DEVICE);
        sb3.append("_ultra_pixel_custom_watermark.png");
        WATERMARK_ULTRA_PIXEL_FILE_NAME = sb3.toString();
    }

    private Util() {
    }

    public static void Assert(boolean z) {
        if (!z) {
            throw new AssertionError();
        }
    }

    public static byte[] RGBA2RGB(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3)];
        int i4 = 0;
        int i5 = 0;
        while (i4 < i3) {
            int i6 = i5 + 1;
            int i7 = i4 * 4;
            bArr2[i5] = bArr[i7];
            int i8 = i6 + 1;
            bArr2[i6] = bArr[i7 + 1];
            int i9 = i8 + 1;
            bArr2[i8] = bArr[i7 + 2];
            i4++;
            i5 = i9;
        }
        return bArr2;
    }

    private static String addDebugInfo(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t ");
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("\n");
        return sb3.toString();
    }

    private static String addProperties(String str) {
        if (SystemProperties.get(str) == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t ");
        sb.append(SystemProperties.get(str));
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("\n");
        return sb3.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0220, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        $closeResource(r7, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0224, code lost:
        throw r8;
     */
    public static byte[] appendCaptureResultToExif(byte[] bArr, int i, int i2, int i3, long j, Location location, CameraMetadataNative cameraMetadataNative) {
        byte[] bArr2;
        String str = TAG;
        if (b.isMTKPlatform() && cameraMetadataNative != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ExifInterface exif = ExifInterface.getExif(bArr);
                setTagValue(exif, ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getExifOrientationValue(i3)));
                setTagValue(exif, ExifInterface.TAG_PIXEL_X_DIMENSION, Integer.valueOf(i));
                setTagValue(exif, ExifInterface.TAG_PIXEL_Y_DIMENSION, Integer.valueOf(i2));
                setTagValue(exif, ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(i));
                setTagValue(exif, ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(i2));
                setTagValue(exif, ExifInterface.TAG_MODEL, android.os.Build.MODEL);
                setTagValue(exif, ExifInterface.TAG_MAKE, android.os.Build.MANUFACTURER);
                if (j > 0) {
                    exif.addDateTimeStampTag(ExifInterface.TAG_DATE_TIME, j, TimeZone.getDefault());
                }
                Float f2 = (Float) cameraMetadataNative.get(CaptureResult.LENS_FOCAL_LENGTH);
                StringBuilder sb = new StringBuilder();
                sb.append("LENS_FOCAL_LENGTH: ");
                sb.append(f2);
                Log.d(str, sb.toString());
                if (f2 != null) {
                    setTagValue(exif, ExifInterface.TAG_FOCAL_LENGTH, doubleToRational((double) f2.floatValue(), FOCAL_LENGTH_PRECISION.longValue()));
                }
                Float f3 = (Float) cameraMetadataNative.get(CaptureResult.LENS_APERTURE);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("LENS_APERTURE: ");
                sb2.append(f3);
                Log.d(str, sb2.toString());
                if (f3 != null) {
                    setTagValue(exif, ExifInterface.TAG_F_NUMBER, doubleToRational((double) f3.floatValue(), F_NUMBER_PRECISION.longValue()));
                    setTagValue(exif, ExifInterface.TAG_APERTURE_VALUE, doubleToRational(log2((double) f3.floatValue()) * 2.0d, APERTURE_VALUE_PRECISION.longValue()));
                }
                Integer num = (Integer) cameraMetadataNative.get(CaptureResult.SENSOR_SENSITIVITY);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("SENSOR_SENSITIVITY: ");
                sb3.append(num);
                Log.d(str, sb3.toString());
                if (num != null) {
                    setTagValue(exif, ExifInterface.TAG_ISO_SPEED_RATINGS, num);
                }
                Long l = (Long) cameraMetadataNative.get(CaptureResult.SENSOR_EXPOSURE_TIME);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("SENSOR_EXPOSURE_TIME: ");
                sb4.append(l);
                Log.d(str, sb4.toString());
                if (l != null) {
                    if (l.longValue() <= 4000000000L) {
                        setTagValue(exif, ExifInterface.TAG_EXPOSURE_TIME, new Rational(l.longValue(), NS_TO_S.longValue()));
                    } else {
                        setTagValue(exif, ExifInterface.TAG_EXPOSURE_TIME, new Rational(l.longValue() / 1000, MS_TO_S.longValue()));
                    }
                    setTagValue(exif, ExifInterface.TAG_SHUTTER_SPEED_VALUE, doubleToRational(log2(((double) l.longValue()) / ((double) NS_TO_S.longValue())), SHUTTER_SPEED_VALUE_PRECISION.longValue()));
                }
                Location location2 = (Location) cameraMetadataNative.get(CaptureResult.JPEG_GPS_LOCATION);
                if (location2 == null) {
                    location2 = location;
                }
                StringBuilder sb5 = new StringBuilder();
                sb5.append("JPEG_GPS_LOCATION: ");
                sb5.append(location2);
                Log.d(str, sb5.toString());
                if (location2 != null) {
                    exif.addGpsTags(location2.getLatitude(), location2.getLongitude());
                    exif.addGpsDateTimeStampTag(location2.getTime());
                    double altitude = location2.getAltitude();
                    if (altitude != 0.0d) {
                        exif.setTag(exif.buildTag(ExifInterface.TAG_GPS_ALTITUDE_REF, Short.valueOf(altitude < 0.0d ? (short) 1 : 0)));
                        exif.addGpsTags(location2.getLatitude(), location2.getLongitude());
                    }
                }
                Integer num2 = (Integer) cameraMetadataNative.get(CaptureResult.FLASH_STATE);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("FLASH_STATE: ");
                sb6.append(num2);
                Log.d(str, sb6.toString());
                if (num2 == null || num2.intValue() != 3) {
                    setTagValue(exif, ExifInterface.TAG_FLASH, Short.valueOf(0));
                } else {
                    setTagValue(exif, ExifInterface.TAG_FLASH, Short.valueOf(1));
                }
                exif.writeExif(bArr, (OutputStream) byteArrayOutputStream);
                bArr2 = byteArrayOutputStream.toByteArray();
                try {
                    $closeResource(null, byteArrayOutputStream);
                } catch (IOException | RuntimeException unused) {
                }
            } catch (IOException | RuntimeException unused2) {
                bArr2 = null;
                Log.d(str, "appendExif(): Failed to append exif metadata");
                if (bArr2 != null) {
                }
            }
            return (bArr2 != null || bArr2.length < bArr.length) ? bArr : bArr2;
        }
    }

    public static SpannableStringBuilder appendInApi26(SpannableStringBuilder spannableStringBuilder, CharSequence charSequence, Object obj, int i) {
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(charSequence);
        spannableStringBuilder.setSpan(obj, length, spannableStringBuilder.length(), i);
        return spannableStringBuilder;
    }

    public static <T> int binarySearchRightMost(List<? extends Comparable<? super T>> list, T t) {
        int size = list.size() - 1;
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) / 2;
            if (((Comparable) list.get(i2)).compareTo(t) >= 0) {
                size = i2 - 1;
            } else {
                i = i2 + 1;
            }
        }
        return i;
    }

    public static void broadcastKillService(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        CameraSettings.setBroadcastKillServiceTime(elapsedRealtime);
        Intent intent = new Intent(ACTION_KILL_CAMERA_SERVICE);
        intent.putExtra("time", elapsedRealtime + FragmentTopAlert.HINT_DELAY_TIME);
        intent.putExtra("process_name", new String[]{"android.hardware.camera.provider@2.4-service", "android.hardware.camera.provider@2.4-service_64"});
        context.sendBroadcast(intent);
        CameraStatUtil.trackBroadcastKillService();
    }

    public static void broadcastNewPicture(Context context, Uri uri) {
        int i = VERSION.SDK_INT;
        String str = "android.hardware.action.NEW_PICTURE";
        if (i < 24) {
            context.sendBroadcast(new Intent(str, uri));
            context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
        } else if (i == 29) {
            context.sendBroadcast(new Intent(str, uri));
        }
    }

    public static int[] calcDualCameraWatermarkLocation(int i, int i2, int i3, int i4, float f2, float f3, float f4) {
        float min = ((float) Math.min(i, i2)) / 1080.0f;
        boolean Db = DataRepository.dataItemFeature().Db();
        float f5 = 1.0f;
        int round = Math.round(f2 * min * (Db ? CameraSettings.getResourceFloat(R.dimen.custom_watermark_height_scale, 1.0f) : 1.0f)) & -2;
        int i5 = ((i3 * round) / i4) & -2;
        int round2 = Math.round(f3 * min) & -2;
        if (Db) {
            f5 = CameraSettings.getResourceFloat(R.dimen.custom_watermark_pandingY_scale, 1.0f);
        }
        return new int[]{i5, round, round2, Math.round(f4 * min * f5) & -2};
    }

    public static int calcNavigationBarHeight(Context context) {
        int i = sWindowHeight - ((sWindowWidth * 16) / 9);
        int dimensionPixelSize = i > 0 ? i - context.getResources().getDimensionPixelSize(R.dimen.top_control_panel_height) : 0;
        StringBuilder sb = new StringBuilder();
        sb.append("calculate navBarHeight=");
        sb.append(dimensionPixelSize);
        Log.d(TAG, sb.toString());
        return dimensionPixelSize;
    }

    public static final int calculateDefaultPreviewEdgeSlop(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float f2 = ((float) displayMetrics.widthPixels) / displayMetrics.xdpi;
        float f3 = ((float) displayMetrics.heightPixels) / displayMetrics.ydpi;
        return context.getResources().getDimensionPixelSize(((float) Math.sqrt((double) ((f2 * f2) + (f3 * f3)))) < 5.0f ? R.dimen.preview_edge_touch_slop_small_screen : R.dimen.preview_edge_touch_slop);
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        return true;
    }

    public static void checkLockedOrientation(Activity activity) {
        try {
            if (System.getInt(activity.getContentResolver(), "accelerometer_rotation") == 0) {
                mLockedOrientation = System.getInt(activity.getContentResolver(), "user_rotation");
            } else {
                mLockedOrientation = -1;
            }
        } catch (SettingNotFoundException unused) {
            Log.e(TAG, "user rotation cannot found");
        }
    }

    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static float clamp(float f2, float f3, float f4) {
        return f2 > f4 ? f4 : f2 < f3 ? f3 : f2;
    }

    public static int clamp(int i, int i2, int i3) {
        return i > i3 ? i3 : i < i2 ? i2 : i;
    }

    public static long clamp(long j, long j2, long j3) {
        return j > j3 ? j3 : j < j2 ? j2 : j;
    }

    public static void clearMemoryLimit() {
        if (!sClearMemoryLimit) {
            long currentTimeMillis = System.currentTimeMillis();
            VMRuntime.getRuntime().clearGrowthLimit();
            sClearMemoryLimit = true;
            long currentTimeMillis2 = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append("clearMemoryLimit() consume:");
            sb.append(currentTimeMillis2 - currentTimeMillis);
            Log.v(TAG, sb.toString());
        }
    }

    public static void closeSafely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    public static byte[] composeDepthMapPicture(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int[] iArr, boolean z, boolean z2, int i, String str, int i2, int i3, boolean z3, boolean z4, PictureInfo pictureInfo) {
        byte[] bArr5;
        byte[] bArr6 = bArr3;
        String str2 = str;
        int i4 = i2;
        int i5 = i3;
        String str3 = TAG;
        Log.d(str3, "composeDepthMapPicture: process in portrait depth map picture");
        long currentTimeMillis = System.currentTimeMillis();
        ArcsoftDepthMap arcsoftDepthMap = new ArcsoftDepthMap(bArr2);
        int[] iArr2 = new int[4];
        byte[] bArr7 = z ? getDualCameraWatermarkData(i4, i5, iArr2) : z2 ? getFrontCameraWatermarkData(i4, i5, iArr2) : null;
        int[] iArr3 = new int[4];
        if (str2 != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("generate a TimeWaterMarkData with :");
            sb.append(i4);
            sb.append("x");
            sb.append(i5);
            Log.d(str3, sb.toString());
            bArr5 = getTimeWaterMarkData(i4, i5, str2, iArr3);
        } else {
            bArr5 = null;
        }
        byte[] depthMapData = arcsoftDepthMap.getDepthMapData();
        byte[] writePortraitExif = arcsoftDepthMap.writePortraitExif(DataRepository.dataItemFeature().Ba(), bArr, bArr7, iArr2, bArr5, iArr3, bArr4, iArr, i, z3, z4, pictureInfo, bArr6.length, depthMapData.length);
        byte[] bArr8 = new byte[(writePortraitExif.length + bArr6.length + depthMapData.length)];
        System.arraycopy(writePortraitExif, 0, bArr8, 0, writePortraitExif.length);
        System.arraycopy(bArr6, 0, bArr8, writePortraitExif.length, bArr6.length);
        System.arraycopy(depthMapData, 0, bArr8, writePortraitExif.length + bArr6.length, depthMapData.length);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("composeDepthMapPicture: compose portrait picture cost: ");
        sb2.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(str3, sb2.toString());
        return bArr8;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:146:0x023e, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x023f, code lost:
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:?, code lost:
        $closeResource(r2, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x0243, code lost:
        throw r6;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:127:0x0226, B:144:0x023d] */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0119 A[Catch:{ IOException -> 0x01b2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x011b A[Catch:{ IOException -> 0x01b2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01bc  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01c2 A[SYNTHETIC, Splitter:B:89:0x01c2] */
    public static byte[] composeLiveShotPicture(byte[] bArr, int i, int i2, byte[] bArr2, long j, boolean z, boolean z2, String str, byte[] bArr3, int[] iArr) {
        byte[] bArr4;
        byte[] bArr5;
        byte[] bArr6;
        String str2;
        String str3;
        String str4;
        byte[] bArr7;
        byte[] bArr8;
        Throwable th;
        byte[] bArr9;
        Throwable th2;
        String str5;
        StringWriter stringWriter;
        int[] iArr2;
        Throwable th3;
        byte[] bArr10 = bArr;
        int i3 = i;
        int i4 = i2;
        byte[] bArr11 = bArr2;
        String str6 = str;
        byte[] bArr12 = bArr3;
        int[] iArr3 = iArr;
        String str7 = "lenswatermark";
        String str8 = XmpHelper.GOOGLE_MICROVIDEO_NAMESPACE;
        String str9 = TAG;
        Log.d(str9, "composeLiveShotPicture(): E");
        if (bArr10 == null || bArr10.length == 0) {
            Log.d(str9, "composeLiveShotPicture(): The primary photo of LiveShot is empty");
            return new byte[0];
        } else if (bArr11 == null || bArr11.length == 0) {
            Log.d(str9, "composeLiveShotPicture(): The corresponding movie of LiveShot is empty");
            return bArr;
        } else {
            int[] iArr4 = new int[4];
            int[] iArr5 = new int[4];
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    ExifInterface exifInterface = new ExifInterface();
                    exifInterface.readExif(bArr10);
                    exifInterface.addFileTypeLiveShot(true);
                    exifInterface.writeExif(bArr10, (OutputStream) byteArrayOutputStream);
                    bArr4 = byteArrayOutputStream.toByteArray();
                    try {
                        $closeResource(null, byteArrayOutputStream);
                    } catch (IOException unused) {
                    }
                } catch (Throwable th4) {
                    Throwable th5 = th4;
                    $closeResource(th3, byteArrayOutputStream);
                    throw th5;
                }
            } catch (IOException unused2) {
                bArr4 = null;
                Log.d(str9, "composeLiveShotPicture(): Failed to insert xiaomi specific metadata");
                bArr5 = bArr4;
                if (bArr5 != null) {
                }
                Log.d(str9, "composeLiveShotPicture(): #1: return original jpeg");
                return bArr;
            }
            bArr5 = bArr4;
            if (bArr5 != null || bArr5.length <= bArr10.length) {
                Log.d(str9, "composeLiveShotPicture(): #1: return original jpeg");
                return bArr;
            }
            byte[] bArr13 = z ? getDualCameraWatermarkData(i3, i4, iArr4) : z2 ? getFrontCameraWatermarkData(i3, i4, iArr4) : null;
            byte[] timeWaterMarkData = (str6 == null || str.isEmpty()) ? null : getTimeWaterMarkData(i3, i4, str6, iArr5);
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                StringWriter stringWriter2 = new StringWriter();
                newSerializer.setOutput(stringWriter2);
                str2 = str8;
                try {
                    newSerializer.startDocument("UTF-8", Boolean.valueOf(true));
                    String str10 = "paddingy";
                    String str11 = "paddingx";
                    String str12 = "height";
                    bArr6 = bArr5;
                    String str13 = "width";
                    String str14 = "length";
                    str5 = str9;
                    String str15 = "offset";
                    if (bArr12 != null) {
                        stringWriter = stringWriter2;
                        try {
                            if (bArr12.length > 0 && iArr3 != null) {
                                iArr2 = iArr5;
                                if (iArr3.length >= 4) {
                                    newSerializer.startTag(null, "subimage");
                                    newSerializer.attribute(null, str15, String.valueOf(bArr12.length + (bArr13 != null ? bArr13.length : 0) + (timeWaterMarkData != null ? timeWaterMarkData.length : 0) + bArr11.length));
                                    newSerializer.attribute(null, str14, String.valueOf(bArr12.length));
                                    newSerializer.attribute(null, str11, String.valueOf(iArr3[0]));
                                    newSerializer.attribute(null, str10, String.valueOf(iArr3[1]));
                                    newSerializer.attribute(null, str13, String.valueOf(iArr3[2]));
                                    newSerializer.attribute(null, str12, String.valueOf(iArr3[3]));
                                    newSerializer.endTag(null, "subimage");
                                }
                                if (bArr13 != null && bArr13.length > 0) {
                                    newSerializer.startTag(null, str7);
                                    newSerializer.attribute(null, str15, String.valueOf(bArr13.length + (timeWaterMarkData == null ? timeWaterMarkData.length : 0) + bArr11.length));
                                    newSerializer.attribute(null, str14, String.valueOf(bArr13.length));
                                    newSerializer.attribute(null, str13, String.valueOf(iArr4[0]));
                                    newSerializer.attribute(null, str12, String.valueOf(iArr4[1]));
                                    newSerializer.attribute(null, str11, String.valueOf(iArr4[2]));
                                    newSerializer.attribute(null, str10, String.valueOf(iArr4[3]));
                                    newSerializer.endTag(null, str7);
                                }
                                if (timeWaterMarkData != null && timeWaterMarkData.length > 0) {
                                    newSerializer.startTag(null, "timewatermark");
                                    newSerializer.attribute(null, str15, String.valueOf(timeWaterMarkData.length + bArr11.length));
                                    newSerializer.attribute(null, str14, String.valueOf(timeWaterMarkData.length));
                                    newSerializer.attribute(null, str13, String.valueOf(iArr2[0]));
                                    newSerializer.attribute(null, str12, String.valueOf(iArr2[1]));
                                    newSerializer.attribute(null, str11, String.valueOf(iArr2[2]));
                                    newSerializer.attribute(null, str10, String.valueOf(iArr2[3]));
                                    newSerializer.endTag(null, "timewatermark");
                                }
                                newSerializer.endDocument();
                                str3 = stringWriter.toString();
                                str4 = str5;
                                if (str3 == null) {
                                    Log.d(str4, "composeLiveShotPicture(): #2: return original jpeg");
                                    return bArr;
                                }
                                try {
                                    bArr8 = bArr6;
                                    try {
                                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr8);
                                        try {
                                            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                                            XMPMeta createXMPMeta = XmpHelper.createXMPMeta();
                                            String str16 = str2;
                                            createXMPMeta.setPropertyInteger(str16, XmpHelper.MICROVIDEO_VERSION, 1);
                                            createXMPMeta.setPropertyInteger(str16, XmpHelper.MICROVIDEO_TYPE, 1);
                                            createXMPMeta.setPropertyInteger(str16, XmpHelper.MICROVIDEO_OFFSET, bArr11.length);
                                            createXMPMeta.setPropertyLong(str16, XmpHelper.MICROVIDEO_PRESENTATION_TIMESTAMP, j);
                                            if (str3 != null) {
                                                try {
                                                    createXMPMeta.setProperty(XmpHelper.XIAOMI_XMP_METADATA_NAMESPACE, XmpHelper.XIAOMI_XMP_METADATA_PROPERTY_NAME, str3);
                                                } catch (Throwable th6) {
                                                    th2 = th6;
                                                    bArr9 = null;
                                                }
                                            }
                                            try {
                                                XmpHelper.writeXMPMeta(byteArrayInputStream, byteArrayOutputStream2, createXMPMeta);
                                                if (bArr12 != null) {
                                                    if (bArr12.length > 0 && iArr3 != null && iArr3.length >= 4) {
                                                        byteArrayOutputStream2.write(bArr12);
                                                    }
                                                }
                                                if (bArr13 != null && bArr13.length > 0) {
                                                    byteArrayOutputStream2.write(bArr13);
                                                }
                                                if (timeWaterMarkData != null && timeWaterMarkData.length > 0) {
                                                    byteArrayOutputStream2.write(timeWaterMarkData);
                                                }
                                                byteArrayOutputStream2.flush();
                                                bArr7 = byteArrayOutputStream2.toByteArray();
                                            } catch (Throwable th7) {
                                                bArr9 = null;
                                                th2 = th7;
                                                try {
                                                    throw th2;
                                                } catch (Throwable th8) {
                                                    th = th8;
                                                    th = th;
                                                    bArr7 = bArr9;
                                                    throw th;
                                                }
                                            }
                                            try {
                                                $closeResource(null, byteArrayOutputStream2);
                                                $closeResource(null, byteArrayInputStream);
                                            } catch (Throwable th9) {
                                                th = th9;
                                                throw th;
                                            }
                                        } catch (Throwable th10) {
                                            th = th10;
                                            bArr9 = null;
                                            th = th;
                                            bArr7 = bArr9;
                                            throw th;
                                        }
                                    } catch (Exception unused3) {
                                        bArr7 = null;
                                        Log.d(str4, "composeLiveShotPicture(): failed to insert xmp metadata");
                                        if (bArr7 != null) {
                                        }
                                        Log.d(str4, "composeLiveShotPicture(): #3: return original jpeg");
                                        return bArr;
                                    }
                                } catch (Exception unused4) {
                                    bArr8 = bArr6;
                                    bArr7 = null;
                                    Log.d(str4, "composeLiveShotPicture(): failed to insert xmp metadata");
                                    if (bArr7 != null) {
                                    }
                                    Log.d(str4, "composeLiveShotPicture(): #3: return original jpeg");
                                    return bArr;
                                }
                                if (bArr7 != null || bArr7.length <= bArr8.length) {
                                    Log.d(str4, "composeLiveShotPicture(): #3: return original jpeg");
                                    return bArr;
                                }
                                int length = bArr7.length + bArr11.length;
                                StringBuilder sb = new StringBuilder();
                                sb.append("composeLiveShotPicture(): file size = ");
                                sb.append(length);
                                Log.d(str4, sb.toString());
                                byte[] bArr14 = new byte[length];
                                System.arraycopy(bArr7, 0, bArr14, 0, bArr7.length);
                                System.arraycopy(bArr11, 0, bArr14, bArr7.length, bArr11.length);
                                Log.d(str4, "composeLiveShotPicture(): X");
                                return bArr14;
                            }
                        } catch (IOException unused5) {
                            str4 = str5;
                            Log.d(str4, "composeLiveShotPicture(): Failed to generate xiaomi xmp metadata");
                            str3 = null;
                            if (str3 == null) {
                            }
                        }
                    } else {
                        stringWriter = stringWriter2;
                    }
                    iArr2 = iArr5;
                    newSerializer.startTag(null, str7);
                    newSerializer.attribute(null, str15, String.valueOf(bArr13.length + (timeWaterMarkData == null ? timeWaterMarkData.length : 0) + bArr11.length));
                    newSerializer.attribute(null, str14, String.valueOf(bArr13.length));
                    newSerializer.attribute(null, str13, String.valueOf(iArr4[0]));
                    newSerializer.attribute(null, str12, String.valueOf(iArr4[1]));
                    newSerializer.attribute(null, str11, String.valueOf(iArr4[2]));
                    newSerializer.attribute(null, str10, String.valueOf(iArr4[3]));
                    newSerializer.endTag(null, str7);
                    newSerializer.startTag(null, "timewatermark");
                    newSerializer.attribute(null, str15, String.valueOf(timeWaterMarkData.length + bArr11.length));
                    newSerializer.attribute(null, str14, String.valueOf(timeWaterMarkData.length));
                    newSerializer.attribute(null, str13, String.valueOf(iArr2[0]));
                    newSerializer.attribute(null, str12, String.valueOf(iArr2[1]));
                    newSerializer.attribute(null, str11, String.valueOf(iArr2[2]));
                    newSerializer.attribute(null, str10, String.valueOf(iArr2[3]));
                    newSerializer.endTag(null, "timewatermark");
                    newSerializer.endDocument();
                    str3 = stringWriter.toString();
                    str4 = str5;
                } catch (IOException unused6) {
                    str5 = str9;
                    bArr6 = bArr5;
                    str4 = str5;
                    Log.d(str4, "composeLiveShotPicture(): Failed to generate xiaomi xmp metadata");
                    str3 = null;
                    if (str3 == null) {
                    }
                }
            } catch (IOException unused7) {
                str2 = str8;
                str5 = str9;
                bArr6 = bArr5;
                str4 = str5;
                Log.d(str4, "composeLiveShotPicture(): Failed to generate xiaomi xmp metadata");
                str3 = null;
                if (str3 == null) {
                }
            }
            if (str3 == null) {
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ac, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        $closeResource(r10, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b0, code lost:
        throw r11;
     */
    public static byte[] composeMainSubPicture(byte[] bArr, byte[] bArr2, int[] iArr) {
        Object obj;
        byte[] bArr3;
        ByteArrayInputStream byteArrayInputStream;
        String str = "subimage";
        String str2 = TAG;
        if (!(bArr2 == null || iArr == null || iArr.length < 3)) {
            byte[] bArr4 = null;
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                StringWriter stringWriter = new StringWriter();
                newSerializer.setOutput(stringWriter);
                newSerializer.startDocument("UTF-8", Boolean.valueOf(true));
                newSerializer.startTag(null, str);
                newSerializer.attribute(null, "offset", String.valueOf(bArr2.length));
                newSerializer.attribute(null, "length", String.valueOf(bArr2.length));
                newSerializer.attribute(null, "paddingx", String.valueOf(iArr[0]));
                newSerializer.attribute(null, "paddingy", String.valueOf(iArr[1]));
                newSerializer.attribute(null, "width", String.valueOf(iArr[2]));
                newSerializer.attribute(null, "height", String.valueOf(iArr[3]));
                newSerializer.endTag(null, str);
                newSerializer.endDocument();
                obj = stringWriter.toString();
            } catch (IOException unused) {
                Log.e(str2, "composeMainSubPicture(): Failed to generate xiaomi specific xmp metadata");
                obj = null;
            }
            if (obj == null) {
                return bArr;
            }
            try {
                byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    XMPMeta createXMPMeta = XmpHelper.createXMPMeta();
                    createXMPMeta.setProperty(XmpHelper.XIAOMI_XMP_METADATA_NAMESPACE, XmpHelper.XIAOMI_XMP_METADATA_PROPERTY_NAME, obj);
                    XmpHelper.writeXMPMeta(byteArrayInputStream, byteArrayOutputStream, createXMPMeta);
                    byteArrayOutputStream.write(bArr2);
                    byteArrayOutputStream.flush();
                    bArr3 = byteArrayOutputStream.toByteArray();
                    try {
                        $closeResource(null, byteArrayOutputStream);
                        try {
                            $closeResource(null, byteArrayInputStream);
                        } catch (XMPException | IOException unused2) {
                        }
                        if (bArr3 == null && bArr3.length >= bArr.length) {
                            return bArr3;
                        }
                        Log.e(str2, "composeMainSubPicture(): Failed to append sub image, return original jpeg");
                    } catch (Throwable th) {
                        th = th;
                        bArr4 = bArr3;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (XMPException | IOException unused3) {
                bArr3 = bArr4;
                Log.d(str2, "composeMainSubPicture(): Failed to insert xiaomi specific xmp metadata");
                if (bArr3 == null) {
                }
                Log.e(str2, "composeMainSubPicture(): Failed to append sub image, return original jpeg");
                return bArr;
            } catch (Throwable th3) {
                $closeResource(th, byteArrayInputStream);
                throw th3;
            }
        }
        return bArr;
    }

    private static int computeInitialSampleSize(Options options, int i, int i2) {
        int i3;
        double d2 = (double) options.outWidth;
        double d3 = (double) options.outHeight;
        int ceil = i2 < 0 ? 1 : (int) Math.ceil(Math.sqrt((d2 * d3) / ((double) i2)));
        if (i < 0) {
            i3 = 128;
        } else {
            double d4 = (double) i;
            i3 = (int) Math.min(Math.floor(d2 / d4), Math.floor(d3 / d4));
        }
        if (i3 < ceil) {
            return ceil;
        }
        if (i2 >= 0 || i >= 0) {
            return i < 0 ? ceil : i3;
        }
        return 1;
    }

    public static int computeSampleSize(Options options, int i, int i2) {
        int computeInitialSampleSize = computeInitialSampleSize(options, i, i2);
        if (computeInitialSampleSize > 8) {
            return 8 * ((computeInitialSampleSize + 7) / 8);
        }
        int i3 = 1;
        while (i3 < computeInitialSampleSize) {
            i3 <<= 1;
        }
        return i3;
    }

    private static float computeScale(int i, int i2, float f2) {
        double atan = Math.atan(((double) i) / ((double) i2));
        return (float) ((Math.sin(Math.toRadians(((double) normalizeDegree(f2)) + Math.toDegrees(atan))) / Math.sin(atan)) + ((double) (10.0f / ((float) i))));
    }

    public static String controlAEStateToString(Integer num) {
        if (num == null) {
            return TEDefine.FACE_BEAUTY_NULL;
        }
        int intValue = num.intValue();
        if (intValue == 0) {
            return "inactive";
        }
        if (intValue == 1) {
            return "searching";
        }
        if (intValue == 2) {
            return "converged";
        }
        if (intValue == 3) {
            return "locked";
        }
        if (intValue == 4) {
            return "flash_required";
        }
        if (intValue == 5) {
            return "precapture";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unknown: ");
        sb.append(num);
        return sb.toString();
    }

    public static String controlAFStateToString(Integer num) {
        if (num == null) {
            return TEDefine.FACE_BEAUTY_NULL;
        }
        switch (num.intValue()) {
            case 0:
                return "inactive";
            case 1:
                return "passive_scan";
            case 2:
                return "passive_focused";
            case 3:
                return "active_scan";
            case 4:
                return "focused_locked";
            case 5:
                return "not_focus_locked";
            case 6:
                return "passive_unfocused";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("unknown: ");
                sb.append(num);
                return sb.toString();
        }
    }

    public static String controlAWBStateToString(Integer num) {
        if (num == null) {
            return TEDefine.FACE_BEAUTY_NULL;
        }
        int intValue = num.intValue();
        return intValue != 0 ? intValue != 1 ? intValue != 2 ? intValue != 3 ? EnvironmentCompat.MEDIA_UNKNOWN : "locked" : "converged" : "searching" : "inactive";
    }

    public static String convertOutputFormatToFileExt(int i) {
        return i == 2 ? ".mp4" : ".3gp";
    }

    public static final String convertOutputFormatToMimeType(int i) {
        return i == 2 ? "video/mp4" : "video/3gpp";
    }

    public static int convertSizeToQuality(CameraSize cameraSize) {
        int i = cameraSize.width;
        int i2 = cameraSize.height;
        if (i >= i2) {
            int i3 = i;
            i = i2;
            i2 = i3;
        }
        if (i2 == 1920 && i == 1080) {
            return 6;
        }
        if (i2 == 3840 && i == 2160) {
            return 8;
        }
        if (i2 == 1280 && i == 720) {
            return 5;
        }
        return (i2 < 640 || i != 480) ? -1 : 4;
    }

    public static void coverSubYuvImage(byte[] bArr, int i, int i2, int i3, int i4, byte[] bArr2, int[] iArr) {
        int i5 = (iArr[1] * i3) + iArr[0];
        int i6 = 0;
        for (int i7 = 0; i7 < iArr[3]; i7++) {
            System.arraycopy(bArr2, i6, bArr, i5, iArr[2]);
            i6 += iArr[2];
            i5 += i3;
        }
        int i8 = (i3 * (i2 - 1)) + i + ((iArr[1] / 2) * i4) + iArr[0];
        for (int i9 = 0; i9 < iArr[3] / 2; i9++) {
            System.arraycopy(bArr2, i6, bArr, i8, iArr[2]);
            i8 += i4;
            i6 += iArr[2];
        }
    }

    public static boolean createFile(File file) {
        if (file.exists()) {
            return false;
        }
        String parent = file.getParent();
        if (parent != null) {
            mkdirs(new File(parent), FrameMetricsAggregator.EVERY_DURATION, -1, -1);
        }
        try {
            file.createNewFile();
        } catch (IOException unused) {
        }
        return true;
    }

    public static String createJpegName(long j) {
        String generateName;
        synchronized (sImageFileNamer) {
            generateName = sImageFileNamer.generateName(j);
        }
        return generateName;
    }

    public static MeteringRectangle createMeteringRectangleFrom(int i, int i2, int i3, int i4, int i5) {
        try {
            MeteringRectangle meteringRectangle = new MeteringRectangle(0, 0, 0, 0, 0);
            int i6 = i;
            try {
                modify(meteringRectangle, "mX", i);
                int i7 = i2;
            } catch (Exception unused) {
                int i8 = i2;
                int i9 = i3;
                int i10 = i4;
                int i11 = i5;
                MeteringRectangle meteringRectangle2 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle2;
            }
            try {
                modify(meteringRectangle, "mY", i2);
                int i12 = i3;
            } catch (Exception unused2) {
                int i92 = i3;
                int i102 = i4;
                int i112 = i5;
                MeteringRectangle meteringRectangle22 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle22;
            }
            try {
                modify(meteringRectangle, "mWidth", i3);
            } catch (Exception unused3) {
                int i1022 = i4;
                int i1122 = i5;
                MeteringRectangle meteringRectangle222 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle222;
            }
            try {
                modify(meteringRectangle, "mHeight", i4);
            } catch (Exception unused4) {
                int i11222 = i5;
                MeteringRectangle meteringRectangle2222 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle2222;
            }
            try {
                modify(meteringRectangle, "mWeight", i5);
                return meteringRectangle;
            } catch (Exception unused5) {
                MeteringRectangle meteringRectangle22222 = new MeteringRectangle(i, i2, i3, i4, i5);
                return meteringRectangle22222;
            }
        } catch (Exception unused6) {
            int i13 = i;
            int i82 = i2;
            int i922 = i3;
            int i10222 = i4;
            int i112222 = i5;
            MeteringRectangle meteringRectangle222222 = new MeteringRectangle(i, i2, i3, i4, i5);
            return meteringRectangle222222;
        }
    }

    public static MeteringRectangle createMeteringRectangleFrom(Rect rect, int i) {
        try {
            return createMeteringRectangleFrom(rect.left, rect.top, rect.width(), rect.height(), i);
        } catch (Exception unused) {
            return new MeteringRectangle(rect, i);
        }
    }

    public static Bitmap cropBitmap(Bitmap bitmap, boolean z, float f2, boolean z2, float f3, boolean z3) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        String str = TAG;
        if (z || z2 || z3) {
            Bitmap bitmap2 = null;
            if (bitmap == null || bitmap.isRecycled()) {
                Log.w(str, "cropBitmap: bitmap is invalid!");
                return null;
            }
            Matrix matrix = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float f4 = 1.0f;
            if (z) {
                f4 = computeScale(width, height, f2);
                if ((((((int) f2) + 45) / 90) * 90) % 180 != 0) {
                    i4 = width;
                    i5 = height;
                } else {
                    i5 = width;
                    i4 = height;
                }
                matrix.postTranslate(((float) (-width)) / 2.0f, ((float) (-height)) / 2.0f);
                matrix.postRotate(f2);
                matrix.postTranslate(((float) i5) / 2.0f, ((float) i4) / 2.0f);
                width = i5;
                height = i4;
            }
            if (z2) {
                i = (f3 == 90.0f || f3 == 270.0f) ? 1 : -1;
                i2 = i * -1;
            } else {
                i = 1;
                i2 = 1;
            }
            if (z2 || z) {
                matrix.postScale(((float) i) * f4, ((float) i2) * f4, ((float) width) / 2.0f, ((float) height) / 2.0f);
            }
            if (z3) {
                i3 = Math.min(width, height);
                matrix.postTranslate(((float) (i3 - width)) / 2.0f, ((float) (i3 - height)) / 2.0f);
                height = i3;
            } else {
                i3 = width;
            }
            try {
                bitmap2 = Bitmap.createBitmap(i3, height, Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap2);
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                canvas.drawBitmap(bitmap, matrix, paint);
                bitmap.recycle();
            } catch (Exception | OutOfMemoryError e2) {
                Log.w(str, "Failed to adjust bitmap", e2);
            }
            return bitmap2;
        }
        Log.w(str, "cropBitmap: no effect!");
        return bitmap;
    }

    public static void displayMode(int i) {
        if (DataRepository.dataItemFeature().Hb()) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.DISPLAY_MODE_CHANGED");
            String str = "display_mode";
            if (i == 1) {
                intent.putExtra(str, 2);
            } else {
                intent.putExtra(str, 1);
            }
            try {
                CameraAppImpl.getAndroidContext().sendBroadcast(intent);
            } catch (Exception e2) {
                Log.e(TAG, "send broadcast fial!", e2);
            }
        }
    }

    public static float distance(float f2, float f3, float f4, float f5) {
        float f6 = f2 - f4;
        float f7 = f3 - f5;
        return (float) Math.sqrt((double) ((f6 * f6) + (f7 * f7)));
    }

    private static Rational doubleToRational(double d2, long j) {
        return new Rational((long) ((d2 * ((double) j)) + 0.5d), j);
    }

    public static int dpToPixel(float f2) {
        return Math.round(sPixelDensity * f2);
    }

    public static void dumpBackTrace(String str) {
        StackTraceElement[] stackTrace;
        RuntimeException runtimeException = new RuntimeException();
        StringBuilder sb = new StringBuilder();
        String str2 = "[";
        sb.append(str2);
        sb.append(str);
        String str3 = "]\n";
        sb.append(str3);
        String sb2 = sb.toString();
        String str4 = TAG;
        Log.d(str4, sb2);
        Log.d(str4, "**********print backtrace start *************");
        for (StackTraceElement stackTraceElement : runtimeException.getStackTrace()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(str);
            sb3.append("]:backtrace: ");
            sb3.append(stackTraceElement.getClassName());
            String str5 = " ";
            sb3.append(str5);
            sb3.append(stackTraceElement.getMethodName());
            sb3.append(str5);
            sb3.append(stackTraceElement.getLineNumber());
            Log.d(str4, sb3.toString());
        }
        Log.d(str4, "**********print backtrace end *************");
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append(str);
        sb4.append(str3);
        Log.d(str4, sb4.toString());
    }

    public static void dumpImageInfo(String str, Image image) {
        StringBuilder sb = new StringBuilder();
        Plane[] planes = image.getPlanes();
        for (int i = 0; i < planes.length; i++) {
            Plane plane = planes[i];
            sb.append("plane_");
            sb.append(i);
            sb.append(": ");
            sb.append(plane.getPixelStride());
            String str2 = "|";
            sb.append(str2);
            sb.append(plane.getRowStride());
            sb.append(str2);
            sb.append(plane.getBuffer().remaining());
            sb.append("\n");
        }
        Log.d(str, sb.toString());
    }

    public static String dumpMatrix(float[] fArr) {
        int length = fArr.length;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%f", new Object[]{Float.valueOf(fArr[i])}));
            if (i != length - 1) {
                sb.append(" ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void dumpRect(RectF rectF, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("=(");
        sb.append(rectF.left);
        String str2 = ",";
        sb.append(str2);
        sb.append(rectF.top);
        sb.append(str2);
        sb.append(rectF.right);
        sb.append(str2);
        sb.append(rectF.bottom);
        sb.append(")");
        Log.v(TAG, sb.toString());
    }

    public static ByteBuffer dumpToBitmap(int i, int i2, int i3, int i4, String str) {
        ByteBuffer allocate = ByteBuffer.allocate(i3 * i4 * 4);
        GLES20.glReadPixels(i, i2, i3, i4, 6408, 5121, allocate);
        if (allocate != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("tex_");
            sb.append(createJpegName(System.currentTimeMillis()));
            sb.append(str);
            String generateFilepath = Storage.generateFilepath(sb.toString(), Storage.JPEG_SUFFIX);
            saveBitmap(allocate, i3, i4, Config.ARGB_8888, generateFilepath);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("dump to ");
            sb2.append(generateFilepath);
            Log.d(TAG, sb2.toString());
        }
        allocate.rewind();
        return allocate;
    }

    public static void enterLightsOutMode(Window window) {
        LayoutParams attributes = window.getAttributes();
        attributes.systemUiVisibility |= 1;
        window.setAttributes(attributes);
    }

    public static boolean equals(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static String execCommand(String str, boolean z) {
        String str2 = TAG;
        String[] strArr = {"sh", "-c", str};
        long currentTimeMillis = System.currentTimeMillis();
        String str3 = "";
        try {
            Process exec = Runtime.getRuntime().exec(strArr);
            if (exec.waitFor() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("exit value = ");
                sb.append(exec.exitValue());
                Log.e(str2, sb.toString());
                return str3;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            if (!z) {
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                }
            } else {
                while (true) {
                    String readLine2 = bufferedReader.readLine();
                    if (readLine2 == null) {
                        break;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(readLine2);
                    sb2.append("\r\n");
                    stringBuffer.append(sb2.toString());
                }
            }
            bufferedReader.close();
            str3 = stringBuffer.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("execCommand value=");
            sb3.append(str3);
            sb3.append(" cost=");
            sb3.append(System.currentTimeMillis() - currentTimeMillis);
            Log.v(str2, sb3.toString());
            return str3;
        } catch (InterruptedException e2) {
            Log.e(str2, "execCommand InterruptedException");
            e2.printStackTrace();
        } catch (IOException e3) {
            Log.e(str2, "execCommand IOException");
            e3.printStackTrace();
        }
    }

    public static void expandViewTouchDelegate(View view) {
        if (view.isShown()) {
            Rect rect = new Rect();
            view.getHitRect(rect);
            int dpToPixel = dpToPixel(10.0f);
            rect.top -= dpToPixel;
            rect.bottom += dpToPixel;
            rect.left -= dpToPixel;
            rect.right += dpToPixel;
            TouchDelegate touchDelegate = new TouchDelegate(rect, view);
            if (View.class.isInstance(view.getParent())) {
                ((View) view.getParent()).setTouchDelegate(touchDelegate);
            }
        } else if (View.class.isInstance(view.getParent())) {
            ((View) view.getParent()).setTouchDelegate(null);
        }
    }

    public static void fadeIn(View view) {
        fadeIn(view, 400);
    }

    public static void fadeIn(View view, int i) {
        if (view != null && view.getVisibility() != 0) {
            view.setVisibility(0);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration((long) i);
            view.clearAnimation();
            view.startAnimation(alphaAnimation);
        }
    }

    public static void fadeOut(View view) {
        fadeOut(view, 400);
    }

    public static void fadeOut(View view, int i) {
        if (view != null && view.getVisibility() == 0) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration((long) i);
            view.clearAnimation();
            view.startAnimation(alphaAnimation);
            view.setVisibility(8);
        }
    }

    public static Bitmap flipBitmap(@NonNull Bitmap bitmap, int i) {
        Bitmap bitmap2 = null;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("flipBitmap: ");
        sb.append(width);
        sb.append(" x ");
        sb.append(height);
        Log.d(TAG, sb.toString());
        Matrix matrix = new Matrix();
        if (i == 1) {
            matrix.postScale(1.0f, -1.0f, (float) (width / 2), (float) (height / 2));
        } else {
            matrix.postScale(-1.0f, 1.0f, (float) (width / 2), (float) (height / 2));
        }
        try {
            bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        } catch (NullPointerException | OutOfMemoryError e2) {
            e2.printStackTrace();
        }
        if (bitmap2 == null) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap2);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, matrix, paint);
        bitmap.recycle();
        return bitmap2;
    }

    public static Bitmap generateFrontWatermark2File() {
        Bitmap loadFrontCameraWatermark = loadFrontCameraWatermark(CameraAppImpl.getAndroidContext());
        saveCustomWatermark2File(loadFrontCameraWatermark, false, true);
        return loadFrontCameraWatermark;
    }

    public static Bitmap generateUltraPixelWatermark2File() {
        Bitmap bitmap;
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = true;
        if (DataRepository.dataItemFeature().Db()) {
            Bitmap loadAppCameraWatermark = loadAppCameraWatermark(CameraAppImpl.getAndroidContext(), options, android.os.Build.DEVICE);
            if (loadAppCameraWatermark == null) {
                loadAppCameraWatermark = loadAppCameraWatermark(CameraAppImpl.getAndroidContext(), options, "common");
            }
            int integer = CameraAppImpl.getAndroidContext().getResources().getInteger(R.integer.custom_watermark_startx);
            bitmap = CustomTextWaterMark.newInstance(loadAppCameraWatermark, (float) integer, (float) CameraAppImpl.getAndroidContext().getResources().getInteger(R.integer.custom_watermark_starty), CameraSettings.getString(R.string.device_ultra_pixel_watermark_default_text)).drawToBitmap();
        } else {
            Context androidContext = CameraAppImpl.getAndroidContext();
            StringBuilder sb = new StringBuilder();
            sb.append(android.os.Build.DEVICE);
            sb.append(CameraSettings.getString(R.string.device_ultra_pixel_app_watermark_family_name_suffix));
            bitmap = loadAppCameraWatermark(androidContext, options, sb.toString());
        }
        saveCustomWatermark2File(bitmap, true, false);
        return bitmap;
    }

    public static Bitmap generateWatermark2File() {
        long currentTimeMillis = System.currentTimeMillis();
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = true;
        if (!DataRepository.dataItemFeature().Uc() && !DataRepository.dataItemFeature().hd()) {
            return null;
        }
        Bitmap loadAppCameraWatermark = loadAppCameraWatermark(CameraAppImpl.getAndroidContext(), options, android.os.Build.DEVICE);
        if (loadAppCameraWatermark == null) {
            loadAppCameraWatermark = loadAppCameraWatermark(CameraAppImpl.getAndroidContext(), options, "common");
        }
        if (DataRepository.dataItemFeature().Db()) {
            loadAppCameraWatermark = CustomTextWaterMark.newInstance(loadAppCameraWatermark, (float) CameraAppImpl.getAndroidContext().getResources().getInteger(R.integer.custom_watermark_startx), (float) CameraAppImpl.getAndroidContext().getResources().getInteger(R.integer.custom_watermark_starty), CameraSettings.getCustomWatermark()).drawToBitmap();
        }
        saveCustomWatermark2File(loadAppCameraWatermark, false, false);
        DataRepository.dataItemGlobal().updateCustomWatermarkVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("generateWatermark2File cost time = ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append("ms");
        Log.d(TAG, sb.toString());
        return loadAppCameraWatermark;
    }

    public static CameraSize getAlgorithmPreviewSize(List<CameraSize> list, double d2, CameraSize cameraSize) {
        if (cameraSize != null) {
            String str = TAG;
            if (list == null || list.isEmpty()) {
                Log.w(str, "null preview size list");
                return cameraSize;
            }
            int max = Math.max(SystemProperties.getInt("algorithm_limit_height", cameraSize.height), MiuiSettings.System.SCREEN_KEY_LONG_PRESS_TIMEOUT_DEFAULT);
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CameraSize cameraSize2 = (CameraSize) it.next();
                if (Math.abs((((double) cameraSize2.width) / ((double) cameraSize2.height)) - d2) <= 0.02d && cameraSize2.height < max) {
                    cameraSize = cameraSize2;
                    break;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("getAlgorithmPreviewSize: algorithmSize = ");
            sb.append(cameraSize);
            Log.d(str, sb.toString());
            return cameraSize;
        }
        throw new IllegalArgumentException("limitSize can not be null!");
    }

    public static int getArrayIndex(int[] iArr, int i) {
        if (iArr == null) {
            return -1;
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] == i) {
                return i2;
            }
        }
        return -1;
    }

    public static <T> int getArrayIndex(T[] tArr, T t) {
        if (tArr == null) {
            return -1;
        }
        int i = 0;
        for (T equals : tArr) {
            if (Objects.equals(equals, t)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static int getAttributeIntValue(XmlPullParser xmlPullParser, String str, int i) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        if (TextUtils.isEmpty(attributeValue)) {
            return i;
        }
        try {
            return Integer.parseInt(attributeValue);
        } catch (Exception e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("get attribute ");
            sb.append(str);
            sb.append(" failed");
            Log.w(TAG, sb.toString(), e2);
            return i;
        }
    }

    public static byte[] getBitmapData(Bitmap bitmap, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int getBottomHeight(Resources resources) {
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.bottom_control_height);
        return (!isNotchDevice || !sIsFullScreenNavBarHidden || isLongRatioScreen) ? dimensionPixelSize : dimensionPixelSize - sFullScreenExtraMargin;
    }

    private static String getCaller(StackTraceElement[] stackTraceElementArr, int i) {
        int i2 = i + 4;
        if (i2 >= stackTraceElementArr.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement stackTraceElement = stackTraceElementArr[i2];
        StringBuilder sb = new StringBuilder();
        sb.append(stackTraceElement.getClassName());
        sb.append(".");
        sb.append(stackTraceElement.getMethodName());
        sb.append(":");
        sb.append(stackTraceElement.getLineNumber());
        return sb.toString();
    }

    public static String getCallers(int i) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(getCaller(stackTrace, i2));
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }

    public static int getCenterFocusDepthIndex(byte[] bArr, int i, int i2) {
        if (bArr == null || bArr.length < 25) {
            return 1;
        }
        int length = bArr.length - 25;
        int i3 = length + 1;
        if (bArr[length] != 0) {
            return 1;
        }
        int i4 = i3 + 1;
        int i5 = i4 + 1;
        byte b2 = ((bArr[i4] & 255) << 16) | ((bArr[i3] & 255) << 24);
        int i6 = i5 + 1;
        byte b3 = b2 | ((bArr[i5] & 255) << 8);
        int i7 = i6 + 1;
        byte b4 = b3 | (bArr[i6] & 255);
        int i8 = i7 + 1;
        int i9 = i8 + 1;
        byte b5 = ((bArr[i8] & 255) << 16) | ((bArr[i7] & 255) << 24) | ((bArr[i9] & 255) << 8) | (bArr[i9 + 1] & 255);
        Resources resources = CameraAppImpl.getAndroidContext().getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.focus_area_width);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.focus_area_height);
        int i10 = dimensionPixelSize * b4;
        int i11 = sWindowWidth;
        int i12 = i10 / i11;
        int i13 = (int) (((float) (dimensionPixelSize2 * b5)) / ((((float) i11) * ((float) i2)) / ((float) i)));
        int[] iArr = new int[5];
        int i14 = 0;
        int i15 = (b5 - i13) / 2;
        int i16 = 0;
        while (i16 < i13) {
            int i17 = i15 + 1;
            int i18 = (i15 * b4) + ((b4 - i12) / 2);
            int i19 = 0;
            while (i19 < i12) {
                int i20 = i18 + 1;
                byte b6 = bArr[i18];
                iArr[b6] = iArr[b6] + 1;
                i19++;
                i18 = i20;
            }
            i16++;
            i15 = i17;
        }
        for (int i21 = 1; i21 < 5; i21++) {
            if (iArr[i14] < iArr[i21]) {
                i14 = i21;
            }
        }
        return i14;
    }

    public static int getChildMeasureWidth(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        int i = marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        int measuredWidth = view.getMeasuredWidth();
        if (measuredWidth > 0) {
            return measuredWidth + i;
        }
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        view.measure(makeMeasureSpec, makeMeasureSpec);
        return view.getMeasuredWidth() + i;
    }

    private static File getColorMapXmlMapFile() {
        int i = VERSION.SDK_INT;
        String str = TAG;
        if (i >= 26) {
            File file = (!b.Nn || !SystemProperties.get("ro.boot.hwc").equalsIgnoreCase("India")) ? new File("/vendor/etc/screen_light.xml") : new File("/vendor/etc/screen_light_ind.xml");
            if (file.exists()) {
                return file;
            }
            Log.e(str, "screen_light.xml do not found under /vendor/etc, roll back to /system/etc");
        }
        File file2 = new File("/system/etc/screen_light.xml");
        if (file2.exists()) {
            return file2;
        }
        Log.e(str, "screen_light.xml do not found under /system/etc");
        return null;
    }

    public static String getDebugInfo() {
        StringBuilder sb = new StringBuilder();
        String str = "1";
        if (str.equals(SystemProperties.get("persist.camera.debug.show_af")) || str.equals(SystemProperties.get("persist.camera.debug.enable"))) {
            sb.append(addProperties("persist.camera.debug.param0"));
            sb.append(addProperties("persist.camera.debug.param1"));
            sb.append(addProperties("persist.camera.debug.param2"));
            sb.append(addProperties("persist.camera.debug.param3"));
            sb.append(addProperties("persist.camera.debug.param4"));
            sb.append(addProperties("persist.camera.debug.param5"));
            sb.append(addProperties("persist.camera.debug.param6"));
            sb.append(addProperties("persist.camera.debug.param7"));
            sb.append(addProperties("persist.camera.debug.param8"));
            sb.append(addProperties("persist.camera.debug.param9"));
        }
        if (str.equals(SystemProperties.get("persist.camera.debug.show_awb"))) {
            sb.append(addProperties("persist.camera.debug.param10"));
            sb.append(addProperties("persist.camera.debug.param11"));
            sb.append(addProperties("persist.camera.debug.param12"));
            sb.append(addProperties("persist.camera.debug.param13"));
            sb.append(addProperties("persist.camera.debug.param14"));
            sb.append(addProperties("persist.camera.debug.param15"));
            sb.append(addProperties("persist.camera.debug.param16"));
            sb.append(addProperties("persist.camera.debug.param17"));
            sb.append(addProperties("persist.camera.debug.param18"));
            sb.append(addProperties("persist.camera.debug.param19"));
        }
        if (str.equals(SystemProperties.get("persist.camera.debug.show_aec"))) {
            sb.append(addProperties("persist.camera.debug.param20"));
            sb.append(addProperties("persist.camera.debug.param21"));
            sb.append(addProperties("persist.camera.debug.param22"));
            sb.append(addProperties("persist.camera.debug.param23"));
            sb.append(addProperties("persist.camera.debug.param24"));
            sb.append(addProperties("persist.camera.debug.param25"));
            sb.append(addProperties("persist.camera.debug.param26"));
            sb.append(addProperties("persist.camera.debug.param27"));
            sb.append(addProperties("persist.camera.debug.param28"));
            sb.append(addProperties("persist.camera.debug.param29"));
        }
        sb.append(addProperties("persist.camera.debug.checkerf"));
        sb.append(addProperties("persist.camera.debug.fc"));
        if (str.equals(SystemProperties.get("persist.camera.debug.hht"))) {
            sb.append(addProperties("camera.debug.hht.luma"));
        }
        if (str.equals(SystemProperties.get("persist.camera.debug.autoscene"))) {
            sb.append(addProperties("camera.debug.hht.iso"));
        }
        return sb.toString();
    }

    public static String getDebugInformation(CaptureResult captureResult) {
        StringBuilder sb = new StringBuilder();
        AECFrameControl aECFrameControl = CaptureResultParser.getAECFrameControl(captureResult);
        AFFrameControl aFFrameControl = CaptureResultParser.getAFFrameControl(captureResult);
        String str = "1";
        if (!(!str.equals(SystemProperties.get("camera.preview.debug.show_shortGain")) || aECFrameControl == null || aECFrameControl.getAecExposureDatas() == null)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("short gain : ");
            sb2.append(aECFrameControl.getAecExposureDatas()[0].getLinearGain());
            sb.append(addDebugInfo(sb2.toString()));
        }
        if (!(!str.equals(SystemProperties.get("camera.preview.debug.show_adrcGain")) || aECFrameControl == null || aECFrameControl.getAecExposureDatas() == null)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("adrc gain : ");
            sb3.append(aECFrameControl.getAecExposureDatas()[2].getSensitivity() / aECFrameControl.getAecExposureDatas()[0].getSensitivity());
            sb.append(addDebugInfo(sb3.toString()));
        }
        if (str.equals(SystemProperties.get("camera.preview.debug.show_afRegion"))) {
            MeteringRectangle[] meteringRectangleArr = (MeteringRectangle[]) captureResult.get(CaptureResult.CONTROL_AF_REGIONS);
            if (meteringRectangleArr != null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("af region : ");
                sb4.append(meteringRectangleArr[0].getRect().toString());
                sb.append(addDebugInfo(sb4.toString()));
            }
        }
        if (str.equals(SystemProperties.get("camera.preview.debug.show_afMode"))) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("af mode : ");
            sb5.append(captureResult.get(CaptureResult.CONTROL_AF_MODE));
            sb.append(addDebugInfo(sb5.toString()));
        }
        if (str.equals(SystemProperties.get("camera.preview.debug.show_afStatus"))) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("af state : ");
            sb6.append(captureResult.get(CaptureResult.CONTROL_AF_STATE));
            sb.append(addDebugInfo(sb6.toString()));
        }
        if (str.equals(SystemProperties.get("camera.preview.debug.show_afLensPosition")) && aFFrameControl != null) {
            String str2 = "";
            if (aFFrameControl.getUseDACValue() == 0) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(aFFrameControl.getTargetLensPosition());
                sb7.append(str2);
                str2 = sb7.toString();
            }
            StringBuilder sb8 = new StringBuilder();
            sb8.append("af lens position : ");
            sb8.append(str2);
            sb.append(addDebugInfo(sb8.toString()));
        }
        if (str.equals(SystemProperties.get("camera.preview.debug.show_distance")) && captureResult.get(CaptureResult.LENS_FOCUS_DISTANCE) != null) {
            float floatValue = ((Float) captureResult.get(CaptureResult.LENS_FOCUS_DISTANCE)).floatValue();
            StringBuilder sb9 = new StringBuilder();
            sb9.append("distance : ");
            sb9.append(floatValue);
            sb.append(addDebugInfo(sb9.toString()));
            StringBuilder sb10 = new StringBuilder();
            sb10.append("distance(m) : ");
            sb10.append(1.0f / floatValue);
            sb.append(addDebugInfo(sb10.toString()));
        }
        if (str.equals(SystemProperties.get("camera.preview.debug.show_gyro")) && aFFrameControl != null) {
            for (int i = 0; i < aFFrameControl.getAFGyroData().getSampleCount(); i++) {
                StringBuilder sb11 = new StringBuilder();
                sb11.append("gyro : x: ");
                sb11.append(aFFrameControl.getAFGyroData().getpAngularVelocityX()[i]);
                sb11.append(", y: ");
                sb11.append(aFFrameControl.getAFGyroData().getpAngularVelocityY()[i]);
                sb11.append(", z: ");
                sb11.append(aFFrameControl.getAFGyroData().getpAngularVelocityZ()[i]);
                sb.append(addDebugInfo(sb11.toString()));
            }
        }
        if (str.equals(SystemProperties.get("camera.preview.debug.sat_info"))) {
            byte[] satDbgInfo = CaptureResultParser.getSatDbgInfo(captureResult);
            if (satDbgInfo != null) {
                sb.append(addDebugInfo(new String(satDbgInfo)));
            }
        }
        return sb.toString();
    }

    public static int getDialogTopMargin(int i) {
        return isNotchDevice ? i - sStatusBarHeight : i;
    }

    public static int getDisplayOrientation(int i, int i2) {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i2);
        if (capabilities == null) {
            return 90;
        }
        int sensorOrientation = capabilities.getSensorOrientation();
        return capabilities.getFacing() == 0 ? (360 - ((sensorOrientation + i) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT)) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT : ((sensorOrientation - i) + ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT;
    }

    public static Rect getDisplayRect(Context context) {
        return getDisplayRect(context, DataRepository.dataItemRunning().getUiStyle());
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x007b  */
    public static Rect getDisplayRect(Context context, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (i != 0) {
            if (i != 3) {
                i2 = (int) (((float) (sWindowWidth * 16)) / 9.0f);
                if (!isLongRatioScreen) {
                    i5 = sWindowHeight - i2;
                    i4 = sNavigationBarHeight;
                } else if (DataRepository.dataItemGlobal().getDisplayMode() == 2) {
                    i5 = sWindowHeight - i2;
                    i4 = getBottomHeight(context.getResources());
                } else {
                    i3 = (sWindowHeight - ((int) (((float) (sWindowWidth * 4)) / 3.0f))) - getBottomHeight(context.getResources());
                }
            } else if (DataRepository.dataItemGlobal().getDisplayMode() == 2) {
                i2 = (int) ((((double) sWindowWidth) * 19.5d) / 9.0d);
                i3 = 0;
            } else {
                int i6 = sWindowHeight;
                i3 = sStatusBarHeight;
                i2 = i6 - i3;
            }
            if (i3 <= 2) {
                i3 = 0;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("top_margin:");
            sb.append(i3);
            sb.append(",preview_height:");
            sb.append(i2);
            sb.append(",bottom_height:");
            sb.append(getBottomHeight(context.getResources()));
            sb.append(",nav_height:");
            sb.append(sNavigationBarHeight);
            Log.d("display_rect", sb.toString());
            return new Rect(0, i3, sWindowWidth, i2 + i3);
        }
        i2 = (int) (((float) (sWindowWidth * 4)) / 3.0f);
        i5 = sWindowHeight - i2;
        i4 = getBottomHeight(context.getResources());
        i3 = i5 - i4;
        if (i3 <= 2) {
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("top_margin:");
        sb2.append(i3);
        sb2.append(",preview_height:");
        sb2.append(i2);
        sb2.append(",bottom_height:");
        sb2.append(getBottomHeight(context.getResources()));
        sb2.append(",nav_height:");
        sb2.append(sNavigationBarHeight);
        Log.d("display_rect", sb2.toString());
        return new Rect(0, i3, sWindowWidth, i2 + i3);
    }

    public static int getDisplayRotation(Activity activity) {
        int i;
        if (!b.di() || !CameraSettings.isFrontCamera() || activity.getRequestedOrientation() != 7) {
            int i2 = mLockedOrientation;
            i = (i2 == 0 || i2 == 2) ? mLockedOrientation : 0;
        } else {
            i = activity.getWindowManager().getDefaultDisplay().getRotation();
        }
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 90;
        }
        if (i != 2) {
            return i != 3 ? 0 : 270;
        }
        return 180;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0050, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        $closeResource(r0, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0054, code lost:
        throw r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006e  */
    private static byte[] getDualCameraWatermarkData(int i, int i2, int[] iArr) {
        String str;
        byte[] bArr;
        Bitmap decodeByteArray;
        if (DataRepository.dataItemFeature().Uc() || DataRepository.dataItemFeature().hd()) {
            str = new File(CameraAppImpl.getAndroidContext().getFilesDir(), WATERMARK_FILE_NAME).getPath();
            if (!new File(str).exists()) {
                generateWatermark2File();
            }
        } else {
            str = CameraSettings.getDualCameraWaterMarkFilePathVendor();
        }
        byte[] bArr2 = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            bArr = IOUtils.toByteArray((InputStream) fileInputStream);
            try {
                $closeResource(null, fileInputStream);
            } catch (IOException e2) {
                Throwable th = e2;
                bArr2 = bArr;
                e = th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.d(TAG, "Failed to load dual camera water mark", e);
            bArr = bArr2;
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
            }
            return bArr;
        }
        if (!(bArr == null || iArr == null || iArr.length < 4)) {
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
                int i3 = i;
                int i4 = i2;
                int[] calcDualCameraWatermarkLocation = calcDualCameraWatermarkLocation(i3, i4, decodeByteArray.getWidth(), decodeByteArray.getHeight(), CameraSettings.getResourceFloat(R.dimen.dualcamera_watermark_size_ratio, 1.0f), CameraSettings.getResourceFloat(R.dimen.dualcamera_watermark_padding_x_ratio, 1.0f), CameraSettings.getResourceFloat(R.dimen.dualcamera_watermark_padding_y_ratio, 1.0f));
                System.arraycopy(calcDualCameraWatermarkLocation, 0, iArr, 0, calcDualCameraWatermarkLocation.length);
            }
        }
        return bArr;
    }

    /* JADX INFO: finally extract failed */
    public static long getDuration(FileDescriptor fileDescriptor) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(fileDescriptor);
            long parseLong = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
            mediaMetadataRetriever.release();
            return parseLong;
        } catch (IllegalArgumentException e2) {
            Log.e(TAG, e2.getMessage(), e2);
            mediaMetadataRetriever.release();
            return 0;
        } catch (Throwable th) {
            mediaMetadataRetriever.release();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public static long getDuration(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            long parseLong = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
            mediaMetadataRetriever.release();
            return parseLong;
        } catch (Exception e2) {
            Log.e(TAG, e2.getMessage(), e2);
            mediaMetadataRetriever.release();
            return 0;
        } catch (Throwable th) {
            mediaMetadataRetriever.release();
            throw th;
        }
    }

    public static ExifInterface getExif(String str) {
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(str);
        } catch (IOException e2) {
            Log.d(TAG, e2.getMessage());
        }
        return exifInterface;
    }

    public static ExifInterface getExif(byte[] bArr) {
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(bArr);
        } catch (IOException e2) {
            Log.d(TAG, e2.getMessage());
        }
        return exifInterface;
    }

    public static String getFileTitleFromPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf < 0 || lastIndexOf >= str.length() - 1) {
            return null;
        }
        String substring = str.substring(lastIndexOf + 1);
        if (TextUtils.isEmpty(substring)) {
            return null;
        }
        int indexOf = substring.indexOf(".");
        if (indexOf >= 0) {
            substring = substring.substring(0, indexOf);
        }
        return substring;
    }

    public static byte[] getFirstPlane(Image image) {
        Plane[] planes = image.getPlanes();
        if (planes.length <= 0) {
            return null;
        }
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bArr = new byte[buffer.remaining()];
        buffer.get(bArr);
        return bArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004b, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004c, code lost:
        if (r0 != null) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        $closeResource(r2, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
        throw r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006b  */
    private static byte[] getFrontCameraWatermarkData(int i, int i2, int[] iArr) {
        byte[] bArr;
        Bitmap decodeByteArray;
        StringBuilder sb = new StringBuilder();
        sb.append(android.os.Build.DEVICE);
        sb.append("_front");
        sb.append(b.Sh());
        sb.append(".png");
        String sb2 = sb.toString();
        try {
            AssetManager assets = CameraAppImpl.getAndroidContext().getAssets();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("watermarks/");
            sb3.append(sb2);
            InputStream open = assets.open(sb3.toString());
            bArr = IOUtils.toByteArray(open);
            if (open != null) {
                try {
                    $closeResource(null, open);
                } catch (IOException e2) {
                    e = e2;
                }
            }
        } catch (IOException e3) {
            e = e3;
            bArr = null;
            Log.d(TAG, "Failed to load front camera water mark", e);
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
            }
            return bArr;
        }
        if (!(bArr == null || iArr == null || iArr.length < 4)) {
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
                int i3 = i;
                int i4 = i2;
                int[] calcDualCameraWatermarkLocation = calcDualCameraWatermarkLocation(i3, i4, decodeByteArray.getWidth(), decodeByteArray.getHeight(), CameraSettings.getResourceFloat(R.dimen.frontcamera_watermark_size_ratio, 1.0f), CameraSettings.getResourceFloat(R.dimen.frontcamera_watermark_padding_x_ratio, 1.0f), CameraSettings.getResourceFloat(R.dimen.frontcamera_watermark_padding_y_ratio, 1.0f));
                System.arraycopy(calcDualCameraWatermarkLocation, 0, iArr, 0, calcDualCameraWatermarkLocation.length);
            }
        }
        return bArr;
    }

    public static int getIntField(String str, Object obj, String str2, String str3) {
        String str4 = TAG;
        try {
            return Field.of(str, str2, str3).getInt(obj);
        } catch (NoSuchClassException e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("no class ");
            sb.append(str);
            Log.e(str4, sb.toString(), e2);
            return Integer.MIN_VALUE;
        } catch (NoSuchFieldException e3) {
            Log.e(str4, "no field ", e3);
            return Integer.MIN_VALUE;
        }
    }

    public static int getJpegRotation(int i, int i2) {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i);
        int sensorOrientation = capabilities.getSensorOrientation();
        if (i2 != -1) {
            return capabilities.getFacing() == 0 ? ((sensorOrientation - i2) + ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT : (sensorOrientation + i2) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT;
        }
        Log.w(TAG, "getJpegRotation: orientation UNKNOWN!!! return sensorOrientation...");
        return sensorOrientation;
    }

    public static Typeface getLanTineGBTypeface(Context context) {
        return getTypefaceFromFile(context, "system/etc/ANXCamera/fonts/MI+LanTing_GB+Outside+YS_V2.3_20160322.ttf");
    }

    public static Typeface getMFYueYuanTypeface(Context context) {
        return getTypefaceFromFile(context, "system/etc/ANXCamera/fonts/MFYueYuan-Regular.ttf");
    }

    public static Method getMethod(Class<?>[] clsArr, String str, String str2) {
        Method method = null;
        if (clsArr != null) {
            try {
                if (clsArr.length == 1) {
                    method = Method.of(clsArr[0], str, str2);
                }
            } catch (NoSuchMethodException unused) {
                if (clsArr[0].getSuperclass() != null) {
                    clsArr[0] = clsArr[0].getSuperclass();
                    method = getMethod(clsArr, str, str2);
                }
            }
        }
        if (method == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("getMethod fail, ");
            sb.append(str);
            sb.append("[");
            sb.append(str2);
            sb.append("]");
            Log.e(TAG, sb.toString());
        }
        return method;
    }

    public static Typeface getMiuiTimeTypeface(Context context) {
        return getTypefaceFromAssets(context, "fonts/MIUI_Time.ttf");
    }

    public static Typeface getMiuiTypeface(Context context) {
        return getTypefaceFromAssets(context, "fonts/MIUI_Normal.ttf");
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        StringBuilder sb = new StringBuilder();
        sb.append("navBarHeight=");
        sb.append(dimensionPixelSize);
        Log.v(TAG, sb.toString());
        return dimensionPixelSize;
    }

    public static CameraSize getOptimalJpegThumbnailSize(List<CameraSize> list, double d2) {
        String str = TAG;
        CameraSize cameraSize = null;
        if (list == null) {
            Log.w(str, "null thumbnail size list");
            return null;
        }
        double d3 = 0.0d;
        for (CameraSize cameraSize2 : list) {
            if (!(cameraSize2.getWidth() == 0 || cameraSize2.getHeight() == 0)) {
                double width = ((double) cameraSize2.getWidth()) / ((double) cameraSize2.getHeight());
                double abs = Math.abs(width - d2);
                double d4 = d3 - d2;
                if ((abs <= Math.abs(d4) || abs <= 0.001d) && (cameraSize == null || abs < Math.abs(d4) || cameraSize2.getWidth() > cameraSize.getWidth())) {
                    cameraSize = cameraSize2;
                    d3 = width;
                }
            }
        }
        if (cameraSize == null) {
            Log.w(str, "No thumbnail size match the aspect ratio");
            for (CameraSize cameraSize3 : list) {
                if (cameraSize == null || cameraSize3.getWidth() > cameraSize.getWidth()) {
                    cameraSize = cameraSize3;
                }
            }
        }
        return cameraSize;
    }

    public static CameraSize getOptimalPreviewSize(boolean z, int i, List<CameraSize> list, double d2) {
        return getOptimalPreviewSize(z, i, list, d2, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0181  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x012f A[SYNTHETIC] */
    public static CameraSize getOptimalPreviewSize(boolean z, int i, List<CameraSize> list, double d2, CameraSize cameraSize) {
        boolean z2;
        Point point;
        int i2;
        Iterator it;
        Point point2;
        CameraSize cameraSize2;
        CameraSize cameraSize3 = cameraSize;
        CameraSize cameraSize4 = null;
        String str = TAG;
        if (list == null) {
            Log.w(str, "null preview size list");
            return null;
        }
        int integer = d.getInteger(d.Ro, 0);
        int i3 = ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_END_DEAULT;
        if (integer != 0) {
            boolean z3 = i == Camera2DataContainer.getInstance().getFrontCameraId();
            if (sWindowWidth < 1080) {
                integer &= -15;
            }
            if ((integer & (((z3 ? 2 : 1) << (!z ? 0 : 2)) | 0)) != 0) {
                z2 = true;
                int i4 = sWindowWidth;
                int i5 = sWindowHeight;
                if (z2) {
                    i5 = Math.min(i5, 1920);
                }
                point = new Point(i4, i5);
                if (!b.vi() && b.Dj()) {
                    i3 = LIMIT_SURFACE_WIDTH;
                }
                i2 = point.x;
                if (i2 > i3) {
                    point.y = (point.y * i3) / i2;
                    point.x = i3;
                }
                if (cameraSize3 != null) {
                    if (point.x > cameraSize3.height || point.y > cameraSize3.width) {
                        double d3 = ((double) point.y) / ((double) point.x);
                        int i6 = cameraSize3.width;
                        int i7 = cameraSize3.height;
                        if (i6 <= i7) {
                            i7 = i6;
                        }
                        point.x = i7;
                        point.y = (int) (d3 * ((double) point.x));
                    }
                    z2 = false;
                }
                it = list.iterator();
                CameraSize cameraSize5 = null;
                double d4 = Double.MAX_VALUE;
                double d5 = Double.MAX_VALUE;
                while (true) {
                    if (it.hasNext()) {
                        point2 = point;
                        cameraSize2 = cameraSize5;
                        break;
                    }
                    cameraSize2 = (CameraSize) it.next();
                    Point point3 = point;
                    if (Math.abs((((double) cameraSize2.width) / ((double) cameraSize2.height)) - d2) > 0.02d) {
                        point2 = point3;
                    } else {
                        point2 = point3;
                        if (!z2 || (point2.x > cameraSize2.height && point2.y > cameraSize2.width)) {
                            int abs = Math.abs(point2.x - cameraSize2.height) + Math.abs(point2.y - cameraSize2.width);
                            if (abs == 0) {
                                cameraSize4 = cameraSize2;
                                break;
                            }
                            if (cameraSize2.height <= point2.x && cameraSize2.width <= point2.y) {
                                double d6 = (double) abs;
                                if (d6 < d4) {
                                    d4 = d6;
                                    cameraSize5 = cameraSize2;
                                }
                            }
                            double d7 = (double) abs;
                            if (d7 < d5) {
                                d5 = d7;
                                cameraSize4 = cameraSize2;
                            }
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("getOptimalPreviewSize: ");
                            sb.append(cameraSize2.toString());
                            sb.append(" | ");
                            sb.append(point2.toString());
                            Log.e(str, sb.toString());
                        }
                    }
                    point = point2;
                }
                if (cameraSize2 == null) {
                    cameraSize2 = cameraSize4;
                }
                if (cameraSize2 == null) {
                    Log.w(str, String.format(Locale.ENGLISH, "no preview size match the aspect ratio: %.2f", new Object[]{Double.valueOf(d2)}));
                    double d8 = Double.MAX_VALUE;
                    for (CameraSize cameraSize6 : list) {
                        double abs2 = (double) (Math.abs(point2.x - cameraSize6.getHeight()) + Math.abs(point2.y - cameraSize6.getWidth()));
                        if (abs2 < d8) {
                            cameraSize2 = cameraSize6;
                            d8 = abs2;
                        }
                    }
                }
                if (cameraSize2 != null) {
                    Log.i(str, String.format(Locale.ENGLISH, "best preview size: %dx%d", new Object[]{Integer.valueOf(cameraSize2.getWidth()), Integer.valueOf(cameraSize2.getHeight())}));
                }
                return cameraSize2;
            }
        }
        z2 = false;
        int i42 = sWindowWidth;
        int i52 = sWindowHeight;
        if (z2) {
        }
        point = new Point(i42, i52);
        i3 = LIMIT_SURFACE_WIDTH;
        i2 = point.x;
        if (i2 > i3) {
        }
        if (cameraSize3 != null) {
        }
        it = list.iterator();
        CameraSize cameraSize52 = null;
        double d42 = Double.MAX_VALUE;
        double d52 = Double.MAX_VALUE;
        while (true) {
            if (it.hasNext()) {
            }
            point = point2;
        }
        if (cameraSize2 == null) {
        }
        if (cameraSize2 == null) {
        }
        if (cameraSize2 != null) {
        }
        return cameraSize2;
    }

    public static CameraSize getOptimalVideoSnapshotPictureSize(List<CameraSize> list, double d2, int i, int i2) {
        String str = TAG;
        CameraSize cameraSize = null;
        if (list == null) {
            Log.e(str, "null size list");
            return null;
        }
        for (CameraSize cameraSize2 : list) {
            if (Math.abs((((double) cameraSize2.getWidth()) / ((double) cameraSize2.getHeight())) - d2) <= 0.02d && ((cameraSize == null || cameraSize2.getWidth() > cameraSize.getWidth()) && cameraSize2.getWidth() <= i && cameraSize2.getHeight() <= i2)) {
                cameraSize = cameraSize2;
            }
        }
        if (cameraSize == null) {
            Log.w(str, "No picture size match the aspect ratio");
            for (CameraSize cameraSize3 : list) {
                if (cameraSize == null || cameraSize3.getWidth() > cameraSize.getWidth()) {
                    cameraSize = cameraSize3;
                }
            }
        }
        return cameraSize;
    }

    public static byte[] getPixels(byte[] bArr, int i, int i2, int[] iArr) {
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[(iArr[2] * iArr[3] * i2)];
        int i3 = ((iArr[1] * i) + iArr[0]) * i2;
        int i4 = 0;
        for (int i5 = 0; i5 < iArr[3]; i5++) {
            System.arraycopy(bArr, i3, bArr2, i4, iArr[2] * i2);
            i3 += i * i2;
            i4 += iArr[2] * i2;
        }
        return bArr2;
    }

    public static Rect getPreviewRect(Context context) {
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        Rect displayRect = getDisplayRect(context, uiStyle);
        if (uiStyle == 3 && isNotchDevice && DataRepository.dataItemFeature().Gc()) {
            displayRect.top = 0;
        }
        return displayRect;
    }

    public static float getRatio(String str) {
        char c2;
        switch (str.hashCode()) {
            case -2109552250:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_18_7_5X9)) {
                    c2 = 6;
                    break;
                }
            case 50858:
                if (str.equals(ComponentConfigRatio.RATIO_1X1)) {
                    c2 = 2;
                    break;
                }
            case 53743:
                if (str.equals(ComponentConfigRatio.RATIO_4X3)) {
                    c2 = 0;
                    break;
                }
            case 1515430:
                if (str.equals(ComponentConfigRatio.RATIO_16X9)) {
                    c2 = 1;
                    break;
                }
            case 1517352:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_18X9)) {
                    c2 = 3;
                    break;
                }
            case 1518313:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_19X9)) {
                    c2 = 4;
                    break;
                }
            case 1456894192:
                if (str.equals(ComponentConfigRatio.RATIO_FULL_195X9)) {
                    c2 = 5;
                    break;
                }
            default:
                c2 = 65535;
                break;
        }
        switch (c2) {
            case 0:
                return 1.3333333f;
            case 1:
                return 1.7777777f;
            case 2:
                return 1.0f;
            case 3:
                return 2.0f;
            case 4:
                return 2.1111112f;
            case 5:
                return 2.1666667f;
            case 6:
                return 2.0833333f;
            default:
                return 1.3333333f;
        }
    }

    public static int[] getRelativeLocation(View view, View view2) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        view2.getLocationInWindow(iArr);
        iArr[0] = iArr[0] - i;
        iArr[1] = iArr[1] - i2;
        return iArr;
    }

    public static double getScreenInches(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        double sqrt = Math.sqrt(Math.pow((double) (((float) sWindowWidth) / displayMetrics.xdpi), 2.0d) + Math.pow((double) (((float) sWindowHeight) / displayMetrics.ydpi), 2.0d));
        StringBuilder sb = new StringBuilder();
        sb.append("getScreenInches=");
        sb.append(sqrt);
        Log.d(TAG, sb.toString());
        return sqrt;
    }

    public static int getScreenLightColor(int i) {
        initScreenLightColorMap();
        int size = COLOR_TEMPERATURE_LIST.size();
        String str = TAG;
        if (size == 0 || COLOR_TEMPERATURE_MAP.size() == 0) {
            Log.e(str, "color temperature list empty!");
            return -1;
        }
        int binarySearchRightMost = binarySearchRightMost(COLOR_TEMPERATURE_LIST, Integer.valueOf(i));
        if (binarySearchRightMost >= COLOR_TEMPERATURE_LIST.size()) {
            binarySearchRightMost = COLOR_TEMPERATURE_LIST.size() - 1;
        } else if (binarySearchRightMost > 0) {
            int i2 = binarySearchRightMost - 1;
            if (((Integer) COLOR_TEMPERATURE_LIST.get(binarySearchRightMost)).intValue() - i > i - ((Integer) COLOR_TEMPERATURE_LIST.get(i2)).intValue()) {
                binarySearchRightMost = i2;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("getScreenLightColor ");
        sb.append(i);
        sb.append("K -> ");
        sb.append(COLOR_TEMPERATURE_LIST.get(binarySearchRightMost));
        sb.append(GpsSpeedRef.KILOMETERS);
        Log.d(str, sb.toString());
        return ((Integer) COLOR_TEMPERATURE_MAP.get(binarySearchRightMost)).intValue();
    }

    public static int getSensorOrientation(int i) {
        return Camera2DataContainer.getInstance().getCapabilities(i).getSensorOrientation();
    }

    public static int getShootOrientation(Activity activity, int i) {
        return ((i - getDisplayRotation(activity)) + ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT;
    }

    public static float getShootRotation(Activity activity, float f2) {
        float displayRotation = f2 - ((float) getDisplayRotation(activity));
        while (displayRotation < 0.0f) {
            displayRotation += 360.0f;
        }
        while (displayRotation > 360.0f) {
            displayRotation -= 360.0f;
        }
        return displayRotation;
    }

    private static Object getStaticObjectField(Class<?> cls, String str) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        java.lang.reflect.Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(str);
    }

    public static int getStatusBarHeight(Context context) {
        int i;
        if (DataRepository.dataItemFeature().Sa()) {
            i = context.getResources().getDimensionPixelSize(R.dimen.camera_status_bar_height);
        } else {
            int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            i = identifier > 0 ? context.getResources().getDimensionPixelSize(identifier) : 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("StatusBarHeight=");
        sb.append(i);
        Log.v(TAG, sb.toString());
        return i;
    }

    public static File getStorageDirectory() {
        return isExternalStorageMounted() ? Environment.getExternalStorageDirectory() : INTERNAL_STORAGE_DIRECTORY;
    }

    public static MiYuvImage getSubYuvImage(byte[] bArr, int i, int i2, int i3, int i4, int[] iArr) {
        byte[] bArr2 = new byte[(((iArr[2] * iArr[3]) * 3) / 2)];
        int i5 = (iArr[1] * i3) + iArr[0];
        int i6 = 0;
        for (int i7 = 0; i7 < iArr[3]; i7++) {
            System.arraycopy(bArr, i5, bArr2, i6, iArr[2]);
            i5 += i3;
            i6 += iArr[2];
        }
        int i8 = (i3 * (i2 - 1)) + i + ((iArr[1] / 2) * i4) + iArr[0];
        for (int i9 = 0; i9 < iArr[3] / 2; i9++) {
            System.arraycopy(bArr, i8, bArr2, i6, iArr[2]);
            i8 += i4;
            i6 += iArr[2];
        }
        return new MiYuvImage(bArr2, iArr[2], iArr[3], 35);
    }

    public static byte[] getTimeWaterMarkData(int i, int i2, String str, int[] iArr) {
        NewStyleTextWaterMark newStyleTextWaterMark = new NewStyleTextWaterMark(str, i, i2, 0);
        if (iArr != null && iArr.length >= 4) {
            iArr[0] = newStyleTextWaterMark.getWidth();
            iArr[1] = newStyleTextWaterMark.getHeight();
            iArr[2] = newStyleTextWaterMark.getPaddingX();
            iArr[3] = newStyleTextWaterMark.getPaddingY();
        }
        return ((StringTexture) newStyleTextWaterMark.getTexture()).getBitmapData(CompressFormat.PNG);
    }

    public static String getTimeWatermark() {
        return getTimeWatermark(b.oj());
    }

    public static String getTimeWatermark(boolean z) {
        StringBuilder sb = new StringBuilder();
        if (z) {
            sb.append(new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date()).toCharArray());
        } else {
            sb.append(new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH).format(new Date()).toCharArray());
        }
        sb.append(" ");
        Time time = new Time();
        time.set(System.currentTimeMillis());
        String str = "%02d";
        sb.append(String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(time.hour)}));
        sb.append(":");
        sb.append(String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(time.minute)}));
        return sb.toString();
    }

    public static long getTotalMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    private static synchronized Typeface getTypefaceFromAssets(Context context, String str) {
        Typeface typeface;
        synchronized (Util.class) {
            if (!sTypefaces.containsKey(str)) {
                sTypefaces.put(str, Typeface.createFromAsset(context.getAssets(), str));
            }
            typeface = (Typeface) sTypefaces.get(str);
        }
        return typeface;
    }

    private static synchronized Typeface getTypefaceFromFile(Context context, String str) {
        Typeface typeface;
        synchronized (Util.class) {
            if (!sTypefaces.containsKey(str)) {
                sTypefaces.put(str, Typeface.createFromFile(new File(str)));
            }
            typeface = (Typeface) sTypefaces.get(str);
        }
        return typeface;
    }

    public static String getWatermarkFileName() {
        return (!CameraSettings.getCustomWatermark().equals(CameraSettings.getDefaultWatermarkStr()) || !CameraSettings.isUltraPixelRearOn() || DataRepository.dataItemFeature().Ra()) ? CameraSettings.isFrontCameraWaterMarkOpen() ? WATERMARK_FRONT_FILE_NAME : WATERMARK_FILE_NAME : WATERMARK_ULTRA_PIXEL_FILE_NAME;
    }

    public static int[] getWatermarkRange(int i, int i2, int i3, boolean z, boolean z2, float f2) {
        int[] iArr = new int[4];
        if (i3 != 0) {
            if (i3 != 90) {
                if (i3 != 180) {
                    if (i3 == 270) {
                        if (z && z2) {
                            iArr[0] = 0;
                            int i4 = (int) (((float) i2) * f2);
                            iArr[1] = i2 - i4;
                            iArr[2] = i;
                            iArr[3] = i4;
                        } else if (z) {
                            iArr[0] = 0;
                            int i5 = (int) (((float) i2) * f2);
                            iArr[1] = i2 - i5;
                            iArr[2] = i / 2;
                            iArr[3] = i5;
                        } else {
                            int i6 = i / 2;
                            iArr[0] = i6;
                            int i7 = (int) (((float) i2) * f2);
                            iArr[1] = i2 - i7;
                            iArr[2] = i6;
                            iArr[3] = i7;
                        }
                    }
                } else if (z && z2) {
                    iArr[0] = 0;
                    iArr[1] = 0;
                    iArr[2] = (int) (((float) i) * f2);
                    iArr[3] = i2;
                } else if (z) {
                    iArr[0] = 0;
                    iArr[1] = 0;
                    iArr[2] = (int) (((float) i) * f2);
                    iArr[3] = (int) (((float) i2) * 0.6f);
                } else {
                    iArr[0] = 0;
                    int i8 = i2 / 2;
                    iArr[1] = i8;
                    iArr[2] = (int) (((float) i) * f2);
                    iArr[3] = i8;
                }
            } else if (z && z2) {
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = i;
                iArr[3] = (int) (((float) i2) * f2);
            } else if (z) {
                int i9 = i / 2;
                iArr[0] = i9;
                iArr[1] = 0;
                iArr[2] = i9;
                iArr[3] = (int) (((float) i2) * f2);
            } else {
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = i / 2;
                iArr[3] = (int) (((float) i2) * f2);
            }
        } else if (z && z2) {
            int i10 = (int) (((float) i) * f2);
            iArr[0] = i - i10;
            iArr[1] = 0;
            iArr[2] = i10;
            iArr[3] = i2;
        } else if (z) {
            int i11 = (int) (((float) i) * f2);
            iArr[0] = i - i11;
            float f3 = (float) i2;
            iArr[1] = (int) (0.4f * f3);
            iArr[2] = i11;
            iArr[3] = (int) (f3 * 0.6f);
        } else {
            int i12 = (int) (((float) i) * f2);
            iArr[0] = i - i12;
            iArr[1] = 0;
            iArr[2] = i12;
            iArr[3] = i2 / 2;
        }
        iArr[0] = (iArr[0] / 2) * 2;
        iArr[1] = (iArr[1] / 2) * 2;
        iArr[2] = (iArr[2] / 4) * 4;
        iArr[3] = (iArr[3] / 4) * 4;
        return iArr;
    }

    public static String getZoomRatioText(float f2) {
        StringBuilder sb = new StringBuilder();
        float decimal = HybridZoomingSystem.toDecimal(f2);
        int i = (int) decimal;
        if (((int) ((10.0f * decimal) - ((float) (i * 10)))) == 0) {
            sb.append(String.valueOf(i));
        } else {
            sb.append(String.valueOf(decimal));
        }
        sb.append("X");
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0103, code lost:
        if ((r0 instanceof android.content.res.XmlResourceParser) == false) goto L_0x0128;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0119, code lost:
        if ((r0 instanceof android.content.res.XmlResourceParser) == false) goto L_0x0128;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0121, code lost:
        if ((r0 instanceof android.content.res.XmlResourceParser) == false) goto L_0x0128;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0123, code lost:
        ((android.content.res.XmlResourceParser) r0).close();
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0078 A[Catch:{ XmlPullParserException -> 0x011c, IOException -> 0x0114, all -> 0x0106 }] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00fe A[EDGE_INSN: B:63:0x00fe->B:47:0x00fe ?: BREAK  
EDGE_INSN: B:63:0x00fe->B:47:0x00fe ?: BREAK  
EDGE_INSN: B:63:0x00fe->B:47:0x00fe ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00fe A[EDGE_INSN: B:63:0x00fe->B:47:0x00fe ?: BREAK  
EDGE_INSN: B:63:0x00fe->B:47:0x00fe ?: BREAK  , SYNTHETIC] */
    private static void initScreenLightColorMap() {
        FileReader fileReader;
        XmlPullParser xmlPullParser;
        if (COLOR_TEMPERATURE_LIST.size() <= 0 && COLOR_TEMPERATURE_MAP.size() <= 0) {
            File colorMapXmlMapFile = getColorMapXmlMapFile();
            if (colorMapXmlMapFile != null) {
                try {
                    fileReader = new FileReader(colorMapXmlMapFile);
                    try {
                        XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
                        newInstance.setNamespaceAware(false);
                        xmlPullParser = newInstance.newPullParser();
                        try {
                            xmlPullParser.setInput(fileReader);
                        } catch (FileNotFoundException | XmlPullParserException e2) {
                            e = e2;
                        }
                    } catch (FileNotFoundException | XmlPullParserException e3) {
                        e = e3;
                        xmlPullParser = null;
                        e.printStackTrace();
                        String str = TAG;
                        if (xmlPullParser == null) {
                        }
                        while (true) {
                            try {
                                if (xmlPullParser.next() == 3) {
                                }
                            } catch (XmlPullParserException unused) {
                                closeSafely(fileReader);
                            } catch (IOException unused2) {
                                closeSafely(fileReader);
                            } catch (Throwable th) {
                                closeSafely(fileReader);
                                if (xmlPullParser instanceof XmlResourceParser) {
                                    ((XmlResourceParser) xmlPullParser).close();
                                }
                                throw th;
                            }
                        }
                        closeSafely(fileReader);
                    }
                } catch (FileNotFoundException | XmlPullParserException e4) {
                    e = e4;
                    xmlPullParser = null;
                    fileReader = null;
                    e.printStackTrace();
                    String str2 = TAG;
                    if (xmlPullParser == null) {
                    }
                    while (true) {
                        if (xmlPullParser.next() == 3) {
                        }
                    }
                    closeSafely(fileReader);
                }
            } else {
                xmlPullParser = null;
                fileReader = null;
            }
            String str22 = TAG;
            if (xmlPullParser == null) {
                Log.d(str22, "Cannot find screen color map in system, try local resource.");
                int identifier = CameraAppImpl.getAndroidContext().getResources().getIdentifier("screen_light", "xml", CameraAppImpl.getAndroidContext().getPackageName());
                if (identifier <= 0) {
                    Log.e(str22, "res/xml/screen_light.xml not found!");
                    return;
                }
                xmlPullParser = CameraAppImpl.getAndroidContext().getResources().getXml(identifier);
            }
            while (true) {
                if (xmlPullParser.next() == 3) {
                    break;
                } else if (xmlPullParser.getEventType() == 2) {
                    if (!"screen".equals(xmlPullParser.getName())) {
                        continue;
                    } else if (!SCREEN_VENDOR.equals(xmlPullParser.getAttributeValue(null, d.VENDOR))) {
                        skip(xmlPullParser);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("load screen light parameters for ");
                        sb.append(SCREEN_VENDOR);
                        Log.d(str22, sb.toString());
                        while (true) {
                            if (xmlPullParser.next() == 1) {
                                break;
                            } else if (xmlPullParser.getEventType() == 2) {
                                if (!"light".equals(xmlPullParser.getName())) {
                                    break;
                                }
                                int attributeIntValue = getAttributeIntValue(xmlPullParser, "CCT", 0);
                                int attributeIntValue2 = getAttributeIntValue(xmlPullParser, "R", 0);
                                int attributeIntValue3 = getAttributeIntValue(xmlPullParser, "G", 0);
                                int attributeIntValue4 = getAttributeIntValue(xmlPullParser, Field.BYTE_SIGNATURE_PRIMITIVE, 0);
                                COLOR_TEMPERATURE_LIST.add(Integer.valueOf(attributeIntValue));
                                COLOR_TEMPERATURE_MAP.add(Integer.valueOf(Color.rgb(attributeIntValue2, attributeIntValue3, attributeIntValue4)));
                            }
                        }
                    }
                }
            }
            closeSafely(fileReader);
        }
    }

    public static void initialize(Context context) {
        updateDeviceConfig(context);
        sIsnotchScreenHidden = isNotchScreenHidden(context);
        isNotchDevice = SystemProperties.getInt("ro.miui.notch", 0) == 1 && !sIsnotchScreenHidden;
        if (android.os.Build.DEVICE.equalsIgnoreCase("laurel_sprout")) {
            isNotchDevice = !sIsnotchScreenHidden;
        }
        sIsFullScreenNavBarHidden = isFullScreenNavBarHidden(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        sPixelDensity = displayMetrics.noncompatDensity;
        sImageFileNamer = new ImageFileNamer(context.getString(R.string.image_file_name_format));
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        int i = point.x;
        int i2 = point.y;
        if (i < i2) {
            sWindowWidth = i;
            sWindowHeight = i2;
        } else {
            sWindowWidth = i2;
            sWindowHeight = i;
        }
        if ("hercules".equals(android.os.Build.DEVICE)) {
            sWindowHeight = 2244;
        }
        isLongRatioScreen = isLongRatioScreen(sWindowWidth, sWindowHeight);
        sFullScreenExtraMargin = context.getResources().getDimensionPixelSize(R.dimen.fullscreen_extra_margin);
        sNavigationBarHeight = getNavigationBarHeight(context);
        if (isNotchDevice) {
            if (isLongRatioScreen) {
                sStatusBarHeight = getStatusBarHeight(context);
            } else {
                sStatusBarHeight = sWindowHeight - (sWindowWidth * 2);
            }
            if (sIsFullScreenNavBarHidden && !isLongRatioScreen) {
                int i3 = sNavigationBarHeight;
                int i4 = sFullScreenExtraMargin;
                sNavigationBarHeight = i3 - i4;
                sStatusBarHeight += i4 / 2;
            }
        } else {
            sStatusBarHeight = 0;
        }
        CameraSettings.BOTTOM_CONTROL_HEIGHT = getBottomHeight(context.getResources());
        sAAID = Global.getString(context.getContentResolver(), "ad_aaid");
        Log.i(TAG, String.format(Locale.ENGLISH, "windowSize=%dx%d density=%.4f", new Object[]{Integer.valueOf(sWindowWidth), Integer.valueOf(sWindowHeight), Float.valueOf(sPixelDensity)}));
    }

    public static void installPackage(Context context, String str, PackageInstallerListener packageInstallerListener, boolean z, boolean z2) {
        String str2 = TAG;
        if (context == null || TextUtils.isEmpty(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid params. pkgName=");
            sb.append(str);
            Log.w(str2, sb.toString());
            return;
        }
        try {
            Object packageInstallObserver = CompatibilityUtils.getPackageInstallObserver(packageInstallerListener);
            Class cls = Class.forName("miui.content.pm.PreloadedAppPolicy");
            Method of = Method.of(cls, "installPreloadedDataApp", CompatibilityUtils.getInstallMethodDescription());
            int i = z ? 1 : z2 ? 2 : 0;
            boolean invokeBoolean = of.invokeBoolean(cls, null, context, str, packageInstallObserver, Integer.valueOf(i));
            StringBuilder sb2 = new StringBuilder();
            sb2.append("installPackage: result=");
            sb2.append(invokeBoolean);
            Log.d(str2, sb2.toString());
        } catch (Exception e2) {
            Log.e(str2, e2.getMessage(), e2);
            if (packageInstallerListener != null) {
                packageInstallerListener.onPackageInstalled(str, false);
            }
        }
    }

    public static boolean isAEStable(int i) {
        return i == 2 || i == 3 || i == 4;
    }

    public static boolean isAWBStable(int i) {
        return i == 2 || i == 3;
    }

    private static boolean isAccessibilityEnable() {
        return sIsAccessibilityEnable;
    }

    public static boolean isAccessible() {
        return VERSION.SDK_INT >= 14 && isAccessibilityEnable();
    }

    public static boolean isActivityInvert(Activity activity) {
        return getDisplayRotation(activity) == 180;
    }

    public static boolean isAntibanding60() {
        return ANTIBANDING_60_COUNTRY.contains(mCountryIso);
    }

    public static final boolean isAppLocked(Context context, String str) {
        return GeneralUtils.isAppLocked(context, str);
    }

    public static boolean isContains(RectF rectF, RectF rectF2) {
        if (rectF == null || rectF2 == null) {
            return false;
        }
        float f2 = rectF.left;
        float f3 = rectF.right;
        if (f2 >= f3) {
            return false;
        }
        float f4 = rectF.top;
        float f5 = rectF.bottom;
        return f4 < f5 && f2 <= rectF2.left && f4 <= rectF2.top && f3 >= rectF2.right && f5 >= rectF2.bottom;
    }

    public static boolean isDebugOsBuild() {
        if (!"userdebug".equals(android.os.Build.TYPE)) {
            if (!"eng".equals(android.os.Build.TYPE) && !sIsDumpLog) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDevices(String str) {
        try {
            Class cls = Class.forName("miui.os.Build");
            if (cls != null) {
                Object staticObjectField = getStaticObjectField(cls, str);
                if (staticObjectField == null) {
                    return false;
                }
                return Boolean.parseBoolean(staticObjectField.toString());
            }
        } catch (Exception e2) {
            Log.e(TAG, "getClass error", e2);
        }
        return false;
    }

    public static boolean isDumpImageEnabled() {
        if (sIsDumpImageEnabled == null) {
            sIsDumpImageEnabled = Boolean.valueOf(new File(Storage.generatePrimaryFilepath("algoup_dump_images")).exists());
        }
        return sIsDumpImageEnabled.booleanValue();
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0064 A[SYNTHETIC, Splitter:B:29:0x0064] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0073 A[SYNTHETIC, Splitter:B:35:0x0073] */
    private static boolean isEqual(byte[] bArr, File file) {
        String str = TAG;
        if (bArr == null || bArr.length == 0 || !file.exists()) {
            return false;
        }
        FileInputStream fileInputStream = null;
        byte[] bArr2 = new byte[512];
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream2 = new FileInputStream(file);
            while (true) {
                try {
                    int read = fileInputStream2.read(bArr2, 0, 512);
                    if (read == -1) {
                        break;
                    }
                    instance.update(bArr2, 0, read);
                } catch (IOException | NoSuchAlgorithmException e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    try {
                        Log.e(str, e.getMessage(), e);
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e3) {
                                Log.e(str, e3.getMessage(), e3);
                            }
                        }
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream2 = fileInputStream;
                        if (fileInputStream2 != null) {
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e4) {
                            Log.e(str, e4.getMessage(), e4);
                        }
                    }
                    throw th;
                }
            }
            String str2 = new String(instance.digest());
            instance.reset();
            boolean equals = str2.equals(new String(instance.digest(bArr)));
            try {
                fileInputStream2.close();
            } catch (IOException e5) {
                Log.e(str, e5.getMessage(), e5);
            }
            return equals;
        } catch (IOException | NoSuchAlgorithmException e6) {
            e = e6;
            Log.e(str, e.getMessage(), e);
            if (fileInputStream != null) {
            }
            return false;
        }
    }

    public static boolean isEqualsZero(double d2) {
        return Math.abs(d2) < 1.0E-8d;
    }

    public static boolean isExternalStorageMounted() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isFingerPrintKeyEvent(KeyEvent keyEvent) {
        return keyEvent != null && 27 == keyEvent.getKeyCode() && keyEvent.getDevice() != null && b.Rh().contains(keyEvent.getDevice().getName());
    }

    public static boolean isForceNameSuffix() {
        if (sIsForceNameSuffix == null) {
            sIsForceNameSuffix = Boolean.valueOf(new File(Storage.generatePrimaryFilepath(FORCE_NAME_SUFFIX_FILE)).exists());
        }
        return sIsForceNameSuffix.booleanValue();
    }

    public static boolean isFullScreenNavBarHidden(Context context) {
        return true;
    }

    public static boolean isGlobalVersion() {
        return SystemProperties.get("ro.product.mod_device", "").contains("_global") || DataRepository.dataItemFeature().Sa();
    }

    private static boolean isGyroscopeStable(float[] fArr) {
        return fArr != null && fArr.length == 3 && Math.abs(fArr[0]) < 0.7f && Math.abs(fArr[1]) < 5.0f && Math.abs(fArr[2]) < 0.7f;
    }

    public static boolean isGyroscopeStable(float[] fArr, float[] fArr2) {
        if (fArr == null) {
            return true;
        }
        boolean isGyroscopeStable = isGyroscopeStable(fArr);
        if (isGyroscopeStable) {
            if (fArr2 == null) {
                return true;
            }
            if (isGyroscopeStable(fArr2)) {
                return isGyroscopeStable;
            }
        }
        return false;
    }

    public static boolean isInVideoCall(Activity activity) {
        if (!b.isMTKPlatform() || !PermissionManager.checkPhoneStatePermission(activity)) {
            return false;
        }
        return CompatibilityUtils.isInVideoCall(activity);
    }

    public static boolean isInternationalBuild() {
        return SystemProperties.get("ro.product.mod_device", "").endsWith("_global");
    }

    public static boolean isLabOptionsVisible() {
        if (sIsLabOptionsVisible == null) {
            sIsLabOptionsVisible = Boolean.valueOf(new File(Storage.generatePrimaryFilepath(LAB_OPTIONS_VISIBLE_FILE)).exists());
        }
        return sIsLabOptionsVisible.booleanValue();
    }

    public static boolean isLayoutRTL(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (context.getResources().getConfiguration().getLayoutDirection() == 1) {
            z = true;
        }
        return z;
    }

    public static boolean isLivePhotoStable(LivePhotoResult livePhotoResult, int i) {
        return livePhotoResult != null && isAEStable(livePhotoResult.getAEState()) && isAWBStable(livePhotoResult.getAWBState()) && livePhotoResult.isGyroScopeStable() && livePhotoResult.getFilterId() == i;
    }

    private static boolean isLongRatioScreen(int i, int i2) {
        float f2 = ((float) i2) / ((float) i);
        if (((double) Math.abs(f2 - 2.1666667f)) >= 0.02d && ((double) Math.abs(f2 - 2.1111112f)) >= 0.02d) {
            if (!"hercules".equals(android.os.Build.DEVICE)) {
                if (!"draco".equals(android.os.Build.DEVICE)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isMemoryRich(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem > 419430400;
    }

    public static boolean isNonUI() {
        return SystemProperties.getBoolean(NONUI_MODE_PROPERTY, false);
    }

    public static boolean isNonUIEnabled() {
        return !SystemProperties.get(NONUI_MODE_PROPERTY).equals("");
    }

    public static boolean isNotchScreenHidden(Context context) {
        if (VERSION.SDK_INT < 28) {
            return false;
        }
        boolean z = true;
        if (Global.getInt(context.getContentResolver(), "force_black_v2", 0) != 1) {
            z = false;
        }
        return z;
    }

    public static boolean isPackageAvailable(Context context, String str) {
        String str2 = TAG;
        if (context == null || str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid params. packageName=");
            sb.append(str);
            Log.w(str2, sb.toString());
            return false;
        }
        try {
            int applicationEnabledSetting = context.getPackageManager().getApplicationEnabledSetting(str);
            boolean z = true;
            if (!(applicationEnabledSetting == 0 || applicationEnabledSetting == 1)) {
                z = false;
            }
            return z;
        } catch (IllegalArgumentException e2) {
            Log.e(str2, e2.getMessage());
            return false;
        } catch (NullPointerException e3) {
            Log.e(str2, e3.getMessage());
            return false;
        }
    }

    public static boolean isPathExist(String str) {
        return !TextUtils.isEmpty(str) && new File(str).exists();
    }

    public static boolean isProduceFocusInfoSuccess(byte[] bArr) {
        return bArr != null && 25 < bArr.length && bArr[bArr.length - 25] == 0;
    }

    public static boolean isQuotaExceeded(Exception exc) {
        if (exc != null && (exc instanceof FileNotFoundException)) {
            String message = exc.getMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("isQuotaExceeded: msg=");
            sb.append(message);
            Log.e(TAG, sb.toString());
            if (message != null) {
                return message.toLowerCase().contains("quota exceeded");
            }
        }
        return false;
    }

    public static boolean isScreenSlideOff(Context context) {
        return System.getInt(context.getContentResolver(), MiuiSettings.System.MIUI_SLIDER_COVER_STATUS, -1) == 1;
    }

    public static boolean isSetContentDesc() {
        return "1".equals(SystemProperties.get("camera.content.description.debug"));
    }

    public static boolean isShowAfRegionView() {
        return "1".equals(SystemProperties.get("camera.preview.debug.afRegion_view"));
    }

    public static boolean isShowDebugInfo() {
        String str = "1";
        return str.equals(SystemProperties.get("persist.camera.enable.log")) || str.equals(SystemProperties.get("persist.camera.debug.show_af")) || str.equals(SystemProperties.get("persist.camera.debug.show_awb")) || str.equals(SystemProperties.get("persist.camera.debug.show_aec")) || str.equals(SystemProperties.get("persist.camera.debug.autoscene")) || str.equals(SystemProperties.get("persist.camera.debug.hht"));
    }

    public static boolean isShowDebugInfoView() {
        return "1".equals(SystemProperties.get("camera.preview.debug.debugInfo_view"));
    }

    public static boolean isShowPreviewDebugInfo() {
        return "1".equals(SystemProperties.get("camera.preview.enable.log"));
    }

    public static boolean isStringValueContained(Object obj, int i) {
        return isStringValueContained(obj, (CharSequence[]) CameraAppImpl.getAndroidContext().getResources().getStringArray(i));
    }

    public static boolean isStringValueContained(Object obj, List<? extends CharSequence> list) {
        if (!(list == null || obj == null)) {
            for (CharSequence equals : list) {
                if (equals.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isStringValueContained(Object obj, CharSequence[] charSequenceArr) {
        if (!(charSequenceArr == null || obj == null)) {
            for (CharSequence equals : charSequenceArr) {
                if (equals.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSupported(int i, int[] iArr) {
        return getArrayIndex(iArr, i) != -1;
    }

    public static <T> boolean isSupported(T t, T[] tArr) {
        return getArrayIndex(tArr, t) != -1;
    }

    public static boolean isSupported(String str, List<String> list) {
        return list != null && list.indexOf(str) >= 0;
    }

    public static boolean isTimeout(long j, long j2, long j3) {
        return j < j2 || j - j2 > j3;
    }

    public static boolean isUriValid(Uri uri, ContentResolver contentResolver) {
        String str = TAG;
        if (uri == null) {
            return false;
        }
        try {
            ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
            if (openFileDescriptor == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Fail to open URI. URI=");
                sb.append(uri);
                Log.e(str, sb.toString());
                return false;
            }
            openFileDescriptor.close();
            return true;
        } catch (IOException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("IOException occurs when opening URI: ");
            sb2.append(e2.getMessage());
            Log.e(str, sb2.toString(), e2);
            return false;
        }
    }

    public static boolean isValidValue(String str) {
        return !TextUtils.isEmpty(str) && str.matches("^[0-9]+$");
    }

    public static boolean isViewIntersectWindow(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return iArr[0] < sWindowWidth && iArr[0] + view.getWidth() >= 0 && iArr[1] < sWindowHeight && iArr[1] + view.getHeight() >= 0;
    }

    public static boolean isZoomAnimationEnabled() {
        return SystemProperties.getBoolean(ZOOM_ANIMATION_PROPERTY, !DataRepository.dataItemFeature().qc());
    }

    public static String join(String str, List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                stringBuffer.append((String) list.get(i));
            } else {
                stringBuffer.append((String) list.get(i));
                stringBuffer.append(str);
            }
        }
        return stringBuffer.toString();
    }

    public static Bitmap load960fpsCameraWatermark(Context context) {
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = true;
        StringBuilder sb = new StringBuilder();
        sb.append(android.os.Build.DEVICE);
        sb.append("_960fps");
        return loadAppCameraWatermark(context, options, sb.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004d, code lost:
        if (r3 != null) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        $closeResource(r4, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0052, code lost:
        throw r5;
     */
    protected static Bitmap loadAppCameraWatermark(Context context, Options options, String str) {
        String str2;
        if (str == null) {
            return null;
        }
        if (str.equalsIgnoreCase("common")) {
            str2 = "common.webp";
        } else {
            String Sh = b.Sh();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(Sh);
            sb.append(".webp");
            str2 = sb.toString();
        }
        AssetManager assets = context.getAssets();
        try {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("watermarks/");
            sb2.append(str2);
            InputStream open = assets.open(sb2.toString());
            Bitmap decodeStream = BitmapFactory.decodeStream(open, null, options);
            if (open != null) {
                $closeResource(null, open);
            }
            return decodeStream;
        } catch (Exception e2) {
            Log.d(TAG, "Failed to load app camera watermark ", e2);
            return null;
        }
    }

    public static Bitmap loadFrontCameraWatermark(Context context) {
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = true;
        StringBuilder sb = new StringBuilder();
        sb.append(android.os.Build.DEVICE);
        sb.append("_front");
        return loadAppCameraWatermark(context, options, sb.toString());
    }

    private static double log2(double d2) {
        return Math.log(d2) / LOG_2;
    }

    public static Bitmap makeBitmap(byte[] bArr, int i) {
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (!options.mCancel && options.outWidth != -1) {
                if (options.outHeight != -1) {
                    options.inSampleSize = computeSampleSize(options, -1, i);
                    options.inJustDecodeBounds = false;
                    options.inDither = false;
                    options.inPreferredConfig = Config.ARGB_8888;
                    return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                }
            }
            return null;
        } catch (OutOfMemoryError e2) {
            Log.e(TAG, "Got oom exception ", e2);
            return null;
        }
    }

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes("UTF8"));
            byte[] digest = instance.digest();
            String str2 = "";
            for (byte b2 : digest) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(Integer.toHexString((b2 & 255) | -256).substring(6));
                str2 = sb.toString();
            }
            return str2;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static final String millisecondToTimeString(long j, boolean z) {
        long j2 = j / 1000;
        long j3 = j2 / 60;
        long j4 = j3 / 60;
        long j5 = j3 - (j4 * 60);
        long j6 = j2 - (j3 * 60);
        StringBuilder sb = new StringBuilder();
        if (j4 > 0) {
            if (j4 < 10) {
                sb.append('0');
            }
            sb.append(j4);
            sb.append(':');
        }
        if (j5 < 10) {
            sb.append('0');
        }
        sb.append(j5);
        sb.append(':');
        if (j6 < 10) {
            sb.append('0');
        }
        sb.append(j6);
        if (z) {
            sb.append('.');
            long j7 = (j - (j2 * 1000)) / 10;
            if (j7 < 10) {
                sb.append('0');
            }
            sb.append(j7);
        }
        return sb.toString();
    }

    public static boolean mkdirs(File file, int i, int i2, int i3) {
        if (file.exists()) {
            return false;
        }
        String parent = file.getParent();
        if (parent != null) {
            mkdirs(new File(parent), i, i2, i3);
        }
        return file.mkdir();
    }

    private static void modify(Object obj, String str, int i) throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field declaredField = obj.getClass().getDeclaredField(str);
        declaredField.setAccessible(true);
        declaredField.setInt(obj, i);
    }

    public static int nextPowerOf2(int i) {
        int i2 = i - 1;
        int i3 = i2 | (i2 >>> 16);
        int i4 = i3 | (i3 >>> 8);
        int i5 = i4 | (i4 >>> 4);
        int i6 = i5 | (i5 >>> 2);
        return (i6 | (i6 >>> 1)) + 1;
    }

    private static float normalizeDegree(float f2) {
        if (f2 < 0.0f) {
            f2 += 360.0f;
        } else if (f2 > 360.0f) {
            f2 %= 360.0f;
        }
        return f2 <= 45.0f ? f2 : f2 <= 90.0f ? 90.0f - f2 : f2 <= 135.0f ? f2 - 90.0f : f2 <= 180.0f ? 180.0f - f2 : f2 <= 225.0f ? f2 - 180.0f : f2 <= 270.0f ? 270.0f - f2 : f2 <= 315.0f ? f2 - 270.0f : 360.0f - f2;
    }

    public static int parseInt(String str, int i) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e2) {
            Log.e(TAG, e2.getMessage(), e2);
            if (!isDebugOsBuild()) {
                return i;
            }
            throw e2;
        }
    }

    public static boolean pointInView(float f2, float f3, View view) {
        boolean z = false;
        if (view == null) {
            return false;
        }
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        if (f2 >= ((float) iArr[0]) && f2 < ((float) (iArr[0] + view.getWidth())) && f3 >= ((float) iArr[1]) && f3 < ((float) (iArr[1] + view.getHeight()))) {
            z = true;
        }
        return z;
    }

    public static void prepareMatrix(Matrix matrix, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        matrix.setScale(z ? -1.0f : 1.0f, 1.0f);
        matrix.postRotate((float) i);
        if (i == 90 || i == 270) {
            matrix.postScale(((float) i2) / ((float) i7), ((float) i3) / ((float) i6));
        } else {
            matrix.postScale(((float) i2) / ((float) i6), ((float) i3) / ((float) i7));
        }
        matrix.postTranslate((float) i4, (float) i5);
    }

    public static void printLog(String str, Object... objArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objArr.length; i += 2) {
            sb.append(objArr[i].toString());
            sb.append(" = ");
            sb.append(objArr[i + 1].toString());
            sb.append(" ");
        }
        Log.d(str, sb.toString());
    }

    public static void rectFToRect(RectF rectF, Rect rect) {
        rect.left = Math.round(rectF.left);
        rect.top = Math.round(rectF.top);
        rect.right = Math.round(rectF.right);
        rect.bottom = Math.round(rectF.bottom);
    }

    public static void removeCustomWatermark() {
        if (DataRepository.dataItemFeature().hd()) {
            WatermarkMiSysUtils.eraseFile(WATERMARK_FILE_NAME);
            WatermarkMiSysUtils.eraseFile(WATERMARK_FRONT_FILE_NAME);
            WatermarkMiSysUtils.eraseFile(WATERMARK_ULTRA_PIXEL_FILE_NAME);
        }
        File filesDir = CameraAppImpl.getAndroidContext().getFilesDir();
        File file = new File(filesDir, WATERMARK_FILE_NAME);
        File file2 = new File(filesDir, WATERMARK_FRONT_FILE_NAME);
        File file3 = new File(filesDir, WATERMARK_ULTRA_PIXEL_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        if (file3.exists()) {
            file3.delete();
        }
    }

    public static int replaceStartEffectRender(Activity activity) {
        if (b.tj()) {
            String stringExtra = activity.getIntent().getStringExtra(EXTRAS_START_WITH_EFFECT_RENDER);
            if (stringExtra != null) {
                int identifier = activity.getResources().getIdentifier(stringExtra, "integer", activity.getPackageName());
                if (identifier != 0) {
                    int integer = activity.getResources().getInteger(identifier);
                    CameraSettings.setShaderEffect(integer);
                    return integer;
                }
            }
        }
        return FilterInfo.FILTER_ID_NONE;
    }

    public static void reverseAnimatorSet(AnimatorSet animatorSet) {
        Iterator it = animatorSet.getChildAnimations().iterator();
        while (it.hasNext()) {
            Animator animator = (Animator) it.next();
            if (animator instanceof ValueAnimator) {
                ((ValueAnimator) animator).reverse();
            } else if (animator instanceof AnimatorSet) {
                reverseAnimatorSet((AnimatorSet) animator);
            }
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        return rotateAndMirror(bitmap, i, false);
    }

    public static Bitmap rotateAndMirror(Bitmap bitmap, int i, boolean z) {
        if ((i == 0 && !z) || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        if (z) {
            matrix.postScale(-1.0f, 1.0f);
            i = (i + ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT;
            if (i == 0 || i == 180) {
                matrix.postTranslate((float) bitmap.getWidth(), 0.0f);
            } else if (i == 90 || i == 270) {
                matrix.postTranslate((float) bitmap.getHeight(), 0.0f);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid degrees=");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if (i != 0) {
            matrix.postRotate((float) i, ((float) bitmap.getWidth()) / 2.0f, ((float) bitmap.getHeight()) / 2.0f);
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap == createBitmap) {
                return bitmap;
            }
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError unused) {
            return bitmap;
        }
    }

    public static int roundOrientation(int i, int i2) {
        boolean z = true;
        if (i2 != -1) {
            int abs = Math.abs(i - i2);
            if (Math.min(abs, 360 - abs) < 50) {
                z = false;
            }
        }
        if (!z) {
            return i2;
        }
        int i3 = (((i + 45) / 90) * 90) % ScreenEffect.SCREEN_PAPER_MODE_TWILIGHT_START_DEAULT;
        StringBuilder sb = new StringBuilder();
        sb.append("onOrientationChanged: orientation = ");
        sb.append(i3);
        Log.d(TAG, sb.toString());
        return i3;
    }

    public static int safeDelete(Uri uri, String str, String[] strArr) {
        int i = -1;
        try {
            i = CameraAppImpl.getAndroidContext().getContentResolver().delete(uri, str, strArr);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("safeDelete url=");
            sb.append(uri);
            sb.append(" where=");
            sb.append(str);
            sb.append(" selectionArgs=");
            sb.append(strArr);
            sb.append(" result=");
            sb.append(i);
            Log.v(str2, sb.toString());
            return i;
        } catch (Exception e2) {
            e2.printStackTrace();
            return i;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0033 A[SYNTHETIC, Splitter:B:21:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0041 A[SYNTHETIC, Splitter:B:26:0x0041] */
    public static boolean saveBitmap(Bitmap bitmap, String str) {
        if (bitmap != null) {
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(new File(str));
                try {
                    bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream2);
                    try {
                        fileOutputStream2.flush();
                        fileOutputStream2.close();
                        return true;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return true;
                    }
                } catch (FileNotFoundException e3) {
                    e = e3;
                    fileOutputStream = fileOutputStream2;
                    try {
                        Log.e(TAG, "saveBitmap failed!", e);
                        if (fileOutputStream != null) {
                        }
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (Exception e4) {
                                e4.printStackTrace();
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
            } catch (FileNotFoundException e5) {
                e = e5;
                Log.e(TAG, "saveBitmap failed!", e);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
                return false;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0041 A[SYNTHETIC, Splitter:B:22:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0052 A[SYNTHETIC, Splitter:B:28:0x0052] */
    public static boolean saveBitmap(Buffer buffer, int i, int i2, Config config, String str) {
        if (buffer != null) {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, config);
            createBitmap.copyPixelsFromBuffer(buffer);
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(new File(str));
                try {
                    createBitmap.compress(CompressFormat.JPEG, 100, fileOutputStream2);
                    try {
                        fileOutputStream2.flush();
                        fileOutputStream2.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    createBitmap.recycle();
                    return true;
                } catch (FileNotFoundException e3) {
                    FileOutputStream fileOutputStream3 = fileOutputStream2;
                    e = e3;
                    fileOutputStream = fileOutputStream3;
                    try {
                        Log.e(TAG, "saveBitmap failed!", e);
                        if (fileOutputStream != null) {
                        }
                        createBitmap.recycle();
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }
                        createBitmap.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    FileOutputStream fileOutputStream4 = fileOutputStream2;
                    th = th2;
                    fileOutputStream = fileOutputStream4;
                    if (fileOutputStream != null) {
                    }
                    createBitmap.recycle();
                    throw th;
                }
            } catch (FileNotFoundException e5) {
                e = e5;
                Log.e(TAG, "saveBitmap failed!", e);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
                createBitmap.recycle();
                return false;
            }
        }
        return false;
    }

    public static boolean saveCameraCalibrationToFile(byte[] bArr, String str) {
        String str2 = TAG;
        Context androidContext = CameraAppImpl.getAndroidContext();
        boolean z = true;
        if (!(bArr == null || androidContext == null)) {
            if (isEqual(bArr, androidContext.getFileStreamPath(str))) {
                return true;
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = androidContext.openFileOutput(str, 0);
                fileOutputStream.write(bArr);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e3) {
                Log.e(str2, "saveCameraCalibrationToFile: FileNotFoundException", e3);
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e4) {
                Log.e(str2, "saveCameraCalibrationToFile: IOException", e4);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
            }
            return z;
        }
        z = false;
        return z;
    }

    protected static void saveCustomWatermark2File(Bitmap bitmap, boolean z, boolean z2) {
        boolean z3;
        StringBuilder sb = new StringBuilder();
        sb.append("saveCustomWatermark2File: start... watermarkBitmap = ");
        sb.append(bitmap);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        long currentTimeMillis = System.currentTimeMillis();
        String str2 = z2 ? WATERMARK_FRONT_FILE_NAME : z ? WATERMARK_ULTRA_PIXEL_FILE_NAME : WATERMARK_FILE_NAME;
        if (bitmap != null && !bitmap.isRecycled()) {
            boolean z4 = true;
            if (DataRepository.dataItemFeature().hd()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.PNG, 90, byteArrayOutputStream);
                z4 = WatermarkMiSysUtils.writeFileToPersist(byteArrayOutputStream.toByteArray(), str2);
            }
            if (z4) {
                FileOutputStream fileOutputStream = null;
                try {
                    File filesDir = CameraAppImpl.getAndroidContext().getFilesDir();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str2);
                    sb3.append(TEMP_SUFFIX);
                    File file = new File(filesDir, sb3.toString());
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                    try {
                        bitmap.compress(CompressFormat.PNG, 90, fileOutputStream2);
                        fileOutputStream2.flush();
                        z3 = file.renameTo(new File(filesDir, str2));
                        closeSilently(fileOutputStream2);
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        try {
                            Log.e(str, "saveCustomWatermark2File Failed to write image", e);
                            closeSilently(fileOutputStream);
                            z3 = false;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("saveCustomWatermark2File: watermarkBitmap = ");
                            sb4.append(bitmap);
                            sb4.append(", save result = ");
                            sb4.append(z3);
                            sb4.append(", cost time = ");
                            sb4.append(System.currentTimeMillis() - currentTimeMillis);
                            sb4.append("ms");
                            Log.d(str, sb4.toString());
                        } catch (Throwable th) {
                            th = th;
                            closeSilently(fileOutputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = fileOutputStream2;
                        closeSilently(fileOutputStream);
                        throw th;
                    }
                } catch (IOException e3) {
                    e = e3;
                    Log.e(str, "saveCustomWatermark2File Failed to write image", e);
                    closeSilently(fileOutputStream);
                    z3 = false;
                    StringBuilder sb42 = new StringBuilder();
                    sb42.append("saveCustomWatermark2File: watermarkBitmap = ");
                    sb42.append(bitmap);
                    sb42.append(", save result = ");
                    sb42.append(z3);
                    sb42.append(", cost time = ");
                    sb42.append(System.currentTimeMillis() - currentTimeMillis);
                    sb42.append("ms");
                    Log.d(str, sb42.toString());
                }
                StringBuilder sb422 = new StringBuilder();
                sb422.append("saveCustomWatermark2File: watermarkBitmap = ");
                sb422.append(bitmap);
                sb422.append(", save result = ");
                sb422.append(z3);
                sb422.append(", cost time = ");
                sb422.append(System.currentTimeMillis() - currentTimeMillis);
                sb422.append("ms");
                Log.d(str, sb422.toString());
            }
        }
        z3 = false;
        StringBuilder sb4222 = new StringBuilder();
        sb4222.append("saveCustomWatermark2File: watermarkBitmap = ");
        sb4222.append(bitmap);
        sb4222.append(", save result = ");
        sb4222.append(z3);
        sb4222.append(", cost time = ");
        sb4222.append(System.currentTimeMillis() - currentTimeMillis);
        sb4222.append("ms");
        Log.d(str, sb4222.toString());
    }

    public static void saveImageToJpeg(Image image) {
        Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        ByteBuffer buffer2 = planes[2].getBuffer();
        int[] iArr = {planes[0].getRowStride(), planes[2].getRowStride()};
        int limit = buffer.limit();
        int limit2 = buffer2.limit();
        byte[] bArr = new byte[(limit + limit2)];
        buffer.rewind();
        buffer2.rewind();
        buffer.get(bArr, 0, limit);
        buffer2.get(bArr, limit, limit2);
        ImageHelper.saveYuvToJpg(bArr, image.getWidth(), image.getHeight(), iArr, System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        sb.append("saveImageToJpeg: ");
        sb.append(buffer.remaining());
        sb.append("|");
        sb.append(buffer2.remaining());
        Log.d(TAG, sb.toString());
    }

    public static void saveLastFrameGaussian2File(Bitmap bitmap) {
        boolean z;
        FileOutputStream fileOutputStream;
        IOException e2;
        File filesDir;
        File file;
        StringBuilder sb = new StringBuilder();
        sb.append("saveLastFrameGaussian2File: start... blurBitmap = ");
        sb.append(bitmap);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        long currentTimeMillis = System.currentTimeMillis();
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                filesDir = CameraAppImpl.getAndroidContext().getFilesDir();
                file = new File(filesDir, "blur.jpg.tmp");
                fileOutputStream = new FileOutputStream(file);
            } catch (IOException e3) {
                fileOutputStream = null;
                e2 = e3;
                try {
                    Log.e(str, "saveLastFrameGaussian2File Failed to write image", e2);
                    closeSilently(fileOutputStream);
                    z = false;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("saveLastFrameGaussian2File: blurBitmap = ");
                    sb3.append(bitmap);
                    sb3.append(", save result = ");
                    sb3.append(z);
                    sb3.append(", cost time = ");
                    sb3.append(System.currentTimeMillis() - currentTimeMillis);
                    sb3.append("ms");
                    Log.d(str, sb3.toString());
                } catch (Throwable th) {
                    th = th;
                    closeSilently(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
                closeSilently(fileOutputStream);
                throw th;
            }
            try {
                bitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                z = file.renameTo(new File(filesDir, LAST_FRAME_GAUSSIAN_FILE_NAME));
                closeSilently(fileOutputStream);
            } catch (IOException e4) {
                e2 = e4;
                Log.e(str, "saveLastFrameGaussian2File Failed to write image", e2);
                closeSilently(fileOutputStream);
                z = false;
                StringBuilder sb32 = new StringBuilder();
                sb32.append("saveLastFrameGaussian2File: blurBitmap = ");
                sb32.append(bitmap);
                sb32.append(", save result = ");
                sb32.append(z);
                sb32.append(", cost time = ");
                sb32.append(System.currentTimeMillis() - currentTimeMillis);
                sb32.append("ms");
                Log.d(str, sb32.toString());
            }
            StringBuilder sb322 = new StringBuilder();
            sb322.append("saveLastFrameGaussian2File: blurBitmap = ");
            sb322.append(bitmap);
            sb322.append(", save result = ");
            sb322.append(z);
            sb322.append(", cost time = ");
            sb322.append(System.currentTimeMillis() - currentTimeMillis);
            sb322.append("ms");
            Log.d(str, sb322.toString());
        }
        z = false;
        StringBuilder sb3222 = new StringBuilder();
        sb3222.append("saveLastFrameGaussian2File: blurBitmap = ");
        sb3222.append(bitmap);
        sb3222.append(", save result = ");
        sb3222.append(z);
        sb3222.append(", cost time = ");
        sb3222.append(System.currentTimeMillis() - currentTimeMillis);
        sb3222.append("ms");
        Log.d(str, sb3222.toString());
    }

    public static boolean saveLiveShotMicroVideoInSdcard() {
        return android.util.Log.isLoggable("liveshotsmv", 3);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004e A[SYNTHETIC, Splitter:B:17:0x004e] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005c A[SYNTHETIC, Splitter:B:22:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    public static void saveYuv(byte[] bArr, long j) {
        String str = "Failed to flush/close stream";
        String str2 = TAG;
        FileOutputStream fileOutputStream = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("sdcard/DCIM/Camera/dump_");
            sb.append(j);
            sb.append(".yuv");
            String sb2 = sb.toString();
            FileOutputStream fileOutputStream2 = new FileOutputStream(sb2);
            try {
                fileOutputStream2.write(bArr);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("saveYuv: ");
                sb3.append(sb2);
                Log.v(str2, sb3.toString());
                try {
                    fileOutputStream2.flush();
                    fileOutputStream2.close();
                } catch (Exception e2) {
                    Log.e(str2, str, e2);
                }
            } catch (Exception e3) {
                e = e3;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.e(str2, "Failed to write image", e);
                    if (fileOutputStream == null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (Exception e4) {
                            Log.e(str2, str, e4);
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
        } catch (Exception e5) {
            e = e5;
            Log.e(str2, "Failed to write image", e);
            if (fileOutputStream == null) {
            }
        }
    }

    public static void saveYuvToJpg(byte[] bArr, int i, int i2, int[] iArr, long j) {
        String str = TAG;
        if (bArr == null) {
            Log.w(str, "saveYuvToJpg: null data");
            return;
        }
        YuvImage yuvImage = new YuvImage(bArr, 17, i, i2, iArr);
        StringBuilder sb = new StringBuilder();
        sb.append("sdcard/DCIM/Camera/dump_");
        sb.append(j);
        sb.append(Storage.JPEG_SUFFIX);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("saveYuvToJpg: ");
        sb3.append(sb2);
        Log.v(str, sb3.toString());
        try {
            yuvImage.compressToJpeg(new Rect(0, 0, i, i2), 100, new FileOutputStream(sb2));
        } catch (FileNotFoundException e2) {
            Log.e(str, e2.getMessage(), e2);
        }
    }

    public static void scaleCamera2Matrix(Matrix matrix, Rect rect, float f2) {
        matrix.postScale(f2, f2);
        matrix.preTranslate(((float) (-rect.width())) / 2.0f, ((float) (-rect.height())) / 2.0f);
    }

    public static void setAccessibilityFocusable(View view, boolean z) {
        if (VERSION.SDK_INT < 16) {
            return;
        }
        if (z) {
            ViewCompat.setImportantForAccessibility(view, 1);
        } else {
            ViewCompat.setImportantForAccessibility(view, 2);
        }
    }

    public static void setBrightnessRampRate(int i) {
        Stub.asInterface(ServiceManager.getService("power"));
    }

    public static void setPixels(byte[] bArr, int i, int i2, byte[] bArr2, int[] iArr) {
        if (bArr != null && bArr2 != null) {
            int i3 = ((iArr[1] * i) + iArr[0]) * i2;
            int i4 = 0;
            for (int i5 = 0; i5 < iArr[3]; i5++) {
                System.arraycopy(bArr2, i4, bArr, i3, iArr[2] * i2);
                i4 += iArr[2] * i2;
                i3 += i * i2;
            }
        }
    }

    public static void setScreenEffect(boolean z) {
        if (b.Si()) {
            try {
                DisplayFeatureManager.getInstance().setScreenEffect(14, z ? 1 : 0);
            } catch (Exception e2) {
                Log.d(TAG, "Meet Exception when calling DisplayFeatureManager#setScreenEffect()", e2);
            }
        }
    }

    private static void setTagValue(ExifInterface exifInterface, int i, Object obj) {
        if (!exifInterface.setTagValue(i, obj)) {
            exifInterface.setTag(exifInterface.buildTag(i, obj));
        }
    }

    public static void showErrorAndFinish(final Activity activity, int i) {
        if (!activity.isFinishing()) {
            AlertDialog show = new Builder(activity).setCancelable(false).setIconAttribute(16843605).setTitle(R.string.camera_error_title).setMessage(i).setNeutralButton(R.string.dialog_ok, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Camera2DataContainer.getInstance().reset();
                    activity.finish();
                }
            }).show();
            boolean z = i == R.string.cannot_connect_camera_twice || i == R.string.cannot_connect_camera_once;
            if (z) {
                CameraStatUtil.trackCameraErrorDialogShow();
            }
            if (sIsKillCameraService && VERSION.SDK_INT >= 26 && b.Ai() && z) {
                if (SystemClock.elapsedRealtime() - CameraSettings.getBroadcastKillServiceTime() > 60000) {
                    broadcastKillService(activity);
                }
                final Button button = show.getButton(-3);
                button.setTextAppearance(GeneralUtils.miuiWidgetButtonDialog());
                button.setEnabled(false);
                final Activity activity2 = activity;
                AnonymousClass2 r3 = new CountDownTimer(5000, 1000) {
                    public void onFinish() {
                        if (!((ActivityBase) activity2).isActivityPaused()) {
                            button.setEnabled(true);
                            button.setText(activity2.getResources().getString(R.string.dialog_ok));
                        }
                    }

                    public void onTick(long j) {
                        if (!((ActivityBase) activity2).isActivityPaused()) {
                            button.setText(activity2.getResources().getString(R.string.dialog_ok_time, new Object[]{Long.valueOf(j / 1000)}));
                        }
                    }
                };
                final CountDownTimer start = r3.start();
                show.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        CountDownTimer countDownTimer = start;
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                    }
                });
            }
            ((ActivityBase) activity).setErrorDialog(show);
        }
    }

    private static void skip(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            while (i != 0) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    i++;
                } else if (next == 3) {
                    i--;
                }
            }
            return;
        }
        throw new IllegalStateException();
    }

    public static void startScreenSlideAlphaInAnimation(View view) {
        ViewCompat.setAlpha(view, 0.0f);
        ViewCompat.animate(view).alpha(1.0f).setDuration(350).setStartDelay(400).setInterpolator(new SineEaseInOutInterpolator()).start();
    }

    public static int stringSparseArraysIndexOf(SparseArray<String> sparseArray, String str) {
        if (str != null) {
            for (int i = 0; i < sparseArray.size(); i++) {
                if (str.equals(sparseArray.valueAt(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void updateAccessibility(Context context) {
        sIsAccessibilityEnable = ((AccessibilityManager) context.getSystemService("accessibility")).isEnabled();
    }

    public static void updateDeviceConfig(Context context) {
        sRegion = SystemProperties.get("ro.miui.region");
        String str = ((TelephonyManager) context.getSystemService("phone")).getSimState() != 5 ? sRegion : null;
        if (TextUtils.isEmpty(str)) {
            Country detectCountry = ((CountryDetector) context.getSystemService("country_detector")).detectCountry();
            if (detectCountry != null) {
                str = detectCountry.getCountryIso();
            }
        }
        mCountryIso = str;
        StringBuilder sb = new StringBuilder();
        sb.append("antiBanding mCountryIso=");
        sb.append(mCountryIso);
        sb.append(" sRegion=");
        sb.append(sRegion);
        Log.d(TAG, sb.toString());
        sIsDumpLog = SystemProperties.getBoolean("camera_dump_parameters", DEBUG);
        sIsDumpOrigJpg = SystemProperties.getBoolean("camera_dump_orig_jpg", false);
        sIsKillCameraService = SystemProperties.getBoolean("kill_camera_service_enable", true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0017, code lost:
        $closeResource(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001a, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0014, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0015, code lost:
        if (r0 != null) goto L_0x0017;
     */
    public static void verifyAssetZip(Context context, String str, String str2, int i) throws IOException {
        InputStream open = context.getAssets().open(str);
        verifyZip(open, str2, i);
        if (open != null) {
            $closeResource(null, open);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002e, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002a, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002b, code lost:
        $closeResource(r1, r2);
     */
    public static void verifyFileZip(Context context, String str, String str2, int i) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("verifyAssetZip ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        FileInputStream fileInputStream = new FileInputStream(new File(str));
        verifyZip((InputStream) fileInputStream, str2, i);
        $closeResource(null, fileInputStream);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000f, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0010, code lost:
        $closeResource(r1, r0);
     */
    public static void verifySdcardZip(Context context, String str, String str2, int i) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(str);
        verifyZip((InputStream) fileInputStream, str2, i);
        $closeResource(null, fileInputStream);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c0, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        $closeResource(r11, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c4, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00cb, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        $closeResource(r10, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00cf, code lost:
        throw r11;
     */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0013 A[SYNTHETIC] */
    public static void verifyZip(InputStream inputStream, String str, int i) throws IOException {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append("/");
                    sb.append(nextEntry.getName());
                    File file2 = new File(sb.toString());
                    boolean z = true;
                    if (!file2.exists()) {
                        if (nextEntry.isDirectory()) {
                            file2.mkdirs();
                        } else {
                            File file3 = new File(file2.getParent());
                            if (!file3.exists()) {
                                file3.mkdirs();
                            }
                            file2.createNewFile();
                            if (z) {
                                String str2 = TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("corrupted ");
                                sb2.append(nextEntry.getName());
                                Log.w(str2, sb2.toString());
                                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                                byte[] bArr = new byte[i];
                                while (true) {
                                    int read = zipInputStream.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                $closeResource(null, fileOutputStream);
                            }
                        }
                    } else if (!nextEntry.isDirectory()) {
                        if (!file2.isFile()) {
                            file2.delete();
                            file2.createNewFile();
                        }
                        if (Verifier.crc32(file2, i) != nextEntry.getCrc()) {
                            if (z) {
                            }
                        }
                    } else if (!file2.isDirectory()) {
                        file2.delete();
                        file2.mkdirs();
                    }
                    z = false;
                    if (z) {
                    }
                } else {
                    $closeResource(null, zipInputStream);
                    return;
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00d3, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        $closeResource(r11, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00d7, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00da, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00db, code lost:
        if (r1 != null) goto L_0x00dd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        $closeResource(r11, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00e0, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00e7, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00e8, code lost:
        $closeResource(r11, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00eb, code lost:
        throw r12;
     */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0017 A[SYNTHETIC] */
    public static void verifyZip(String str, String str2, int i) throws IOException {
        ZipFile zipFile = new ZipFile(str);
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("/");
            sb.append(zipEntry.getName());
            File file2 = new File(sb.toString());
            boolean z = true;
            if (!file2.exists()) {
                if (zipEntry.isDirectory()) {
                    file2.mkdirs();
                } else {
                    File file3 = new File(file2.getParent());
                    if (!file3.exists()) {
                        file3.mkdirs();
                    }
                    file2.createNewFile();
                    if (z) {
                        String str3 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("corrupted ");
                        sb2.append(zipEntry.getName());
                        Log.w(str3, sb2.toString());
                        InputStream inputStream = zipFile.getInputStream(zipEntry);
                        FileOutputStream fileOutputStream = new FileOutputStream(file2);
                        byte[] bArr = new byte[i];
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        $closeResource(null, fileOutputStream);
                        if (inputStream != null) {
                            $closeResource(null, inputStream);
                        } else {
                            continue;
                        }
                    }
                }
            } else if (!zipEntry.isDirectory()) {
                if (!file2.isFile()) {
                    file2.delete();
                    file2.createNewFile();
                }
                if (Verifier.crc32(file2, i) != zipEntry.getCrc()) {
                    if (z) {
                    }
                }
            } else if (!file2.isDirectory()) {
                file2.delete();
                file2.mkdirs();
            }
            z = false;
            if (z) {
            }
        }
        $closeResource(null, zipFile);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:4|5|6|7|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0037, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0038, code lost:
        r0 = new java.lang.StringBuilder();
        r0.append("review image fail. uri=");
        r0.append(r3);
        com.android.camera.log.Log.e(r1, r0.toString(), r4);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x002c */
    public static void viewUri(Uri uri, Context context) {
        boolean isUriValid = isUriValid(uri, context.getContentResolver());
        String str = TAG;
        if (!isUriValid) {
            StringBuilder sb = new StringBuilder();
            sb.append("Uri invalid. uri=");
            sb.append(uri);
            Log.e(str, sb.toString());
            return;
        }
        context.startActivity(new Intent(REVIEW_ACTION, uri));
        context.startActivity(new Intent("android.intent.action.VIEW", uri));
    }

    public static String viewVisibilityToString(int i) {
        return i != 0 ? i != 4 ? i != 8 ? "UNKNOWN" : "GONE" : "INVISIBLE" : "VISIBLE";
    }
}
