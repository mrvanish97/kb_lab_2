package com.uonagent.cryptography.padding

import org.junit.jupiter.api.Test

internal class SHA12PaddingTest {

    @Test
    fun padding() {
        val m = "1000000020000000300000004000000050000000600000007000000".toByteArray(Charsets.UTF_8)
        val p = SHA12Padding.padding(m, 64)
        println(p.contentToString())
    }
}