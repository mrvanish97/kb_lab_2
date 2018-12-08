package com.uonagent.cryptography.ellipticcurves

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NaiveECPointTest {

    @Test
    fun plus() {
        val a = 2
        val b = 3
        val m = 97
        val x = 3
        val y = 6
        val p = NaiveECPoint(x, y, a, b, m)
        val exp = arrayListOf(
                NaiveECPoint.ZERO,
                NaiveECPoint(3, 6, a, b, m),
                NaiveECPoint(80, 10, a, b, m),
                NaiveECPoint(80, 87, a, b, m),
                NaiveECPoint(3, 91, a, b, m),
                NaiveECPoint.ZERO,
                NaiveECPoint(3, 6, a, b, m)
        )
        val act = arrayListOf(NaiveECPoint.ZERO)
        while (act.size != exp.size) {
            act.add(act.last() + p)
        }
        assertEquals(exp, act)
    }

    @Test
    fun heh() {
        val a = 2
        val b = 3
        val m = 97
        val p = NaiveECPoint(3, 6, a, b, m)
        val q = NaiveECPoint(95, 31, a, b, m)
        assertEquals(NaiveECPoint(24, 2, a, b, m), p + q)
    }

    @Test
    fun times() {
        val a = 36
        val b = 192
        val m = 257
        assertEquals(NaiveECPoint(241, 114, a, b, m), 149 * NaiveECPoint(4, 20, a, b, m))
    }
}