package org.example

import net.hamnaberg.arities.*

fun main(args: Array<String>) {
    val (a, b, c, d) = Tuples.of("Hello", 123, 345L, true)
    println(c)
    println(b)
    println(d)
    println(a)
}
