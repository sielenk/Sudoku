package de.masitec.sudoku

class Constraint(val cells: MutableSet<Cell>, val allowedValues: MutableSet<CellValue>) {
    init {
        cells.forEach {
            it.constraints += this
        }
    }

    constructor(cells: MutableSet<Cell>) : this(cells, CellValue.all.toMutableSet())
}
