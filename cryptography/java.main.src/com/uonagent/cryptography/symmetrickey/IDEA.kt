package com.uonagent.cryptography.symmetrickey

import com.uonagent.cryptography.math.*
import java.security.SecureRandom

private const val INT_KEY_LENGTH = 52
private const val SHIFT = 25
private const val KEY_BITS = 128
private const val KEY_BYTES = KEY_BITS / 8
private const val TEXT_BITS = 64
private const val TEXT_BYTES = TEXT_BITS / 8
private const val INCORRECT_KEY_LENGTH = "Key length is incorrect"
private const val INCORRECT_TEXT_LENGTH = "Text length is incorrect"
private const val M = 0x10000

private const val GET_KEY_ERROR = "Such key doesn't exist"
private const val INVALID_RADIX = "Radix must be either 2 or 16"

object IDEA : BlockCipherAlgorithm {

    override fun generateKey(): ByteArray {
        val key = ByteArray(KEY_BYTES)
        val generator = SecureRandom.getInstanceStrong()
        generator.nextBytes(key)
        return key
    }

    override val textSize = TEXT_BYTES
    override val keySize = KEY_BYTES

    override fun encrypt(openText: ByteArray, key: ByteArray): ByteArray {
        val k = Key(key)
        return process(openText, k)
    }

    override fun decrypt(cipherText: ByteArray, key: ByteArray): ByteArray {
        val k = Key(key)
        k.inverse()
        return process(cipherText, k)
    }

    override fun encrypt(openText: ByteArray, key: String, radix: Int): ByteArray {
        val k = Key(key, radix)
        return process(openText, k)
    }

    override fun decrypt(cipherText: ByteArray, key: String, radix: Int): ByteArray {
        val k = Key(key, radix)
        k.inverse()
        return process(cipherText, k)
    }

    private fun process(text: ByteArray, key: Key): ByteArray {
        if (text.size != TEXT_BYTES) {
            throw IllegalArgumentException(INCORRECT_TEXT_LENGTH)
        }

        val d = bytesToInts(text)
        val a = IntArray(10)

        for (round in 0 until 8) {
            a[0] = mult(d[0], key[round, 0]) // ai
            a[1] = d[1].sumMod(key[round, 1], M) // bi
            a[2] = d[2].sumMod(key[round, 2], M) // ci
            a[3] = mult(d[3], key[round, 3]) // di

            a[4] = a[0] xor a[2] // ai xor ci
            a[5] = a[1] xor a[3] // bi xor di

            a[6] = mult(a[4], key[round, 4]) // ei * k5
            a[7] = a[5].sumMod(a[6], M) // fi + ei * k5
            a[8] = mult(a[7], key[round, 5]) // (fi + ei * k5) * k6
            a[9] = a[6].sumMod(a[8], M) // ei * k5 + (fi + ei * k5) * k6

            d[0] = a[0] xor a[8] // ai xor ((fi + ei * k5) * k6)
            d[1] = a[2] xor a[8] // ci xor ((fi + ei * k5) * k6)
            d[2] = a[1] xor a[9] // bi xor (ei * k5 + (fi + ei * k5) * k6)
            d[3] = a[3] xor a[9] // di xor (ei * k5 + (fi + ei * k5) * k6)
        }

        val d1 = d[1]
        val d2 = d[2]

        d[0] = mult(d[0], key[8, 0])
        d[1] = d2.sumMod(key[8, 1], M)
        d[2] = d1.sumMod(key[8, 2], M)
        d[3] = mult(d[3], key[8, 3])

        return intsToBytes(d)
    }

    private fun bytesToInts(bytes: ByteArray): IntArray {
        val ints = IntArray(bytes.size ushr 1)
        var a: Int
        var b: Int
        for (i in ints.indices) {
            /*val a = bytes[i * 2].toInt() shl 24 ushr 24 shl 8
            val b = bytes[i * 2 + 1].toInt() shl 24 ushr 24*/
            a = bytes[i * 2].toInt() shl 24 ushr 24 shl 8
            b = bytes[i * 2 + 1].toInt() shl 24 ushr 24
            ints[i] = a or b
        }
        return ints
    }

    private fun intsToBytes(ints: IntArray): ByteArray {
        val bytes = ByteArray(ints.size * 2)
        for (i in ints.indices) {
            bytes[i * 2] = (ints[i] ushr 8).toByte()
            bytes[i * 2 + 1] = (ints[i] shl 8 ushr 8).toByte()
        }
        return bytes
    }

    class Key {

        private var keyArr: IntArray

        constructor(key: ByteArray) {
            keyArr = parseKeyToArray(bytesToBinString(key))
        }

        constructor(key: String, radix: Int) {
            if (radix != 2 && radix != 16) {
                throw IllegalArgumentException(INVALID_RADIX)
            }
            val s = key.filter { it.isLetterOrDigit() }
            keyArr = if (radix == 2) {
                if (s.length != KEY_BITS) {
                    throw IllegalArgumentException(INCORRECT_KEY_LENGTH)
                }
                parseKeyToArray(s)
            } else {
                if (s.length != KEY_BITS / 4) {
                    throw IllegalArgumentException(INCORRECT_KEY_LENGTH)
                }
                parseKeyToArray(hexToBinString(s))
            }
        }

        private fun bytesToBinString(byteArray: ByteArray): String {
            val sb = StringBuilder("")
            for (k in byteArray) {
                for (i in 7 downTo 0) {
                    sb.append(Integer.remainderUnsigned(k.toInt() shr i, 2))
                }
            }
            return sb.toString()
        }

        private fun hexToBinString(hex: String): String {
            val bytes = ByteArray(KEY_BYTES)
            for (i in bytes.indices) {
                bytes[i] = Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16).toByte()
            }
            return bytesToBinString(bytes)
        }

        private fun parseKeyToArray(key: String): IntArray {
            var keyString = key

            val arr = IntArray(INT_KEY_LENGTH)
            var l: Int
            for (pass in 0..6) {
                l = if (pass != 6) 8 else 4
                for (i in 0 until l) {
                    arr[i + pass * 8] = Integer.parseInt(keyString.substring(i * 16, i * 16 + 16), 2)
                }
                val s1 = keyString.substring(SHIFT)
                val s2 = keyString.substring(0, SHIFT)
                keyString = s1 + s2
            }
            return arr
        }

        operator fun get(round: Int, num: Int) = keyArr[getIndex(round, num)]

        private fun getIndex(round: Int, num: Int): Int {
            if (round < 0 || round > 8 || num < 0 || num > 5 || (round == 8 && num > 3)) {
                throw IllegalArgumentException(GET_KEY_ERROR)
            }
            return round * 6 + num
        }

        fun inverse() {
            val inv = IntArray(INT_KEY_LENGTH)
            for (round in 0 until 9) {
                for (i in 0 until 6) {
                    try {
                        inv[getIndex(round, i)] = if (i == 0 || i == 3) {
                            specialInv(get(8 - round, i))
                        } else if (i == 1 || i == 2) {
                            if (round == 0 || round == 8) {
                                get(8 - round, i) rev M
                            } else {
                                val n = if (i == 1) 2 else 1
                                get(8 - round, n) rev M
                            }
                        } else {
                            get(7 - round, i)
                        }
                    } catch (e: IllegalArgumentException) {
                        break
                    }
                }
            }
            keyArr = inv
        }
        private fun specialInv(a: Int): Int {
            val aa = if (a != 0) a else M
            return aa inv M + 1
        }
    }

    private fun mult(a: Int, b: Int): Int {
        val aa = if (a != 0) a else M
        val bb = if (b != 0) b else M
        return aa.multMod(bb, M + 1) umod M
    }
}