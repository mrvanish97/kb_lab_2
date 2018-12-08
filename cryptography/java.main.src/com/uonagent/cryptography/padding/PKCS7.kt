package com.uonagent.cryptography.padding

private const val BLOCK_SIZE_EXCEPTION = "PCKS7 block size is incorrect"

object PKCS7 : Padding {
    override fun padding(byteArray: ByteArray, blockSize: Int): ByteArray {
        val shift = byteArray.size % blockSize
        if (blockSize >= 256 || blockSize < 1) {
            throw IllegalArgumentException(BLOCK_SIZE_EXCEPTION)
        }
        val emptyNum = if (shift != 0) blockSize - shift else blockSize
        val paddedArr = ByteArray(byteArray.size + emptyNum)
        System.arraycopy(byteArray, 0, paddedArr, 0, byteArray.size)
        val pad = emptyNum.toByte()
        for (i in byteArray.size until paddedArr.size) {
            paddedArr[i] = pad
        }
        return paddedArr
    }

    override fun depadding(byteArray: ByteArray, blockSize: Int): ByteArray {
        val probPadNum = byteArray.last()
        val depaddedArr = ByteArray(byteArray.size - probPadNum)
        System.arraycopy(byteArray, 0, depaddedArr, 0, depaddedArr.size)
        return depaddedArr
    }
}
