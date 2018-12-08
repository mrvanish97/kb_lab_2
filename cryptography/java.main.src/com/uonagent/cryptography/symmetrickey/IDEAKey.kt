package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.message.Message
import java.util.*

class IDEAKey : SymmetricKey {

    override fun encrypt(message: Message): Message {
        val m = IDEACFB.encrypt(message.bytes, key)
        return Message(m)
    }

    override fun decrypt(cipher: Message): Message {
        val m = IDEACFB.decrypt(cipher.bytes, key)
        return Message(m as ByteArray)
    }

    private val key: ByteArray

    constructor() {
        key = IDEACFB.generateKey()
    }

    constructor(byteArray: ByteArray) {
        if (byteArray.size == IDEACFB.keySize) {
            key = byteArray
        } else throw IllegalArgumentException()
    }

    constructor(seed: Long) {
        val r = Random(seed)
        key = ByteArray(IDEACFB.keySize)
        r.nextBytes(key)
    }

    override fun getAlgorithm() = "IDEA CFB"
    override fun getEncoded(): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFormat(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}