package com.sangcomz.fishbun.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    final public long bucketId;
    final public String bucketName;
    public int counter;
    public String thumbnailPath;


    public Album(long bucketId, String bucketName, String thumbnailPath, int counter) {
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        this.counter = counter;
        this.thumbnailPath = thumbnailPath;
    }

    protected Album(Parcel in) {
        bucketId = in.readLong();
        bucketName = in.readString();
        counter = in.readInt();
        thumbnailPath = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(bucketId);
        parcel.writeString(bucketName);
        parcel.writeInt(counter);
        parcel.writeString(thumbnailPath);
    }
}