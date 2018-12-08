package com.uonagent.cryptography.asymmetricencryption

import com.uonagent.cryptography.message.Message
import java.io.Serializable
import java.security.PrivateKey

interface EncryptionPrivateKey: PrivateKey, Serializable {
    val publicKey: EncryptionPublicKey
    fun encrypt(message: Message): Message
}