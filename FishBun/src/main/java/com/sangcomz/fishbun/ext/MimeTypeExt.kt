@file:JvmName("MimeTypeExt")

package com.sangcomz.fishbun.ext

import com.sangcomz.fishbun.MimeType

fun MimeType.equalsMimeType(mimeType: String) = this.type == mimeType