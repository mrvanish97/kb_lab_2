package com.uonagent.cryptography.hash

import org.junit.jupiter.api.RepeatedTest
import java.util.*

internal class HashTest {
    val h = SHA1

    companion object {
        private val rand = Random()
    }

    @RepeatedTest(10000)
    fun kek() {
        val b = ByteArray(1024)
        rand.nextBytes(b)
        val fakeB = ByteArray(1024)
        System.arraycopy(b, 0, fakeB, 0, 1024)
        fakeB[1023] = fakeB[1023].inc()
        assert(!h.calculateHash(b).contentEquals(h.calculateHash(fakeB)))
    }
}