package com.android.camera.effect.renders;

import android.provider.MiuiSettings.System;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.StringTexture;

class TextWaterMark extends WaterMark {
    private static final float RATIO = 0.82f;
    private static final String TAG = "TextWaterMark";
    private static final int TEXT_COLOR = -262152;
    private static final int TEXT_PIXEL_SIZE = 144;
    private final int[][] PIC_WIDTHS = {new int[]{0, 149}, new int[]{150, 239}, new int[]{240, 279}, new int[]{280, 400}, new int[]{401, 1439}, new int[]{1440, 1511}, new int[]{1512, 1799}, new int[]{1800, 1899}, new int[]{1900, 2299}, new int[]{2300, 3120}, new int[]{3121, System.STATUS_BAR_UPDATE_NETWORK_SPEED_INTERVAL_DEFAULT}};
    private final int[][] WATERMARK_FONT_SIZES = {new int[]{5, 4, 2, 4, 3, 7}, new int[]{8, 6, 2, 6, 3, 7}, new int[]{11, 6, 5, 6, 5, 12}, new int[]{12, 7, 5, 7, 5, 12}, new int[]{50, 32, 11, 31, 20, 47}, new int[]{58, 36, 19, 38, 24, 55}, new int[]{65, 41, 24, 42, 27, 63}, new int[]{80, 50, 24, 50, 32, 75}, new int[]{83, 52, 25, 52, 33, 78}, new int[]{104, 65, 33, 65, 42, 98}, new int[]{128, 80, 40, 80, 48, 132}};
    private int mCenterX;
    private int mCenterY;
    private int mCharMargin;
    private int mFontIndex;
    private int mPadding;
    private int mWaterHeight;
    private String mWaterText;
    private BasicTexture mWaterTexture;
    private int mWaterWidth;

    public TextWaterMark(String str, int i, int i2, int i3) {
        int i4 = i;
        int i5 = i2;
        super(i4, i5, i3);
        this.mWaterText = str;
        this.mWaterTexture = StringTexture.newInstance(this.mWaterText, 144.0f, TEXT_COLOR, 0.0f, false, 1);
        this.mFontIndex = getFontIndex(i4, i5);
        this.mWaterWidth = getWaterMarkWidth(this.mWaterText, this.mFontIndex);
        int[][] iArr = this.WATERMARK_FONT_SIZES;
        int i6 = this.mFontIndex;
        this.mWaterHeight = (int) (((float) iArr[i6][0]) / RATIO);
        this.mPadding = iArr[i6][5];
        this.mCharMargin = (int) ((((float) this.mWaterHeight) * 0.18f) / 2.0f);
        calcCenterAxis();
        if (Util.sIsDumpLog) {
            print();
        }
    }

    private void calcCenterAxis() {
        int i = this.mOrientation;
        if (i == 0) {
            int i2 = this.mPictureWidth;
            int i3 = this.mPadding;
            this.mCenterX = (i2 - i3) - (this.mWaterWidth / 2);
            this.mCenterY = ((this.mPictureHeight - i3) - (this.mWaterHeight / 2)) + this.mCharMargin;
        } else if (i == 90) {
            int i4 = this.mPictureWidth;
            int i5 = this.mPadding;
            this.mCenterX = ((i4 - i5) - (this.mWaterHeight / 2)) + this.mCharMargin;
            this.mCenterY = i5 + (this.mWaterWidth / 2);
        } else if (i == 180) {
            int i6 = this.mPadding;
            this.mCenterX = (this.mWaterWidth / 2) + i6;
            this.mCenterY = (i6 + (this.mWaterHeight / 2)) - this.mCharMargin;
        } else if (i == 270) {
            int i7 = this.mPadding;
            this.mCenterX = ((this.mWaterHeight / 2) + i7) - this.mCharMargin;
            this.mCenterY = (this.mPictureHeight - i7) - (this.mWaterWidth / 2);
        }
    }

    private int getFontIndex(int i, int i2) {
        int min = Math.min(i, i2);
        int length = this.WATERMARK_FONT_SIZES.length - 1;
        int i3 = 0;
        while (true) {
            int[][] iArr = this.PIC_WIDTHS;
            if (i3 >= iArr.length) {
                return length;
            }
            if (min >= iArr[i3][0] && min <= iArr[i3][1]) {
                return i3;
            }
            i3++;
        }
    }

    private int getWaterMarkWidth(String str, int i) {
        char[] charArray;
        int[][] iArr = this.WATERMARK_FONT_SIZES;
        int i2 = iArr[i][1];
        int i3 = iArr[i][2];
        int i4 = iArr[i][3];
        int i5 = iArr[i][4];
        int i6 = 0;
        for (char c2 : str.toCharArray()) {
            if (c2 >= '0' && c2 <= '9') {
                i6 += i2;
            } else if (c2 == ':') {
                i6 += i5;
            } else if (c2 == '-') {
                i6 += i3;
            } else if (c2 == ' ') {
                i6 += i4;
            }
        }
        return i6;
    }

    private void print() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("WaterMark mPictureWidth=");
        sb.append(this.mPictureWidth);
        sb.append(" mPictureHeight =");
        sb.append(this.mPictureHeight);
        sb.append(" mWaterText=");
        sb.append(this.mWaterText);
        sb.append(" mFontIndex=");
        sb.append(this.mFontIndex);
        sb.append(" mCenterX=");
        sb.append(this.mCenterX);
        sb.append(" mCenterY=");
        sb.append(this.mCenterY);
        sb.append(" mWaterWidth=");
        sb.append(this.mWaterWidth);
        sb.append(" mWaterHeight=");
        sb.append(this.mWaterHeight);
        sb.append(" mPadding=");
        sb.append(this.mPadding);
        Log.v(str, sb.toString());
    }

    public int getCenterX() {
        return this.mCenterX;
    }

    public int getCenterY() {
        return this.mCenterY;
    }

    public int getHeight() {
        return this.mWaterHeight;
    }

    public int getPaddingX() {
        return this.mPadding;
    }

    public int getPaddingY() {
        return this.mPadding;
    }

    public BasicTexture getTexture() {
        return this.mWaterTexture;
    }

    public int getWidth() {
        return this.mWaterWidth;
    }
}
