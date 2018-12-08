package com.uonagent.cryptography.serializer

import com.uonagent.cryptography.padding.DummyPadding
import com.uonagent.cryptography.padding.Padding
import java.io.*

object Serializer {

    fun serialize(obj: Any) = serialize(obj, DummyPadding, 0)
    fun deserialize(data: ByteArray) = deserialize(data, DummyPadding, 0)

    @Throws(IOException::class)
    fun serialize(obj: Any, padder: Padding, textSize: Int): ByteArray {
        val out = ByteArrayOutputStream()
        val os = ObjectOutputStream(out)
        os.writeObject(obj)
        return padder.padding(out.toByteArray(), textSize)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserialize(data: ByteArray, padder: Padding, textSize: Int): Any {
        val depadded = padder.depadding(data, textSize)
        val inp = ByteArrayInputStream(depadded)
        val ins = ObjectInputStream(inp)
        return ins.readObject()
    }
}

fun Any.toBytes() = Serializer.serialize(this)

fun ByteArray.toObject() = Serializer.deserialize(this)