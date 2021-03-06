package com.bumptech.glide.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.bumptech.glide.manager.c.a;

/* compiled from: DefaultConnectivityMonitorFactory */
public class g implements d {
    private static final String TAG = "ConnectivityMonitor";
    private static final String hk = "android.permission.ACCESS_NETWORK_STATE";

    @NonNull
    public c a(@NonNull Context context, @NonNull a aVar) {
        boolean z = ContextCompat.checkSelfPermission(context, hk) == 0;
        String str = TAG;
        if (Log.isLoggable(str, 3)) {
            Log.d(str, z ? "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor" : "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor");
        }
        return z ? new f(context, aVar) : new k();
    }
}
