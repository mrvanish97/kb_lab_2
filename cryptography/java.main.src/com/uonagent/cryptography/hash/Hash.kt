package com.uonagent.cryptography.hash

import java.io.Serializable
import java.nio.charset.Charset

interface Hash: Serializable {
    fun calculateHash(obj: Any): ByteArray
    fun calculateHash(string: String): ByteArray
    fun calculateHash(string: String, charset: Charset): ByteArray
    fun calculateHash(byteArray: ByteArray): ByteArray
    val lenght: Int
}