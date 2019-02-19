package com.hypernovalabs.multichoiceform.form;

import android.os.Parcel;
import android.os.Parcelable;

import com.hypernovalabs.multichoiceform.view.MCFStepView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

/**
 * Created by celso on 2/19/19
 */
public class MCFStepObj implements Parcelable {

    private String displayText;

    protected MCFStepObj() {
        displayText = "";
    }

    protected MCFStepObj(Parcel in) {
        displayText = in.readString();
    }

    protected MCFStepObj(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayText);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MCFStepObj> CREATOR = new Parcelable.Creator<MCFStepObj>() {
        @Override
        public MCFStepObj createFromParcel(Parcel in) {
            return new MCFStepObj(in);
        }

        @Override
        public MCFStepObj[] newArray(int size) {
            return new MCFStepObj[size];
        }
    };

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}