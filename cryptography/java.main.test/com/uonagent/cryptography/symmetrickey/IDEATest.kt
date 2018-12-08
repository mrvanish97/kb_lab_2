package com.uonagent.cryptography.symmetrickey

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.security.SecureRandom

internal class IDEATest {

    private val openText = byteArrayOf(0, 0, 0, 1, 0, 2, 0, 3)
    private val expCipher = byteArrayOf(0x11, 0xFB.toByte(), 0xED.toByte(), 0x2B, 0x01, 0x98.toByte(), 0x6D, 0xE5.toByte())
    private val k = "00010002000300040005000600070008"
    private var r = 16
    private val idea = IDEA


    @Test
    fun encrypt() {
        val enc = idea.encrypt(openText, k, r)
        assertArrayEquals(expCipher, enc)
    }

    @Test
    fun decrypt() {
        val dec = idea.decrypt(expCipher, k, r)
        assertArrayEquals(openText, dec)
    }

    @RepeatedTest(100)
    fun kek() {
        val p = ByteArray(8)
        val g = SecureRandom()
        g.nextBytes(p)
        val k = ByteArray(16)
        g.nextBytes(k)
        val c = idea.encrypt(p, k)
        val d = idea.decrypt(c, k)
        assertArrayEquals(p, d)
    }

    @Test
    fun lol() {
        for (i in 1..10000) {
            val p = ByteArray(8)
            val g = SecureRandom()
            g.nextBytes(p)
            val k = ByteArray(16)
            g.nextBytes(k)
            val c = idea.encrypt(p, k)
            val d = idea.decrypt(c, k)
            if (!arrEq(p, d)) {
                println(p)
                println(c)
                println(d)
                println(k)
            }
        }
    }


    @Test
    fun topkek() {
        val p = byteArrayOf(-4, -52, 88, 34, -1, -56, -88, -104)
        val k = byteArrayOf(-94, -16, 96, 0, 3, -84, -119, 80, 76, -7, 82, -70, 89, 109, -112, 9)
        val c = idea.encrypt(p, k)
        val d = idea.decrypt(c, k)
        assertArrayEquals(p, d)
    }

    private fun arrEq(a: ByteArray, b: ByteArray): Boolean {
        for (i in a.indices) {
            if (a[i] != b[i]) {
                return false
            }
        }
        return true
    }
}