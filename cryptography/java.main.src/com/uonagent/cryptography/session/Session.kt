package com.uonagent.cryptography.session

import com.uonagent.cryptography.asymmetricencryption.AsymmetricCipher
import com.uonagent.cryptography.extensions.toBigInt
import com.uonagent.cryptography.extensions.toBytesSized
import com.uonagent.cryptography.symmetrickey.SessionCipher
import java.math.BigInteger
import java.util.*

private const val SESSION_NOT_STARTED = "Session initialization has been not completed yet"

@Deprecated("Шыш")
class Session(private val asymmetricCipher: AsymmetricCipher<BigInteger, BigInteger, BigInteger, BigInteger>,
              private val symmetricCipher: SessionCipher) {

    private var hasPrivateKey = false
    private var privateKey: BigInteger? = null
    private lateinit var sessionKey: ByteArray
    lateinit var publicKey: BigInteger
    private var status = false

    fun initializeSession(): BigInteger {
        status = false
        hasPrivateKey = true
        val k = asymmetricCipher.generateKey()
        privateKey = k.second
        publicKey = k.first
        return publicKey
    }

    fun createSessionKey(publicKey: BigInteger): BigInteger {
        hasPrivateKey = false
        this.publicKey = publicKey
        sessionKey = symmetricCipher.generateKey()
        status = true
        return asymmetricCipher.encrypt(sessionKey.toBigInt(), publicKey)
    }

    fun acceptSessionKey(cipherSessionKey: BigInteger) {
        if (!hasPrivateKey) {
            throw IllegalStateException("You have to be the owner of the private key to proceed this")
        }
        sessionKey = asymmetricCipher.decrypt(cipherSessionKey, publicKey, privateKey!!)
                .toBytesSized(symmetricCipher.keySize)
        status = true
    }

    fun encrypt(message: Any): ByteArray {
        if (!status) {
            throw IllegalStateException(SESSION_NOT_STARTED)
        }
        return symmetricCipher.encrypt(message, sessionKey)
    }

    fun decrypt(cipher: ByteArray): Any {
        if (!status) {
            throw IllegalStateException(SESSION_NOT_STARTED)
        }
        return symmetricCipher.decrypt(cipher, sessionKey)
    }

    private fun bigIntToBytes(bigInteger: BigInteger): ByteArray {
        var d = bigInteger.toByteArray()
        if (d.size > symmetricCipher.keySize) {
            d = Arrays.copyOfRange(d, d.size - symmetricCipher.keySize, d.size)
        }
        if (d.size < symmetricCipher.keySize) {
            val nd = ByteArray(symmetricCipher.keySize) { 0 }
            System.arraycopy(d, 0, nd, symmetricCipher.keySize - d.size, d.size)
            d = nd
        }
        return d
    }
}

