package de.masitec.sudoku

fun main() {
    val cells = Cells { x ->
        arrayOf(
            arrayOf(x, x, 1, 3, 9, 2, 4, x, x),
            arrayOf(x, 9, 7, x, x, x, 8, 2, x),
            arrayOf(2, x, x, x, x, x, x, x, 1),
            arrayOf(x, x, x, x, 7, x, x, x, x),
            arrayOf(x, 2, x, 1, x, 6, x, 4, x),
            arrayOf(x, x, x, x, 8, x, x, x, x),
            arrayOf(4, x, x, x, x, x, x, x, 3),
            arrayOf(x, 7, 5, x, x, x, 2, 1, x),
            arrayOf(x, x, 2, 6, 1, 4, 5, x, x)
        )
    }

    var oldUnknownCount = cells.unknownCount
    while (true) {
        print(cells)
        println()
        println()

        cells.step()

        val newUnknownCount = cells.unknownCount
        if (oldUnknownCount == newUnknownCount) {
            break
        }

        oldUnknownCount = newUnknownCount
    }
}
