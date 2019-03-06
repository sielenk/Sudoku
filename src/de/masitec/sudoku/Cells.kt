package de.masitec.sudoku

import java.lang.StringBuilder


class Cells(init: (Int?) -> Array<Array<Int?>>) {
    companion object {
        private fun row(r: LargeIndex, i: LargeIndex) = Pair(r, i)
        private fun column(c: LargeIndex, i: LargeIndex) = Pair(i, c)
        private fun block(b: LargeIndex, i: LargeIndex): Pair<LargeIndex, LargeIndex> {
            val bx = b.i % 3
            val by = b.i / 3
            val ix = i.i % 3
            val iy = i.i / 3

            return Pair(LargeIndex(ix + bx * 3), LargeIndex(iy + by * 3))
        }
    }

    val cells =
        LargeIndex.range.map { y ->
            LargeIndex.range.map { x ->
                Cell(x, y)
            }
        }.flatten()

    init {
        for (f in arrayOf(::row, ::column, ::block)) {
            for (u in LargeIndex.range) {
                Constraint(LargeIndex.range.map { v ->
                    val (r, c) = f(u, v)
                    this[r, c]
                }.toMutableSet())
            }
        }

        val initValues = init(null)

        assert(initValues.size == 9)

        initValues.mapIndexed { y, row ->
            assert(row.size == 9)

            row.mapIndexed { x, value ->
                if (value != null) {
                    val xx = LargeIndex(x)
                    val yy = LargeIndex(y)
                    val cell = this[xx, yy]

                    assert(cell.x == xx)
                    assert(cell.y == yy)

                    cell.set(CellValue(value))
                }
            }
        }
    }

    val unknownCount
        get() = cells.count { it.value == null }

    operator fun get(x: LargeIndex, y: LargeIndex) = cells[x.i + y.i * 9]

    override fun toString(): String {
        val builder = StringBuilder()

        cells.forEachIndexed { index, cell ->
            builder.append(cell.out())

            if (index % 9 == 8) {
                builder.appendln()
                if ((index / 9) % 3 == 2 && index != 80) {
                    builder.appendln("--- --- --- + --- --- --- + --- --- ---")
                }
            } else {
                builder.append(" ")
                if (index % 3 == 2) {
                    builder.append("| ")
                }
            }
        }

        return builder.toString()
    }

    fun update() {
        cells.forEach { cell ->
            val newValue = cell.newValue

            if (newValue != null) {
                cell.set(newValue)
            }
        }
    }
}
