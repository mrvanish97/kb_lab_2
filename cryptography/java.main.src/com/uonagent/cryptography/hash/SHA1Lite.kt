package com.uonagent.cryptography.hash

import java.math.BigInteger
import java.nio.charset.Charset

object SHA1Lite : Hash {

    val sha = SHA1

    override fun calculateHash(obj: Any) = sha.calculateHash(obj).compress()

    override fun calculateHash(string: String) = sha.calculateHash(string).compress()

    override fun calculateHash(string: String, charset: Charset) = sha.calculateHash(string, charset).compress()

    override fun calculateHash(byteArray: ByteArray) = sha.calculateHash(byteArray).compress()

    private fun ByteArray.compress() = byteArrayOf(this[0])

    override val lenght = 1
}