package com.uonagent.cryptography.symmetrickey

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IDEACFBTest {

    private val open = hashSetOf<Int>()
    private val key = IDEACFB.generateKey()
    private var cipher: ByteArray? = null

    @Test
    fun encrypt() {
        cipher = IDEACFB.encrypt("", key)
    }

    @Test
    fun decrypt() {
        cipher = IDEACFB.encrypt("", key)
        val n = IDEACFB.decrypt(cipher!!, key)
        val s = n as String
        assertEquals("", s)
    }

}