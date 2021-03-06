package com.android.camera.fragment.beauty;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.ColorConstant;
import com.android.camera.data.data.TypeItem;
import com.android.camera.ui.ColorImageView;
import java.util.List;

public class MakeupSingleCheckAdapter extends Adapter<SingleCheckViewHolder> {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public boolean mIsCustomWidth;
    private int mItemHorizontalMargin = 0;
    /* access modifiers changed from: private */
    public int mItemMargin;
    /* access modifiers changed from: private */
    public int mItemWidth;
    /* access modifiers changed from: private */
    public int mPreSelectedItem = 0;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public int mSelectedItem = 0;
    private List<TypeItem> mSingleCheckList;
    private OnItemClickListener onItemClickListener;

    class SingleCheckViewHolder extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public View itemView;
        private MakeupSingleCheckAdapter mAdapter;
        private ColorImageView mBase;
        private TextView mText;

        public SingleCheckViewHolder(View view, MakeupSingleCheckAdapter makeupSingleCheckAdapter) {
            super(view);
            this.mAdapter = makeupSingleCheckAdapter;
            this.itemView = view;
            this.mText = (TextView) view.findViewById(R.id.makeup_item_name);
            this.mBase = (ColorImageView) view.findViewById(R.id.makeup_item_icon);
            view.setOnClickListener(this);
        }

        private void colorAnimate(final ColorImageView colorImageView, int i, int i2) {
            ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            ofObject.setDuration(200);
            ofObject.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    colorImageView.setColorAndRefresh(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            ofObject.start();
        }

        private void textColorAnimate(final TextView textView, int i, int i2) {
            ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            ofObject.setDuration(200);
            ofObject.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    textView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            ofObject.start();
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition == MakeupSingleCheckAdapter.this.mSelectedItem) {
                this.mAdapter.onItemHolderClick(this);
                return;
            }
            MakeupSingleCheckAdapter makeupSingleCheckAdapter = MakeupSingleCheckAdapter.this;
            makeupSingleCheckAdapter.mPreSelectedItem = makeupSingleCheckAdapter.mSelectedItem;
            MakeupSingleCheckAdapter.this.mSelectedItem = adapterPosition;
            SingleCheckViewHolder singleCheckViewHolder = (SingleCheckViewHolder) MakeupSingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(MakeupSingleCheckAdapter.this.mPreSelectedItem);
            SingleCheckViewHolder singleCheckViewHolder2 = (SingleCheckViewHolder) MakeupSingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(MakeupSingleCheckAdapter.this.mSelectedItem);
            Resources resources = MakeupSingleCheckAdapter.this.mContext.getResources();
            if (singleCheckViewHolder != null) {
                colorAnimate(singleCheckViewHolder.mBase, ColorConstant.COLOR_COMMON_SELECTED, resources.getColor(R.color.beautycamera_beauty_advanced_item_backgroud_normal));
            }
            if (singleCheckViewHolder2 != null) {
                colorAnimate(singleCheckViewHolder2.mBase, resources.getColor(R.color.beautycamera_beauty_advanced_item_backgroud_normal), ColorConstant.COLOR_COMMON_SELECTED);
            }
            if (singleCheckViewHolder != null) {
                textColorAnimate(singleCheckViewHolder.mText, ColorConstant.COLOR_COMMON_SELECTED, resources.getColor(R.color.beautycamera_beauty_advanced_item_text_normal));
            }
            if (singleCheckViewHolder2 != null) {
                textColorAnimate(singleCheckViewHolder2.mText, resources.getColor(R.color.beautycamera_beauty_advanced_item_text_normal), ColorConstant.COLOR_COMMON_SELECTED);
            }
            if (singleCheckViewHolder == null) {
                this.mAdapter.notifyItemChanged(MakeupSingleCheckAdapter.this.mPreSelectedItem);
            }
            this.mAdapter.onItemHolderClick(this);
        }

        public void setDataToView(TypeItem typeItem, int i) throws Exception {
            String str;
            boolean z = true;
            boolean z2 = i == 0;
            if (i != MakeupSingleCheckAdapter.this.getItemCount() - 1) {
                z = false;
            }
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.itemView.getLayoutParams();
            if (MakeupSingleCheckAdapter.this.mIsCustomWidth) {
                marginLayoutParams.width = MakeupSingleCheckAdapter.this.mItemWidth;
            }
            marginLayoutParams.leftMargin = 0;
            marginLayoutParams.rightMargin = 0;
            if (z2) {
                marginLayoutParams.leftMargin = MakeupSingleCheckAdapter.this.mItemMargin;
            } else if (z) {
                marginLayoutParams.rightMargin = MakeupSingleCheckAdapter.this.mItemMargin;
            }
            this.itemView.setTag(typeItem);
            Resources resources = MakeupSingleCheckAdapter.this.mContext.getResources();
            this.mText.setText(resources.getString(typeItem.getTextResource()));
            TextView textView = this.mText;
            int access$500 = MakeupSingleCheckAdapter.this.mSelectedItem;
            int i2 = ColorConstant.COLOR_COMMON_SELECTED;
            textView.setTextColor(i == access$500 ? -16733953 : resources.getColor(R.color.beautycamera_beauty_advanced_item_text_normal));
            if (Util.isAccessible()) {
                TextView textView2 = this.mText;
                if (i == MakeupSingleCheckAdapter.this.mSelectedItem) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(resources.getString(typeItem.getTextResource()));
                    sb.append(resources.getString(R.string.accessibility_open));
                    str = sb.toString();
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(resources.getString(typeItem.getTextResource()));
                    sb2.append(resources.getString(R.string.accessibility_closed));
                    str = sb2.toString();
                }
                textView2.setContentDescription(str);
            }
            this.mBase.setImageResource(typeItem.getImageResource());
            ColorImageView colorImageView = this.mBase;
            if (i != MakeupSingleCheckAdapter.this.mSelectedItem) {
                i2 = resources.getColor(R.color.beautycamera_beauty_advanced_item_backgroud_normal);
            }
            colorImageView.setColor(i2);
        }
    }

    public MakeupSingleCheckAdapter(Context context, List<TypeItem> list, int i) {
        this.mContext = context;
        this.mSingleCheckList = list;
        this.mItemHorizontalMargin = i;
    }

    public MakeupSingleCheckAdapter(Context context, List<TypeItem> list, int i, boolean z, int i2, int i3) {
        this.mContext = context;
        this.mSingleCheckList = list;
        this.mItemHorizontalMargin = i;
        this.mIsCustomWidth = z;
        this.mItemWidth = i2;
        this.mItemMargin = i3;
    }

    public int getItemCount() {
        return this.mSingleCheckList.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    public void onBindViewHolder(SingleCheckViewHolder singleCheckViewHolder, int i) {
        try {
            singleCheckViewHolder.setDataToView((TypeItem) this.mSingleCheckList.get(i), i);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public SingleCheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SingleCheckViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.makeup_item, viewGroup, false), this);
    }

    public void onItemHolderClick(SingleCheckViewHolder singleCheckViewHolder) {
        OnItemClickListener onItemClickListener2 = this.onItemClickListener;
        if (onItemClickListener2 != null) {
            onItemClickListener2.onItemClick(null, singleCheckViewHolder.itemView, singleCheckViewHolder.getAdapterPosition(), singleCheckViewHolder.getItemId());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public void setSelectedPosition(int i) {
        this.mSelectedItem = i;
    }
}
