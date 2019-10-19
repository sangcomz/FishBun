@file:JvmName("RegexUtil")

package com.sangcomz.fishbun.util

private val GIF_PATTERN = """(.+?).gif$""".toRegex()

fun checkGif(path: String) = path.matches(GIF_PATTERN)
