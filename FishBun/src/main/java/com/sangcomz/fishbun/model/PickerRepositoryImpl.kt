package com.sangcomz.fishbun.model

import android.net.Uri
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.bean.Album
import java.util.concurrent.Future

class PickerRepositoryImpl(private val imageDataSource: ImageDataSource) : PickerRepository {
    override fun getAllMediaThumbnailsPath(
        bucketId: Long,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): Future<List<Uri>> {
        return imageDataSource.getAllMediaThumbnailsPath(
            bucketId,
            exceptMimeTypeList,
            specifyFolderList
        )
    }

    override fun getDirectoryPath(bucketId: Long): Future<String> {
        return imageDataSource.getDirectoryPath(bucketId)
    }

}