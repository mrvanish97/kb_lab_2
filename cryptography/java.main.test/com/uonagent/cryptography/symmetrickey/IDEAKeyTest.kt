package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.message.Message
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class IDEAKeyTest {

    companion object {
        private val random = Random()
    }

    @Test
    fun kek() {
        val k = IDEAKey()
        val b = ByteArray(1024)
        random.nextBytes(b)
        val m = Message(b)
        val p = k.decrypt(k.encrypt(m))
        assertEquals(m, p)
    }
}