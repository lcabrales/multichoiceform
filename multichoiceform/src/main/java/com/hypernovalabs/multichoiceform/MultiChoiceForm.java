package com.hypernovalabs.multichoiceform;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernovalabs.multichoiceform.model.ExtraModel;
import com.hypernovalabs.multichoiceform.model.FormStep;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ldemorais on 04/04/2017.
 * <p>
 * Main helper of the library. Holds a form instance, including all of the FormSteps.
 */
public class MultiChoiceForm {
    public static final int REQ_SELECTION = 27;

    private Activity mContext;
    private ArrayList<FormStep> mFormSteps;
    private int mToolbarBackgroundColor, mToolbarTitleColor;
    private int mValidationColor;
    private Duration mValidationDuration;
    private String mRequiredText;
    private String mEmptyViewTitle, mEmptyViewMsg;

    //TODO IMPROVE GITHUB DOCUMENTATION
    //TODO MAVEN - GRADLE TUTORIAL

    /**
     * Builder class of FormStep
     */
    public static class Builder {
        private MultiChoiceForm form;

        public Builder(AppCompatActivity context) {
            form = new MultiChoiceForm();
            form.mContext = context;
            form.mValidationColor = R.color.redt2;
            form.mValidationDuration = Duration.MEDIUM;
            form.mEmptyViewTitle = context.getString(R.string.form_empty_view_title);
            form.mEmptyViewMsg = context.getString(R.string.form_empty_view_msg);
        }

        public MultiChoiceForm build() {
            MultiChoiceForm builtForm = form;
            form = new MultiChoiceForm();

            return builtForm;
        }

        public Builder setSteps(ArrayList<FormStep> steps) {
            form.mFormSteps = steps;
            return this;
        }

        public Builder setToolbarColors(int backgroundColor, int textColor) {
            form.mToolbarBackgroundColor = backgroundColor;
            form.mToolbarTitleColor = textColor;
            return this;
        }

        public Builder setRequiredText(String text) {
            form.mRequiredText = text;
            return this;
        }

        public Builder setValidationColor(int color) {
            form.mValidationColor = color;
            return this;
        }

        public Builder setValidationDuration(Duration duration) {
            this.form.mValidationDuration = duration;
            return this;
        }

        public Builder setEmptyViewTexts(String title, String message) {
            this.form.mEmptyViewTitle = title;
            this.form.mEmptyViewMsg = message;
            return this;
        }
    }

    /**
     * Defines the necessary listeners for the FormStep, options listener and title tooltip listener.
     * It has to be called after building the form in order for it to be enabled.
     * Otherwise, form would not be enabled.
     */
    public void setupForm() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormStep step = (FormStep) view.getTag();

                Intent intent = new Intent(mContext, SelectionsActivity.class);

                ExtraModel model = new ExtraModel();
                model.data = step.getData();
                model.selection = step.getView().getSelection();
                model.id = step.getView().getId();
                model.title = step.getView().getTitle();
                model.toolbarBackgroundColor = mToolbarBackgroundColor;
                model.toolbarTitleColor = mToolbarTitleColor;
                model.emptyViewTitle = mEmptyViewTitle;
                model.emptyViewMsg = mEmptyViewMsg;

                intent.putExtra(SelectionsActivity.EXTRA_MODEL_KEY, model);

                mContext.startActivityForResult(intent, REQ_SELECTION);

                mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };

        View.OnClickListener titleListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tooltip(view);
            }
        };

        for (FormStep step : mFormSteps) {
            step.getView().setOnClickListener(listener);
            step.getView().setTag(step);

            step.getView().getTitleView().setOnClickListener(titleListener);
        }
    }

    public boolean setStepData(FormStep step, ArrayList<String> data) {
        if (mFormSteps.contains(step)) {
            for (FormStep s : mFormSteps) {
                if (s.equals(step)) {
                    s.setData(data);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Handles the SelectionsActivity onResult.
     * Edit the FormStep UI to show the selected option
     *
     * @param requestCode - onActivityResult requestCode
     * @param resultCode  - onActivityResult resultCode
     * @param data        - onActivityResult data
     */
    public void handleActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (requestCode == REQ_SELECTION && resultCode == RESULT_OK) {
            String selection = data.getStringExtra(SelectionsActivity.SELECTION_KEY);
            int id = data.getIntExtra(SelectionsActivity.ID_KEY, 0);
            if (id != 0) {
                FormStep step = FormStep.getStepFromId(mFormSteps, id);
                if (step != null) {
                    step.getView().setSelection(selection);
                }
            }
        }
    }

    /**
     * Shows a Toast with the full name of the FormStep's title.
     *
     * @param view - FormStep view
     */
    private void tooltip(View view) {
        Toast.makeText(mContext, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Validate all of the required FormSteps, they cannot be empty.
     *
     * @return true - all of the required FormSteps are selected
     * false - at least one required FormStep is not selected
     */
    public boolean validate() {
        for (FormStep formStep : mFormSteps) {
            if (formStep.isRequired() && !formStep.getView().isSelected()) {
                if (mRequiredText != null)
                    Toast.makeText(mContext, mRequiredText, Toast.LENGTH_SHORT).show();

                Animation shakeAnimation = AnimationUtils.loadAnimation(mContext, R.anim.shake_horizontal);

                ObjectAnimator
                        .ofObject(
                                formStep.getView(),
                                "backgroundColor",
                                new ArgbEvaluator(),
                                mValidationColor,
                                ContextCompat.getColor(mContext, R.color.transparent))
                        .setDuration(mValidationDuration.getDuration())
                        .start();

                formStep.getView().startAnimation(shakeAnimation);

                return false;
            }
        }

        return true;
    }
}