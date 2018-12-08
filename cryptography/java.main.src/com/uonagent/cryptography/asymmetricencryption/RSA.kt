package com.uonagent.cryptography.asymmetricencryption

import java.math.BigInteger
import java.security.SecureRandom
import java.util.concurrent.Callable
import java.util.concurrent.Executors

private const val BIT_LENGHT = 0x400

object RSA {

    private val e = BigInteger("10001", 16)

    private val executor = Executors.newFixedThreadPool(2)

    private val t = Callable<BigInteger> { probablePrime() }

    fun generateKey(): Pair<BigInteger, BigInteger> {
        val pTask = executor.submit(t)
        val qTask = executor.submit(t)
        val p = pTask.get()
        val q = qTask.get()
        val n = p * q
        val phi = (p - BigInteger.ONE) * (q - BigInteger.ONE)
        val d = e.modInverse(phi)
        return Pair(n, d)
    }

    fun encrypt(message: BigInteger, publicKey: BigInteger): BigInteger =
            message.modPow(e, publicKey)

    fun decrypt(cipher: BigInteger, publicKey: BigInteger, privateKey: BigInteger): BigInteger =
            cipher.modPow(privateKey, publicKey)

    private fun probablePrime(): BigInteger {
        var p: BigInteger
        while (true) {
            p = BigInteger.probablePrime(BIT_LENGHT, SecureRandom.getInstanceStrong())
            if (p.isProbablePrime(BIT_LENGHT)) {
                return p
            }
        }
    }
}