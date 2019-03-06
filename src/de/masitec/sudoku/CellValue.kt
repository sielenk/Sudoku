package de.masitec.sudoku

data class CellValue(val v: Int) {
    companion object {
        val all = (1..9).map(::CellValue).toSet()
        val none = setOf<CellValue>()
    }

    init {
        assert(0 < v && v <= 9)
    }

    override fun toString(): String = v.toString()
}
