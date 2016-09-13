package com.sangcomz.fishbun.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2014-12-22.
 */
public class PickedImageBean implements Parcelable {
    int imgOrder;
    String imgPath;
    int imgPosition;


    public PickedImageBean(int imgOrder, String imgPath, int imgPosition) {
        this.imgOrder = imgOrder;
        this.imgPath = imgPath;
        this.imgPosition = imgPosition;
    }



    public int getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(int imgOrder) {
        this.imgOrder = imgOrder;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getImgPosition() {
        return imgPosition;
    }

    public void setImgPosition(int imgPosition) {
        this.imgPosition = imgPosition;
    }


    protected PickedImageBean(Parcel in) {
        imgOrder = in.readInt();
        imgPath = in.readString();
        imgPosition = in.readInt();
    }

    public static final Creator<PickedImageBean> CREATOR = new Creator<PickedImageBean>() {
        @Override
        public PickedImageBean createFromParcel(Parcel in) {
            return new PickedImageBean(in);
        }

        @Override
        public PickedImageBean[] newArray(int size) {
            return new PickedImageBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imgOrder);
        parcel.writeString(imgPath);
        parcel.writeInt(imgPosition);
    }

//    public void readFromParcel(Parcel parcel) {
//        imgOrder = parcel.readInt();
//        imgPath = parcel.readString();
//        imgPosition = parcel.readInt();
//    }
}
