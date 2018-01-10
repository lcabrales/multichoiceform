package com.hypernovalabs.multichoiceform.model;

import com.hypernovalabs.multichoiceform.view.FormStepView;

import java.util.ArrayList;

/**
 * Created by lucascabrales on 1/5/18.
 */

public class FormStep {
    private ArrayList<String> data;
    private FormStepView view;
    private int id;
    private boolean required;

    public FormStep(ArrayList<String> data, FormStepView view){
        this.data = data;
        this.view = view;
        this.id = view.getId();
        this.required = false;
    }

    public FormStep(ArrayList<String> data, FormStepView view, boolean required){
        this.data = data;
        this.view = view;
        this.id = view.getId();
        this.required = required;
    }

    public static FormStep getStepFromId(ArrayList<FormStep> steps, int id) {
        for (FormStep step : steps) {
            if (step.id == id)
                return step;
        }

        return null;
    }

    public FormStepView getView() {
        return view;
    }

    public void setView(FormStepView view) {
        this.view = view;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
