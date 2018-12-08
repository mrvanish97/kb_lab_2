package com.uonagent.cryptography.padding

object DummyPadding: Padding {
    override fun padding(byteArray: ByteArray, blockSize: Int) = byteArray

    override fun depadding(byteArray: ByteArray, blockSize: Int) = byteArray
}