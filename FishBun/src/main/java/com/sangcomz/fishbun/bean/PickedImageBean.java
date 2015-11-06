package com.sangcomz.fishbun.bean;

/**
 * Created by Administrator on 2014-12-22.
 */
public class PickedImageBean {
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
}
