package com.uonagent.cryptography.diffiehellman

import com.uonagent.cryptography.diffiehellman.naiveecdh.NaiveECDHPrivateKey
import org.junit.jupiter.api.RepeatedTest

internal class ECDHTest {
    @RepeatedTest(10000)
    fun kek() {
        val privateKeyA = NaiveECDHPrivateKey()
        val privateKeyB = NaiveECDHPrivateKey()
        val publicKeyA = privateKeyA.publicKey
        val publicKeyB = privateKeyB.publicKey
        val commonKeyA = publicKeyB.getCommonKey(privateKeyA)
        val commonKeyB = publicKeyA.getCommonKey(privateKeyB)
        assert(commonKeyA.encoded!!.contentEquals(commonKeyB.encoded))
    }
}