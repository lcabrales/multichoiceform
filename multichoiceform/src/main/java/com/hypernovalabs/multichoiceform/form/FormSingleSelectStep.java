package com.hypernovalabs.multichoiceform.form;

import com.hypernovalabs.multichoiceform.view.FormStepView;

import java.util.ArrayList;

/**
 * Single Select Form Step. Provides a list of options and returns only one selected option.
 * Child of FormStep.
 */

public class FormSingleSelectStep extends FormStep {

    /**
     * Simple constructor, sets the parent's type of SINGLE_SELECT.
     *
     * @param data Options data.
     * @param view Associated FormStepView.
     */
    public FormSingleSelectStep(ArrayList<String> data, FormStepView view) {
        super(data, view);
        super.setType(FormStepType.SINGLE_SELECT);
    }

    /**
     * Constructor with the "required" parameter.
     *
     * @param data     Options data.
     * @param view     Associated FormStepView.
     * @param required whether the FormStep is required.
     */
    public FormSingleSelectStep(ArrayList<String> data, FormStepView view, boolean required) {
        super(data, view, required);
        super.setType(FormStepType.SINGLE_SELECT);
    }
}
