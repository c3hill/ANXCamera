package com.android.camera.fragment.manually;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListAdapter;
import com.android.camera.R;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.manually.adapter.ExtraCustomWBListAdapter;
import com.android.camera.fragment.manually.adapter.ExtraHorizontalListAdapter;
import com.android.camera.fragment.manually.adapter.ExtraRecyclerViewAdapter;
import com.android.camera.fragment.manually.adapter.ExtraSlideFocusAdapter;
import com.android.camera.ui.HorizontalListView;
import com.android.camera.ui.HorizontalSlideView;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import java.util.List;

public class FragmentManuallyExtra extends BaseFragment {
    public static final int FRAGMENT_INFO = 254;
    private int mCurrentTitle = -1;
    private ComponentData mData;
    private RecyclerView mExtraList;
    private HorizontalListView mExtraListHorizontal;
    private HorizontalSlideView mHorizontalSlideView;
    /* access modifiers changed from: private */
    public boolean mIsAnimateIng;
    private ManuallyListener mManuallyListener;
    private boolean mNeedAnimation;
    private View mTargetView;

    private void adjustViewBackground(View view, int i) {
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        if (uiStyle == 0) {
            view.setBackgroundResource(R.color.halfscreen_background);
        } else if (uiStyle == 1 || uiStyle == 3) {
            view.setBackgroundResource(R.color.fullscreen_background);
        }
    }

    private void initAdapter(ComponentData componentData) {
        switch (componentData.getDisplayTitleString()) {
            case R.string.pref_camera_iso_title /*2131690057*/:
            case R.string.pref_manual_exposure_title /*2131690248*/:
                initHorizontalListView(componentData);
                return;
            case R.string.pref_camera_whitebalance_title /*2131690214*/:
                initWBRecyclerView(componentData);
                return;
            case R.string.pref_camera_zoom_mode_title /*2131690222*/:
                initLensRecyclerView(componentData);
                return;
            case R.string.pref_qc_focus_position_title /*2131690264*/:
                initSlideFocusView(componentData);
                return;
            default:
                return;
        }
    }

    private void initHorizontalListView(ComponentData componentData) {
        View view = this.mTargetView;
        if (view != null) {
            view.setVisibility(8);
        }
        this.mTargetView = this.mExtraListHorizontal;
        ExtraHorizontalListAdapter extraHorizontalListAdapter = new ExtraHorizontalListAdapter(componentData, this.mCurrentMode, this.mManuallyListener);
        this.mExtraListHorizontal.setAdapter((ListAdapter) extraHorizontalListAdapter);
        this.mExtraListHorizontal.setOnItemClickListener(extraHorizontalListAdapter);
        this.mExtraListHorizontal.setOnItemSingleTapDownListener(extraHorizontalListAdapter);
        this.mExtraListHorizontal.setSelection(extraHorizontalListAdapter.getValuePosition());
        this.mExtraListHorizontal.setVisibility(0);
    }

    private void initLensRecyclerView(ComponentData componentData) {
        View view = this.mTargetView;
        if (view != null) {
            view.setVisibility(8);
        }
        this.mTargetView = this.mExtraList;
        int i = getResources().getDisplayMetrics().widthPixels;
        int i2 = 5;
        if (componentData.getItems().size() <= 5) {
            i2 = componentData.getItems().size();
        }
        ComponentData componentData2 = componentData;
        AnonymousClass4 r3 = new ExtraRecyclerViewAdapter(componentData2, this.mCurrentMode, this.mManuallyListener, i / i2) {
            /* access modifiers changed from: protected */
            public boolean couldNewValueTakeEffect(String str) {
                if (str == null || !str.equals("manual")) {
                    return super.couldNewValueTakeEffect(str);
                }
                return true;
            }
        };
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "manually_extra_list");
        linearLayoutManagerWrapper.setOrientation(0);
        this.mExtraList.setLayoutManager(linearLayoutManagerWrapper);
        this.mExtraList.setAdapter(r3);
        this.mExtraList.scrollToPosition(r3.getValuePosition());
        this.mExtraList.setVisibility(0);
    }

    private void initSlideFocusView(ComponentData componentData) {
        View view = this.mTargetView;
        if (view != null) {
            view.setVisibility(8);
        }
        this.mTargetView = this.mHorizontalSlideView;
        ExtraSlideFocusAdapter extraSlideFocusAdapter = new ExtraSlideFocusAdapter(getContext(), componentData, this.mCurrentMode, this.mManuallyListener);
        this.mHorizontalSlideView.setOnItemSelectListener(extraSlideFocusAdapter);
        this.mHorizontalSlideView.setDrawAdapter(extraSlideFocusAdapter);
        this.mHorizontalSlideView.setSelection(extraSlideFocusAdapter.mapFocusToIndex(Integer.valueOf(componentData.getComponentValue(this.mCurrentMode)).intValue()));
        this.mHorizontalSlideView.setVisibility(0);
    }

    private void initWBRecyclerView(ComponentData componentData) {
        View view = this.mTargetView;
        if (view != null) {
            view.setVisibility(8);
        }
        this.mTargetView = this.mExtraList;
        int i = (int) (((float) getResources().getDisplayMetrics().widthPixels) / 5.5f);
        if (componentData.getItems().size() * i < getResources().getDisplayMetrics().widthPixels) {
            i = getResources().getDisplayMetrics().widthPixels / componentData.getItems().size();
        }
        ComponentData componentData2 = componentData;
        AnonymousClass3 r1 = new ExtraRecyclerViewAdapter(componentData2, this.mCurrentMode, this.mManuallyListener, i) {
            /* access modifiers changed from: protected */
            public boolean couldNewValueTakeEffect(String str) {
                if (str == null || !str.equals("manual")) {
                    return super.couldNewValueTakeEffect(str);
                }
                return true;
            }
        };
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "manually_extra_list");
        linearLayoutManagerWrapper.setOrientation(0);
        this.mExtraList.setLayoutManager(linearLayoutManagerWrapper);
        this.mExtraList.setAdapter(r1);
        this.mExtraList.scrollToPosition(r1.getValuePosition());
        this.mExtraList.setVisibility(0);
    }

    public void animateOut() {
        this.mIsAnimateIng = true;
        Completable.create(new SlideOutOnSubscribe(getView(), 80)).subscribe((Action) new Action() {
            public void run() throws Exception {
                FragmentUtils.removeFragmentByTag(FragmentManuallyExtra.this.getFragmentManager(), FragmentManuallyExtra.this.getFragmentTag());
                FragmentManuallyExtra.this.mIsAnimateIng = false;
            }
        });
    }

    public int getCurrentTitle() {
        return this.mCurrentTitle;
    }

    public int getFragmentInto() {
        return 254;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_manually_extra;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mExtraList = (RecyclerView) view.findViewById(R.id.manually_extra_list);
        this.mExtraListHorizontal = (HorizontalListView) view.findViewById(R.id.manually_extra_list_horizon);
        this.mHorizontalSlideView = (HorizontalSlideView) view.findViewById(R.id.manually_extra_horizon_slideview);
        if (this.mData != null) {
            adjustViewBackground(view, this.mCurrentMode);
            initAdapter(this.mData);
            this.mCurrentTitle = this.mData.getDisplayTitleString();
        }
    }

    public boolean isAnimateIng() {
        return this.mIsAnimateIng;
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        adjustViewBackground(getView(), this.mCurrentMode);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mNeedAnimation) {
            this.mNeedAnimation = false;
            this.mIsAnimateIng = true;
            Completable.create(new SlideInOnSubscribe(view, 80)).subscribe((Action) new Action() {
                public void run() throws Exception {
                    FragmentManuallyExtra.this.mIsAnimateIng = false;
                }
            });
        }
    }

    public void provideAnimateElement(int i, List<Completable> list, int i2) {
        super.provideAnimateElement(i, list, i2);
    }

    public void resetData(ComponentData componentData) {
        this.mData = componentData;
        initAdapter(this.mData);
        this.mCurrentTitle = this.mData.getDisplayTitleString();
    }

    public void setComponentData(ComponentData componentData, int i, boolean z, ManuallyListener manuallyListener) {
        this.mData = componentData;
        this.mCurrentMode = i;
        this.mNeedAnimation = z;
        this.mManuallyListener = manuallyListener;
    }

    public void showCustomWB(ComponentManuallyWB componentManuallyWB) {
        this.mTargetView = this.mExtraListHorizontal;
        ExtraCustomWBListAdapter extraCustomWBListAdapter = new ExtraCustomWBListAdapter(componentManuallyWB, this.mManuallyListener);
        this.mExtraListHorizontal.setAdapter((ListAdapter) extraCustomWBListAdapter);
        this.mExtraListHorizontal.setOnItemClickListener(extraCustomWBListAdapter);
        this.mExtraListHorizontal.setOnItemSingleTapDownListener(extraCustomWBListAdapter);
        this.mExtraListHorizontal.setSelection(extraCustomWBListAdapter.getValuePosition());
        this.mExtraListHorizontal.setVisibility(0);
        this.mExtraList.setVisibility(8);
    }

    public void updateData() {
        this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
        initAdapter(this.mData);
    }
}
