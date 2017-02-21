# FishBun

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FishBun-green.svg?style=true)](https://android-arsenal.com/details/1/2785)
[![Build Status](https://travis-ci.org/sangcomz/FishBun.svg?branch=master)](https://travis-ci.org/sangcomz/FishBun)
[![codecov](https://codecov.io/gh/sangcomz/FishBun/branch/master/graph/badge.svg)](https://codecov.io/gh/sangcomz/FishBun)
[![Download](https://api.bintray.com/packages/sangcomz/maven/fishbun/images/download.svg)](https://bintray.com/sangcomz/maven/fishbun/_latestVersion)

<a href="http://www.methodscount.com/?lib=com.sangcomz%3AFishBun%3A0.6.1"><img src="https://img.shields.io/badge/Methods and size-core: 461 | deps: 24948 | 95 KB-e91e63.svg"/></a>

FishBun is Image Picker for android.

<img src="/pic/fishbuns.png">

##What's New 0.6.3 FishBun

####● Fix Proguard (#60)
####● Fix Demo App

##How to Use FishBun

###Gradle

    repositories {
        jcenter()
    }
    
    ;
    dependencies {
        compile('com.sangcomz:FishBun:0.6.3@aar') {
            transitive = true
        }
    }

###Manifest

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

FishBun.with(MainActivity.this).startAlbum();

if you use in Fragment,

FishBun.with(Fragment.this).startAlbum();

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

            FishBun.with(MainActivity.this)
                    .setPickerCount(20)
                    .setPickerSpanCount(3)
                    .setActionBarColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"))
                    .setActionBarTitleColor(Color.parseColor("#000000"))
                    .textOnImagesSelectionLimitReached("Limit Reached!")
                    .textOnNothingSelected("Nothing Selected")
                    .setArrayPaths(path)
                    .setAlbumSpanCount(2, 4)
                    .setButtonInAlbumActivity(true)
                    .setCamera(true)
                    .setReachLimitAutomaticClose(true)
                    .setAllViewTitle("All")
                    .setActionBarTitle("Image Library")
                    .startAlbum();


##Result Screen
<img src="/pic/sim.gif" width=40%">

##Android M Permission
FishBun check permission before reading external storage.

<img src="/pic/permission.png" width=40%">

#Contribute
We welcome any contributions.

#License

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
