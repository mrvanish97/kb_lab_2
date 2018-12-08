package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.message.Message
import java.security.PrivateKey

interface SymmetricKey: PrivateKey {
    fun encrypt(message: Message): Message
    fun decrypt(cipher: Message): Message
}