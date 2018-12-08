package com.uonagent.cryptography.signature.rsa

import com.uonagent.cryptography.asymmetricencryption.RSA
import com.uonagent.cryptography.extensions.toBigInt
import com.uonagent.cryptography.extensions.toBytesSized
import com.uonagent.cryptography.extensions.toHexString
import com.uonagent.cryptography.hash.Hash
import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.RSA_NAME
import com.uonagent.cryptography.signature.SignaturePublicKey
import java.math.BigInteger

class RSASignaturePublicKey(private val value: BigInteger, private val hash: Hash): SignaturePublicKey {

    override fun hash(message: Message) = hash.calculateHash(message.bytes)

    override fun verify(message: Message, signature: Message): Boolean {
        val actualHash = hash(message)
        val expHash = RSA.encrypt(signature.bytes.toBigInt(), value).toBytesSized(hash.lenght)
        return actualHash.contentEquals(expHash)
    }

    private val bytes: ByteArray by lazy { value.toByteArray() }

    override fun getAlgorithm(): String = RSA_NAME

    override fun getEncoded() = bytes

    override fun getFormat() = bytes.toHexString()
}