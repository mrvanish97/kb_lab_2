package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.padding.Padding
import com.uonagent.cryptography.serializer.Serializer
import java.security.SecureRandom
import java.util.*
import kotlin.experimental.xor

class CFB(override val algorithm: BlockCipherAlgorithm,
          override val padder: Padding) : SymmetricBlockCipher {

    private val generator = SecureRandom()

    override fun encrypt(openText: Any, key: ByteArray): ByteArray {
        val e: (ByteArray) -> ByteArray = { algorithm.encrypt(it, key) }
        return encr(openText, e)
    }

    override fun decrypt(cipherText: ByteArray, key: ByteArray): Any {
        val e: (ByteArray) -> ByteArray = { algorithm.encrypt(it, key) }
        return decr(cipherText, e)
    }

    override fun encrypt(openText: Any, key: String, radix: Int): ByteArray {
        val e: (ByteArray) -> ByteArray = { algorithm.encrypt(it, key, radix) }
        return encr(openText, e)
    }

    override fun decrypt(cipherText: ByteArray, key: String, radix: Int): Any {
        val e: (ByteArray) -> ByteArray = { algorithm.encrypt(it, key, radix) }
        return decr(cipherText, e)
    }

    private fun encr(openText: Any, e: (ByteArray) -> ByteArray): ByteArray {
        val p = Serializer.serialize(openText, padder, algorithm.textSize)
        val iv = ByteArray(algorithm.textSize)
        generator.nextBytes(iv)
        val c = ByteArray(p.size + algorithm.textSize)
        System.arraycopy(iv, 0, c, 0, iv.size) // c_0 = IV

        var cEnc: ByteArray
        var cPrev: ByteArray
        var x: ByteArray

        for (i in algorithm.textSize until c.size step algorithm.textSize) {
            cPrev = Arrays.copyOfRange(c, i - algorithm.textSize, i)
            cEnc = e(cPrev)
            x = xorArr(cEnc, p, 0, i - algorithm.textSize, algorithm.textSize)
            System.arraycopy(x, 0, c, i, algorithm.textSize)
        }

        return c
    }

    private fun decr(c: ByteArray, e: (ByteArray) -> ByteArray): Any {

        var cEnc: ByteArray
        var cPrev: ByteArray
        var x: ByteArray

        val p = ByteArray(c.size - algorithm.textSize)

        for (i in algorithm.textSize until c.size step algorithm.textSize) {
            cPrev = Arrays.copyOfRange(c, i - algorithm.textSize, i)
            cEnc = e(cPrev)
            x = xorArr(cEnc, c, 0, i, algorithm.textSize)
            System.arraycopy(x, 0, p, i - algorithm.textSize, algorithm.textSize)
        }
        return Serializer.deserialize(p, padder, algorithm.textSize)
    }

    fun xorArr(a: ByteArray, b: ByteArray, aInd: Int, bInd: Int, size: Int): ByteArray {
        val xored = ByteArray(a.size)
        for (i in 0 until size) {
            xored[i] = a[i + aInd] xor b[i + bInd]
        }
        return xored
    }

    override fun generateKey() = algorithm.generateKey()

}