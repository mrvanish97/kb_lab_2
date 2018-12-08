package com.uonagent.cryptography.asymmetricencryption

import com.uonagent.cryptography.symmetrickey.IDEA
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.lang.Math.abs
import java.math.BigInteger
import java.security.SecureRandom
import java.util.*
import kotlin.test.assertEquals

internal class RSATest {

    @Test
    fun generateKey() {
        val k = RSA.generateKey()
        val k1 = RSA.generateKey()
    }

    @RepeatedTest(1000)
    fun lol() {
        val m = BigInteger(abs(SecureRandom().nextLong()).toString())
        val k = RSA.generateKey()
        val public = k.first
        val private = k.second
        val enc = RSA.encrypt(m, public)
        val d = RSA.decrypt(enc, public, private)
        assertEquals(m, d)
    }

    @RepeatedTest(10000)
    fun kek() {
        val m = IDEA.generateKey()
        val k = RSA.generateKey()
        val public = k.first
        val private = k.second
        val b = BigInteger(1, m)
        val enc = RSA.encrypt(b, public)
        var d = RSA.decrypt(enc, public, private).toByteArray()
        val old = d.copyOf()
        if (d.size == 17) {
            d = Arrays.copyOfRange(d, 1, 17)
        }
        if (d.size < 16) {
            val nd = ByteArray(16) { 0 }
            System.arraycopy(d, 0, nd, 16 - d.size, d.size)
            d = nd
        }
        assertArrayEquals(m, d, "\n${m.contentToString()}\n${d.contentToString()}\n${old.contentToString()}")
    }
}