package com.uonagent.cryptography.diffiehellman.naiveecdh

import com.uonagent.cryptography.diffiehellman.DHPrivateKey
import com.uonagent.cryptography.diffiehellman.NAIVE_ECDH_NAME
import com.uonagent.cryptography.ellipticcurves.NaiveECPoint
import com.uonagent.cryptography.ellipticcurves.times
import com.uonagent.cryptography.extensions.toByteArray
import com.uonagent.cryptography.extensions.toHexString
import com.uonagent.cryptography.extensions.toInt
import java.security.SecureRandom

class NaiveECDHPrivateKey(private val g: NaiveECPoint): DHPrivateKey {
    constructor(): this(defaultG)

    private val d = SecureRandom.getInstanceStrong().nextInt(g.m - 1) + 1

    override fun getAlgorithm() = NAIVE_ECDH_NAME

    private val bytes by lazy { d.toByteArray() }

    override fun getEncoded() = bytes

    override fun getFormat() = bytes.toHexString()

    companion object {
        val defaultG = NaiveECPoint(4, 20, 36, 192, 257)
    }

    override val publicKey by lazy { NaiveECDHPublicKey(d * g) }
}