package com.hypernovalabs.multichoiceform.sample;

import android.os.Parcel;
import android.os.Parcelable;

import com.hypernovalabs.multichoiceform.form.MCFStep;
import com.hypernovalabs.multichoiceform.form.MCFStepObj;

/**
 * Created by celso on 2/19/19
 */
public class CustomModel extends MCFStepObj implements Parcelable {
    public String id;

    protected CustomModel(Parcel in) {
        super(in);
        id = in.readString();
    }

    protected CustomModel(String id, String displayText) {
        super(displayText);
        this.id = id;
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomModel> CREATOR = new Parcelable.Creator<CustomModel>() {
        @Override
        public CustomModel createFromParcel(Parcel in) {
            return new CustomModel(in);
        }

        @Override
        public CustomModel[] newArray(int size) {
            return new CustomModel[size];
        }
    };

}