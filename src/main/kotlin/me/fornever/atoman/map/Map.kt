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
                    width = trimmedLine.length;
                }

                val row = mutableListOf<Cell>()
                cells.add(row)

                for (i in 0..trimmedLine.length - 1) {
                    val cell = parseCellType(trimmedLine[i])
                    if (type != null) {
                        row.push(new Map . Cell (type));
                    }
                }
            });

            map.width = width;
            map.height = map.cells.length;
            map.cells.forEach(line => {
                while (line.length < width) {
                    line.push(new Map . Cell (Cell.Empty));
                }
            });

            return map;
        }
    }
}
