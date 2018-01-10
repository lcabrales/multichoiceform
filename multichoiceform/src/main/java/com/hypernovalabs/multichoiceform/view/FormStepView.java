package com.hypernovalabs.multichoiceform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.R;

/**
 * Created by lucascabrales on 12/26/17.
 */

public class FormStepView extends LinearLayout {

    private final AttributeSet mAttrs;
    private Context mContext;
    private String mTitle;
    private String mSelection;
    private LinearLayout mLayout;
    private TextView mTitleTextView;
    private TextView mSelectionTextView;
    private ImageView mImageView;
    private View mSeparator;
    private int mSeparatorColor;
    private int mTitleColor;
    private int mSelectionColor;
    private int mDisabledColor;

    public FormStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.form_step_item, this, true);

        mContext = context;
        mAttrs = attrs;

        init();
    }

    private void init() {
        mDisabledColor = ContextCompat.getColor(mContext, R.color.disabled);

        TypedArray a = mContext.getTheme().obtainStyledAttributes(
                mAttrs,
                R.styleable.FormStepView,
                0, 0);

        try {
            mTitle = a.getString(R.styleable.FormStepView_title);
            mSeparatorColor = a.getColor(R.styleable.FormStepView_separatorColor,
                    ContextCompat.getColor(mContext, R.color.gray));
            mTitleColor = a.getColor(R.styleable.FormStepView_titleColor, Color.BLACK);
            mSelectionColor = a.getColor(R.styleable.FormStepView_selectionColor, Color.BLACK);

        } finally {
            a.recycle();
        }

        mLayout = findViewById(R.id.layout);
        mTitleTextView = findViewById(R.id.title);
        mSelectionTextView = findViewById(R.id.selection);
        mImageView = findViewById(R.id.image);
        mSeparator = findViewById(R.id.separator);

        mSelection = "";

        setTitle(mTitle);
        setTitleColor(mTitleColor);
        setSelectionColor(mSelectionColor);
        setSeparatorColor(mSeparatorColor);
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);

        invalidate();
        requestLayout();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setSelection(String selection) {
        mSelection = selection;
        mSelectionTextView.setText(selection);

        invalidate();
        requestLayout();
    }

    public String getSelection() {
        return mSelection;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public LinearLayout getLayout() {
        return mLayout;
    }

    public TextView getSelectionView() {
        return mSelectionTextView;
    }

    public TextView getTitleView() {
        return mTitleTextView;
    }

    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public void setSelectionColor(int color) {
        mSelectionTextView.setTextColor(color);
    }

    public void setSeparatorColor(int color) {
        mSeparator.setBackgroundColor(color);
    }

    public void enable(boolean enable) {
        if (enable) {
            setEnabled(true);
            mImageView.setVisibility(View.VISIBLE);
            mSeparator.setBackgroundColor(mSeparatorColor);
        } else {
            setEnabled(false);
            mImageView.setVisibility(View.GONE);
            mSeparator.setBackgroundColor(mDisabledColor);
        }
    }
}
