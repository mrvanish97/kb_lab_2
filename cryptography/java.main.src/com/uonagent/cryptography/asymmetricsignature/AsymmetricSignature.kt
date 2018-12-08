package com.uonagent.cryptography.asymmetricsignature

import com.uonagent.cryptography.hash.Hash
import java.math.BigInteger

@Deprecated("шыш")
interface AsymmetricSignature<P, S> {
    val hash: Hash
    fun generateKey(): Pair<P, S>
    fun sign(message: Any, publicKey: P, privateKey: S): BigInteger
    fun verify(message: Any, signature: BigInteger, publicKey: P): Boolean
}