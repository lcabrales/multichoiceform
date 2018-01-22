package com.hypernovalabs.multichoiceform.form;

import com.hypernovalabs.multichoiceform.view.MCFStepView;

import java.util.ArrayList;

/**
 * Single Select Form Step. Provides a list of options and returns only one selected option.
 * Child of MCFStep.
 */

public class MCFSingleSelectStep extends MCFStep {

    /**
     * Simple constructor, sets the parent's type of SINGLE_SELECT.
     *
     * @param data Options data.
     * @param view Associated MCFStepView.
     */
    public MCFSingleSelectStep(ArrayList<String> data, MCFStepView view) {
        super(data, view);
        super.setType(MCFStepType.SINGLE_SELECT);
    }

    /**
     * Constructor with the "required" parameter.
     *
     * @param data     Options data.
     * @param view     Associated MCFStepView.
     * @param required whether the MCFStep is required.
     */
    public MCFSingleSelectStep(ArrayList<String> data, MCFStepView view, boolean required) {
        super(data, view, required);
        super.setType(MCFStepType.SINGLE_SELECT);
    }
}
