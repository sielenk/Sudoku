/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

data class Offset(val i: Int) {
    init {
        assert(0 <= i && i < 81)
    }

    constructor(rc: Pair<LargeIndex, LargeIndex>) : this(rc.first.i + 9 * rc.second.i)

    override fun toString() = i.toString()
}