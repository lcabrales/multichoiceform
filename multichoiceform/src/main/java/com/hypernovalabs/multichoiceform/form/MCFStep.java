package com.hypernovalabs.multichoiceform.form;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.hypernovalabs.multichoiceform.view.MCFStepView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lucascabrales on 1/5/18.
 * <p>
 * Main model of a MCFStep. Holds the data, the view, its type and whether it is required.
 * </p>
 */
public class MCFStep {
    private ArrayList<String> data;
    private MCFStepView view;
    private int id;
    private MCFStepType type;
    private boolean required;
    private int tag;

    /**
     * Main constructor, defines whether the MCFStep is required.
     *
     * @param data     Contains all of the options.
     * @param view     Associated MCFStepView.
     * @param required Whether the MCFStep is required.
     */
    MCFStep(@NonNull ArrayList<String> data, @NonNull MCFStepView view, boolean required) {
        this.data = data;
        this.view = view;
        this.id = view.getId();
        this.tag = id;
        this.required = required;
    }

    /**
     * Constructor that defines whether the MCFStep is required. Takes an array as parameter.
     *
     * @param data     Contains all of the options.
     * @param view     Associated MCFStepView.
     * @param required Whether the MCFStep is required.
     */
    MCFStep(@NonNull String[] data, @NonNull MCFStepView view, boolean required) {
        this(new ArrayList<>(Arrays.asList(data)), view, required);
    }

    /**
     * Optionsless constructor.
     *
     * @param view Associated MCFStepView.
     */
    MCFStep(@NonNull MCFStepView view) {
        this(new ArrayList<String>(), view, false);
    }

    /**
     * Optionsless constructor with the added "required" parameter.
     *
     * @param view     Associated MCFStepView.
     * @param required Whether the MCFStep is required.
     */
    MCFStep(@NonNull MCFStepView view, boolean required) {
        this(new ArrayList<String>(), view, required);
    }

    /**
     * Simple constructor of the MCFStep class, isRequired is false. Takes an array as parameter.
     *
     * @param data Contains all of the options.
     * @param view Associated MCFStepView.
     */
    MCFStep(@NonNull String[] data, @NonNull MCFStepView view) {
        this(new ArrayList<>(Arrays.asList(data)), view, false);
    }

    /**
     * Simple constructor of the MCFStep class, isRequired is false.
     *
     * @param data Contains all of the options.
     * @param view Associated MCFStepView.
     */
    MCFStep(@NonNull ArrayList<String> data, @NonNull MCFStepView view) {
        this(data, view, false);
    }

    /**
     * Finds a single MCFStep instance based on its MCFStepView resId.
     *
     * @param steps All of the MultiChoiceForm steps.
     * @param id    resId of MCFStepView
     * @return MCFStep of the associated MCFStepView
     * @deprecated use {@link #getStepFromTag(ArrayList, int)}
     */
    @Deprecated
    public static MCFStep getStepFromId(@NonNull ArrayList<? extends MCFStep> steps, @IdRes int id) {
        for (MCFStep step : steps) {
            if (step.id == id)
                return step;
        }

        return null;
    }

    /**
     * Finds a single MCFStep instance based on its tag.
     *
     * @param steps All of the MultiChoiceForm steps.
     * @param tag   {@link #tag} of MCFStepView
     * @return MCFStep of the associated MCFStepView
     */
    public static MCFStep getStepFromTag(@NonNull ArrayList<? extends MCFStep> steps, int tag) {
        for (MCFStep step : steps) {
            if (step.tag == tag)
                return step;
        }

        return null;
    }

    /**
     * @return Associated MCFStepView.
     */
    public MCFStepView getView() {
        return view;
    }

    /**
     * Sets the associated MCFStepView.
     *
     * @param view Associated MCFStepView.
     */
    public void setView(MCFStepView view) {
        this.view = view;
    }

    /**
     * @return Associated options data.
     */
    public ArrayList<String> getData() {
        return data;
    }

    /**
     * Sets the associated options data.
     *
     * @param data Associated options data.
     */
    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    /**
     * @return Whether the MCFStep is required.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets whether the MCFStep is required.
     *
     * @param required Whether the MCFStep is required.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Gets the associated MCFStepType
     *
     * @return Associated MCFStepType
     */
    public MCFStepType getType() {
        return type;
    }

    /**
     * Sets the associated MCFStepType
     *
     * @param type Associated MCFStepType
     */
    public void setType(MCFStepType type) {
        this.type = type;
    }

    /**
     * Gets the associated tag. This parameter is used to identify the step, should be treated as
     * a UNIQUE IDENTIFIER.
     *
     * @return Associated tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * Sets the associated tag. This parameter is used to identify the step, should be treated as
     * a UNIQUE IDENTIFIER.
     *
     * @param tag Associated tag
     */
    public void setTag(int tag) {
        this.tag = tag;
    }
}
