package mpdev.springboot.aoc2016.utils

import java.security.MessageDigest


class Md5 {

    private val md5: MessageDigest = MessageDigest.getInstance("MD5")

    fun checksum(s: String): ByteArray {
        md5.reset()
        return md5.digest(s.toByteArray())
    }

    fun toString(md5sum: ByteArray): String = md5sum.map { String.format("%02x", it) }.joinToString("")
}