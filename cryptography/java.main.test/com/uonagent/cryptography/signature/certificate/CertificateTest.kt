package com.uonagent.cryptography.signature.certificate

import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.rsa.RSASignaturePrivateKey
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

internal class CertificateTest {


    private val centerPrivateKey = RSASignaturePrivateKey()
    private val centerPublicKey = centerPrivateKey.publicKey

    private val userPrivateKey = RSASignaturePrivateKey()
    private val userPublicKey = userPrivateKey.publicKey

    private val today = LocalDate.now()
    private val expDate = today.plusYears(1)
    private val certParams = hashMapOf<String, Any?>(
            "owner" to "1",
            "centerName" to "test",
            "fromDate" to today,
            "untilDate" to expDate,
            "ownerPublicKey" to userPublicKey
    )

    private val cert = Certificate(certParams, centerPrivateKey)
    private val r = Random()

    @Test
    fun kek() {

        val b = ByteArray(1024)
        r.nextBytes(b)
        val f = b.clone()
        f[0] = f[0].inc()

        val message = Message(b)
        val fake = Message(f)
        val signature = userPrivateKey.sign(message)
        val res1 = cert.verify(message, signature, centerPublicKey)
        val res2 = cert.verify(fake, signature, centerPublicKey)
        assert(res1 && !res2)
    }

}