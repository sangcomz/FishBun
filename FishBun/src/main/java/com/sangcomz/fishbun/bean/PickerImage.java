package com.sangcomz.fishbun.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class PickerImage implements Parcelable {
    public Uri path;
    public int orientation;

    public PickerImage(Uri path, int orientation) {
        this.path = path;
        this.orientation = orientation;
    }

    protected PickerImage(Parcel parcel) {
        path = (Uri) parcel.readParcelable(null);
        orientation = parcel.readInt();
    }

    public static final Creator<PickerImage> CREATOR = new Creator<PickerImage>() {
        @Override
        public PickerImage createFromParcel(Parcel source) {
            return new PickerImage(source);
        }

        @Override
        public PickerImage[] newArray(int size) {
            return new PickerImage[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(path, i);
        parcel.writeInt(orientation);
    }
}
