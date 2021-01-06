# FishBun

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FishBun-green.svg?style=true)](https://android-arsenal.com/details/1/2785)
[![Build Status](https://travis-ci.org/sangcomz/FishBun.svg?branch=master)](https://travis-ci.org/sangcomz/FishBun)
[![codecov](https://codecov.io/gh/sangcomz/FishBun/branch/master/graph/badge.svg)](https://codecov.io/gh/sangcomz/FishBun)
[![Download](https://api.bintray.com/packages/sangcomz/maven/fishbun/images/download.svg)](https://bintray.com/sangcomz/maven/fishbun/_latestVersion)
<p style="float:left;">
 <a href="https://play.google.com/store/apps/details?id=com.sangcomz.fishbundemo">
 <img HEIGHT="40" WIDTH="135" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" />
 </a>
</p>

_FishBun_ is a highly customizable image picker for Android.

<img src="/pic/fishbuns.png">


## What's New in _FishBun_ 1.0.0-alpha05? :tada:

- Refactoring for rapid development:rocket::rocket::rocket:
- Change PickerSpanCount default value (`3`->`4`)
- target SDK 30 support
  - In API 29 or higher, the camera can be used only in the total image folder.
- Fix Issue(#215)
- Change DetailView statusBar color
- Removed Picasso and added Coil
- Support Chinese
- Fix Issue(#223)


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
        .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
        .setAllDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
        .setIsUseAllDoneButton(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
        .setAllViewTitle("All")
        .setMenuAllDoneText("All Done")
        .setActionBarTitle("FishBun Dark")
        .textOnNothingSelected("Please select three or more!")
        .exceptMimeType(listOf(MimeType.GIF))
        .setSpecifyFolderList(arrayListOf("Screenshots", "Camera"))
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
Fishbun 0.10.0 and above only supports projects that have been migrated to [androidx](https://developer.android.com/jetpack/androidx/). For more information, read Google's [migration guide](https://developer.android.com/jetpack/androidx/migrate).

Setting up _FishBun_ requires to add this Gradle configuration:

    repositories {
        jcenter()
    }

    dependencies {
        // Under the Android Plugin 3.0.0. 
        compile 'com.sangcomz:FishBun:1.0.0-alpha05'
        or
        compile 'com.sangcomz:FishBun:0.11.4' //stable version
        
        compile 'io.coil-kt:coil:0.11.0'
        or
        compile 'com.github.bumptech.glide:glide:4.9.0'
                
        // Android plugin 3.0.0 or higher.
        implementation 'com.sangcomz:FishBun:1.0.0-alpha05'
        or
        implementation 'com.sangcomz:FishBun:0.11.4' //stable version
         
        implementation 'io.coil-kt:coil:0.11.0'
        or
        implementation 'com.github.bumptech.glide:glide:4.11.0'

    } 
    
and to allow the following permissions in your `Manifest`:

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

If your app targets Android 11 with compileSdk/targetSdk >= 30 then you will need to add this to the manifest (outside of the application block) in order to capture pictures with the device camera. [Android documentation here](https://developer.android.com/about/versions/11/privacy/package-visibility):

    <queries>
        <intent>
            <action android:name="android.media.action.IMAGE_CAPTURE" />
        </intent>
    </queries>

## How to Use

Use _FishBun_ in an activity:

    FishBun.with(YourActivity).setImageAdapter(new GlideAdapter()).startAlbum();

or in a fragment:

    FishBun.with(YourFragment).setImageAdapter(new CoilAdapter()).startAlbum();

and implement `OnActivityResult`:

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case FishBun.FISHBUN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2

                    path = imageData.getParcelableArrayListExtra(INTENT_PATH);
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
            .setSelectCircleStrokeColor(Color.BLACK)
            .isStartInAllView(false)
            .exceptMimeType(listOf(MimeType.GIF))
            .setSpecifyFolderList(arrayListOf("Screenshots", "Camera"))
            .startAlbum();

### attribute

|            Method Name            | Description                                                           |       Default Value      |
|:---------------------------------:|-----------------------------------------------------------------------|:------------------------:|
|         setSelectedImages         | Set the already selected image                                        | null |
|             setMaxCount           | Maximum number of images selected                                     | 10 |
|             setMinCount           | Minimum number of images selected                                     | 1 |
|            setRequestCode         | Set RequestCode                                                       | 27 |
|    setReachLimitAutomaticClose    | Picker automatically ends when the number of images is selected       | false |
|       exceptMimeType              | Set file type to exclude(gif, png, jpeg, bmp, webp)                   | NONE |
|   setAlbumThumbnailSize           | Thumbnail size of album screen                                        | 70dp |
|      setPickerSpanCount           | Set the picker's span count                                           | 4 |
|      setActionBarColor            | Set background color of action bar, statusBar color, set light theme  | #3F51B5, #303F9F, false  |
|   setActionBarTitleColor          | Set the title color of the action bar                                 | #ffffff |
|   textOnNothingSelected           | Message when nothing is selected                                      | "There is no selected image." |
| textOnImagesSelectionLimitReached | Message when the image is already all selected                        | "Selection full. Deselect an image to choose another." |
|     setButtonInAlbumActivity      | Set Selected button visibility in album screen                        | false |
|       setAlbumSpanCount           | Set the album's span count                                            | 1, 2 |
|   setAlbumSpanCountOnlyLandscape  | Set the album's span count when landscape                             | 2 |
|    setAlbumSpanCountOnlPortrait   | Set the album's span count when portrait                              | 1 |
|       setAllViewTitle             | Set the name of all views                                             | "All view" |
|       setActionBarTitle           | Set the title of the action bar                                       | "Album" |
|    setHomeAsUpIndicatorDrawable   | Customizing back button of the action bar                             | null |
|       setDoneButtonDrawable       | Customizing done button of the action bar                             | null |
|       setAllDoneButtonDrawable    | Customizing all done button of the action bar                         | null |
|       setIsUseAllDoneButton       | Set whether to use the Done button                                    | false |
|       setMenuDoneText             | Set text for Done button                                              | null |
|       setMenuAllDoneText          | Set text for All Done button                                          | null |
|       setMenuTextColor            | Set text color for menu                                               | Integer.MAX_VALUE |
|       setIsUseDetailView          | Set whether to use detail screen                                      | false |
|       setIsShowCount              | Set whether to show counting numbers                                  | false |
|    setSelectCircleStrokeColor     | Set select circle color                                               | #c1ffffff |
|       isStartInAllView            | Set to start with all view                                            | false |
|       setSpecifyFolderList        | Set folder to show                                                    | NONE |
|       hasCameraInPickerPage       | Set whether to use the camera button on picker screen                 | false |


## Android M Permission

Running on Android M, _FishBun_ checks if it has proper permission for you before reading an external storage.

<img src="/pic/permission.png" width="20%">


# Apps using FishBun
## If you are using this library in your app, let me know.

| Project Name | Result Screen   |
|:---------:|---|
| Pandaz(unavailable now)  <p style="float:left;"> |  <img src="/pic/pandaz_result.gif"> |
| Multi photo resize compress crop in batch PicTools  <p style="float:left;"> <a href="https://play.google.com/store/apps/details?id=omkar.tenkale.pictoolsandroid&hl=en_US"> <img HEIGHT="40" WIDTH="135" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" /></a></p> |  <img src="/pic/multi_photo_result.gif"> |


# Contribution

Any suggestions or contributions would be welcomed.
[CONTRIBUTING](https://github.com/sangcomz/FishBun/blob/master/CONTRIBUTING.md)

# Feedback

Bug reports and feature requests can be submitted [here](https://github.com/sangcomz/FishBun/issues) 
(please read the [instructions](https://github.com/sangcomz/FishBun/blob/master/CONTRIBUTING.md) on how to report a bug and request feature).

# License

    Copyright 2019 Seokwon Jeong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
