import Sprite = require('./sprite');

enum CellType {
  Empty,
  Player,
  Ghost,
  Wall,
  Pill
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
          row.push(new Map.Cell(type));
        }
      }
    });

    map.width = width;
    map.height = map.cells.length;
    map.cells.forEach(line => {
      while (line.length < width) {
        line.push(new Map.Cell(CellType.Empty));
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

  cells: Map.Cell[][];
  width: number;
  height: number;

  constructor() {
    this.cells = [];
  }

  forEachCell(callback: (cell: Map.Cell, x: number, y: number) => void) {
    this.cells.forEach((line, y) =>{
      line.forEach((cell, x) => {
        callback(cell, x, y);
      });
    });
  }
}

module Map {
  export class Cell {
    type: CellType;
    timeFromFrameStart: number;
    frameIndex: number;

    constructor(type: CellType) {
      this.type = type;
      this.timeFromFrameStart = 0;
      this.frameIndex = 0;
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

    getFrameNumber() {
      return this.frameIndex;
    }

    update(delta: number, sprite: Sprite) {
      if (this.type === CellType.Empty) {
        return;
      }

      let frames = Sprite.getFrames(sprite);
      let frame = frames[this.frameIndex];
      if (frame.duration == 0) { // Static frame
        return;
      }

      let currentTime = this.timeFromFrameStart;
      while (delta > 0) {
        let step = Math.min(frame.duration - currentTime, delta);
        delta -= step;
        currentTime += step;

        if (currentTime >= frame.duration) {
          currentTime -= frame.duration;
          this.frameIndex = (this.frameIndex + 1) % frames.length;
          frame = frames[this.frameIndex];
        }
      }

      this.timeFromFrameStart = currentTime;
    }
  }
}

export = Map;
