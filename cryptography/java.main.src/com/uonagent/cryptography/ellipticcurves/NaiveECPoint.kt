package com.uonagent.cryptography.ellipticcurves

import com.uonagent.cryptography.extensions.toByteArray
import com.uonagent.cryptography.math.*
import java.io.Serializable

private const val DIFFERENT_GROUPS_MESSAGE = "Both points must belong the same group"
private const val POINT_IS_ZERO = "Point is Zero so it doesn't have any properties"

class NaiveECPoint: Serializable {

    val isZero: Boolean
    val x: Int
    val y: Int
    val a: Int
    val b: Int
    val m: Int

    private constructor(isZero: Boolean, x: Int, y: Int, a: Int, b: Int, m: Int) {
        this.isZero = isZero
        this.x = x
        this.y = y
        this.a = a
        this.b = b
        this.m = m
    }

    constructor(x: Int, y: Int, a: Int, b: Int, m: Int) : this(false, x, y, a, b, m)

    companion object {
        val ZERO = NaiveECPoint(true, 0, 0, 0, 0, 0)
    }

    operator fun plus(q: NaiveECPoint) = when {
        this == !q -> ZERO
        isZero && !q.isZero -> q
        !isZero && q.isZero -> this
        !belongsTheSameGroupAs(q) -> throw ArithmeticException(DIFFERENT_GROUPS_MESSAGE)
        else -> {
            val l = if (this == q) {
                tangentAngle()
            } else {
                lineTo(q)
            }
            val rx = ((l mult l) sub x) sub q.x
            val ry = (l mult (x sub rx)) sub y
            NaiveECPoint(rx, ry, a, b, m)
        }
    }

    operator fun not() = if (!isZero) {
        NaiveECPoint(x, y rev m, a, b, m)
    } else ZERO

    fun mult(n: Int): NaiveECPoint {
        val l = arrayListOf(ZERO)
        var k = n
        var q = this
        while (true) {
            if (k and 1 == 1) {
                l.add(q)
            }
            k = k ushr 1
            if (k != 0) {
                q += q
            } else break
        }
        return l.reduce { acc, ecPoint -> acc + ecPoint }
    }

    override fun equals(other: Any?) = when {
        isZero && (other as NaiveECPoint).isZero -> true
        isZero xor (other as NaiveECPoint).isZero -> false
        else -> x == other.x && y == other.y && belongsTheSameGroupAs(other)
    }

    fun lineTo(q: NaiveECPoint) = (q.y sub y) div (q.x sub x)

    fun tangentAngle() = (3 mult x mult x sum a) div ((y shl 1) % m)

    fun belongsTheSameGroupAs(p: NaiveECPoint) = a == p.a && b == p.b && m == p.m

    override fun toString(): String {
        return "NaiveECPoint(isZero=$isZero, x=$x, y=$y, a=$a, b=$b, m=$m)"
    }

    private infix fun Int.mult(b: Int) = this.multMod(b, m)
    private infix fun Int.div(b: Int) = this.divMod(b, m)
    private infix fun Int.sum(b: Int) = this.sumMod(b, m)
    private infix fun Int.sub(b: Int) = this.subMod(b, m)
    private operator fun Int.not() = this rev m

    override fun hashCode(): Int {
        var result = isZero.hashCode()
        result = 31 * result + x
        result = 31 * result + y
        result = 31 * result + a
        result = 31 * result + b
        result = 31 * result + m
        return result
    }

    fun coordinatesToByteArray(): ByteArray {
        val xArr = this.x.toByteArray()
        val yArr = this.y.toByteArray()
        val e = ByteArray(8)
        System.arraycopy(xArr, 0, e, 0, 4)
        System.arraycopy(yArr, 0, e, 4, 4)
        return e
    }
}

operator fun Int.times(p: NaiveECPoint) = p.mult(this)