package com.sangcomz.fishbun.datasource

interface CameraDataSource {
    fun getCameraPath(): String
    fun getPicturePath(): String?
}