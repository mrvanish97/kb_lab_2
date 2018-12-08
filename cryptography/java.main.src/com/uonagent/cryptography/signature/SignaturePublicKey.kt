package com.uonagent.cryptography.signature

import com.uonagent.cryptography.hash.Hash
import com.uonagent.cryptography.message.Message
import java.io.Serializable
import java.security.PublicKey

interface SignaturePublicKey: PublicKey, Serializable {
    fun hash(message: Message): ByteArray
    fun verify(message: Message, signature: Message): Boolean
}