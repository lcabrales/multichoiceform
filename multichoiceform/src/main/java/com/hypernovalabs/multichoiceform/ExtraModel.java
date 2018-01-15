package com.hypernovalabs.multichoiceform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Holds everything that needs to be sent to OptionsActivity. Only to be used internally.
 */
public class ExtraModel implements Parcelable {
    public ArrayList<String> data;
    public String selection;
    public String title;
    public int id;
    public boolean required;
    public int toolbarBackgroundColor;
    public int toolbarTitleColor;
    public String emptyViewTitle;
    public String emptyViewMsg;

    public ExtraModel() {

    }

    //region Parcelable
    protected ExtraModel(Parcel in) {
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
    }

    @SuppressWarnings("unused")
    public static final Creator<ExtraModel> CREATOR = new Creator<ExtraModel>() {
        @Override
        public ExtraModel createFromParcel(Parcel in) {
            return new ExtraModel(in);
        }

        @Override
        public ExtraModel[] newArray(int size) {
            return new ExtraModel[size];
        }
    };
    //endregion
}
