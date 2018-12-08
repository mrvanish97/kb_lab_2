package com.uonagent.cryptography.hash

import com.uonagent.cryptography.padding.SHA12Padding
import com.uonagent.cryptography.math.rotl
import com.uonagent.cryptography.serializer.Serializer
import java.nio.ByteBuffer
import java.nio.charset.Charset

private const val ARRAY_SIZE = 20

object SHA1 : Hash {

    override fun calculateHash(obj: Any) = calculateHash(Serializer.serialize(obj))
    override fun calculateHash(string: String) = calculateHash(string, Charsets.UTF_8)

    override fun calculateHash(string: String, charset: Charset) =
            calculateHash(string.toByteArray(charset))

    override fun calculateHash(byteArray: ByteArray): ByteArray {
        val padded = SHA12Padding.padding(byteArray, 64)
        val h = intArrayOf(
                1732584193,
                -271733879,
                -1732584194,
                271733878,
                -1009589776
        )
        val w = IntArray(80)
        val a = IntArray(5)
        var temp: Int
        for (i in 0 until (padded.size ushr 6)) {
            for (t in 0..15) {
                w[t] = padded[i, t]
            }
            for (t in 16..79) {
                w[t] = (w[t - 3] xor w[t - 8] xor w[t - 14] xor w[t - 16]) rotl 1
            }
            for (k in h.indices) {
                a[k] = h[k]
            }
            for (t in 0..79) {
                temp = (a[0] rotl 5) + f(t, a[1], a[2], a[3]) + a[4] + w[t] + k(t)
                a[4] = a[3]
                a[3] = a[2]
                a[2] = a[1] rotl 30
                a[1] = a[0]
                a[0] = temp
            }
            for (k in h.indices) {
                h[k] += a[k]
            }
        }
        val hash = ByteArray(ARRAY_SIZE)
        for (i in h.indices) {
            val hArr = ByteBuffer.allocate(4).putInt(h[i]).array()
            System.arraycopy(hArr, 0, hash, i shl 2, 4)
        }
        return hash
    }

    private operator fun ByteArray.get(i: Int, t: Int): Int {
        val index = (i shl 6) + (t shl 2)
        return ByteBuffer.wrap(this, index, 4).int
    }

    private fun f(t: Int, x: Int, y: Int, z: Int) = when (t) {
        in 0..19 -> ch(x, y, z)
        in 40..59 -> maj(x, y, z)
        else -> parity(x, y, z)
    }

    private fun ch(x: Int, y: Int, z: Int) = z xor (x and (y xor z))

    private fun parity(x: Int, y: Int, z: Int) = x xor y xor z

    private fun maj(x: Int, y: Int, z: Int) = (x and y) xor (x and z) xor (y and z)

    private fun k(t: Int) = when (t) {
        in 0..19 -> 1518500249
        in 20..39 -> 1859775393
        in 40..59 -> -1894007588
        in 60..79 -> -899497514
        else -> throw IllegalArgumentException()
    }

    override val lenght = ARRAY_SIZE

}