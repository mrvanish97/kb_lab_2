package com.uonagent.cryptography.ellipticcurves

import com.uonagent.cryptography.math.*

object ECGroup {

    private val params = hashMapOf<Int, List<IntArray>>()

    private val points = hashMapOf<IntArray, List<NaiveECPoint>>()

    fun checkParameters(a: Int, b: Int, m: Int) =
            m > 3 && 4.multMod(a.powMod(3, m), m).sumMod(27.multMod(b.powMod(2, m), m), m) != 0

    fun getListOfPossibleParameters(m: Int) = params[m] ?: calculateList(m)

    private fun calculateList(m: Int): List<IntArray> {
        val l = arrayListOf<IntArray>()
        for (a in 0..m) {
            for (b in 0..m) {
                if (checkParameters(a, b, m)) {
                    l.add(intArrayOf(a, b))
                }
            }
        }
        params[m] = l
        return l
    }

    fun getListOfPoints(a: Int, b: Int, m: Int): List<NaiveECPoint> {
        val an = a toMemberOfField m
        val bn = b toMemberOfField m
        return points[intArrayOf(an, bn, m)] ?: calculateListOfPoints(an, bn, m)
    }

    fun getN(a: Int, b: Int, m: Int) = getListOfPoints(a, b, m).size

    private fun calculateListOfPoints(a: Int, b: Int, m: Int): List<NaiveECPoint> {
        val array = arrayListOf(NaiveECPoint.ZERO)
        for (x in 0 until m) {
            f(x, a, b, m).forEach {
                array.add(NaiveECPoint(x, it, a, b, m))
            }
        }
        points[intArrayOf(a, b, m)] = array
        return array
    }

    fun f(x: Int, a: Int, b: Int, m: Int): List<Int> {
        infix fun Int.times(b: Int) = this.multMod(b, m)
        infix fun Int.pow(n: Int) = this.powMod(n, m)
        infix fun Int.plus(b: Int) = this.sumMod(b, m)
        val l = arrayListOf<Int>()
        val ySq = ((x pow 3) plus (a times x)) plus b
        val y1 = ySq sqrt m
        val y2: Int
        if (y1 != null) {
            l.add(y1)
            y2 = y1 rev m
            if (y1 != y2) {
                l.add(y2)
            }
        }
        return l
    }

    private infix fun Int.toMemberOfField(m: Int) = ((this % m) + m) % m

}