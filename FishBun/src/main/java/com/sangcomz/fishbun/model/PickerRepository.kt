package com.sangcomz.fishbun.model

import android.net.Uri
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.bean.Album
import java.util.concurrent.Future

interface PickerRepository {
    fun getAllMediaThumbnailsPath(
        bucketId: Long,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): Future<List<Uri>>

    fun getDirectoryPath(bucketId: Long): Future<String>
}