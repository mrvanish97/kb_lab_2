package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.padding.PKCS7
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CFBTest {

    private val a = IDEA
    private val c = CFB(a, PKCS7)
    private val k = a.generateKey()

    @Test
    fun encrypt() {
        c.encrypt("2", k)
    }

    @Test
    fun decrypt() {
    }

    @Test
    fun lol() {
        val testString = hashSetOf(1, 2, 55)
        val s = c.decrypt(c.encrypt(testString, k), k) as Set<*>
        assertEquals(testString, s)
    }
}