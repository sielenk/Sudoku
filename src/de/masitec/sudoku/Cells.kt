/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

import java.lang.StringBuilder


class Cells(init: (Int?) -> Array<Array<Int?>>) {
    companion object {
        private fun row(r: LargeIndex, i: LargeIndex) = Position(r, i)
        private fun column(c: LargeIndex, i: LargeIndex) = Position(i, c)
        private fun block(b: LargeIndex, i: LargeIndex): Position {
            val bx = b.i % 3
            val by = b.i / 3
            val ix = i.i % 3
            val iy = i.i / 3

            return Position(LargeIndex(ix + bx * 3), LargeIndex(iy + by * 3))
        }
    }

    val cells =
        LargeIndex.range.map { y ->
            LargeIndex.range.map { x ->
                Cell(this, x, y)
            }
        }.flatten()

    val constraints = mutableSetOf<Constraint>()

    init {
        for (f in arrayOf(::row, ::column, ::block)) {
            for (u in LargeIndex.range) {
                Constraint(
                    constraints,
                    LargeIndex.range.map { v ->
                        this[f(u, v)]
                    }.toMutableSet()
                )
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

    operator fun get(p: Position) = this[p.x, p.y]
    operator fun get(x: LargeIndex, y: LargeIndex) = cells[x.i + y.i * 9]

    override fun toString() = format(setOf())

    fun format(highlightCells: Set<Cell>): String {
        val builder = StringBuilder()

        cells.forEachIndexed { index, cell ->
            val cellText = cell.value?.toString() ?: " "

            builder.append(
                if (highlightCells.contains(cell)) {
                    "($cellText)"
                } else {
                    " $cellText "
                }
            )

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

    fun solve(step: (Set<Cell>) -> Unit) {
        while (true) {
            val progress1 = constraints.toSet().map { constraint -> constraint.split(step) }.any { it }
            val progress2 = cells.map { cell -> cell.update(step) }.any { it }

            if (!progress1 && !progress2) {
                break
            }
        }
    }
}
