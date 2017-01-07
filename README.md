# FishBun

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FishBun-green.svg?style=true)](https://android-arsenal.com/details/1/2785)
[![Build Status](https://travis-ci.org/sangcomz/FishBun.svg?branch=master)](https://travis-ci.org/sangcomz/FishBun)
[![codecov](https://codecov.io/gh/sangcomz/FishBun/branch/master/graph/badge.svg)](https://codecov.io/gh/sangcomz/FishBun)
[![Download](https://api.bintray.com/packages/sangcomz/maven/fishbun/images/download.svg)](https://bintray.com/sangcomz/maven/fishbun/_latestVersion)

<a href="http://www.methodscount.com/?lib=com.sangcomz%3AFishBun%3A0.6.1"><img src="https://img.shields.io/badge/Methods and size-core: 461 | deps: 24948 | 95 KB-e91e63.svg"/></a>

FishBun is Image Picker for android.

<img src="/pic/fishbuns.png">

##What's New 0.6.1 FishBun

####● Glide to Picasso

##How to Use FishBun

###Gradle

    repositories {
        jcenter()
    }
    
    
    dependencies {
        compile('com.sangcomz:FishBun:0.6.1@aar') {
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
                    path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    //You can get image path(ArrayList<String>
                    break;
                }
        }
    }

you can use also this

            FishBun.with(MainActivity.this)
                    .setAlbumThumnaliSize(150)//you can resize album thumnail size
                    .setActionBarColor(Color.BLACK, Color.BLUE) // actionBar and StatusBar color
            //        .setActionBarColor(Color.BLACK)           // only actionbar color
                    .setPickerCount(12)//you can restrict photo count
                    .setArrayPaths(path)//you can choice again.
                    .setPickerSpanCount(5)
                    .setRequestCode(11) //request code is 11. default == Define.ALBUM_REQUEST_CODE(27)
                    .setCamera(true)//you can use camera
                    .textOnImagesSelectionLimitReached("Limit Reached!")
                    .textOnNothingSelected("Nothing Selected")
                    .setButtonInAlbumActiviy(true)
                    .setReachLimitAutomaticClose(true)
                    .setAlbumSpanCount(2, 4)
                    .startAlbum();


##Result Screen
<img src="/pic/sim.gif" width=40%">

##Android M Permission
FishBun check permission before reading external storage.

<img src="/pic/permission.png" width=40%">

#Contribute
We welcome any contributions.

#License

    Copyright 2015 Jeong Seok-Won

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.