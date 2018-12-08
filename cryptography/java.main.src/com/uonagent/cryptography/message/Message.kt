package com.uonagent.cryptography.message

import com.uonagent.cryptography.extensions.toHexString
import com.uonagent.cryptography.serializer.Serializer
import java.io.Serializable
import java.math.BigInteger
import java.nio.charset.Charset

open class Message(byteArray: ByteArray): Serializable {

    protected var byteArray: ByteArray = ByteArray(byteArray.size)

    init {
        System.arraycopy(byteArray, 0, this.byteArray, 0, byteArray.size)
    }

    constructor(obj: Any): this(Serializer.serialize(obj))

    constructor(string: String, charset: Charset): this(string.toByteArray(charset))

    constructor(string: String): this(string, Charsets.UTF_8)

    constructor(bigInteger: BigInteger): this(bigInteger.toByteArray())

    val bytes = byteArray.clone()

    val format by lazy { byteArray.toHexString() }

    fun getObject() = Serializer.deserialize(byteArray)

    override fun equals(other: Any?): Boolean {
        return byteArray.contentEquals((other as Message).byteArray)
    }

    override fun hashCode(): Int {
        var result = byteArray.contentHashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}