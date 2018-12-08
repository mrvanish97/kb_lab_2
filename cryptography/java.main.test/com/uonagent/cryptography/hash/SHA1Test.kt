package com.uonagent.cryptography.hash

import com.uonagent.cryptography.extensions.toHexString
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SHA1Test {

    @Test
    fun calculate() {
        val s = "ukmjynhtbgrvfecd"
        val h = SHA1.calculateHash(s)
        println(h.toHexString())
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", h.toHexString())
    }
}