package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.padding.PKCS7
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

internal class PKCS7Test {

    private val orig = byteArrayOf(1, 1, 1, 1, 1, 1, 1)
    private val blockSize = 8
    private val expPadded = byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1)

    @Test
    fun padding() {
        val p = PKCS7.padding(orig, blockSize)
        assertArrayEquals(expPadded, p)
    }

    @Test
    fun depadding() {
        val d = PKCS7.depadding(expPadded, blockSize)
        assertArrayEquals(orig, d)
    }
}