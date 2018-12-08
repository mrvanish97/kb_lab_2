package com.uonagent.cryptography.asymmetricencryption

@Deprecated("шыш")
interface AsymmetricCipher<M, C, P, S> {
    fun generateKey(): Pair<P, S>
    fun encrypt(message: M, publicKey: P): C
    fun decrypt(cipher: C, publicKey: P, privateKey: S): M
}