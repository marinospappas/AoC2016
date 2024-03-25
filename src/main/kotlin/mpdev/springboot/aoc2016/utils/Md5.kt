package mpdev.springboot.aoc2016.utils

import java.security.MessageDigest


class Md5 {

    val md5: MessageDigest = MessageDigest.getInstance("MD5")

    fun checksum(s: String): ByteArray {
        md5.reset()
        return md5.digest(s.toByteArray())
    }
}