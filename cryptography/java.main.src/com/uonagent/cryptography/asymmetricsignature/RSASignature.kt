package com.uonagent.cryptography.asymmetricsignature

import com.uonagent.cryptography.asymmetricencryption.RSA
import com.uonagent.cryptography.extensions.toBigIntHash
import com.uonagent.cryptography.hash.Hash
import java.math.BigInteger

@Deprecated("шыш")
open class RSASignature(override val hash: Hash) : AsymmetricSignature<BigInteger, BigInteger> {

    private val rsa = RSA

    override fun generateKey() = rsa.generateKey()

    override fun sign(message: Any, publicKey: BigInteger, privateKey: BigInteger): BigInteger {
        val bigIntHash = message.toBigIntHash()
        return rsa.decrypt(bigIntHash, publicKey, privateKey)
    }

    override fun verify(message: Any, signature: BigInteger, publicKey: BigInteger): Boolean {
        val bigIntHash = message.toBigIntHash()
        val recovered = rsa.encrypt(signature, publicKey)
        return bigIntHash.compareTo(recovered) == 0
    }
}