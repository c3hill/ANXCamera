package com.android.camera;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

public class OnScreenHint {
    private ViewGroup mHintView;

    public OnScreenHint(ViewGroup viewGroup) {
        this.mHintView = viewGroup;
    }

    public static OnScreenHint makeText(Activity activity, CharSequence charSequence) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(R.id.on_screen_hint);
        OnScreenHint onScreenHint = new OnScreenHint(viewGroup);
        ((TextView) viewGroup.findViewById(R.id.message)).setText(charSequence);
        return onScreenHint;
    }

    public void cancel() {
        Util.fadeOut(this.mHintView);
    }

    public int getHintViewVisibility() {
        return this.mHintView.getVisibility();
    }

    public void setText(CharSequence charSequence) {
        ViewGroup viewGroup = this.mHintView;
        String str = "This OnScreenHint was not created with OnScreenHint.makeText()";
        if (viewGroup != null) {
            TextView textView = (TextView) viewGroup.findViewById(R.id.message);
            if (textView != null) {
                textView.setText(charSequence);
                return;
            }
            throw new RuntimeException(str);
        }
        throw new RuntimeException(str);
    }

    public void show() {
        Util.fadeIn(this.mHintView);
    }
}
