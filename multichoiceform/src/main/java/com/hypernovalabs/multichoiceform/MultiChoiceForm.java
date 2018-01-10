package com.hypernovalabs.multichoiceform;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.hypernovalabs.multichoiceform.model.ExtraModel;
import com.hypernovalabs.multichoiceform.model.FormStep;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ldemorais on 04/04/2017.
 */

public class MultiChoiceForm {
    public static final int REQ_SELECTION = 27;

    //    private T mContext;
    private Activity mContext;
    private ArrayList<FormStep> mFormSteps;
    private int mToolbarBackgroundColor, mToolbarTitleColor;
    private int mValidationColor;
    private String mRequiredText;

    public static class Builder {
        private MultiChoiceForm form;

        public Builder(AppCompatActivity context) {
            form = new MultiChoiceForm();
            this.form.mContext = context;
        }

        public MultiChoiceForm build() {
            MultiChoiceForm builtForm = form;
            form = new MultiChoiceForm();

            return builtForm;
        }

        public Builder setSteps(ArrayList<FormStep> steps) {
            this.form.mFormSteps = steps;
            return this;
        }

        public Builder setToolbarColors(int backgroundColor, int textColor) {
            this.form.mToolbarBackgroundColor = backgroundColor;
            this.form.mToolbarTitleColor = textColor;
            return this;
        }

        public Builder setRequiredText(String text) {
            this.form.mRequiredText = text;
            return this;
        }

        public Builder setValidationColor(int color) {
            this.form.mValidationColor = color;
            return this;
        }
    }

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

                intent.putExtra(SelectionsActivity.EXTRA_MODEL_KEY, model);

                mContext.startActivityForResult(intent, REQ_SELECTION);
            }
        };

        for (FormStep step : mFormSteps) {
            step.getView().setOnClickListener(listener);
            step.getView().setTag(step);
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
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
     * Valida las selecciones del formulario, la única validación verifica la selección de cualquier opción.
     * Todas presentes en la matriz 'views' son obligatorias
     *
     * @return true = todas las obligatorias se seleccionaron
     * false = alguna no se seleccionó
     */
    public boolean validate() {
        for (FormStep formStep : mFormSteps) {
            if (formStep != null && formStep.isRequired()) {
                String text = formStep.getView().getSelection();

                if (text.equals("")) {
                    if (mRequiredText != null)
                        Toast.makeText(mContext, mRequiredText, Toast.LENGTH_SHORT).show();

                    Animation shakeAnimation = AnimationUtils.loadAnimation(mContext, R.anim.shake_horizontal);

                    ObjectAnimator
                            .ofObject(
                                    formStep.getView(),
                                    "backgroundColor",
                                    new ArgbEvaluator(),
                                    ContextCompat.getColor(mContext, mValidationColor != 0 ? mValidationColor : R.color.redt2),
                                    ContextCompat.getColor(mContext, R.color.transparent))
                            .setDuration(600)
                            .start();

                    formStep.getView().startAnimation(shakeAnimation);
                    return false;
                }

            }
        }
        return true;
    }
}