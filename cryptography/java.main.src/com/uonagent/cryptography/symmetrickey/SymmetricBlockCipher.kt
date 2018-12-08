package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.padding.Padding

@Deprecated("шыш")
interface SymmetricBlockCipher: SymmetricCipher<Any, ByteArray, ByteArray> {
    val algorithm: BlockCipherAlgorithm
    val padder: Padding
}