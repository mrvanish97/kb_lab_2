package com.uonagent.cryptography.signature

import com.uonagent.cryptography.message.Message
import java.io.Serializable
import java.security.PrivateKey

interface SignaturePrivateKey: PrivateKey, Serializable {
    fun hash(message: Message): ByteArray
    fun sign(message: Message): Message
    val publicKey: SignaturePublicKey
}