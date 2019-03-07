/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

data class LargeIndex(val i: Int) {
    companion object {
        val range = (0..8).map(::LargeIndex)
    }

    init {
        assert(0 <= i && i < 9)
    }

    override fun toString() = i.toString()
}
