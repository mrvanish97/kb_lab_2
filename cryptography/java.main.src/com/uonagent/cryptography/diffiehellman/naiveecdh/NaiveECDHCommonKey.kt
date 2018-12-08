package com.uonagent.cryptography.diffiehellman.naiveecdh

import com.uonagent.cryptography.diffiehellman.DHCommonKey
import com.uonagent.cryptography.ellipticcurves.NaiveECPoint
import com.uonagent.cryptography.extensions.toHexString

class NaiveECDHCommonKey(val s: NaiveECPoint): DHCommonKey {

    override fun getAlgorithm(): String = "Naive NaiveECDHPrivateKey"

    override fun getEncoded() = s.coordinatesToByteArray()

    override fun getFormat() = encoded.toHexString()

}