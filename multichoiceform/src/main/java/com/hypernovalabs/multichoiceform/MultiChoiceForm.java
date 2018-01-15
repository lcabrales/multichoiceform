package com.hypernovalabs.multichoiceform;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernovalabs.multichoiceform.form.FormDateStep;
import com.hypernovalabs.multichoiceform.form.FormSingleSelectStep;
import com.hypernovalabs.multichoiceform.form.FormStep;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Main helper of the library. Holds a form instance, including all of the FormSteps.
 */
public class MultiChoiceForm {
    public static final int REQ_SELECTION = 27;

    private Activity mContext;
    private ArrayList<? extends FormStep> mFormSteps;
    private int mToolbarBackgroundColor, mToolbarTitleColor;
    private int mValidationColor;
    private Duration mValidationDuration;
    private ValidationAnim mValidationAnim;
    private String mRequiredText;
    private String mEmptyViewTitle, mEmptyViewMsg;

    //TODO DESELECT MANUALLY (END CLIENT) (v0.5.0)
    //TODO MAKE ARRAY LIST INTERNAL (v0.5.0)
    //TODO CREATE A PARENT FOR FormStep TO HANDLE OTHER TYPE OF VIEWS (v0.6.0)
    //TODO CREATE A WIKI (DOCUMENTATION) PAGE (v0.6.0)

    /**
     * Builder class of MultiChoiceForm.
     */
    public static class Builder {
        private MultiChoiceForm form;

        /**
         * Constructor of the Builder class.
         *
         * @param context Mandatory activity Context.
         */
        public Builder(AppCompatActivity context) {
            form = new MultiChoiceForm();
            form.mContext = context;
            form.mValidationColor = R.color.redt2;
            form.mValidationAnim = ValidationAnim.SHAKE_HORIZONTAL;
            form.mValidationDuration = Duration.MEDIUM;
            form.mEmptyViewTitle = context.getString(R.string.form_empty_view_title);
            form.mEmptyViewMsg = context.getString(R.string.form_empty_view_msg);
        }

        /**
         * Builds a MultiChoiceForm instance.
         *
         * @return MultiChoiceForm instance
         */
        public MultiChoiceForm build() {
            MultiChoiceForm builtForm = form;
            form = new MultiChoiceForm();

            return builtForm;
        }

        /**
         * Sets all of the steps to be handled.
         *
         * @param steps FormSteps to be handled.
         * @return Current instance of Builder.
         */
        public Builder setSteps(ArrayList<FormStep> steps) {
            form.mFormSteps = steps;
            return this;
        }

        /**
         * Sets both of OptionsActivity Toolbar colors.
         *
         * @param backgroundColor Toolbar background color.
         * @param textColor       Toolbar title color.
         * @return Current instance of Builder.
         */
        public Builder setToolbarColors(int backgroundColor, int textColor) {
            form.mToolbarBackgroundColor = backgroundColor;
            form.mToolbarTitleColor = textColor;
            return this;
        }

        /**
         * Sets the text to be shown in a Toast in case the validation fails.
         *
         * @param text Required text value.
         * @return Current instance of Builder.
         */
        public Builder setRequiredText(String text) {
            form.mRequiredText = text;
            return this;
        }

        /**
         * Sets the validation animation main color.
         *
         * @param color Any color, 50% alpha color is recommended.
         * @return Current instance of Builder.
         */
        public Builder setValidationColor(int color) {
            form.mValidationColor = color;
            return this;
        }

        /**
         * Sets the animation of the validation
         *
         * @param validationAnim Enum value of ValidationAnim.
         * @return Current instance of Builder.
         */
        public Builder setValidationAnimation(ValidationAnim validationAnim) {
            this.form.mValidationAnim = validationAnim;
            return this;
        }

        /**
         * Sets the duration of the validation animation.
         *
         * @param duration Enum value of Duration.
         * @return Current instance of Builder.
         */
        public Builder setValidationDuration(Duration duration) {
            this.form.mValidationDuration = duration;
            return this;
        }

        /**
         * If a FormStep does not have any data associated with it, it will show an empty view
         * when prompted. This method defines both the title and the message that will show on the
         * empty view.
         *
         * @param title   Title of EmptyView.
         * @param message Message of EmptyView.
         * @return Current instance of Builder.
         */
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

                switch (step.getType()) {
                    case SINGLE_SELECT:
                        Intent intent = getIntent((FormSingleSelectStep) step);
                        mContext.startActivityForResult(intent, REQ_SELECTION);
                        mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case DATE:
                        handleDateStep((FormDateStep) step);
                        break;
                }

            }
        };

        View.OnClickListener titleListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tooltip(view);
            }
        };

        for (FormStep step : mFormSteps) {
            step.getView().getLayout().setOnClickListener(listener);
            step.getView().getLayout().setTag(step);

            step.getView().getTitleView().setOnClickListener(titleListener);
        }
    }

    /**
     * Returns the intent related to OptionsActivity with all the needed parameters.
     *
     * @param step Associated FormSingleSelectStep.
     * @return Intent to OptionsActivity.
     */
    private Intent getIntent(FormSingleSelectStep step) {
        Intent intent = new Intent(mContext, OptionsActivity.class);

        ExtraModel model = new ExtraModel();
        model.data = step.getData();
        model.selection = step.getView().getSelection();
        model.id = step.getView().getId();
        model.title = step.getView().getTitle();
        model.toolbarBackgroundColor = mToolbarBackgroundColor;
        model.toolbarTitleColor = mToolbarTitleColor;
        model.emptyViewTitle = mEmptyViewTitle;
        model.emptyViewMsg = mEmptyViewMsg;

        intent.putExtra(OptionsActivity.EXTRA_MODEL_KEY, model);

        return intent;
    }

    /**
     * Creates an AlertDialog with a DatePicker. Buttons' texts are customizable.
     * MinDate and MaxDate also customizable.
     *
     * @param step Associated FormDateStep
     */
    private void handleDateStep(FormDateStep step) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(step.getView().getTitle());

        builder.setView(R.layout.picker_date);

        builder.setPositiveButton(step.getPositiveButton(), null);
        builder.setNegativeButton(step.getNegativeButton(), null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        DatePicker datePicker = dialog.findViewById(R.id.date_picker);
        if (datePicker != null) {
            if (step.getMinDate() != null)
                datePicker.setMinDate(step.getMinDate().getTime());

            if (step.getMaxDate() != null)
                datePicker.setMaxDate(step.getMaxDate().getTime());
        }

        Button btnAccept = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnAccept.setTag(R.id.dialog, dialog);
        btnAccept.setTag(R.id.date_picker, datePicker);
        btnAccept.setTag(R.id.form_step, step);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = (AlertDialog) view.getTag(R.id.dialog);
                DatePicker datePicker = (DatePicker) view.getTag(R.id.date_picker);
                FormDateStep step = (FormDateStep) view.getTag(R.id.form_step);

                String date = Utils.getDateFromInteger(
                        datePicker.getDayOfMonth(),
                        datePicker.getMonth(),
                        datePicker.getYear(),
                        step.getDateFormat());

                step.getView().setSelection(date);

                dialog.dismiss();
            }
        });
    }

    /**
     * Shows a Toast with the full name of the FormStep's title.
     *
     * @param view FormStep view.
     */
    private void tooltip(View view) {
        String text = ((TextView) view).getText().toString();
        if (text.length() > 0)
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the OptionsActivity onResult.
     * Edit the FormStep UI to show the selected option.
     *
     * @param requestCode onActivityResult requestCode.
     * @param resultCode  onActivityResult resultCode.
     * @param data        onActivityResult data.
     */
    public void handleActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (requestCode == REQ_SELECTION && resultCode == RESULT_OK) {
            String selection = data.getStringExtra(OptionsActivity.SELECTION_KEY);
            int id = data.getIntExtra(OptionsActivity.ID_KEY, 0);
            if (id != 0) {
                FormStep step = FormStep.getStepFromId((ArrayList<FormStep>) mFormSteps, id);
                if (step != null) {
                    step.getView().setSelection(selection);
                }
            }
        }
    }

    /**
     * Validate all of the required FormSteps, they cannot be empty.
     *
     * @return Whether all of the required FormSteps are selected.
     */
    public boolean validate() {
        boolean trust = true;

        for (FormStep formStep : mFormSteps) {
            if (formStep.isRequired() && !formStep.getView().isSelected()) {
                if (mRequiredText != null)
                    Toast.makeText(mContext, mRequiredText, Toast.LENGTH_SHORT).show();

                Animation animation = AnimationUtils.loadAnimation(mContext, mValidationAnim.getResId());

                ObjectAnimator
                        .ofObject(
                                formStep.getView(),
                                "backgroundColor",
                                new ArgbEvaluator(),
                                mValidationColor,
                                ContextCompat.getColor(mContext, R.color.transparent))
                        .setDuration(mValidationDuration.getDuration())
                        .start();

                formStep.getView().startAnimation(animation);

                trust = false;
            }
        }

        return trust;
    }
}