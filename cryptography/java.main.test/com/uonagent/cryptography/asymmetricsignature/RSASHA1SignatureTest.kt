package com.uonagent.cryptography.asymmetricsignature

import org.junit.jupiter.api.Test

internal class RSASHA1SignatureTest {

    @Test
    fun signAndVerify() {
        val m = ""
        val k = RSASHA1Signature.generateKey()
        val s = RSASHA1Signature.sign(m, k.first, k.second)
        val status = RSASHA1Signature.verify("", s, k.first)
        assert(status)
    }
}