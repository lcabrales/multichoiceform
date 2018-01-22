package com.hypernovalabs.multichoiceform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
 * <p>
 * Holds the definition for the MCFStepView.
 */
public class MCFStepView extends LinearLayout {

    private final AttributeSet mAttrs;
    private Context mContext;
    private LinearLayout mLayout;
    private TextView mTitleTextView;
    private TextView mSelectionTextView;
    private ImageView mArrowImageView;
    private View mSeparator;
    private Drawable mArrowDrawable;
    private String mTitle;
    private String mSelection;
    private int mSeparatorColor;
    private int mTitleColor;
    private int mSelectionColor;
    private int mDisabledColor;
    private boolean mEnabled;

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
        mDisabledColor = ContextCompat.getColor(mContext, R.color.mcf_disabled);

        TypedArray a = mContext.getTheme().obtainStyledAttributes(
                mAttrs,
                R.styleable.MCFStepView,
                0, 0);

        try {
            mTitle = a.getString(R.styleable.MCFStepView_mcf_title);
            mSeparatorColor = a.getColor(R.styleable.MCFStepView_mcf_separatorColor,
                    ContextCompat.getColor(mContext, R.color.mcf_gray));
            mTitleColor = a.getColor(R.styleable.MCFStepView_mcf_titleColor, Color.BLACK);
            mSelectionColor = a.getColor(R.styleable.MCFStepView_mcf_selectionColor, Color.BLACK);
            mArrowDrawable = a.getDrawable(R.styleable.MCFStepView_mcf_arrowDrawable);
            mEnabled = a.getBoolean(R.styleable.MCFStepView_mcf_enabled, true);

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
        setSelectionColor(mSelectionColor);
        setSeparatorColor(mSeparatorColor);
        setArrowImageView(mArrowDrawable);
        setEnabled(mEnabled);
    }

    /**
     * Sets the MCFStepView's title.
     *
     * @param title Title of the MCFStepView.
     */
    public void setTitle(String title) {
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
     * Sets on UI the selected option text.
     *
     * @param selection Selected option text value.
     */
    public void setSelection(String selection) {
        mSelection = selection;
        mSelectionTextView.setText(selection);

        invalidate();
        requestLayout();
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
        mTitleTextView.setTextColor(color);
    }

    /**
     * Sets MCFStep's selection color.
     *
     * @param color Any color.
     */
    public void setSelectionColor(int color) {
        mSelectionTextView.setTextColor(color);
    }

    /**
     * Sets MCFStep's separator color.
     *
     * @param color Any color.
     */
    public void setSeparatorColor(int color) {
        mSeparator.setBackgroundColor(color);
    }

    /**
     * Checks if the MCFStep has been selected.
     *
     * @return Whether it is selected.
     */
    public boolean isSelected() {
        return getSelection().length() > 0;
    }

    /**
     * Used to enable or disable completely the MCFStep.
     *
     * @param enable Whether to enable it.
     */
    @Override
    public void setEnabled(boolean enable) {
        mEnabled = enable;

        if (enable) {
            mLayout.setEnabled(true);
            mArrowImageView.setVisibility(View.VISIBLE);
            mSeparator.setBackgroundColor(mSeparatorColor);
        } else {
            mLayout.setEnabled(false);
            mArrowImageView.setVisibility(View.GONE);
            mSeparator.setBackgroundColor(mDisabledColor);
        }

        invalidate();
        requestLayout();
    }

    /**
     * @return Whether the MCFStepView is enabled.
     */
    public boolean isEnabled() {
        return mEnabled;
    }

    /**
     * Removes the current selection of the MCFStepView. Also, enables or disables it.
     * This can be used to deselect a dependent MCFStep if its parent has been selected (changed
     * data dependency).
     *
     * @param enable Whether to enable the MCFStepView.
     */
    public void deselect(boolean enable) {
        setSelection(null);
        setEnabled(enable);
    }

    /**
     * Removes the current selection of the MCFStepView and disables it.
     */
    public void deselect() {
        deselect(false);
    }
}
