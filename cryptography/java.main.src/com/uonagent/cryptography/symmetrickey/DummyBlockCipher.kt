package com.uonagent.cryptography.symmetrickey

class DummyBlockCipher(override val textSize: Int) : BlockCipherAlgorithm {
    override fun encrypt(openText: ByteArray, key: ByteArray): ByteArray {
        return openText.reversedArray()
    }

    override fun decrypt(cipherText: ByteArray, key: ByteArray): ByteArray {
        return cipherText.reversedArray()
    }

    override fun encrypt(openText: ByteArray, key: String, radix: Int): ByteArray {
        return openText.reversedArray()
    }

    override fun decrypt(cipherText: ByteArray, key: String, radix: Int): ByteArray {
        return cipherText.reversedArray()
    }

    override fun generateKey(): ByteArray {
        return byteArrayOf()
    }

    override val keySize = 0
}