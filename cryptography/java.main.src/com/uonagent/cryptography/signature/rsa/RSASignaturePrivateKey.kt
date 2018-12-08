package com.uonagent.cryptography.signature.rsa

import com.uonagent.cryptography.asymmetricencryption.RSA
import com.uonagent.cryptography.extensions.toBigInt
import com.uonagent.cryptography.extensions.toHexString
import com.uonagent.cryptography.hash.Hash
import com.uonagent.cryptography.hash.SHA1
import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.RSA_NAME
import com.uonagent.cryptography.signature.SignaturePrivateKey

class RSASignaturePrivateKey(private val hash: Hash): SignaturePrivateKey {

    constructor(): this(SHA1)

    private val key = RSA.generateKey()

    override val publicKey = RSASignaturePublicKey(key.first, hash)

    override fun hash(message: Message) = hash.calculateHash(message.bytes)

    override fun sign(message: Message): Message {
        val messageHash = hash(message)
        val bigIntMessage = messageHash.toBigInt()
        val bigIntSign = RSA.decrypt(bigIntMessage, key.first, key.second)
        return Message(bigIntSign)
    }

    override fun getAlgorithm() = RSA_NAME

    private val bytes: ByteArray by lazy { key.second.toByteArray() }

    override fun getEncoded() = bytes

    override fun getFormat() = bytes.toHexString()
}