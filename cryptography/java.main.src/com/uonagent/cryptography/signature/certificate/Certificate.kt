package com.uonagent.cryptography.signature.certificate

import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.SignaturePrivateKey
import com.uonagent.cryptography.signature.SignaturePublicKey
import java.io.Serializable
import java.security.SignatureException
import java.security.cert.CertificateException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.BASIC_ISO_DATE
private val utf8 = Charsets.UTF_8

open class Certificate(map: Map<String, Any?>, centerPrivateKey: SignaturePrivateKey): Serializable {

    val owner: String by map
    val centerName: String by map
    val fromDate: LocalDate by map
    val untilDate: LocalDate by map
    val ownerPublicKey: SignaturePublicKey by map

    private val bytes= makeByteArray()

    private val message = Message(bytes)

    val signature = signCertificate(centerPrivateKey)

    private fun makeByteArray(): ByteArray {
        val l = arrayListOf<Byte>()
        var buffer = owner.toByteArray(utf8)
        buffer.forEach { l.add(it) }
        buffer = centerName.toByteArray(utf8)
        buffer.forEach { l.add(it) }
        buffer = fromDate.format(dateFormatter).toByteArray(utf8)
        buffer.forEach { l.add(it) }
        buffer = untilDate.format(dateFormatter).toByteArray(utf8)
        buffer.forEach { l.add(it) }
        buffer = ownerPublicKey.encoded
        buffer.forEach { l.add(it) }
        return l.toByteArray()
    }

    fun verify(message: Message, signature: Message, centerPublicKey: SignaturePublicKey): Boolean {
        verifyCertificate(centerPublicKey)
        return ownerPublicKey.verify(message, signature)
    }

    private fun verifyCertificate(centerPublicKey: SignaturePublicKey) {
        val time = LocalDate.now()
        if (time >= untilDate || time < fromDate) {
            throw CertificateException("Certificate is no longer valid")
        }
        val s = centerPublicKey.verify(message, signature)
        if (!s) throw SignatureException("Certificate's signature is invalid")
    }

    fun signCertificate(centerPrivateKey: SignaturePrivateKey): Message {
        return centerPrivateKey.sign(message)
    }

    override fun toString(): String {
        return "Owner: $owner, Certification center: $centerName, " +
                "Valid from ${fromDate.format(DateTimeFormatter.ISO_DATE)} until " +
                "${untilDate.format(DateTimeFormatter.ISO_DATE)}\nPublic Key: ${ownerPublicKey.format}"
    }
}