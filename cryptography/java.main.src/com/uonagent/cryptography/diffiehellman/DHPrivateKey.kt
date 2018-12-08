package com.uonagent.cryptography.diffiehellman

import java.io.Serializable
import java.security.PrivateKey

interface DHPrivateKey: PrivateKey, Serializable {
    val publicKey: DHPublicKey
}