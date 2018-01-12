package com.hypernovalabs.multichoiceform.model;

import android.support.annotation.IdRes;

import com.hypernovalabs.multichoiceform.view.FormStepView;

import java.util.ArrayList;

/**
 * Created by lucascabrales on 1/5/18.
 * <p>
 *     Main model of a FormStep. Holds the data, the view and whether it is required.
 * </p>
 */
public class FormStep {
    private ArrayList<String> data;
    private FormStepView view;
    private int id;
    private boolean required;

    /**
     * Simple constructor of the FormStep class, isRequired is false.
     * @param data Contains all of the options.
     * @param view Associated FormStepView.
     */
    public FormStep(ArrayList<String> data, FormStepView view){
        this.data = data;
        this.view = view;
        this.id = view.getId();
        this.required = false;
    }

    /**
     * Constructor that defines whether the FormStep is required.
     * @param data Contains all of the options.
     * @param view Associated FormStepView.
     * @param required Whether the FormStep is required.
     */
    public FormStep(ArrayList<String> data, FormStepView view, boolean required){
        this.data = data;
        this.view = view;
        this.id = view.getId();
        this.required = required;
    }

    /**
     * Finds a single FormStep instance based on its FormStepView resId.
     * @param steps All of the MultiChoiceForm steps.
     * @param id resId of FormStepView
     * @return FormStep of the associated FormStepView
     */
    public static FormStep getStepFromId(ArrayList<FormStep> steps, @IdRes int id) {
        for (FormStep step : steps) {
            if (step.id == id)
                return step;
        }

        return null;
    }

    /**
     *
     * @return Associated FormStepView.
     */
    public FormStepView getView() {
        return view;
    }

    /**
     * Sets the associated FormStepView.
     * @param view Associated FormStepView.
     */
    public void setView(FormStepView view) {
        this.view = view;
    }

    /**
     *
     * @return Associated options data.
     */
    public ArrayList<String> getData() {
        return data;
    }

    /**
     * Sets the associated options data.
     * @param data Associated options data.
     */
    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    /**
     *
     * @return Whether the FormStep is required.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets whether the FormStep is required.
     * @param required Whether the FormStep is required.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
}
