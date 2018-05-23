package com.hypernovalabs.multichoiceform.form;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;

import com.hypernovalabs.multichoiceform.model.Regex;
import com.hypernovalabs.multichoiceform.view.MCFStepView;

/**
 * Created by ldemorais on 5/23/18.
 * ldemorais@hypernovalabs.com
 * <p>
 * Provides a new activity with a single {@link TextInputLayout} and an optional explanatory text.
 * Also performs a Regex validation on the {@link TextInputEditText} if required.
 * </p>
 */
public class MCFTextInputStep extends MCFStep {

    private Regex mRegex;
    private int mInputType;
    private int mMaxLength;
    private String mExplanatoryText;

    /**
     * @param view     MCFStepView associated with this step.
     * @param required Whether the step is required.
     */
    public MCFTextInputStep(@NonNull MCFStepView view, @NonNull boolean required) {
        super(view, required);
        super.setType(MCFStepType.TEXT_INPUT);

        mInputType = InputType.TYPE_CLASS_TEXT;
    }

    /**
     * @param view     MCFStepView associated with this step.
     * @param required Whether the step is required.
     * @param regex    Regular expression object associated with this step.
     */
    public MCFTextInputStep(@NonNull MCFStepView view, @NonNull boolean required, Regex regex) {
        super(view, required);
        super.setType(MCFStepType.TEXT_INPUT);

        mInputType = InputType.TYPE_CLASS_TEXT;
        mRegex = regex;
    }

    /**
     * Returns the {@link Regex} object associated with this step.
     *
     * @return Regex object associated with this step.
     */
    public Regex getRegex() {
        return mRegex;
    }

    /**
     * Sets the {@link Regex} object associated with this step.
     *
     * @param regex Regex object associated with this step.
     */
    public void setRegex(Regex regex) {
        this.mRegex = regex;
    }

    /**
     * Returns the {@link InputType} associated with this step. Default is {@link InputType#TYPE_CLASS_TEXT}
     *
     * @return InputType associated with this step.
     */
    public int getInputType() {
        return mInputType;
    }

    /**
     * Sets the {@link InputType} associated with this step.
     *
     * @param inputType InputType associated with this step.
     */
    public void setInputType(int inputType) {
        mInputType = inputType;
    }

    /**
     * Returns the maximum length for this step's input.
     *
     * @return Maximum length for this step's input.
     */
    public int getMaxLength() {
        return mMaxLength;
    }

    /**
     * Sets the maximum length for this step's input.
     *
     * @param maxLength Maximum length for this step's input.
     */
    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
    }

    /**
     * Returns the explanatory text shown in the input activity.
     *
     * @return Explanatory text shown in the input activity.
     */
    public String getExplanatoryText() {
        return mExplanatoryText;
    }

    /**
     * Sets the explanatory text shown in the input activity.
     *
     * @param explanatoryText Explanatory text shown in the input activity.
     */
    public void setExplanatoryText(String explanatoryText) {
        this.mExplanatoryText = explanatoryText;
    }
}
