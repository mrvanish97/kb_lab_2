package com.uonagent.cryptography.padding

import java.nio.ByteBuffer


private const val SHA1_BLOCK_BITS = 512
private const val SHA1_BLOCK_BYTES = SHA1_BLOCK_BITS ushr 3
private const val SHA2_BLOCK_BITS = 1024
private const val SHA2_BLOCK_BYTES = SHA2_BLOCK_BITS ushr 3

private const val UNSUPPORTED_BLOCK_SIZE = "SHA 1/2 Padding can work only with 64 or 128 bytes block sizes"
private const val DEPADDING_IS_UNSUPPORTED = "SHA 1/2 Padding does not support deppading"

object SHA12Padding: Padding {

    private val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)

    override fun padding(byteArray: ByteArray, blockSize: Int): ByteArray {
        if (blockSize != SHA1_BLOCK_BYTES && blockSize != SHA2_BLOCK_BYTES) {
            throw IllegalArgumentException(UNSUPPORTED_BLOCK_SIZE)
        }
        var r = blockSize - byteArray.size % blockSize
        /**
         * твое тело раздает wifi
         * в твоей крови lte
         * твой мозг -- это пиксельный рай
         * сотни картинок извне
         * ты весишь один гигабайт
         * из-за электронных диеееет
         * */
        r = if (r - 9 < 0) r + blockSize else r
        val paddedSize = byteArray.size + r
        val padded = ByteArray(paddedSize)
        System.arraycopy(byteArray, 0, padded, 0, byteArray.size)
        padded[byteArray.size] = -128
        val longSize = byteArray.size.toLong() shl 3
        val longArr = longToBytes(longSize)
        for (i in longArr.indices) {
            padded[paddedSize - 8 + i] = longArr[i]
        }
        return padded
    }

    override fun depadding(byteArray: ByteArray, blockSize: Int): ByteArray {
        /**
         * ДЕВОЧКА INTERNET
         * */
        throw UnsupportedOperationException(DEPADDING_IS_UNSUPPORTED)
    }

    private fun longToBytes(x: Long): ByteArray {
        buffer.putLong(0, x)
        return buffer.array()
    }
}