package com.uonagent.cryptography.math

import java.io.*
import java.math.BigInteger

private const val IN_ARR = "in_arr"
private const val OUT_ARR = "out_arr"

private const val P = 65536

private const val N_ERROR = "N can't be greater than ${P - 1}"
private const val ROTL_N_ARG_EXC = "N must be in range from 0 until 32"

object ModArithmetic {

    private lateinit var outArr: IntArray
    private lateinit var inArr: IntArray

    init {
        val fiInArr: FileInputStream
        val fiOutArr: FileInputStream
        try {
            fiInArr = FileInputStream(IN_ARR)
            fiOutArr = FileInputStream(OUT_ARR)
            val oInArr = ObjectInputStream(fiInArr)
            val oOutArr = ObjectInputStream(fiOutArr)
            inArr = oInArr.readObject() as IntArray
            outArr = oOutArr.readObject() as IntArray
        } catch (e: IOException) {
            makeFiles()
        }
    }

    private fun makeFiles() {

        inArr = IntArray(P)
        outArr = IntArray(P)

        val v = BigInteger("3", 10)
        val p = BigInteger("${P + 1}", 10)

        for (i in outArr.indices) {
            outArr[i] = v.modPow(BigInteger("$i", 10), p).toInt()
        }

        val inArrMap = hashMapOf<Int, Int>()

        for (i in outArr.indices) {
            inArrMap[outArr[i]] = i
        }

        for (i in inArr.indices) {
            inArr[i] = inArrMap[i + 1]!!
        }

        val f1 = FileOutputStream("in_arr")
        val f2 = FileOutputStream("out_arr")

        val o1 = ObjectOutputStream(f1)
        val o2 = ObjectOutputStream(f2)

        o1.writeObject(inArr)
        o2.writeObject(outArr)
    }

    fun mult(a: Int, b: Int, m: Int) = if (a == 0 || b == 0) {
        0
    } else {
        if (m == P + 1 && a < m && b < m) {
            val iA = inArr[a - 1]
            val iB = inArr[b - 1]
            outArr[sum(iA, iB, P)] umod P
        } else {
            (a * b) umod m
        }
    }

    fun sum(a: Int, b: Int, m: Int) = (a + b) umod m

    fun sub(a: Int, b: Int, m: Int) = a.sumMod(b rev m, m)

    fun rev(a: Int, m: Int) = (m - a) umod m

    fun pow(a: Int, n: Int, m: Int): Int {

        if (n > P) {
            throw IllegalArgumentException(N_ERROR)
        }

        var nb = n

        val pows = IntArray(16)

        pows[0] = a umod m
        for (i in 1 until pows.size) {
            val p = pows[i - 1]
            pows[i] = if (p != P) {
                val s = p * p
                s umod m
            } else 1
        }

        var ans = 1
        for (e in pows) {
            if (nb == 0) break
            if (nb umod 2 == 1) {
                ans = mult(ans, e, m)
            }
            nb = nb ushr 1
        }
        return ans
    }

    fun inv(a: Int, m: Int) = a.powMod(m - 2, m)

    fun rotl(a: Int, n: Int) = if (n in 0..32) {
        (a shl n) or (a ushr (32 - n))
    } else throw IllegalArgumentException(ROTL_N_ARG_EXC)

    fun div(a: Int, b: Int, m: Int) = a.multMod(b inv m, m)

    fun legendre(a: Int, m: Int) = a.powMod((m - 1) ushr 1, m)

    fun sqrt(a: Int, m: Int) = when {
        a == 0 -> 0
        a.legendre(m) == 1 -> sqrtCalculation(a, m)
        else -> null
    }

    private fun sqrtCalculation(a: Int, m: Int): Int? {
        val sq = sq(m)
        return if (sq[0] == 1) {
            a.powMod((m + 1) ushr 2, m)
        } else {
            infix fun Int.pow(b: Int) = this.powMod(b, m)
            infix fun Int.times(b: Int) = this.multMod(b, m)

            val z = findNotSquarable(m) ?: return null
            var c = z pow sq[1]
            var r = a pow ((sq[1] + 1) ushr 1)
            var t = a pow sq[1]
            var n = sq[0]
            var i: Int
            var b: Int

            while (t != 1) {
                i = 1
                while (t pow (1 shl i) != 1) {
                    ++i
                }
                if (i >= n) return null
                b = c pow (1 shl (n - i - 1))
                r = r times b
                t = t times b times b
                c = b times b
                n = i
            }

            r
        }
    }

    private fun findNotSquarable(m: Int): Int? {
        for (x in 2..m) {
            if (x.legendre(m) == m - 1) {
                return x
            }
        }
        return null
    }

    fun sq(a: Int): IntArray {
        val v = a - 1
        val r = IntArray(2)
        r[0] = Integer.numberOfTrailingZeros(v)
        r[1] = v ushr r[0]
        return r
    }
}

infix fun Int.inv(m: Int) = ModArithmetic.inv(this, m)

infix fun Int.rev(m: Int) = ModArithmetic.rev(this, m)

infix fun Int.umod(m: Int) = Integer.remainderUnsigned(this, m)

fun Int.sumMod(b: Int, m: Int) = ModArithmetic.sum(this, b, m)

fun Int.multMod(b: Int, m: Int) = ModArithmetic.mult(this, b, m)

fun Int.powMod(n: Int, m: Int) = ModArithmetic.pow(this, n, m)

fun Int.divMod(b: Int, m: Int) = ModArithmetic.div(this, b, m)

fun Int.subMod(b: Int, m: Int) = ModArithmetic.sub(this, b, m)

fun Int.legendre(m: Int) = ModArithmetic.legendre(this, m)

infix fun Int.sqrt(m: Int) = ModArithmetic.sqrt(this, m)