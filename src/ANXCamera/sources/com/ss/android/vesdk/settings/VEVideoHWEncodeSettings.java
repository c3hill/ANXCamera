package com.ss.android.vesdk.settings;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.Keep;

@Keep
public class VEVideoHWEncodeSettings implements Parcelable {
    public static final Creator<VEVideoHWEncodeSettings> CREATOR = new Creator<VEVideoHWEncodeSettings>() {
        public VEVideoHWEncodeSettings createFromParcel(Parcel parcel) {
            return new VEVideoHWEncodeSettings(parcel);
        }

        public VEVideoHWEncodeSettings[] newArray(int i) {
            return new VEVideoHWEncodeSettings[i];
        }
    };
    public long mBitrate = 6000000;
    public int mGop = 35;
    public int mProfile = VEVideoEncodeProfile.ENCODE_PROFILE_UNKNOWN.ordinal();

    public VEVideoHWEncodeSettings() {
    }

    protected VEVideoHWEncodeSettings(Parcel parcel) {
        this.mBitrate = parcel.readLong();
        this.mProfile = parcel.readInt();
        this.mGop = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mBitrate);
        parcel.writeInt(this.mProfile);
        parcel.writeInt(this.mGop);
    }
}
