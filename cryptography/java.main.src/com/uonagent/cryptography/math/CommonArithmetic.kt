package com.uonagent.cryptography.math

infix fun Int.rotl(n: Int) = ModArithmetic.rotl(this, n)

infix fun Int.pow(n: Int): Int {
    var i = n
    var a = this
    var res = 1
    while (i != 0) {
        if ((i and 1) != 0) {
            res *= a
        }
        a *= a
        i = i ushr 1
    }
    return res
}