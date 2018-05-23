package com.hypernovalabs.multichoiceform.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hypernovalabs.multichoiceform.form.MCFTextInputStep;

/**
 * Created by ldemorais on 5/23/18.
 * ldemorais@hypernovalabs.com
 * <p>
 * This is used to perform a regular expression validation on {@link MCFTextInputStep}
 * </p>
 */
public class Regex implements Parcelable {

    private String regex;
    private String errorMessage;

    /**
     * @param regex        Regular expression.
     * @param errorMessage Error message in case the regex fails.
     */
    public Regex(String regex, String errorMessage) {
        this.regex = regex;
        this.errorMessage = errorMessage;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //region Parcelable
    protected Regex(Parcel in) {
        regex = in.readString();
        errorMessage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(regex);
        dest.writeString(errorMessage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Regex> CREATOR = new Parcelable.Creator<Regex>() {
        @Override
        public Regex createFromParcel(Parcel in) {
            return new Regex(in);
        }

        @Override
        public Regex[] newArray(int size) {
            return new Regex[size];
        }
    };
    //endregion
}
