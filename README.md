# FishBun (붕어빵)
[![Build Status](https://travis-ci.org/sangcomz/FishBun.svg?branch=master)](https://travis-ci.org/sangcomz/FishBun)

##Download
You can only use download and import your project(Only FishBun).

##How to Use FishBun

FishBun.with(MainActivity.this).startAlbum();

###and add OnActivityResult

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path = imageData.getStringArrayListExtra(Define.INTENT_PATH); //You can get image path(ArrayList<String>
                    break;
                }
        }
    }

you can use also this

            FishBun.with(MainActivity.this)
                    .setAlbumThumnaliSize(150)
                    .setPickerCount(12)
                    .setArrayPaths(path)
                    .startAlbum();

##Result Screen
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/1.png)
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/2.png)
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/3.png)
![ScreenShot](https://github.com/sangcomz/FishBun/blob/master/pic/4.png)
