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

import com.hypernovalabs.multichoiceform.form.MCFDateStep;
import com.hypernovalabs.multichoiceform.form.MCFSingleSelectStep;
import com.hypernovalabs.multichoiceform.form.MCFStep;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Main helper of the library. Holds a form instance, including all of the {@link MCFStep}.
 */
public class MultiChoiceForm {
    public static final int REQUEST_SELECTION = 27;

    private Activity mContext;
    private ArrayList<? extends MCFStep> mMCFSteps;
    private int mToolbarBackgroundColor, mToolbarTitleColor;
    private int mValidationColor;
    private Duration mValidationDuration;
    private ValidationAnim mValidationAnim;
    private String mRequiredText;
    private String mEmptyViewTitle, mEmptyViewMsg;
    private String mSearchViewHint;
    private int mSearchViewIconTint;
    private Toast mToast;

    /**
     * Builder class of {@link MultiChoiceForm}.
     */
    public static class Builder {
        private MultiChoiceForm form;

        /**
         * Constructor of the Builder class.
         *
         * @param context Mandatory activity Context.
         * @param steps   MCFSteps to be handled.
         */
        public Builder(@NonNull AppCompatActivity context, @NonNull ArrayList<MCFStep> steps) {
            form = new MultiChoiceForm();
            form.mContext = context;
            form.mMCFSteps = steps;
            form.mValidationColor = R.color.mcf_red_t;
            form.mValidationAnim = ValidationAnim.SHAKE_HORIZONTAL;
            form.mValidationDuration = Duration.MEDIUM;
            form.mEmptyViewTitle = context.getString(R.string.mcf_form_empty_view_title);
            form.mEmptyViewMsg = context.getString(R.string.mcf_form_empty_view_msg);
            form.mSearchViewHint = context.getString(R.string.mcf_search_title);
            form.mSearchViewIconTint = Utils.getDefaultThemeAttr(context, R.attr.colorAccent);
        }

        /**
         * Builds a {@link MultiChoiceForm} instance.
         *
         * @return {@link MultiChoiceForm} instance
         */
        public MultiChoiceForm build() {
            MultiChoiceForm builtForm = form;
            form = new MultiChoiceForm();

            return builtForm;
        }

        /**
         * Sets both of {@link OptionsActivity} toolbar colors.
         *
         * @param backgroundColor {@link android.widget.Toolbar} background color.
         * @param textColor       {@link android.widget.Toolbar} title color.
         * @return Current instance of this builder.
         */
        public Builder setToolbarColors(int backgroundColor, int textColor) {
            form.mToolbarBackgroundColor = backgroundColor;
            form.mToolbarTitleColor = textColor;
            return this;
        }

        /**
         * Sets the text to be shown in a {@link Toast} in case the validation fails.
         *
         * @param text Required text value.
         * @return Current instance of this builder.
         */
        public Builder setRequiredText(String text) {
            form.mRequiredText = text;
            return this;
        }

        /**
         * Sets the validation animation main color.
         *
         * @param color Any color, 50% alpha color is recommended.
         * @return Current instance of this builder.
         */
        public Builder setValidationColor(int color) {
            form.mValidationColor = color;
            return this;
        }

        /**
         * Sets the animation of the validation
         *
         * @param validationAnim Enum value of {@link ValidationAnim}.
         * @return Current instance of this builder.
         */
        public Builder setValidationAnimation(ValidationAnim validationAnim) {
            this.form.mValidationAnim = validationAnim;
            return this;
        }

        /**
         * Sets the duration of the validation animation.
         *
         * @param duration Enum value of {@link Duration}.
         * @return Current instance of this builder.
         */
        public Builder setValidationDuration(Duration duration) {
            this.form.mValidationDuration = duration;
            return this;
        }

        /**
         * If a {@link MCFStep} does not have any data associated with it, it will show an empty view
         * when prompted. This method defines both the title and the message that will show on the
         * empty view.
         *
         * @param title   Title of EmptyView.
         * @param message Message of EmptyView.
         * @return Current instance of this builder.
         */
        public Builder setEmptyViewTexts(String title, String message) {
            this.form.mEmptyViewTitle = title;
            this.form.mEmptyViewMsg = message;
            return this;
        }

        /**
         * If a {@link MCFStep} is searchable, this is used to show the SearchView's hint.
         *
         * @param hint hint of SearchView.
         * @return Current instance of this builder.
         */
        public Builder setSearchViewHint(String hint) {
            this.form.mSearchViewHint = hint;
            return this;
        }

        /**
         * If a {@link MCFStep} is searchable, this is used to show the SearchView's icon. Takes
         * the app's theme {@link R.attr#colorAccent} by default.
         *
         * @param color tint color of the SearchView icon.
         * @return Current instance of this builder.
         */
        public Builder setSearchViewIconTint(int color) {
            this.form.mSearchViewIconTint = color;
            return this;
        }
    }

    /**
     * Defines the necessary listeners for the {@link MCFStep}, options listener and title tooltip listener.
     * It has to be called after building the form in order for it to be enabled.
     * Otherwise, form would not be enabled.
     */
    public void setupForm() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepCalled(view);
            }
        };
        for (MCFStep step : mMCFSteps) {
            step.getView().getLayout().setTag(step);
            step.getView().getLayout().setOnClickListener(listener);
        }
    }

    private void stepCalled(View view) {
        MCFStep step;
        if (view instanceof TextView) {
            step = (MCFStep) ((View) view.getParent()).getTag();
        } else {
            step = (MCFStep) view.getTag();
        }
        switch (step.getType()) {
            case SINGLE_SELECT:
                Intent intent = getIntent((MCFSingleSelectStep) step);
                mContext.startActivityForResult(intent, REQUEST_SELECTION);
                mContext.overridePendingTransition(R.anim.mcf_slide_in_right, R.anim.mcf_slide_out_left);
                break;
            case DATE:
                handleDateStep((MCFDateStep) step);
                break;
        }
    }

    /**
     * Returns the intent related to {@link OptionsActivity} with all the needed parameters.
     *
     * @param step Associated {@link MCFSingleSelectStep}.
     * @return Intent to {@link OptionsActivity}.
     */
    private Intent getIntent(MCFSingleSelectStep step) {
        Intent intent = new Intent(mContext, OptionsActivity.class);

        MultiChoiceFormConfig model = new MultiChoiceFormConfig();
        model.data = step.getData();
        model.selection = step.getView().getSelection();
        model.id = step.getView().getId();
        model.title = step.getView().getTitle();
        model.toolbarBackgroundColor = mToolbarBackgroundColor;
        model.toolbarTitleColor = mToolbarTitleColor;
        model.emptyViewTitle = mEmptyViewTitle;
        model.emptyViewMsg = mEmptyViewMsg;
        model.isSearchable = step.isSearchable();
        model.searchViewHint = mSearchViewHint;
        model.searchViewIconTint = mSearchViewIconTint;

        intent.putExtra(OptionsActivity.EXTRA_CONFIG, model);

        return intent;
    }

    /**
     * Creates an {@link AlertDialog} with a {@link DatePicker}. Buttons' texts are customizable.
     * {@link DatePicker#setMinDate(long)} and {@link DatePicker#setMaxDate(long)} are supported.
     *
     * @param step Associated MCFDateStep
     */
    private void handleDateStep(MCFDateStep step) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(step.getView().getTitle());

        builder.setView(R.layout.mcf_picker_date);

        builder.setPositiveButton(step.getPositiveButton(), null);
        builder.setNegativeButton(step.getNegativeButton(), null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        DatePicker datePicker = dialog.findViewById(R.id.mcf_date_picker);
        if (datePicker != null) {
            if (step.getMinDate() != null)
                datePicker.setMinDate(step.getMinDate().getTime());

            if (step.getMaxDate() != null)
                datePicker.setMaxDate(step.getMaxDate().getTime());
        }

        Button btnAccept = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnAccept.setTag(R.id.mcf_dialog, dialog);
        btnAccept.setTag(R.id.mcf_date_picker, datePicker);
        btnAccept.setTag(R.id.mcf_form_step, step);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = (AlertDialog) view.getTag(R.id.mcf_dialog);
                DatePicker datePicker = (DatePicker) view.getTag(R.id.mcf_date_picker);
                MCFDateStep step = (MCFDateStep) view.getTag(R.id.mcf_form_step);

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
     * Shows a {@link Toast} with the full name of the MCFStep's title.
     *
     * @param view MCFStep view.
     * @deprecated
     */
    private void tooltip(View view) {
        String text = ((TextView) view).getText().toString();
        if (text.length() > 0)
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the {@link OptionsActivity} onResult.
     * Edit the {@link com.hypernovalabs.multichoiceform.view.MCFStepView} to show the selected option.
     *
     * @param requestCode onActivityResult requestCode.
     * @param resultCode  onActivityResult resultCode.
     * @param data        onActivityResult data.
     */
    public void handleActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (requestCode == REQUEST_SELECTION && resultCode == RESULT_OK) {
            String selection = data.getStringExtra(OptionsActivity.EXTRA_SELECTION);
            int id = data.getIntExtra(MultiChoiceFormConfig.EXTRA_ID_KEY, 0);
            if (id != 0) {
                MCFStep step = MCFStep.getStepFromId((ArrayList<MCFStep>) mMCFSteps, id);
                if (step != null) {
                    step.getView().setSelection(selection);
                }
            }
        }
    }

    /**
     * Validate all of the {@link MCFStep} where {@link MCFStep#isRequired()}, they cannot be empty.
     *
     * @return Whether all of the required {@link MCFStep}s are selected.
     */
    public boolean validate() {
        boolean trust = true;

        for (MCFStep step : mMCFSteps) {
            if (step.isRequired() && !step.getView().isSelected()) {
                if (mRequiredText != null) {

                    if (mToast != null) {
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(mContext, mRequiredText, Toast.LENGTH_SHORT);
                    mToast.show();
                }

                Animation animation = AnimationUtils.loadAnimation(mContext, mValidationAnim.getResId());

                ObjectAnimator
                        .ofObject(
                                step.getView(),
                                "backgroundColor",
                                new ArgbEvaluator(),
                                mValidationColor,
                                ContextCompat.getColor(mContext, R.color.mcf_transparent))
                        .setDuration(mValidationDuration.getDuration())
                        .start();

                step.getView().startAnimation(animation);

                trust = false;
            }
        }

        return trust;
    }

    /**
     * Retrieves the current list of {@link MCFStep}s.
     *
     * @return Current list of {@link MCFStep}s.
     */
    public ArrayList<? extends MCFStep> getSteps() {
        return mMCFSteps;
    }
}