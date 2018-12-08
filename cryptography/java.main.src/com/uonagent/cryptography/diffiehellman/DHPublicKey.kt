package com.uonagent.cryptography.diffiehellman

import java.io.Serializable
import java.security.PublicKey

interface DHPublicKey: PublicKey, Serializable {
    fun getCommonKey(privateKey: DHPrivateKey): DHCommonKey
}