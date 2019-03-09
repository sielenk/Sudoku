/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

class Cell(private val cells: Cells, val x: LargeIndex, val y: LargeIndex, var value: CellValue?) {
    private val influencers by lazy {
        listOf(
            LargeIndex.range.filter { it != y }.map { cells[x, it] },
            LargeIndex.range.filter { it != x }.map { cells[it, y] },
            Position(LargeIndex((x.i / 3) * 3), LargeIndex((y.i / 3 * 3))).let { offset ->
                LargeIndex.range
                    .map { offset + Position(LargeIndex(it.i % 3), LargeIndex(it.i / 3)) }
                    .filterNot { it.x == x && it.y == y }
                    .map { cells[it] }
            })
    }

    constructor(cells: Cells, x: LargeIndex, y: LargeIndex) : this(cells, x, y, null)

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
                ?: influencers
                    .map {
                        val requiredSet = allowedValues - it.flatMap { cell -> cell.allowedValues }
                        requiredSet.singleOrNull()
                    }
                    .filterNotNull()
                    .toSet()
                    .singleOrNull()

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
