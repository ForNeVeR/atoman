enum CellType {
  Empty,
  Player,
  Ghost,
  Wall,
  Pill
}

class Cell {
  type: CellType;

  constructor(type: CellType) {
    this.type = type;
  }

  getSpriteName() {
    switch (this.type) {
      case CellType.Empty:
        return null;
      case CellType.Player:
        return 'Pacman-Chomping-Right';
      case CellType.Ghost:
        return 'Red-Ghost-Right';
      case CellType.Wall:
        return 'Wall';
      case CellType.Pill:
        return 'Pill';
    }
  }
}

class Map {
  static parse(data: string): Map {
    let lines = data.split('\n');
    let map = new Map();
    let width = 0;
    lines.forEach(line => {
      line = line.trim();
      if (line === '') {
        return;
      }

      if (line.length > width) {
        width = line.length;
      }

      var row = [];
      map.cells.push(row);

      for (let i = 0; i < line.length; ++i) {
        var type = Map.parseCellType(line[i]);
        if (type != null) {
          row.push(new Cell(type));
        }
      }
    });

    map.width = width;
    map.height = map.cells.length;
    map.cells.forEach(line => {
      while (line.length < width) {
        line.push(new Cell(CellType.Empty));
      }
    });

    return map;
  }

  static parseCellType(char: string) {
    switch (char) {
      case ' ':
        return CellType.Empty;
      case '#':
        return CellType.Wall;
      case 'o':
        return CellType.Player;
      case '.':
        return CellType.Pill;
      case 'g':
        return CellType.Ghost;
    }

    return null;
  }

  constructor() {
    this.cells = [];
  }

  cells: Cell[][];
  width: number;
  height: number;
}

export = Map;
