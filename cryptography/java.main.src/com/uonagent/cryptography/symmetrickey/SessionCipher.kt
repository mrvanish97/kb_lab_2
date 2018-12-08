package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.padding.Padding

@Deprecated("шыш")
interface SessionCipher : SymmetricCipher<Any, ByteArray, ByteArray> {
    val algorithm: BlockCipherAlgorithm
    val padder: Padding
    val blockCipher: SymmetricBlockCipher
    val textSize: Int
    val keySize: Int
}