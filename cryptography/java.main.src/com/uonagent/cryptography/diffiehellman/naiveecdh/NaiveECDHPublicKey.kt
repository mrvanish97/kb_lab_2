package com.uonagent.cryptography.diffiehellman.naiveecdh

import com.uonagent.cryptography.diffiehellman.DHCommonKey
import com.uonagent.cryptography.diffiehellman.DHPrivateKey
import com.uonagent.cryptography.diffiehellman.DHPublicKey
import com.uonagent.cryptography.diffiehellman.NAIVE_ECDH_NAME
import com.uonagent.cryptography.ellipticcurves.NaiveECPoint
import com.uonagent.cryptography.ellipticcurves.times
import com.uonagent.cryptography.extensions.toHexString
import com.uonagent.cryptography.extensions.toInt

class NaiveECDHPublicKey(private val h: NaiveECPoint): DHPublicKey {

    override fun getCommonKey(privateKey: DHPrivateKey): DHCommonKey {
        val d = privateKey.encoded.toInt()
        return NaiveECDHCommonKey(d * h)
    }

    override fun getAlgorithm() = NAIVE_ECDH_NAME

    private val bytes by lazy { h.coordinatesToByteArray() }

    override fun getEncoded() = bytes

    override fun getFormat() = bytes.toHexString()

}