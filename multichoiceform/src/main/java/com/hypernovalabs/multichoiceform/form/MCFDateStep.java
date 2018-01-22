package com.hypernovalabs.multichoiceform.form;

import com.hypernovalabs.multichoiceform.view.MCFStepView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Provides an AlertDialog containing a DatePicker to select a date.
 * DateFormat is customizable, along with min and max Date, and the Dialog buttons.
 * Child of MCFStep.
 */

public class MCFDateStep extends MCFStep {
    private Date maxDate;
    private Date minDate;
    private String positiveButton;
    private String negativeButton;
    private SimpleDateFormat dateFormat;

    /**
     * Main constructor of MCFDateStep. Sets default values for some parameters.
     *
     * @param view Associated MCFStepView.
     */
    public MCFDateStep(MCFStepView view) {
        super(view);
        super.setType(MCFStepType.DATE);

        positiveButton = "Accept";
        negativeButton = "Cancel";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    /**
     * Constructor with the added "required" parameter.
     *
     * @param view     MCFStepView.
     * @param required Whether the MCFStep is required
     */
    public MCFDateStep(MCFStepView view, boolean required) {
        super(view, required);
        super.setType(MCFStepType.DATE);

        positiveButton = "Accept";
        negativeButton = "Cancel";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    /**
     * Returns the max date of the DatePicker.
     *
     * @return Maximum date.
     */
    public Date getMaxDate() {
        return maxDate;
    }

    /**
     * Sets the max date value.
     *
     * @param maxDate Maximum date value.
     */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    /**
     * Returns the min date of the DatePicker.
     *
     * @return Minimum date.
     */
    public Date getMinDate() {
        return minDate;
    }

    /**
     * Sets the min date value.
     *
     * @param minDate Minimum date.
     */
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    /**
     * Returns the text value of the Dialog's positive button.
     *
     * @return Positive button text.
     */
    public String getPositiveButton() {
        return positiveButton;
    }

    /**
     * Sets the text value for the Dialog's positive button.
     *
     * @param positiveButton Positive button text.
     */
    public void setPositiveButton(String positiveButton) {
        this.positiveButton = positiveButton;
    }

    /**
     * Returns the text value of the Dialog's negative button.
     *
     * @return Negative button text.
     */
    public String getNegativeButton() {
        return negativeButton;
    }

    /**
     * Sets the text value for the Dialog's negative button.
     *
     * @param negativeButton Negative button text.
     */
    public void setNegativeButton(String negativeButton) {
        this.negativeButton = negativeButton;
    }

    /**
     * Returns the MCFDateStep's SimpleDateFormat
     *
     * @return MCFDateStep's SimpleDateFormat
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * Sets the MCFDateStep's SimpleDateFormat
     *
     * @param dateFormat MCFDateStep's SimpleDateFormat
     */
    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}
