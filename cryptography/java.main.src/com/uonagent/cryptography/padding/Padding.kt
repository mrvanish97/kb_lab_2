package com.uonagent.cryptography.padding

interface Padding {
    fun padding(byteArray: ByteArray, blockSize: Int): ByteArray
    fun depadding(byteArray: ByteArray, blockSize: Int): ByteArray
}