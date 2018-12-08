package com.uonagent.cryptography.signature

import com.uonagent.cryptography.hash.SHA1
import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.ecdsa.NaiveECDSAPrivateKey
import com.uonagent.cryptography.signature.rsa.RSASignaturePrivateKey
import org.junit.jupiter.api.RepeatedTest
import java.util.*

internal class RSASignatureRoutineTest {

    companion object {
        private val rand = Random()
    }

    @RepeatedTest(10000)
    fun kek() {
        val b = ByteArray(1024)
        rand.nextBytes(b)
        val fakeB = b.clone()[1023].inc()
        val message = Message(b)
        val fake = Message(fakeB)
        val privateKey = NaiveECDSAPrivateKey()
        val signature = privateKey.sign(message)
        val publicKey = privateKey.publicKey
        val resultPlainMessage = publicKey.verify(message, signature)
        val resultFakeMessage = publicKey.verify(fake, signature)
        assert(resultPlainMessage && !resultFakeMessage)
    }
}