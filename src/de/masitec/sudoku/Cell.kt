/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

class Cell(val x: LargeIndex, val y: LargeIndex, var value: CellValue?) {
    constructor(x: LargeIndex, y: LargeIndex) : this(x, y, null)

    val constraints = mutableSetOf<Constraint>()

    val constrainingCells by lazy {
        constraints.flatMap { it.cells.asIterable() }.filter { it != this }
    }

    val allowedValues
        get() =
            value
                ?.let { setOf(it) }
                ?: constraints.fold(CellValue.all) { acc, constraint -> acc.intersect(constraint.allowedValues) }

    val requiredValue
        get() =
            value
                ?: constraints
                    .map { constraint ->
                        allowedValues.minus(
                            constraint
                                .cells
                                .filter { cell -> cell != this }
                                .fold(CellValue.none) { acc, cell -> acc + cell.allowedValues })
                    }
                    .fold(CellValue.none) { acc, set -> acc + set }
                    .firstOrNull()

    fun out() =
        value
            ?.let { " ${it} " }
            ?: "   "

    override fun toString(): String =
        "[$x,$y] ${value?.toString() ?: "$allowedValues $requiredValue -> $newValue"}"

    val newValue
        get() =
            value
                ?: allowedValues.singleOrNull()
                ?: requiredValue


    fun set(v: CellValue) {
        constraints.forEach { constraint ->
            assert(constraint.cells.contains(this))
            assert(constraint.allowedValues.contains(v))

            constraint.cells -= this
            constraint.allowedValues -= v
        }
        constraints.clear()

        value = v
    }
}
