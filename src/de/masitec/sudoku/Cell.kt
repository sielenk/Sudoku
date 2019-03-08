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

    fun update(step: (Set<Cell>) -> Unit): Boolean {
        if (value == null) {
            val requiredValue = requiredValue
            if (requiredValue != null) {
                set(requiredValue)
                step(setOf(this))
                return true
            }
        }
        return false
    }

    fun set(newValue: CellValue) {
        value = newValue

        constraints.toList().forEach { constraint ->
            constraint.remove(this, setOf(newValue))
        }
    }

    override fun toString(): String = "($x,$y) ${value?.toString() ?: "$allowedValues"}"
}
