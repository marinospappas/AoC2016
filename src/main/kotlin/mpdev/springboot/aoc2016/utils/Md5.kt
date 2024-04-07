package mpdev.springboot.aoc2016.utils

import java.security.MessageDigest


class Md5 {

    private val md5: MessageDigest = MessageDigest.getInstance("MD5")

    fun checksum(s: String): ByteArray {
        md5.reset()
        return md5.digest(s.toByteArray())
    }

    fun checksum(ba: ByteArray): ByteArray {
        md5.reset()
        return md5.digest(ba)
    }
}

fun ByteArray.toHexString(): String{
    val hexChars = "0123456789abcdef".toCharArray()
    var s = ""
    forEach {
        val byte = it.toInt()
        s += "${hexChars[byte ushr 4 and 0x0F]}${hexChars[byte and 0x0F]}"
    }
    return s
}