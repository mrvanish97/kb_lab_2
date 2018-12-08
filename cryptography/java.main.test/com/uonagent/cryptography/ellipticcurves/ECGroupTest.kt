package com.uonagent.cryptography.ellipticcurves

import org.junit.jupiter.api.Test

internal class ECGroupTest {

    @Test
    fun kek() {
        val m = 257
        val params = ECGroup.getListOfPossibleParameters(m)
        val ns = hashMapOf<IntArray, Int>()
        params.forEach {
            ns[it] = ECGroup.getN(it[0], it[1], m)
        }
        val l = ns
                .toList()
                .sortedWith(compareBy({ -it.second }, { it.first[0] + it.first[1] }))
        l.forEach {
            println("${it.second} - ${it.first.contentToString()}")
        }
        val win = l
                .filter { it.second == 257 }
                .find {
                    ECGroup.getListOfPoints(it.first[0], it.first[1], m)
                            .contains(NaiveECPoint(4, 20, it.first[0], it.first[1], m))
                } ?: Pair(intArrayOf(), 0)
        println(win.first.contentToString())
    }

    @Test
    fun lol() {
        val a = 1
        val b = 11
        val m = 47
        val pts = ECGroup.getListOfPoints(a, b, m)
        println(pts.size)
        pts.forEach {
            println(it)
        }
    }
}