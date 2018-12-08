package com.uonagent.cryptography.symmetrickey

@Deprecated("шыш")
interface BlockCipherAlgorithm: SymmetricCipher<ByteArray, ByteArray, ByteArray> {
    val textSize: Int
    val keySize: Int
}