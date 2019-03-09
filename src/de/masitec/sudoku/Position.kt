/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

data class Position(val x: LargeIndex, val y: LargeIndex) {
    operator fun plus(other: Position) = Position(LargeIndex(x.i + other.x.i), LargeIndex(y.i + other.y.i))
}
