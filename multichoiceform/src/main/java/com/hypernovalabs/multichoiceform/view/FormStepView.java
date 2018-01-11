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
 * Holds the definition for the FormStep multi choice view
 */
public class FormStepView extends LinearLayout {

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
     * Single constructor for a custom view
     *
     * @param context - any context, activity recommended
     * @param attrs   - view custom attritubes
     */
    public FormStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.form_step_item, this, true);

        mContext = context;
        mAttrs = attrs;

        init();
    }

    /**
     * Initializes the custom view and sets the defined attributes
     */
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
            mArrowDrawable = a.getDrawable(R.styleable.FormStepView_arrowDrawable);
            mEnabled = a.getBoolean(R.styleable.FormStepView_enabled, true);

            if (mArrowDrawable == null)
                mArrowDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_action_arrow);
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
        enable(mEnabled);
    }

    /**
     * Sets the FormStep title
     *
     * @param title - text value
     */
    public void setTitle(String title) {
        mTitleTextView.setText(title);

        invalidate();
        requestLayout();
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     * Sets on UI the selected option text
     *
     * @param selection - text value
     */
    public void setSelection(String selection) {
        mSelection = selection;
        mSelectionTextView.setText(selection);

        invalidate();
        requestLayout();
    }

    public String getSelection() {
        return mSelection;
    }

    public void setArrowImageView(Drawable drawable) {
        mArrowImageView.setBackground(drawable);
    }

    public ImageView getArrowImageView() {
        return mArrowImageView;
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

    /**
     * Checks if the FormStep has been selected
     *
     * @return
     */
    public boolean isSelected() {
        return getSelection().length() > 0;
    }

    /**
     * Used to enable or disable completely the FormStep
     *
     * @param enable - true     -> enables it
     *               - false    -> disables it
     */
    public void enable(boolean enable) {
        mEnabled = enable;

        if (enable) {
            setEnabled(true);
            mArrowImageView.setVisibility(View.VISIBLE);
            mSeparator.setBackgroundColor(mSeparatorColor);
        } else {
            setEnabled(false);
            mArrowImageView.setVisibility(View.GONE);
            mSeparator.setBackgroundColor(mDisabledColor);
        }
    }

    public boolean isEnabled() {
        return mEnabled;
    }
}
