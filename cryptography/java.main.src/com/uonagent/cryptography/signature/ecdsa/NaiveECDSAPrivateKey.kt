package com.uonagent.cryptography.signature.ecdsa

import com.uonagent.cryptography.diffiehellman.naiveecdh.NaiveECDHPrivateKey
import com.uonagent.cryptography.ellipticcurves.NaiveECPoint
import com.uonagent.cryptography.ellipticcurves.times
import com.uonagent.cryptography.extensions.oneByteToInt
import com.uonagent.cryptography.extensions.toByteArray
import com.uonagent.cryptography.extensions.toInt
import com.uonagent.cryptography.hash.Hash
import com.uonagent.cryptography.hash.SHA1Lite
import com.uonagent.cryptography.math.inv
import com.uonagent.cryptography.math.multMod
import com.uonagent.cryptography.math.sumMod
import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.*
import java.security.SecureRandom

class NaiveECDSAPrivateKey(private val g: NaiveECPoint, private val hash: Hash): SignaturePrivateKey {

    constructor(): this(defaultG, SHA1Lite)

    companion object {
        val defaultG = NaiveECPoint(4, 20, 36, 192, 257)
    }
    private val key = NaiveECDHPrivateKey(g)
    private val d = key.encoded.toInt()
    private var k = 0
    private var r = 0
    private var s = 0
    private lateinit var p: NaiveECPoint

    override fun hash(message: Message) = hash.calculateHash(message.bytes)

    override fun sign(message: Message): Message {
        infix fun Int.mult(b: Int) = this.multMod(b, g.m)
        infix fun Int.sum(b: Int) = this.sumMod(b, g.m)
        val z = hash(message).oneByteToInt()
        val random = SecureRandom.getInstanceStrong()
        while (true) {
            k = random.nextInt(g.m - 1) + 1
            p = k * g
            r = p.x % g.m
            if (r == 0) {
                continue
            }
            s = (k inv g.m) mult (z sum (r mult d))
            if (s != 0) {
                break
            }
        }
        return Message(makeByteArray(r, s))
    }

    private fun makeByteArray(r: Int, s: Int): ByteArray {
        val byteArray = ByteArray(8)
        var buffer = r.toByteArray()
        System.arraycopy(buffer, 0, byteArray, 0, 4)
        buffer = s.toByteArray()
        System.arraycopy(buffer, 0, byteArray, 4, 4)
        return byteArray
    }

    override val publicKey by lazy { NaiveECDSAPublicKey(d * g, g, hash) }

    override fun getAlgorithm() = NAIVE_ECDSA_NAME

    override fun getEncoded() = key.encoded

    override fun getFormat() = key.format
}