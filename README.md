# FishBun

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FishBun-green.svg?style=true)](https://android-arsenal.com/details/1/2785)
[![Build Status](https://travis-ci.org/sangcomz/FishBun.svg?branch=master)](https://travis-ci.org/sangcomz/FishBun)
[![codecov](https://codecov.io/gh/sangcomz/FishBun/branch/master/graph/badge.svg)](https://codecov.io/gh/sangcomz/FishBun)
[![Download](https://api.bintray.com/packages/sangcomz/maven/fishbun/images/download.svg)](https://bintray.com/sangcomz/maven/fishbun/_latestVersion)
<a href="http://www.methodscount.com/?lib=com.sangcomz%3AFishBun%3A0.6.4"><img src="https://img.shields.io/badge/Methods count-core: 499 | deps: 26008-e91e63.svg"/></a>
<p style="float:left;">
 <a href="https://play.google.com/store/apps/details?id=com.sangcomz.fishbundemo">
 <img HEIGHT="40" WIDTH="135" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" />
 </a>
</p>

FishBun is Image Picker for android.

<img src="/pic/fishbuns.png">

## What's New 0.6.5 FishBun

#### ● Displays the number of images selected on the album activity
#### ● Add image selection effect (#76)
#### ● add setMinCount() (#75)
#### ● Deprecated setPickerCount() -> setMaxCount()

### Gradle

    repositories {
        jcenter()
    }

    ;
    dependencies {
        compile('com.sangcomz:FishBun:0.6.5@aar') {
            transitive = true
        }
    }

### Manifest

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

## How to Use FishBun

    FishBun.with(Your Activity).startAlbum();

if you use in Fragment,

    FishBun.with(Your Fragment).startAlbum();

and add OnActivityResult

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    //You can get image path(ArrayList<String>) Under version 0.6.2

                    path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    //You can get image path(ArrayList<Uri>) Version 0.6.2 or later
                    break;
                }
        }
    }

you can use also this

                FishBun.with(Your Activity or Fragment)
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


## Result Screen
<img src="/pic/sim.gif" width=40%>

## Android M Permission
FishBun check permission before reading external storage.


<img src="/pic/permission.png" width=40%>

# Contribute
We welcome any contributions.

# License

    Copyright 2017 Jeong Seok-Won

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.