package com.android.camera.network.live;

import android.os.Build;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.network.net.base.ErrorCode;
import com.android.camera.network.net.base.ResponseListener;
import com.android.camera.sticker.LiveStickerInfo;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.ss.android.ugc.effectmanager.EffectConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TTLiveStickerResourceRequest extends BaseJsonRequest<List<LiveStickerInfo>> {
    private static final String APP_VERSION = "3.0.1";
    private static final String BASE_URL = "https://effect.snssdk.com/effect/api/v3/effects";
    private static final String DEVICE_ID = "123456";
    private static final String DEVICE_TYPE = "Xiaomi";
    private static final long MAX_CACHE_TIME = 86400000;
    private static final String PLATFORM = "android";
    private static final String SDK_VERSION = "4.4.0";

    public TTLiveStickerResourceRequest(String str, String str2) {
        super(BASE_URL);
        String globalAccessKey = Util.isGlobalVersion() ? Util.getGlobalAccessKey() : Util.getAccessKey();
        String str3 = Util.sRegion;
        addParam("app_version", APP_VERSION);
        addParam("device_id", DEVICE_ID);
        addParam(EffectConfiguration.KEY_ACCESS_KEY, globalAccessKey);
        addParam(EffectConfiguration.KEY_SDK_VERSION, SDK_VERSION);
        addParam("channel", str);
        addParam(EffectConfiguration.KEY_DEVICE_PLATFORM, PLATFORM);
        addParam(EffectConfiguration.KEY_DEVICE_TYPE, Build.MODEL);
        addParam(EffectConfiguration.KEY_PANEL, str2);
        addParam(EffectConfiguration.KEY_REGION, str3);
    }

    /* access modifiers changed from: private */
    public List<LiveStickerInfo> loadFromCache() throws BaseRequestException {
        try {
            return processJson(new JSONObject(CameraSettings.getTTLiveStickerJsonCache()));
        } catch (JSONException e2) {
            throw new BaseRequestException(ErrorCode.PARSE_ERROR, e2.getMessage(), e2);
        }
    }

    public void execute(ResponseListener responseListener) {
        execute(true, responseListener);
    }

    public void execute(final boolean z, final ResponseListener responseListener) {
        AnonymousClass1 r0 = new ResponseListener() {
            public void onResponse(Object... objArr) {
                if (!z) {
                    CameraSettings.setLiveStickerLastCacheTime(0);
                }
                responseListener.onResponse(objArr);
            }

            public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                if (errorCode == ErrorCode.NETWORK_NOT_CONNECTED) {
                    responseListener.onResponseError(errorCode, str, obj);
                    return;
                }
                try {
                    Log.v("BaseRequest", "Network error, TT sticker load from cache");
                    responseListener.onResponse(TTLiveStickerResourceRequest.this.loadFromCache());
                } catch (BaseRequestException unused) {
                    responseListener.onResponseError(errorCode, str, obj);
                }
            }
        };
        if (!z) {
            CameraSettings.setLiveStickerLastCacheTime(0);
        }
        long liveStickerLastCacheTime = CameraSettings.getLiveStickerLastCacheTime();
        long currentTimeMillis = System.currentTimeMillis();
        String str = "BaseRequest";
        if (liveStickerLastCacheTime <= 0 || currentTimeMillis - liveStickerLastCacheTime > MAX_CACHE_TIME) {
            Log.v(str, "TT sticker directly request");
            super.execute(r0);
            return;
        }
        try {
            Log.v(str, "TT sticker load from cache");
            responseListener.onResponse(loadFromCache());
        } catch (BaseRequestException e2) {
            responseListener.onResponseError(e2.getErrorCode(), e2.getMessage(), e2);
        }
    }

    /* access modifiers changed from: protected */
    public void hasUpdate(String str) {
        String tTLiveStickerJsonCache = CameraSettings.getTTLiveStickerJsonCache();
        if (!str.equals(tTLiveStickerJsonCache) && !tTLiveStickerJsonCache.isEmpty()) {
            CameraSettings.setTTLiveStickerNeedRedDot(true);
            CameraSettings.setLiveModuleClicked(false);
            CameraSettings.setLiveStickerRedDotTime(System.currentTimeMillis());
        }
    }

    /* access modifiers changed from: protected */
    public List<LiveStickerInfo> processJson(JSONObject jSONObject) throws BaseRequestException, JSONException {
        if (jSONObject != null) {
            String str = "status_code";
            if (jSONObject.has(str)) {
                if (jSONObject.getInt(str) == 0) {
                    String str2 = PhotosOemApi.PATH_SPECIAL_TYPE_DATA;
                    if (!jSONObject.isNull(str2)) {
                        String jSONObject2 = jSONObject.toString();
                        hasUpdate(jSONObject2);
                        JSONObject optJSONObject = jSONObject.optJSONObject(str2);
                        ArrayList arrayList = new ArrayList();
                        JSONArray jSONArray = optJSONObject.getJSONArray("effects");
                        if (jSONArray != null) {
                            arrayList.ensureCapacity(jSONArray.length());
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                                LiveStickerInfo liveStickerInfo = new LiveStickerInfo();
                                liveStickerInfo.id = jSONObject3.optString("id");
                                liveStickerInfo.name = jSONObject3.optString("name");
                                String str3 = "url_list";
                                liveStickerInfo.icon = jSONObject3.getJSONObject("icon_url").getJSONArray(str3).optString(0);
                                String str4 = "file_url";
                                liveStickerInfo.url = jSONObject3.getJSONObject(str4).getJSONArray(str3).optString(0);
                                liveStickerInfo.hash = jSONObject3.getJSONObject(str4).optString("uri");
                                liveStickerInfo.hint = jSONObject3.optString("hint");
                                liveStickerInfo.hintIcon = jSONObject3.getJSONObject("hint_icon").getJSONArray(str3).optString(0);
                                arrayList.add(liveStickerInfo);
                            }
                        }
                        CameraSettings.setTTLiveStickerJsonCache(jSONObject2);
                        CameraSettings.setLiveStickerLastCacheTime(System.currentTimeMillis());
                        return arrayList;
                    }
                    throw new BaseRequestException(ErrorCode.BODY_EMPTY, "response empty data");
                }
                throw new BaseRequestException(ErrorCode.SERVER_ERROR, jSONObject.optString("message"));
            }
        }
        throw new BaseRequestException(ErrorCode.PARSE_ERROR, "response has no status_code");
    }
}
