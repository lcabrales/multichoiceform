package com.hypernovalabs.multichoiceform.form;

import android.support.annotation.NonNull;

import com.hypernovalabs.multichoiceform.view.MCFStepView;

import java.util.ArrayList;

/**
 * Single Select Form Step. Provides a list of options and returns only one selected option.
 * Child of MCFStep.
 */

public class MCFSingleSelectStep extends MCFStep {

    private boolean isSearchable = false;

    /**
     * Simple constructor, sets the parent's type of SINGLE_SELECT. Takes an array as parameter.
     *
     * @param data Options data.
     * @param view Associated MCFStepView.
     */
    public MCFSingleSelectStep(@NonNull String[] data, @NonNull MCFStepView view) {
        super(data, view);
        super.setType(MCFStepType.SINGLE_SELECT);
    }

    /**
     * Simple constructor, sets the parent's type of SINGLE_SELECT.
     *
     * @param data Options data.
     * @param view Associated MCFStepView.
     */
    public MCFSingleSelectStep(@NonNull ArrayList<String> data, @NonNull MCFStepView view) {
        super(data, view);
        super.setType(MCFStepType.SINGLE_SELECT);
    }

    /**
     * Constructor with the "required" parameter. Takes an array as parameter.
     *
     * @param data     Options data.
     * @param view     Associated MCFStepView.
     * @param required whether the MCFStep is required.
     */
    public MCFSingleSelectStep(@NonNull String[] data, @NonNull MCFStepView view, @NonNull boolean required) {
        super(data, view, required);
        super.setType(MCFStepType.SINGLE_SELECT);
    }

    /**
     * Constructor with the "required" parameter.
     *
     * @param data     Options data.
     * @param view     Associated MCFStepView.
     * @param required whether the MCFStep is required.
     */
    public MCFSingleSelectStep(@NonNull ArrayList<String> data, @NonNull MCFStepView view, @NonNull boolean required) {
        super(data, view, required);
        super.setType(MCFStepType.SINGLE_SELECT);
    }

    /**
     *
     * @return whether the step implements a SearchView
     */
    public boolean isSearchable() {
        return isSearchable;
    }

    /**
     * Sets whether the step implements a SearchView
     * @param searchable whether the step implements a SearchView
     */
    public void setSearchable(boolean searchable) {
        isSearchable = searchable;
    }
}
