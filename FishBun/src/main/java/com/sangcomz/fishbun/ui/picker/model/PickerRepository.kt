package com.sangcomz.fishbun.ui.picker.model

import android.net.Uri
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.util.future.CallableFutureTask
import java.util.concurrent.Future

interface PickerRepository {
    fun getAllMediaThumbnailsPath(bucketId: Long): CallableFutureTask<List<Uri>>

    fun getDirectoryPath(bucketId: Long): CallableFutureTask<String>
}