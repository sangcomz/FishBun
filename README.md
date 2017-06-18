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

FishBun is Image Picker for android.

<img src="/pic/fishbuns.png">

## What's New 0.7.1 FishBun

#### ● You can use Detail View (#73)
#### ● Refactoring FishBun (#86) **[(The default usage has changed)](#how-to-use)**
#### ● Fix Count issue (#96)

## Customization Screen

You can create image pickers in various ways.

##### Normal Style
<img src="/pic/default1.png" width=40%> <img src="/pic/default2.png" width=40%>

##### Dark Style
<img src="/pic/dark1.png" width=40%> <img src="/pic/dark2.png" width=40%>

##### Light Style
<img src="/pic/light1.png" width=40%> <img src="/pic/light2.png" width=40%>

## How to Setup

### Gradle

    repositories {
        jcenter()
    }

    dependencies {
        compile('com.sangcomz:FishBun:0.7.1@aar') {
            transitive = true
        }
    }

### Manifest

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

## How to Use

    FishBun.with(Your Activity).MultiPageMode().startAlbum();

if you use in Fragment,

    FishBun.with(Your Fragment).MultiPageMode().startAlbum();

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
                        .MultiPageMode()
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
FishBun check permission before reading external storage.

<img src="/pic/permission.png" width=20%>

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
