package me.fornever.atoman.map

private data class Map(val cells: List<List<Cell>>, val width: Int, val height: Int) {

    companion object {

        fun parse(data: String): Map {
            val lines = data.split('\n')
            val cells = mutableListOf<MutableList<Cell>>()
            var width = 0

            for (line in lines) {
                val trimmedLine = line.trim()
                if (trimmedLine == "") {
                    continue
                }

                if (trimmedLine.length > width) {
                    width = trimmedLine.length
                }

                val row = mutableListOf<Cell>()
                cells.add(row)

                for (element in trimmedLine) {
                    val type = parseCellType(element)
                    if (type != null) {
                        row.add(Cell(type))
                    }
                }
            }

            for (row in cells) {
                while (row.size < width) {
                    row.add(Cell(CellType.Empty))
                }
            }

            return Map(
                cells,
                width,
                cells.size
            )
        }

        private fun parseCellType(char: Char): CellType? =
            when (char) {
                ' ' -> CellType.Empty
                '#' -> CellType.Wall
                'o' -> CellType.Player
                '.' -> CellType.Pill
                'g' -> CellType.Ghost
                else -> null
            }
    }
}
