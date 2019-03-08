/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

class Constraint(
    private val constraints: MutableSet<Constraint>,
    val cells: MutableSet<Cell>,
    val allowedValues: MutableSet<CellValue>
) {
    init {
        constraints += this
        cells.forEach { cell -> cell.constraints += this }
    }

    constructor(constraints: MutableSet<Constraint>, cells: MutableSet<Cell>) : this(
        constraints,
        cells,
        CellValue.all.toMutableSet()
    )

    fun remove(cell: Cell, removeValues: Set<CellValue>) {
        assert(cells.contains(cell))

        cells -= cell
        cell.constraints -= this

        if (cells.isEmpty()) {
            constraints -= this
        }

        allowedValues -= removeValues
    }

    fun split(step: (Set<Cell>) -> Unit): Boolean {
        val constraintSplits = mutableSetOf(this)

        return cells
            .groupBy { cell -> cell.allowedValues }
            .filter { (allowedValues, newCells) -> allowedValues.size == newCells.size }
            .map { (allowedValues, newCells: List<Cell>) ->
                if (allowedValues.size == 1) {
                    newCells.single().set(allowedValues.single())
                    step(newCells.toSet())
                    true
                } else if (newCells.size < cells.size) {
                    constraintSplits.forEach { constraint ->
                        newCells.forEach { cell ->
                            constraint.remove(cell, allowedValues)
                        }
                    }
                    constraintSplits += Constraint(
                        constraints,
                        newCells.toMutableSet(),
                        allowedValues.toMutableSet()
                    )
                    step(newCells.toSet())
                    true
                } else {
                    false
                }
            }
            .any { it }
    }
}
