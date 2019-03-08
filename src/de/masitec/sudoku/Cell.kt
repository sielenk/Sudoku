/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

class Cell(val x: LargeIndex, val y: LargeIndex, var value: CellValue?) {
    constructor(x: LargeIndex, y: LargeIndex) : this(x, y, null)

    val constraints = mutableSetOf<Constraint>()

    val allowedValues
        get() =
            value
                ?.let { setOf(it) }
                ?: constraints
                    .map { it.allowedValues }
                    .fold(CellValue.all) { acc, allowedValues -> acc intersect allowedValues }

    fun set(newValue: CellValue) {
        value = newValue

        constraints.toList().forEach { constraint ->
            constraint.remove(this, setOf(newValue))
        }
    }

    override fun toString(): String = "($x,$y) ${value?.toString() ?: "$allowedValues"}"
}
