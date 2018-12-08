package com.uonagent.cryptography.extensions

import com.uonagent.cryptography.hash.SHA1
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*

private val hexArray = "0123456789abcdef".toCharArray()

fun ByteArray.toHexString(): String {
    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val v = this[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}

fun ByteArray.toBigInt() = BigInteger(1, this)

fun BigInteger.toBytesSized(size: Int): ByteArray {
    var d = this.toByteArray()
    if (d.size > size) {
        d = Arrays.copyOfRange(d, d.size - size, d.size)
    } else if (d.size < size) {
        val nd = ByteArray(size) { 0 }
        System.arraycopy(d, 0, nd, size - d.size, d.size)
        d = nd
    }
    return d
}

fun Any.toBigIntHash(): BigInteger {
    val messageHash = SHA1.calculateHash(this)
    return messageHash.toBigInt()
}

fun ByteArray.toInt() = ByteBuffer.wrap(this).int

fun ByteArray.toByte() = ByteBuffer.wrap(this).int

fun ByteArray.toLong() = ByteBuffer.wrap(this).long

fun Int.toByteArray(): ByteArray = ByteBuffer.allocate(4).putInt(this).array()

fun ByteArray.takeIntFromPosition(position: Int) = this.copyOfRange(position, position + 4).toInt()

fun ByteArray.oneByteToInt() = byteArrayOf(0, 0, 0, this[0]).toInt()