package com.sangcomz.fishbun.ui.detail;

/**
 * Created by sangcomz on 11/06/2017.
 */

class DetailController {

    private DetailActivity detailActivity;
//    private ArrayList<Uri> pickedImages = new ArrayList<>();

    DetailController(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

//    void setPickedImages(ArrayList<Uri> pickedImages) {
//        this.pickedImages = pickedImages;
//    }
//
//    ArrayList<Uri> getPickedImage() {
//        return pickedImages;
//    }
//
//    void addPickedImage(Uri image) {
//        if (!isAdded(image)) {
//            pickedImages.add(image);
//        }
//        detailActivity.onCheckStateChange(image);
//    }
//
//    void removePickedImage(Uri image) {
//        pickedImages.remove(image);
//        detailActivity.onCheckStateChange(image);
//    }
//
//    boolean isAdded(Uri image) {
//        return pickedImages.contains(image);
//    }
}
