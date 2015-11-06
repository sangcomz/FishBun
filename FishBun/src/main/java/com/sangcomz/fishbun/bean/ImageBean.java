package com.sangcomz.fishbun.bean;

/**
 * Created by Administrator on 2014-12-22.
 */
public class ImageBean {
    int imgOrder;
    String imgPath;
    boolean isInit = false;


    public ImageBean(int imgOrder, String imgPath) {
        this.imgOrder = imgOrder;
        this.imgPath = imgPath;
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

    public boolean isInit() {
        return isInit;
    }

    public void setIsInit(boolean isInit) {
        this.isInit = isInit;
    }
}
