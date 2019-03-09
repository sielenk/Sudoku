/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

fun main() {
    val cells = Cells { x ->
        arrayOf(
            arrayOf(8, 7, x, x, x, 9, x, 1, x),
            arrayOf(6, x, x, x, x, x, x, x, 4),
            arrayOf(x, x, x, x, x, 5, x, 6, x),
            arrayOf(x, x, 6, 3, x, x, x, 7, x),
            arrayOf(x, x, 9, x, x, x, 6, x, x),
            arrayOf(x, 1, x, x, x, x, x, x, 8),
            arrayOf(x, x, 5, x, 1, x, 9, x, 7),
            arrayOf(2, x, x, 8, x, x, x, x, 3),
            arrayOf(x, x, x, 4, x, x, x, x, x)
        )
    }

    print(cells)
    cells.solve {
        println()
        println()
        print(cells.format(it))
    }
}
