package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.padding.PKCS7

object IDEACFB : SessionCipher {

    override val keySize: Int
        get() = algorithm.keySize

    override val textSize: Int
        get() = algorithm.textSize

    override val algorithm = IDEA
    override val padder = PKCS7
    override val blockCipher = CFB(algorithm, padder)

    override fun encrypt(openText: Any, key: ByteArray) = blockCipher.encrypt(openText, key)

    override fun decrypt(cipherText: ByteArray, key: ByteArray) = blockCipher.decrypt(cipherText, key)

    override fun encrypt(openText: Any, key: String, radix: Int) = blockCipher.encrypt(openText, key, radix)

    override fun decrypt(cipherText: ByteArray, key: String, radix: Int) = blockCipher.decrypt(cipherText, key, radix)

    override fun generateKey(): ByteArray = algorithm.generateKey()

}