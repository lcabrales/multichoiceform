package com.hypernovalabs.multichoiceform.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.hypernovalabs.multichoiceform.OptionsActivity;

import java.util.ArrayList;

/**
 * Main configuration model.
 * Defines everything that needs to be sent to {@link OptionsActivity}. Only to be used internally.
 */
public class MCFOptionsConfig implements Parcelable {

    public ArrayList<String> data;
    public String selection;
    public String title;
    public int id;
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
        selection = in.readString();
        title = in.readString();
        id = in.readInt();
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
        dest.writeString(selection);
        dest.writeString(title);
        dest.writeInt(id);
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
