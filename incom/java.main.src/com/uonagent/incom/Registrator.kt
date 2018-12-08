package com.uonagent.incom

import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.certificate.Certificate
import java.rmi.Remote

interface Registrator: Remote {
    fun registate(name: String, encryptedPrivateKey: Message, passwordHash: String): Certificate
}