package com.sangcomz.fishbun

import com.sangcomz.fishbun.ext.equalsMimeType
import junit.framework.Assert.assertEquals
import org.junit.Test

class MimeTypeEqualTest {

    @Test
    @Throws(Exception::class)
    fun check_GIF_isCorrect() {
        assertEquals(MimeType.GIF.equalsMimeType("image/gif"), true)
        assertEquals(MimeType.GIF.equalsMimeType("image/png"), false)
        assertEquals(MimeType.GIF.equalsMimeType("image/jpeg"), false)
        assertEquals(MimeType.GIF.equalsMimeType("image/bmp"), false)
        assertEquals(MimeType.GIF.equalsMimeType("image/webp"), false)
    }

    @Test
    @Throws(Exception::class)
    fun check_PNG_isCorrect() {
        assertEquals(MimeType.PNG.equalsMimeType("image/gif"), false)
        assertEquals(MimeType.PNG.equalsMimeType("image/png"), true)
        assertEquals(MimeType.PNG.equalsMimeType("image/jpeg"), false)
        assertEquals(MimeType.PNG.equalsMimeType("image/bmp"), false)
        assertEquals(MimeType.PNG.equalsMimeType("image/webp"), false)
    }


    @Test
    @Throws(Exception::class)
    fun check_JPEG_isCorrect() {
        assertEquals(MimeType.JPEG.equalsMimeType("image/gif"), false)
        assertEquals(MimeType.JPEG.equalsMimeType("image/png"), false)
        assertEquals(MimeType.JPEG.equalsMimeType("image/jpeg"), true)
        assertEquals(MimeType.JPEG.equalsMimeType("image/bmp"), false)
        assertEquals(MimeType.JPEG.equalsMimeType("image/webp"), false)
    }

    @Test
    @Throws(Exception::class)
    fun check_BMP_isCorrect() {
        assertEquals(MimeType.BMP.equalsMimeType("image/gif"), false)
        assertEquals(MimeType.BMP.equalsMimeType("image/png"), false)
        assertEquals(MimeType.BMP.equalsMimeType("image/jpeg"), false)
        assertEquals(MimeType.BMP.equalsMimeType("image/bmp"), true)
        assertEquals(MimeType.BMP.equalsMimeType("image/webp"), false)
    }

    @Test
    @Throws(Exception::class)
    fun check_WEBP_isCorrect() {
        assertEquals(MimeType.WEBP.equalsMimeType("image/gif"), false)
        assertEquals(MimeType.WEBP.equalsMimeType("image/png"), false)
        assertEquals(MimeType.WEBP.equalsMimeType("image/jpeg"), false)
        assertEquals(MimeType.WEBP.equalsMimeType("image/bmp"), false)
        assertEquals(MimeType.WEBP.equalsMimeType("image/webp"), true)
    }
}