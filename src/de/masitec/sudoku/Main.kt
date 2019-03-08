/*
 * Copyright (c) 2019 Marvin H. Sielenkemper
 */

package de.masitec.sudoku

fun main() {
    val cells = Cells { x ->
        arrayOf(
            arrayOf(4, x, 5, x, 2, x, x, x, x),
            arrayOf(x, x, x, 7, 5, x, x, x, x),
            arrayOf(x, x, x, x, x, x, 4, x, 3),
            arrayOf(x, 2, x, x, x, 8, x, x, 6),
            arrayOf(x, x, x, x, x, x, 7, x, 1),
            arrayOf(x, 8, x, x, 9, x, x, x, x),
            arrayOf(x, x, 3, 2, 7, x, x, x, x),
            arrayOf(x, x, 1, x, x, x, 6, 9, x),
            arrayOf(x, x, 7, 6, x, x, x, 1, x)
        )
    }

    print(cells)
    cells.solve {
        println()
        println()
        print(cells.format(it))
    }
}
