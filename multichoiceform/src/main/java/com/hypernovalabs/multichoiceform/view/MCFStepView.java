package com.hypernovalabs.multichoiceform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Dimension;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.R;

/**
 * Created by lucascabrales on 12/26/17.
 * <p>
 * Holds the definition for the MCFStepView.
 */
public class MCFStepView extends LinearLayout {

    private final AttributeSet mAttrs;
    private final Context mContext;
    private LinearLayout mLayout;
    private TextView mTitleTextView, mSelectionTextView;
    private ImageView mArrowImageView;
    private View mSeparator;
    private Drawable mArrowDrawable;
    private String mTitle, mSelection;
    private int mSeparatorColor, mTitleColor, mSelectionColor, mDisabledSeparatorColor, mDisabledTitleColor,
            mDisabledSelectionColor, mTitleMaxLines, mSelectionMaxLines;
    private float mTitleSize, mSelectionSize;
    private boolean isEnabled, isMasked;

    /**
     * Single constructor for the MCFStepView.
     *
     * @param context Any context, activity recommended.
     * @param attrs   MCFStepView's custom attritubes.
     */
    public MCFStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.mcf_step_item, this, true);

        mContext = context;
        mAttrs = attrs;

        init();
    }

    /**
     * Initializes the custom view and sets the defined attributes.
     */
    private void init() {
        TypedArray a = mContext.getTheme().obtainStyledAttributes(
                mAttrs,
                R.styleable.MCFStepView,
                0, 0);

        try {
            mTitle = a.getString(R.styleable.MCFStepView_mcf_title);
            mTitleColor = a.getColor(R.styleable.MCFStepView_mcf_titleColor, Color.BLACK);
            mTitleMaxLines = a.getInteger(R.styleable.MCFStepView_mcf_titleMaxLines, 1);
            mTitleSize = a.getDimension(R.styleable.MCFStepView_mcf_titleSize, getResources().getDimension(R.dimen.mcf_text_size_default));
            mSeparatorColor = a.getColor(R.styleable.MCFStepView_mcf_separatorColor,
                    ContextCompat.getColor(mContext, R.color.mcf_gray));
            mSelectionColor = a.getColor(R.styleable.MCFStepView_mcf_selectionColor, Color.GRAY);
            mSelectionMaxLines = a.getInteger(R.styleable.MCFStepView_mcf_selectionMaxLines, 1);
            mSelectionSize = a.getDimension(R.styleable.MCFStepView_mcf_selectionSize, getResources().getDimension(R.dimen.mcf_text_size_default));
            mArrowDrawable = a.getDrawable(R.styleable.MCFStepView_mcf_arrowDrawable);
            isEnabled = a.getBoolean(R.styleable.MCFStepView_mcf_enabled, true);
            mDisabledSeparatorColor = a.getColor(R.styleable.MCFStepView_mcf_disabledSeparatorColor,
                    ContextCompat.getColor(mContext, R.color.mcf_disabled));
            mDisabledTitleColor = a.getColor(R.styleable.MCFStepView_mcf_disabledTitleColor,
                    ContextCompat.getColor(mContext, R.color.mcf_gray));
            mDisabledSelectionColor = a.getColor(R.styleable.MCFStepView_mcf_disabledSelectionColor,
                    ContextCompat.getColor(mContext, R.color.mcf_gray));

            if (mArrowDrawable == null)
                mArrowDrawable = ContextCompat.getDrawable(mContext, R.drawable.mcf_ic_action_arrow);
        } finally {
            a.recycle();
        }

        mLayout = findViewById(R.id.layout);
        mTitleTextView = findViewById(R.id.title);
        mSelectionTextView = findViewById(R.id.selection);
        mArrowImageView = findViewById(R.id.image);
        mSeparator = findViewById(R.id.separator);

        mSelection = "";

        setTitle(mTitle);
        setTitleColor(mTitleColor);
        setTitleSize(mTitleSize);
        setTitleMaxLines(mTitleMaxLines);
        setSelectionColor(mSelectionColor);
        setSelectionMaxLines(mSelectionMaxLines);
        setSelectionSize(mSelectionSize);
        setSeparatorColor(mSeparatorColor);
        setArrowImageView(mArrowDrawable);
        setDisabledSeparatorColor(mDisabledSeparatorColor);
        setDisabledTitleColor(mDisabledTitleColor);
        setDisabledSelectionColor(mDisabledSelectionColor);
        setEnabled(isEnabled);
    }

    /**
     * Sets the MCFStepView's title.
     *
     * @param title Title of the MCFStepView.
     */
    public void setTitle(String title) {
        mTitle = title;
        mTitleTextView.setText(title);

        invalidate();
        requestLayout();
    }

    /**
     * Returns the title of the step.
     *
     * @return Title of the MCFStepView.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Sets the selected option text.
     *
     * @param selection Selected option text value.
     */
    public void setSelection(String selection) {
        mSelection = selection;

        if (isMasked) selection = selection.replaceAll(".", "•");
        mSelectionTextView.setText(selection);

        invalidate();
        requestLayout();
    }

    /**
     * Sets the selected options text from a string resource
     *
     * @param resId String resource
     */
    public void setSelection(@StringRes int resId) {
        setSelection(mContext.getString(resId));
    }

    /**
     * Returns the selected value.
     *
     * @return Current selection of the MCFStepView.
     */
    public String getSelection() {
        return mSelection;
    }

    /**
     * Sets the right arrow image Drawable.
     *
     * @param drawable Any Drawable.
     */
    public void setArrowImageView(Drawable drawable) {
        mArrowDrawable = drawable;
        mArrowImageView.setBackground(drawable);
    }

    /**
     * Returns the right arrow ImageView.
     *
     * @return MCFStepView's arrow ImageView.
     */
    public ImageView getArrowImageView() {
        return mArrowImageView;
    }

    /**
     * Returns the view layout.
     *
     * @return MCFStepView's root.
     */
    public LinearLayout getLayout() {
        return mLayout;
    }

    /**
     * Returns the selection TextView.
     *
     * @return MCFStepView's selection TextView.
     */
    public TextView getSelectionView() {
        return mSelectionTextView;
    }

    /**
     * Returns the title TextView.
     *
     * @return MCFStepView's title TextView.
     */
    public TextView getTitleView() {
        return mTitleTextView;
    }

    /**
     * Sets MCFStep's title color.
     *
     * @param color Any color.
     */
    public void setTitleColor(int color) {
        mTitleColor = color;
        mTitleTextView.setTextColor(color);
    }

    /**
     * Returns MCFStep's title color.
     *
     * @return MCFStep's title color.
     */
    public int getTitleColor() {
        return mTitleColor;
    }

    /**
     * Sets MCFStep's selection color.
     *
     * @param color Any color.
     */
    public void setSelectionColor(int color) {
        mSelectionColor = color;
        mSelectionTextView.setTextColor(color);
    }

    /**
     * Returns MCFStep's selection color.
     *
     * @return MCFStep's selection color.
     */
    public int getSelectionColor() {
        return mSelectionColor;
    }

    /**
     * Sets MCFStep's separator color.
     *
     * @param color Any color.
     */
    public void setSeparatorColor(int color) {
        mSeparatorColor = color;
        mSeparator.setBackgroundColor(color);
    }

    /**
     * Returns MCFStep's separator color.
     *
     * @return MCFStep's separator color.
     */
    public int getSeparatorColor() {
        return mSeparatorColor;
    }

    /**
     * Checks if the MCFStep has been selected.
     *
     * @return Whether it is selected.
     */
    public boolean isSelected() {
        return getSelection() != null && getSelection().length() > 0;
    }

    /**
     * Used to enable or disable completely the MCFStep.
     *
     * @param enable Whether to enable it.
     */
    @Override
    public void setEnabled(boolean enable) {
        isEnabled = enable;

        if (enable) {
            mLayout.setEnabled(true);
            ((View) mArrowImageView.getParent()).setVisibility(View.VISIBLE);
            mTitleTextView.setTextColor(mTitleColor);
            mSelectionTextView.setTextColor(mSelectionColor);
            mSeparator.setBackgroundColor(mSeparatorColor);
        } else {
            mLayout.setEnabled(false);
            ((View) mArrowImageView.getParent()).setVisibility(View.GONE);
            mTitleTextView.setTextColor(mDisabledTitleColor);
            mSelectionTextView.setTextColor(mDisabledSelectionColor);
            mSeparator.setBackgroundColor(mDisabledSeparatorColor);
        }

        invalidate();
        requestLayout();
    }

    /**
     * @return Whether the MCFStepView is enabled.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Removes the current selection of the MCFStepView. Also, enables or disables it.
     * This can be used to deselect a dependent MCFStep if its parent has been selected (changed
     * data dependency).
     *
     * @param enable Whether to enable the MCFStepView.
     */
    public void deselect(boolean enable) {
        setSelection("");
        setEnabled(enable);
    }

    /**
     * Removes the current selection of the MCFStepView and disables it.
     */
    public void deselect() {
        deselect(false);
    }

    /**
     * Sets MCFStep's title max lines; with default 1
     *
     * @param maxLines title TextView maxLines attribute
     */
    public void setTitleMaxLines(int maxLines) {
        mTitleMaxLines = maxLines;

        mTitleTextView.setMaxLines(maxLines);

        invalidate();
        requestLayout();
    }

    /**
     * Returns the max lines of title TextView.
     *
     * @return MCFStepView's title TextView max Lines.
     */
    public int getTitleMaxLines() {
        return mTitleMaxLines;
    }

    /**
     * Sets MCFStep's title size; with default 16sp
     *
     * @param size title TextView textSize attribute
     */
    public void setTitleSize(@Dimension float size) {
        mTitleSize = size;

        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        invalidate();
        requestLayout();
    }

    /**
     * Returns the size of title TextView.
     *
     * @return MCFStepView's title TextView size.
     */
    public float getTitleSize() {
        return mTitleSize;
    }

    /**
     * Sets MCFStep's selection max lines; with default 1
     *
     * @param maxLines selection TextView maxLines attribute
     */
    public void setSelectionMaxLines(int maxLines) {
        mSelectionMaxLines = maxLines;

        mSelectionTextView.setMaxLines(maxLines);

        invalidate();
        requestLayout();
    }

    /**
     * Sets MCFStep's selection size; with default 16sp
     *
     * @param size selection TextView textSize attribute
     */
    public void setSelectionSize(@Dimension float size) {
        mSelectionSize = size;

        mSelectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        invalidate();
        requestLayout();
    }

    /**
     * Returns the size of selection TextView.
     *
     * @return MCFStepView's selection TextView size.
     */
    public float getSelectionSize() {
        return mTitleSize;
    }

    /**
     * Returns the max lines of selection TextView.
     *
     * @return MCFStepView's selection TextView max Lines.
     */
    public int getSelectionMaxLines() {
        return mSelectionMaxLines;
    }

    /**
     * Returns whether the selection text is masked (for passwords).
     *
     * @return Whether the selection text is masked.
     */
    public boolean isMasked() {
        return isMasked;
    }

    /**
     * Sets whether the selection text is masked (for passwords).
     *
     * @param masked Whether the selection text is masked.
     */
    public void setMasked(boolean masked) {
        this.isMasked = masked;
    }

    /**
     * Sets MCFStep's disabled title color.
     *
     * @param color Any color.
     */
    public void setDisabledTitleColor(int color) {
        mDisabledTitleColor = color;
        setEnabled(isEnabled);
    }

    /**
     * Returns MCFStep's disabled title color.
     *
     * @return MCFStep's disabled title color.
     */
    public int getDisabledTitleColor() {
        return mDisabledTitleColor;
    }

    /**
     * Sets MCFStep's disabled selection color.
     *
     * @param color Any color.
     */
    public void setDisabledSelectionColor(int color) {
        mDisabledSelectionColor = color;
        setEnabled(isEnabled);
    }

    /**
     * Returns MCFStep's disabled selection color.
     *
     * @return MCFStep's disabled selection color.
     */
    public int getDisabledSelectionColor() {
        return mDisabledTitleColor;
    }

    /**
     * Sets MCFStep's disabled separator color.
     *
     * @param color Any color.
     */
    public void setDisabledSeparatorColor(int color) {
        mDisabledSeparatorColor = color;
        setEnabled(isEnabled);
    }

    /**
     * Returns MCFStep's disabled separator color.
     *
     * @return MCFStep's disabled separator color.
     */
    public int getDisabledSeparatorColor() {
        return mDisabledSeparatorColor;
    }
}
