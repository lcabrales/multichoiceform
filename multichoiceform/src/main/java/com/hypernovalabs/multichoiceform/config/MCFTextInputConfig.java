package com.hypernovalabs.multichoiceform.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.hypernovalabs.multichoiceform.TextInputActivity;
import com.hypernovalabs.multichoiceform.model.Regex;

import java.util.ArrayList;

/**
 * Created by ldemorais on 5/23/18.
 * ldemorais@hypernovalabs.com
 * <p>
 * Defines everything that needs to be sent to {@link TextInputActivity}. Only to be used internally.
 * </p>
 */
public class MCFTextInputConfig implements Parcelable {

    public ArrayList<String> data;
    public String selection;
    public String title;
    public int tag;
    public boolean required;
    public boolean hasAutoFocus;
    public boolean isPassword;
    public int toolbarBackgroundColor;
    public int toolbarTitleColor;
    public Regex regex;
    public int inputType;
    public int maxLength;
    public String explanatoryText;
    public int saveIconTint;

    public MCFTextInputConfig() {

    }

    //region Parcelable
    protected MCFTextInputConfig(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<String>();
            in.readList(data, String.class.getClassLoader());
        } else {
            data = null;
        }
        selection = in.readString();
        title = in.readString();
        tag = in.readInt();
        required = in.readByte() != 0x00;
        hasAutoFocus = in.readByte() != 0x00;
        isPassword = in.readByte() != 0x00;
        toolbarBackgroundColor = in.readInt();
        toolbarTitleColor = in.readInt();
        regex = (Regex) in.readValue(Regex.class.getClassLoader());
        inputType = in.readInt();
        maxLength = in.readInt();
        explanatoryText = in.readString();
        saveIconTint = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (data == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(data);
        }
        dest.writeString(selection);
        dest.writeString(title);
        dest.writeInt(tag);
        dest.writeByte((byte) (required ? 0x01 : 0x00));
        dest.writeByte((byte) (hasAutoFocus ? 0x01 : 0x00));
        dest.writeByte((byte) (isPassword ? 0x01 : 0x00));
        dest.writeInt(toolbarBackgroundColor);
        dest.writeInt(toolbarTitleColor);
        dest.writeValue(regex);
        dest.writeInt(inputType);
        dest.writeInt(maxLength);
        dest.writeString(explanatoryText);
        dest.writeInt(saveIconTint);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MCFTextInputConfig> CREATOR = new Parcelable.Creator<MCFTextInputConfig>() {
        @Override
        public MCFTextInputConfig createFromParcel(Parcel in) {
            return new MCFTextInputConfig(in);
        }

        @Override
        public MCFTextInputConfig[] newArray(int size) {
            return new MCFTextInputConfig[size];
        }
    };
    //endregion
}
