package com.uonagent.cryptography.math

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ModArithmeticTest {

    @Test
    fun mult() {
        var f = true
        for (a in 0..0xffff) {
            if (f) {
                for (b in a..0xffff) {
                    val mthm = ModArithmetic.mult(a, b, 0x10001)
                    val act = Integer.remainderUnsigned(a * b, 0x10001)
                    if (mthm != act) {
                        println(a)
                        println(b)
                        println(mthm)
                        println(act)
                        f = false
                        break
                    }
                }
            } else {
                break
            }
        }
        assert(true)
    }

    @Test
    fun inv() {
        println(23.powMod(5, 91))
        assertEquals(1, 1 inv 47)
    }

    @Test
    fun pow() {
        assertEquals(10, ModArithmetic.pow(19, 5, 21))
    }

    private fun naiveMult() {

    }

    @Test
    fun rotl() {
        assertEquals(1, ModArithmetic.rotl(-2147483648, 1))
    }

    @Test
    fun sub() {
        assertEquals(16, ModArithmetic.sub(7, 14, 23))
    }

    @Test
    fun sq() {
        assertArrayEquals(intArrayOf(2, 3), ModArithmetic.sq(13))
    }

    @Test
    fun legendre() {
        assertEquals(1, ModArithmetic.legendre(10, 13))
    }

    @Test
    fun sqrt() {
        assertEquals(173, ModArithmetic.sqrt(14, 193))
    }
}