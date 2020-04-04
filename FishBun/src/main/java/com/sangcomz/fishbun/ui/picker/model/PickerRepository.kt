package com.sangcomz.fishbun.ui.picker.model

import android.net.Uri
import com.sangcomz.fishbun.MimeType
import java.util.concurrent.Future

interface PickerRepository {
    fun getAllMediaThumbnailsPath(bucketId: Long): Future<List<Uri>>

    fun getDirectoryPath(bucketId: Long): Future<String>
}