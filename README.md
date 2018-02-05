# FishBun

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FishBun-green.svg?style=true)](https://android-arsenal.com/details/1/2785)
[![Build Status](https://travis-ci.org/sangcomz/FishBun.svg?branch=master)](https://travis-ci.org/sangcomz/FishBun)
[![codecov](https://codecov.io/gh/sangcomz/FishBun/branch/master/graph/badge.svg)](https://codecov.io/gh/sangcomz/FishBun)
[![Download](https://api.bintray.com/packages/sangcomz/maven/fishbun/images/download.svg)](https://bintray.com/sangcomz/maven/fishbun/_latestVersion)
<a href="http://www.methodscount.com/?lib=com.sangcomz%3AFishBun%3A0.6.7"><img src="https://img.shields.io/badge/Methods count-core: 499 | deps: 26008-e91e63.svg"/></a>
<p style="float:left;">
 <a href="https://play.google.com/store/apps/details?id=com.sangcomz.fishbundemo">
 <img HEIGHT="40" WIDTH="135" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" />
 </a>
</p>

_FishBun_ is a highly customizable image picker for Android.

<img src="/pic/fishbuns.png">


## What's New in _FishBun_ 0.8.6?

- Crash without Picasso ([#113](https://github.com/sangcomz/FishBun/issues/113))



## Customizable Styles

_FishBun_ supports various visual styles and allows fine-tuning for details. Just to show some examples:

#### Default

##### Code

```java
FishBun.with(WithActivityActivity.this)
        .setImageAdapter(new GlideAdapter())
        .startAlbum();
```

##### Screenshots

<img src="/pic/default1.png" width="30%"> <img src="/pic/default2.png" width="30%"> <img src="/pic/default3.png" width="30%">

#### Dark

##### Code

```java
FishBun.with(WithActivityActivity.this)
        .setImageAdapter(new GlideAdapter())
        .setMaxCount(5)
        .setMinCount(3)
        .setPickerSpanCount(5)
        .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
        .setActionBarTitleColor(Color.parseColor("#ffffff"))
        .setArrayPaths(path)
        .setAlbumSpanCount(2, 3)
        .setButtonInAlbumActivity(false)
        .setCamera(true)
        .exceptGif(true)
        .setReachLimitAutomaticClose(true)
        .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_back_white))
        .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
        .setAllViewTitle("All")
        .setActionBarTitle("FishBun Dark")
        .textOnNothingSelected("Please select three or more!")
        .startAlbum();
```

##### Screenshots

<img src="/pic/dark1.png" width="30%"> <img src="/pic/dark2.png" width="30%"> <img src="/pic/dark3.png" width="30%">

#### Light

##### Code

```java
FishBun.with(WithActivityActivity.this)
        .setImageAdapter(new GlideAdapter())
        .setPickerCount(50)
        .setPickerSpanCount(4)
        .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
        .setActionBarTitleColor(Color.parseColor("#000000"))
        .setArrayPaths(path)
        .setAlbumSpanCount(1, 2)
        .setButtonInAlbumActivity(true)
        .setCamera(false)
        .exceptGif(true)
        .setReachLimitAutomaticClose(false)
        .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp))
        .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_black_24dp))
        .setAllViewTitle("All of your photos")
        .setActionBarTitle("FishBun Light")
        .textOnImagesSelectionLimitReached("You can't select any more.")
        .textOnNothingSelected("I need a photo!")
        .startAlbum();
```

##### Screenshots

<img src="/pic/light1.png" width="30%"> <img src="/pic/light2.png" width="30%"> <img src="/pic/light3.png" width="30%">


## How to Setup

Setting up _FishBun_ requires to add this Gradle configuration:

    repositories {
        jcenter()
    }

    dependencies {
        // Under the Android Plugin 3.0.0. 
        compile 'com.sangcomz:FishBun:0.8.6'
        
        compile 'com.squareup.picasso:picasso:2.5.2'
        or
        compile 'com.github.bumptech.glide:glide:4.5.0'
                
        // Android plugin 3.0.0 or higher.
        implementation 'com.sangcomz:FishBun:0.8.6'
        
        implementation 'com.squareup.picasso:picasso:2.5.2'
        or
        implementation 'com.github.bumptech.glide:glide:4.5.0'

    } 

and to allow the following permissions in your `Manifest`:

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


## How to Use

Use _FishBun_ in an activity:

    FishBun.with(YourActivity).setImageAdapter(new GlideAdapter()).startAlbum();

or in a fragment:

    FishBun.with(YourFragment).setImageAdapter(new PicassoAdapter()).startAlbum();

and implement `OnActivityResult`:

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2

                    path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
                    break;
                }
        }
    }

Various customizable features can be controlled by chained methods as in:

    FishBun.with(YourActivity or YourFragment)
            .setImageAdapter(new GlideAdapter())
            .setIsUseDetailView(false)
            .setPickerCount(5) //Deprecated
            .setMaxCount(5)
            .setMinCount(1)
            .setPickerSpanCount(6)
            .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
            .setActionBarTitleColor(Color.parseColor("#ffffff"))
            .setArrayPaths(path)
            .setAlbumSpanCount(2, 4)
            .setButtonInAlbumActivity(false)
            .setCamera(true)
            .setReachLimitAutomaticClose(true)
            .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_back_white))
            .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
            .setAllViewTitle("All")
            .setActionBarTitle("Image Library")
            .textOnImagesSelectionLimitReached("Limit Reached!")
            .textOnNothingSelected("Nothing Selected")
            .startAlbum();


## Android M Permission

Running on Android M, _FishBun_ checks if it has proper permission for you before reading an external storage.

<img src="/pic/permission.png" width="20%">


# Contribution

Any suggestions or contributions would be welcomed.


# License

    Copyright 2018 Seok-Won Jeong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
