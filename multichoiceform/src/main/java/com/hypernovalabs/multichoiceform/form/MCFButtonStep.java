package com.hypernovalabs.multichoiceform.form;

import android.support.annotation.NonNull;

import com.hypernovalabs.multichoiceform.MultiChoiceForm;
import com.hypernovalabs.multichoiceform.view.MCFStepView;

/**
 * Created by ldemorais on 7/6/18. ldemorais@hypernovalabs.com <p>This {@link MCFStep} acts as a
 * visual button only, without present any options. The app must set the {@link
 * android.view.View.OnClickListener} for this step. Allows a visual step to be added to {@link
 * MultiChoiceForm#mMCFSteps}, this way it can be checked for {@link #required} with {@link
 * MultiChoiceForm#validate()}.</p>
 */
public class MCFButtonStep extends MCFStep {

    /**
     * Simple constructor, sets the parent's type of {@link MCFStep#BUTTON}.
     *
     * @param view Associated MCFStepView.
     */
    public MCFButtonStep(@NonNull MCFStepView view) {
        super(view);
        super.setType(MCFStep.BUTTON);
    }

    /**
     * Constructor with the "required" parameter.
     *
     * @param view     Associated MCFStepView.
     * @param required Whether the MCFStep is required.
     */
    public MCFButtonStep(@NonNull MCFStepView view, @NonNull boolean required) {
        super(view, required);
        super.setType(MCFStep.BUTTON);
    }
}
