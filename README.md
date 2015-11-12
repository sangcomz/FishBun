# FishBun (붕어빵)         ![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/fish_bun_icon_small.png)

[![Build Status](https://travis-ci.org/sangcomz/FishBun.svg?branch=master)](https://travis-ci.org/sangcomz/FishBun)  [![Download](https://api.bintray.com/packages/sangcomz/maven/fishbun/images/download.svg)](https://bintray.com/sangcomz/maven/fishbun/_latestVersion)

##How to Use FishBun

###Gradle

    repositories {
        jcenter()
    }
    
    
    dependencies {
        compile 'com.sangcomz:FishBun:0.0.2@aar'
    }

###Manifest

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

FishBun.with(MainActivity.this).startAlbum();

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
                    .setPickerCount(12)//you can restrict photo count
                    .setArrayPaths(path)//you can choice again.
                    .startAlbum();


##Result Screen
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/1.png)
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/2.png)
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/3.png)
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/4.png)

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
