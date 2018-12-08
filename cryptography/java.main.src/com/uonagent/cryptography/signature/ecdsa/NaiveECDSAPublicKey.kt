package com.uonagent.cryptography.signature.ecdsa

import com.uonagent.cryptography.ellipticcurves.NaiveECPoint
import com.uonagent.cryptography.ellipticcurves.times
import com.uonagent.cryptography.extensions.oneByteToInt
import com.uonagent.cryptography.extensions.takeIntFromPosition
import com.uonagent.cryptography.extensions.toHexString
import com.uonagent.cryptography.extensions.toInt
import com.uonagent.cryptography.hash.Hash
import com.uonagent.cryptography.math.inv
import com.uonagent.cryptography.math.multMod
import com.uonagent.cryptography.math.sumMod
import com.uonagent.cryptography.math.umod
import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.NAIVE_ECDSA_NAME
import com.uonagent.cryptography.signature.SignaturePublicKey

class NaiveECDSAPublicKey(private val h: NaiveECPoint,
                          private val g: NaiveECPoint,
                          private val hash: Hash): SignaturePublicKey {

    override fun hash(message: Message) = hash.calculateHash(message.bytes)

    override fun verify(message: Message, signature: Message): Boolean {
        val signatureBytes = signature.bytes
        val m = h.m
        val z = hash(message).oneByteToInt()

        infix fun Int.mult(b: Int) = this.multMod(b, m)
        infix fun Int.sum(b: Int) = this.sumMod(b, m)

        val r = signatureBytes.takeIntFromPosition(0)
        val s = signatureBytes.takeIntFromPosition(4)
        val a = (s inv m) mult z
        val b = (s inv m) mult r
        val p = (a * g) + (b * h)
        val x = p.x
        return r umod m == x umod m
    }

    private val bytes by lazy { h.coordinatesToByteArray() }

    override fun getAlgorithm() = NAIVE_ECDSA_NAME

    override fun getEncoded() = bytes.clone()

    override fun getFormat() = bytes.toHexString()
}