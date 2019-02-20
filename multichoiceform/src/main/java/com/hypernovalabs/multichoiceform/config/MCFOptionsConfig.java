package com.hypernovalabs.multichoiceform.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.hypernovalabs.multichoiceform.OptionsActivity;
import com.hypernovalabs.multichoiceform.form.MCFStepObj;

import java.util.ArrayList;

/**
 * Main configuration model.
 * Defines everything that needs to be sent to {@link OptionsActivity}. Only to be used internally.
 */
public class MCFOptionsConfig implements Parcelable {

    public ArrayList<String> data;
    public ArrayList<? extends MCFStepObj> customData;
    public String selection;
    public MCFStepObj customSelection;
    /**
     * For now, it only works with custom data
     */
    public int selectedPosition;
    public String title;
    public int tag;
    public boolean required;
    public int toolbarBackgroundColor;
    public int toolbarTitleColor;
    public String emptyViewTitle;
    public String emptyViewMsg;
    public boolean isSearchable;
    public String searchViewHint;
    public int searchViewIconTint;

    public MCFOptionsConfig() {
    }

    //region Parcelable
    protected MCFOptionsConfig(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<>();
            in.readList(data, String.class.getClassLoader());
        } else {
            data = null;
        }
        if (in.readByte() == 0x01) {
            customData = new ArrayList<>();
            in.readList(customData, MCFStepObj.class.getClassLoader());
        } else {
            customData = null;
        }
        selection = in.readString();
        customSelection = (MCFStepObj) in.readValue(MCFStepObj.class.getClassLoader());
        selectedPosition = in.readInt();
        title = in.readString();
        tag = in.readInt();
        required = in.readByte() != 0x00;
        toolbarBackgroundColor = in.readInt();
        toolbarTitleColor = in.readInt();
        emptyViewTitle = in.readString();
        emptyViewMsg = in.readString();
        isSearchable = in.readByte() != 0x00;
        searchViewHint = in.readString();
        searchViewIconTint = in.readInt();
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
        if (customData == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(customData);
        }
        dest.writeString(selection);
        dest.writeValue(customSelection);
        dest.writeInt(selectedPosition);
        dest.writeString(title);
        dest.writeInt(tag);
        dest.writeByte((byte) (required ? 0x01 : 0x00));
        dest.writeInt(toolbarBackgroundColor);
        dest.writeInt(toolbarTitleColor);
        dest.writeString(emptyViewTitle);
        dest.writeString(emptyViewMsg);
        dest.writeByte((byte) (isSearchable ? 0x01 : 0x00));
        dest.writeString(searchViewHint);
        dest.writeInt(searchViewIconTint);
    }

    @SuppressWarnings("unused")
    public static final Creator<MCFOptionsConfig> CREATOR = new Creator<MCFOptionsConfig>() {
        @Override
        public MCFOptionsConfig createFromParcel(Parcel in) {
            return new MCFOptionsConfig(in);
        }

        @Override
        public MCFOptionsConfig[] newArray(int size) {
            return new MCFOptionsConfig[size];
        }
    };
    //endregion
}
