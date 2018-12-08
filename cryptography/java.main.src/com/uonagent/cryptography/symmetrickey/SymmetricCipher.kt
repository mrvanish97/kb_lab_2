package com.uonagent.cryptography.symmetrickey

@Deprecated("шыш")
interface SymmetricCipher<O, C, K> {
    fun encrypt(openText: O, key: K): C
    fun decrypt(cipherText: C, key: K): O
    fun encrypt(openText: O, key: String, radix: Int): C
    fun decrypt(cipherText: C, key: String, radix: Int): O
    fun generateKey(): K
}