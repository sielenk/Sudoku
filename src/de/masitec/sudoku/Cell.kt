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

    val requiredValues
        get() =
            value
                ?.let { setOf<CellValue>() }
                ?: constraints.map { constraint ->
                    allowedValues - constraint.cells
                        .filter { cell -> cell != this }
                        .fold(CellValue.none) { acc, cell -> acc + cell.allowedValues }
                }.  fold(CellValue.none) { acc, set -> acc + set }


    fun out() =
        value
            ?.let { " ${it} " }
            ?: "   "

    override fun toString(): String =
        "[$x,$y] ${value?.toString() ?: "? $allowedValues $requiredValues"}"

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
