package com.uonagent.cryptography.math

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CommonArithmeticKtTest {

    @Test
    fun pow() {
        assertEquals(5 * 5 * 5, 5 pow 3)
    }
}